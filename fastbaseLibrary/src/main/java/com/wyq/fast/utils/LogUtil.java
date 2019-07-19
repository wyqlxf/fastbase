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

import android.text.TextUtils;
import android.util.Log;

import com.wyq.fast.app.FastApp;

/**
 * Author: WangYongQi
 * Log output
 */

public final class LogUtil {

    // Log label name
    private static final String logTagName = "TagFast";

    public static void logVerbose(String string) {
        if (FastApp.isDebugLog()) {
            logVerbose(null, string);
        }
    }

    public static void logVerbose(Class<?> c, String string) {
        if (FastApp.isDebugLog()) {
            final String className = c != null ? c.getName() : "";
            if (TextUtils.isEmpty(className)) {
                Log.v(logTagName, "" + string);
            } else {
                Log.v(logTagName, className + ":" + string);
            }
        }
    }

    public static void logDebug(String string) {
        if (FastApp.isDebugLog()) {
            logDebug(null, string);
        }
    }

    public static void logDebug(Class<?> c, String string) {
        if (FastApp.isDebugLog()) {
            final String className = c != null ? c.getName() : "";
            if (TextUtils.isEmpty(className)) {
                Log.d(logTagName, "" + string);
            } else {
                Log.d(logTagName, className + ":" + string);
            }
        }
    }

    public static void logInfo(String string) {
        if (FastApp.isDebugLog()) {
            logInfo(null, string);
        }
    }

    public static void logInfo(Class<?> c, String string) {
        if (FastApp.isDebugLog()) {
            final String className = c != null ? c.getName() : "";
            if (TextUtils.isEmpty(className)) {
                Log.i(logTagName, "" + string);
            } else {
                Log.i(logTagName, className + ":" + string);
            }
        }
    }

    public static void logWarn(String string) {
        if (FastApp.isDebugLog()) {
            logWarn(null, string);
        }
    }

    public static void logWarn(Class<?> c, String string) {
        if (FastApp.isDebugLog()) {
            final String className = c != null ? c.getName() : "";
            if (TextUtils.isEmpty(className)) {
                Log.w(logTagName, "" + string);
            } else {
                Log.w(logTagName, className + ":" + string);
            }
        }
    }

    public static void logError(String string) {
        if (FastApp.isDebugLog()) {
            logError(null, string);
        }
    }

    public static void logError(Class<?> c, String string) {
        if (FastApp.isDebugLog()) {
            final String className = c != null ? c.getName() : "";
            if (TextUtils.isEmpty(className)) {
                Log.e(logTagName, "" + string);
            } else {
                Log.e(logTagName, className + ":" + string);
            }
        }
    }

}
