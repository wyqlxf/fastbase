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
package com.wyq.fast.app;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.wyq.fast.interfaces.running.OnAppRunningListener;
import com.wyq.fast.utils.LogUtil;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Author: WangYongQi
 * App running callback
 */

public class AppRunningCallbacks implements Application.ActivityLifecycleCallbacks {

    private boolean paused = true;
    private boolean foreground = false;
    private Runnable runnable;
    private List<OnAppRunningListener> listeners = new CopyOnWriteArrayList<>();

    private volatile static AppRunningCallbacks instance;

    private AppRunningCallbacks() {
    }

    public static AppRunningCallbacks getInstance() {
        if (instance == null) {
            synchronized (AppRunningCallbacks.class) {
                if (instance == null) {
                    instance = new AppRunningCallbacks();
                    if (FastApp.getContext() != null) {
                        FastApp.getContext().registerActivityLifecycleCallbacks(instance);
                    } else {
                        LogUtil.logWarn(AppRunningCallbacks.class, "context is null");
                    }
                }
            }
        }
        return instance;
    }

    /**
     * Whether the application is running in the foreground
     *
     * @return
     */
    public boolean isRunningInForeground() {
        return foreground;
    }

    /**
     * Whether the application is running in the background
     *
     * @return
     */
    public boolean isRunningInBackground() {
        return !foreground;
    }

    /**
     * Add app to run listener
     *
     * @param listener
     */
    public void addAppRunningListener(OnAppRunningListener listener) {
        removeAppRunningListener(listener);
        if (listener != null) {
            listeners.add(listener);
        }
    }

    /**
     * Remove app to run listener
     *
     * @param listener
     */
    public void removeAppRunningListener(OnAppRunningListener listener) {
        if (listener != null && listeners.contains(listener)) {
            listeners.remove(listener);
        }
    }

    @Override
    public void onActivityResumed(Activity activity) {
        paused = false;
        if (runnable != null) {
            FastApp.getDefaultHandler().removeCallbacks(runnable);
        }
        if (!foreground) {
            foreground = true;
            for (OnAppRunningListener listener : listeners) {
                if (listener != null) {
                    listener.onRunningInForeground();
                }
            }
        }
    }

    @Override
    public void onActivityPaused(Activity activity) {
        paused = true;
        if (runnable != null) {
            FastApp.getDefaultHandler().removeCallbacks(runnable);
        }
        // In order to solve the problem of switching from the foreground to the background and then quickly returning to the foreground, the message is delayed here.
        FastApp.getDefaultHandler().postDelayed(runnable = new Runnable() {
            @Override
            public void run() {
                if (foreground && paused) {
                    foreground = false;
                    for (OnAppRunningListener listener : listeners) {
                        if (listener != null) {
                            listener.onRunningInBackground();
                        }
                    }
                } else {
                }
            }
        }, 500);
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
    }

    @Override
    public void onActivityStarted(Activity activity) {
    }

    @Override
    public void onActivityStopped(Activity activity) {
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
    }

}
