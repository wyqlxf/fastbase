/**
 * MIT License
 * <p>
 * Copyright (c) 2019 wangyognqi
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.wyq.fast.activity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.wyq.fast.utils.NetWorkUtil;

import java.lang.reflect.Field;

/**
 * Author: WangYongQi
 * Web Activity base class
 */

public abstract class FastBaseWebActivity extends FastBaseAppCompatActivity {

    // declare a global web view
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initWebView();
    }

    /**
     * return web view
     *
     * @return
     */
    public WebView getWebView() {
        if (webView == null) {
            initWebView();
        }
        return webView;
    }

    /**
     * Initialize the default web view
     */
    private void initWebView() {
        /**
         * Dynamically add WebView to solve the problem that memory is invalid for recycling.
         * When the WebView is built, if the Context of the Activity is passed in, the reference to the memory will be kept;
         * If the ApplicationContext of the Activity is passed in the WebView build, it can prevent memory overflow, but there is a problem:
         * If you need to open a link in WebView or if you open a page with flash, or if your WebView wants to pop up a dialog,
         * Will cause a cast error from the ApplicationContext to the ActivityContext, causing your app to crash.
         * This is because when loading the flash, the system will first use your WebView as the parent control, and then draw the flash on the control.
         * It wants to find an Activity Context to draw it, but you pass in the ApplicationContext.
         */
        webView = new WebView(this);
        WebSettings webSettings = webView.getSettings();
        // The default is false. Setting true allows interaction with js
        webSettings.setJavaScriptEnabled(true);
        // Set whether WebView uses viewport
        webSettings.setUseWideViewPort(true);
        // Set whether the WebView uses the preview mode to load the interface.
        webSettings.setLoadWithOverviewMode(true);
        // Set whether to allow access to files inside WebView, allowing access by default
        webSettings.setAllowFileAccess(true);
        // Whether to allow access to content URL (Content Url) in WebView, allowed by default
        webSettings.setAllowContentAccess(true);
        // Set whether the script allows automatic opening of the popup window. The default (false) is not allowed. It is applicable to the JavaScript method window.open().
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        // Set whether WebView should not load image resources (resources accessed through http and https URI scheme) from the network, and solve the problem that images are not displayed.
        webSettings.setBlockNetworkImage(false);
        // Set font percentage to fit inline page layout
        webSettings.setTextZoom(100);
        // Set whether the WebView should support scaling using its screen zoom controls and gestures. The default value is true.
        webSettings.setSupportZoom(true);
        // Set whether the WebView should use its built-in zoom mechanism, the default value is true
        webSettings.setBuiltInZoomControls(false);
        // Set whether the WebView should display the screen zoom control when using the built-in zoom mechanism. The default value is false.
        webSettings.setDisplayZoomControls(false);
        // Set the default character encoding set, default "UTF-8"
        webSettings.setDefaultTextEncodingName("UTF-8");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            // Set whether JavaScript should be allowed to run in the file schema URL context to access content in other file scheme URLs
            webSettings.setAllowFileAccessFromFileURLs(true);
            // Set whether JavaScript should be allowed to run in the context of the file scheme URL to access content from any source
            webSettings.setAllowUniversalAccessFromFileURLs(true);
        }
        /**
         * To solve the problem that the WebView loaded after 5.0 is the beginning of Https, but the content inside the link, such as the image link for Http will not load.
         * MIXED_CONTENT_ALWAYS_ALLOW: Allows loading of content from any source, even if the origin is unsafe;
         * MIXED_CONTENT_NEVER_ALLOW: Https is not allowed to load the contents of Http, that is, it does not allow loading an unsafe resource from the origin of security;
         * MIXED_CONTENT_COMPATIBILITY_MODE: When it comes to hybrid content, WebView will try to be compatible with the style of the latest web browser.
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        /**
         * Android WebView comes with a caching mechanism:
         * Browser caching mechanism
         * Application Cache caching mechanism
         * Dom Storage caching mechanism
         * Web SQL Database caching mechanism (no longer recommended, no longer maintained, replaced by the Indexed Database caching mechanism)
         * Android started adding support for IndexedDB in 4.4, just open the switch that allows JS to execute.
         */
        // Turn on DOM storage API function Large storage space (5MB), Dom Storage mechanism is similar to Android's SharedPreference mechanism
        webSettings.setDomStorageEnabled(true);
        // Open the Application Caches feature to easily build offline apps
        webSettings.setAppCacheEnabled(true);
        /**
         * Set the cache mode
         * LOAD_CACHE_ONLY: Do not use the network, only read the local cache data
         * LOAD_NO_CACHE: Does not use the cache, only gets data from the network.
         * LOAD_DEFAULT: (default) Determines whether to fetch data from the network based on cache-control.
         * LOAD_CACHE_ELSE_NETWORK, as long as there is local, whether expired, or no-cache, use the data in the cache
         */
        if (NetWorkUtil.isNetworkConnected()) {
            /**
             * If there is a network, go to the browser cache mechanism.
             * Control the file cache mechanism based on fields such as Cache-Control (or Expires) and Last-Modified (or ETag) in the HTTP protocol header
             * Cache-Control: Used to control the effective length of the file cache in the local cache. For example, if the server returns a packet: Cache-Control: max-age=600, it means that the file should be cached locally, and the effective duration is 600 seconds (from the request). ;
             * In the next 600 seconds, if there is a request for this resource, the browser will not issue an HTTP request, but will use the locally cached file directly.
             * Expires: Same as Cache-Control, which controls the effective time of the cache. Expires is a field in the HTTP1.0 standard.
             * Cache-Control is a new field in the HTTP 1.1 standard. When these two fields appear at the same time, the Cache-Control has a higher priority.
             * Last-Modified: Identifies the latest update time of the file on the server. If the file cache expires on the next request, the browser will send the time to the server through the If-Modified-Since field.
             * The server compares the timestamp to determine if the file has been modified. If not modified, the server returns 304 telling the browser to continue using the cache; if there is a change, it returns 200 and returns the latest file.
             * ETag: The function is the same as Last-Modified, which is to identify the latest update time of the file on the server. The difference is that the value of ETag is a feature string that identifies the file.
             * ETag and Last-Modified can be used one or two at the same time as needed. When two are used at the same time, the file is considered not updated as long as one of the conditions in the base is satisfied.
             * Common usages are: Cache-Control is used with Last-Modified; Expires is used with ETag.
             * The browser caching mechanism is the mechanism of the browser kernel and is generally a standard implementation.
             */
            webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        } else {
            /**
             * If there is no network, take the Application Cache cache mechanism
             * Cache in file units, and the file has a certain update mechanism (similar to the browser cache mechanism)
             * Store static files (such as JS, CSS, font files)
             * AppCache is a supplement to the browser caching mechanism, not a replacement
             */
            webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        }

        /**
         * Interface Intermodulation Causes Remote Code Execution Vulnerability
         * The reason for the vulnerability is: When JS gets the Android object, it can call all the methods in the Android object, including the system class (java.lang.Runtime class), to perform arbitrary code execution.
         * Prior to Android 4.2, use the intercept prompt() for bug fixes
         * After Android 4.2, Google specified that the called function was annotated with @JavascriptInterface to avoid exploits;
         * The searchBoxJavaBridge_ interface causes a remote code execution vulnerability
         * Vulnerability causes: Under Android 3.0, the Android system will add a JS mapping object to the WebView via the Js interface of searchBoxJavaBridge_ by default: searchBoxJavaBridge_ object,
         * This interface may be utilized to implement remote arbitrary code and delete the searchBoxJavaBridge_ interface.
         * When the system accessibility service is turned on, in the system below Android 4.4, the WebView component provided by the system exports the accessibility and accessibilityTraversal interfaces by default.
         * They also have the threat of remote arbitrary code execution, and the same need to remove these two objects through the removeJavascriptInterface method.
         */
        try {
            webView.removeJavascriptInterface("searchBoxJavaBridge_");
            webView.removeJavascriptInterface("accessibility");
            webView.removeJavascriptInterface("accessibilityTraversal");
        } catch (Exception ex) {
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (webView != null) {
            // Execute your own life cycle
            webView.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (webView != null) {
            // Execute your own life cycle
            webView.onPause();
        }
    }

    /**
     * Release WebView to avoid memory leaks
     */
    private void releaseAllWebViewCallback() {
        if (android.os.Build.VERSION.SDK_INT < 16) {
            try {
                Field field = WebView.class.getDeclaredField("mWebViewCore");
                field = field.getType().getDeclaredField("mBrowserFrame");
                field = field.getType().getDeclaredField("sConfigCallback");
                field.setAccessible(true);
                field.set(null, null);
            } catch (Exception e) {
            }
        } else {
            try {
                Field sConfigCallback = Class.forName("android.webkit.BrowserFrame").getDeclaredField("sConfigCallback");
                if (sConfigCallback != null) {
                    sConfigCallback.setAccessible(true);
                    sConfigCallback.set(null, null);
                }
            } catch (Exception e) {
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (webView != null) {
            // Because the WebView is built with the context object of the Activity,
            // you need to remove the WebView from the parent container and then destroy the WebView.
            ViewGroup viewGroup = (ViewGroup) webView.getParent();
            if (viewGroup != null) {
                viewGroup.removeView(webView);
            }
            // Stop loading
            webView.stopLoading();
            // Solve the problem of crash caused by getSettings().setBuiltInZoomControls(true)
            webView.setVisibility(View.GONE);
            // Remove all components
            webView.removeAllViews();
            // Destroy WebView to avoid OOM exceptions
            webView.destroy();
            // WebView object is set to empty
            webView = null;
            // Release WebView
            releaseAllWebViewCallback();
        }
    }

}
