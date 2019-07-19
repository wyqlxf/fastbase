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
package com.wyq.fast.utils;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.view.View;

import com.wyq.fast.app.FastApp;

import androidx.annotation.NonNull;

/**
 * Author: WangYongQi
 * Screen tool class
 */

public final class ScreenUtil {

    /**
     * return screen width
     *
     * @return
     */
    public static int getScreenWidth() {
        if (FastApp.getContext() != null) {
            Resources resources = FastApp.getContext().getResources();
            DisplayMetrics dm = resources.getDisplayMetrics();
            return dm.widthPixels;
        } else {
            LogUtil.logWarn(ScreenUtil.class, "context is null");
            return 0;
        }
    }

    /**
     * return screen height
     *
     * @return
     */
    public static int getScreenHeight() {
        if (FastApp.getContext() != null) {
            Resources resources = FastApp.getContext().getResources();
            DisplayMetrics dm = resources.getDisplayMetrics();
            return dm.heightPixels;
        } else {
            LogUtil.logWarn(ScreenUtil.class, "context is null");
            return 0;
        }
    }

    /**
     * dp to px
     *
     * @param dpValue
     * @return
     */
    public static int dpToPx(float dpValue) {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * px to dp
     *
     * @param pxValue
     * @return
     */
    public static int pxToDp(float pxValue) {
        float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * sp to px
     *
     * @param spValue
     * @return
     */
    public static int spToPx(float spValue) {
        final float fontScale = Resources.getSystem().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * px to sp
     *
     * @param pxValue
     * @return
     */
    public static int pxToSp(final float pxValue) {
        final float fontScale = Resources.getSystem().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * Pass in the view, return an array of the size of the view
     *
     * @param view
     * @return
     */
    public static float[] getViewSize(View view) {
        float size[] = new float[]{0, 0};
        if (view != null) {
            int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            view.measure(w, h);
            int width = view.getMeasuredWidth();
            int height = view.getMeasuredHeight();
            size[0] = width;
            size[1] = height;
        } else {
            LogUtil.logWarn(ScreenUtil.class, "The incoming view is empty, Unable to get view size");
        }
        return size;
    }

    /**
     * return status bar height
     *
     * @return
     */
    public static int getStatusBarHeight() {
        int height = dpToPx(24);
        try {
            if (FastApp.getContext() != null) {
                int resourceId = FastApp.getContext().getResources().getIdentifier("status_bar_height", "dimen", "android");
                if (resourceId > 0) {
                    height = FastApp.getContext().getResources().getDimensionPixelSize(resourceId);
                }
            } else {
                LogUtil.logWarn(ScreenUtil.class, "context is null");
            }
        } catch (Exception ex) {
            LogUtil.logWarn(ScreenUtil.class, "Failed to get status bar, use default height");
        }
        return height;
    }

    /**
     * return screen is locked
     *
     * @return
     */
    public static boolean isScreenLock() {
        if (FastApp.getContext() != null) {
            KeyguardManager km = (KeyguardManager) FastApp.getContext().getSystemService(Context.KEYGUARD_SERVICE);
            if (km == null) {
                return false;
            } else {
                return km.inKeyguardRestrictedInputMode();
            }
        } else {
            LogUtil.logWarn(ScreenUtil.class, "context is null");
            return false;
        }
    }

    /**
     * Screen capture
     *
     * @param activity
     * @return
     */
    public static Bitmap screenShot(@NonNull final Activity activity) {
        return screenShot(activity, false);
    }

    /**
     * Screen capture ,Whether to intercept the status bar
     *
     * @param activity
     * @param isDeleteStatusBar
     * @return
     */
    public static Bitmap screenShot(@NonNull final Activity activity, boolean isDeleteStatusBar) {
        View decorView = activity.getWindow().getDecorView();
        decorView.setDrawingCacheEnabled(true);
        decorView.setWillNotCacheDrawing(false);
        Bitmap bmp = decorView.getDrawingCache();
        if (bmp == null) return null;
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        Bitmap ret;
        if (isDeleteStatusBar) {
            Resources resources = activity.getResources();
            int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
            int statusBarHeight = resources.getDimensionPixelSize(resourceId);
            ret = Bitmap.createBitmap(bmp, 0, statusBarHeight, dm.widthPixels, dm.heightPixels - statusBarHeight);
        } else {
            ret = Bitmap.createBitmap(bmp, 0, 0, dm.widthPixels, dm.heightPixels);
        }
        decorView.destroyDrawingCache();
        return ret;
    }

}
