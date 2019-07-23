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

import android.app.ActivityManager;
import android.content.Context;

import com.wyq.fast.app.FastApp;

import java.util.List;

/**
 * Author: WangYongQi
 * Process tool class
 */

public final class ProcessUtil {

    /**
     * Return whether app running in the main process
     *
     * @return
     */
    public static boolean isMainProcess(Context context) {
        return context.getPackageName().equals(getCurProcessName(context));
    }

    /**
     * Return the current process name
     *
     * @return
     */
    public static String getCurProcessName() {
        if (FastApp.getContext() != null) {
            return getCurProcessName(FastApp.getContext());
        } else {
            LogUtil.logWarn(ProcessUtil.class, "context is null");
            return "";
        }
    }

    /**
     * Return the current process name
     *
     * @return
     */
    private static String getCurProcessName(Context context) {
        try {
            int pid = android.os.Process.myPid();
            ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningAppProcessInfo> runningApps = activityManager.getRunningAppProcesses();
            if (runningApps == null) {
                return "";
            }
            for (ActivityManager.RunningAppProcessInfo info : runningApps) {
                if (info.pid == pid) {
                    return info.processName;
                }
            }
        } catch (Exception ex) {
            LogUtil.logWarn(ProcessUtil.class, ex.toString());
        }
        return "";
    }

}
