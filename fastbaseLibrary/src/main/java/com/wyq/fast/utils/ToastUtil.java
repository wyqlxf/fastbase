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

import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

import com.wyq.fast.app.FastApp;

import java.lang.reflect.Field;

/**
 * Author: WangYongQi
 * Toast pop-up tool
 */

public final class ToastUtil {

    private static Toast toast;
    private final static Object synObj = new Object();
    private final static Handler handler = new Handler(Looper.getMainLooper());

    /**
     * Pop-up window showing short toast in any thread
     *
     * @param text
     */
    public static void showShort(final CharSequence text) {
        if (FastApp.getContext() != null) {
            // If it is the main thread
            if (ThreadUtil.isMainThread()) {
                showToast(text, Toast.LENGTH_SHORT);
            } else {
                Runnable callbackRunnable = new Runnable() {
                    @Override
                    public void run() {
                        // Perform tasks in the main thread
                        showToast(text, Toast.LENGTH_SHORT);
                    }
                };
                handler.post(callbackRunnable);
            }
        } else {
            LogUtil.logWarn(ToastUtil.class, "context is null");
        }
    }

    /**
     * Pop-up window showing long toast in any thread
     *
     * @param text
     */
    public static void showLong(final CharSequence text) {
        if (FastApp.getContext() != null) {
            // If it is the main thread
            if (ThreadUtil.isMainThread()) {
                showToast(text, Toast.LENGTH_LONG);
            } else {
                Runnable callbackRunnable = new Runnable() {
                    @Override
                    public void run() {
                        // Perform tasks in the main thread
                        showToast(text, Toast.LENGTH_LONG);
                    }
                };
                handler.post(callbackRunnable);
            }
        } else {
            LogUtil.logWarn(ToastUtil.class, "context is null");
        }
    }

    /**
     * Pop-up window showing toast
     *
     * @param text
     * @param duration
     */
    private static void showToast(final CharSequence text, int duration) {
        synchronized (synObj) {
            if (toast != null) {
                toast.cancel();
                toast = null;
            }
            toast = Toast.makeText(FastApp.getContext(), text, duration);
            // If the SDK version of the software equal to 7.1
            if (Build.VERSION.SDK_INT == Build.VERSION_CODES.N_MR1) {
                hook(toast);
            }
            toast.setGravity(FastApp.getToastGravity(), FastApp.getToastXOffset(), FastApp.getToastYOffset());
            toast.show();
        }
    }

    /**
     * On android 7.1.1, if you display two windowManager.LayoutParams.type at the same time, you will get an error.
     * Solve the crash with the reflection agent Toast&NT$Handler.
     *
     * @param toast
     */
    private static void hook(Toast toast) {
        try {
            if (toast != null && mFieldTN != null) {
                Object object = mFieldTN.get(toast);
                if (mFieldHandlerTN != null) {
                    Handler preHandler = (Handler) mFieldHandlerTN.get(object);
                    mFieldHandlerTN.set(object, new SafeHandler(preHandler));
                }
            }
        } catch (Exception e) {
        }
    }

    private static Field mFieldTN;
    private static Field mFieldHandlerTN;

    static {
        try {
            mFieldTN = Toast.class.getDeclaredField("mTN");
            if (mFieldTN != null) {
                mFieldTN.setAccessible(true);
                mFieldHandlerTN = mFieldTN.getType().getDeclaredField("mHandler");
                if (mFieldHandlerTN != null) {
                    mFieldHandlerTN.setAccessible(true);
                }
            }
        } catch (Exception e) {
        }
    }

    private static class SafeHandler extends Handler {
        private Handler handler;

        public SafeHandler(Handler handler) {
            this.handler = handler;
        }

        @Override
        public void dispatchMessage(Message msg) {
            try {
                super.dispatchMessage(msg);
            } catch (Exception e) {
            }
        }

        @Override
        public void handleMessage(Message msg) {
            if (handler != null) {
                // Need to delegate to the original Handler to execute
                handler.handleMessage(msg);
            }
        }
    }

}
