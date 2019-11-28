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
import android.text.TextUtils;
import android.widget.Toast;

import com.wyq.fast.app.FastApp;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import androidx.annotation.StringRes;

/**
 * Author: WangYongQi
 * Toast pop-up tool
 */

public final class ToastUtil {

    private static Toast toast;
    private static Object notificationManagerObj;
    private final static Object synObj = new Object();
    private final static Handler handler = new Handler(Looper.getMainLooper());

    /**
     * Pop-up window showing short toast in any thread
     *
     * @param text
     */
    public static void showShort(final CharSequence text) {
        if (FastApp.getContext() != null) {
            if (!TextUtils.isEmpty(text)) {
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
                LogUtil.logWarn(ToastUtil.class, "text is null");
            }
        } else {
            LogUtil.logWarn(ToastUtil.class, "context is null");
        }
    }

    /**
     * Pop-up window showing short toast in any thread
     *
     * @param resId
     */
    public static void showShort(@StringRes final int resId) {
        if (FastApp.getContext() != null) {
            final CharSequence text = FastApp.getContext().getResources().getText(resId);
            if (!TextUtils.isEmpty(text)) {
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
                LogUtil.logWarn(ToastUtil.class, "text is null");
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
            if (!TextUtils.isEmpty(text)) {
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
                LogUtil.logWarn(ToastUtil.class, "text is null");
            }
        } else {
            LogUtil.logWarn(ToastUtil.class, "context is null");
        }
    }

    /**
     * Pop-up window showing long toast in any thread
     *
     * @param resId
     */
    public static void showLong(@StringRes final int resId) {
        if (FastApp.getContext() != null) {
            final CharSequence text = FastApp.getContext().getResources().getText(resId);
            if (!TextUtils.isEmpty(text)) {
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
                LogUtil.logWarn(ToastUtil.class, "text is null");
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
                hookTNToast(toast);
            }
            if (!NotificationUtil.isNotificationEnabled()) {
                hookNMToast(toast);
            }
            toast.setGravity(FastApp.getToastGravity(), FastApp.getToastXOffset(), FastApp.getToastYOffset());
            toast.show();
        }
    }

    /**
     * Solve the problem that Toast cannot pop up after disabling notification permissions on some models
     *
     * @param toast
     */
    private static void hookNMToast(Toast toast) {
        try {
            Method serviceMethod = Toast.class.getDeclaredMethod("getService");
            serviceMethod.setAccessible(true);
            // hook NotificationManager
            if (notificationManagerObj == null) {
                notificationManagerObj = serviceMethod.invoke(null);
                Class notificationManagerClass = Class.forName("android.app.INotificationManager");
                Object notificationManagerProxy = Proxy.newProxyInstance(toast.getClass().getClassLoader(), new Class[]{notificationManagerClass}, new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        // Mandatory use of system Toast
                        if ("enqueueToast".equals(method.getName()) || "enqueueToastEx".equals(method.getName())) {
                            args[0] = "android";
                        }
                        return method.invoke(notificationManagerObj, args);
                    }
                });
                Field sServiceFiled = Toast.class.getDeclaredField("sService");
                sServiceFiled.setAccessible(true);
                sServiceFiled.set(null, notificationManagerProxy);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * On android 7.1.1, if you display two windowManager.LayoutParams.type at the same time, you will get an error.
     * Solve the crash with the reflection agent Toast&NT$Handler.
     *
     * @param toast
     */
    private static void hookTNToast(Toast toast) {
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
