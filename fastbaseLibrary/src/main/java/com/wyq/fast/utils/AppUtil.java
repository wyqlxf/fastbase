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

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;

import com.wyq.fast.app.FastApp;
import com.wyq.fast.config.Config;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import androidx.core.content.ContextCompat;

/**
 * Author: WangYongQi
 * App tool class
 */

public final class AppUtil {

    /**
     * Return the app package name
     *
     * @return
     */
    public static String getPackageName() {
        if (FastApp.getContext() != null) {
            return FastApp.getContext().getPackageName();
        } else {
            LogUtil.logWarn(AppUtil.class, "context is null");
            return "";
        }
    }

    /**
     * Return the app version name
     *
     * @return
     */
    public static String getVersionName() {
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            return packageInfo.versionName;
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.logError(AppUtil.class, e.toString());
            return "";
        }
    }

    /**
     * Return app version number
     *
     * @return
     */
    public static int getVersionCode() {
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.logError(AppUtil.class, e.toString());
            return 0;
        }
    }

    /**
     * Return app uuid
     *
     * @return
     */
    public static String getAppUUID() {
        String randomUUID = SPUtil.getInstance(Config.SP_APP_UUID_NAME).getString("randomUUID");
        if (!TextUtils.isEmpty(randomUUID)) {
            return randomUUID;
        }
        /**
         * Generate variable non-repeating UUIDs
         * UUID is a number generated on a machine that is guaranteed to be unique to all machines in the same time and space.
         * Usually the platform will provide the generated API. According to the standards developed by the Open Software Foundation (OSF),
         * Ethernet card addresses, nanosecond time, chip ID codes, and many possible numbers are used.
         * The UUID consists of the following parts:
         * (1) The current date and time, the first part of the UUID is related to time.
         * If you generate a UUID after a UUID is generated, the first part is different and the others are the same.
         * (2) Clock sequence
         * (3) Global unique machine identification number. If there is a network card,
         * it is obtained from the MAC address of the network card. No network card is obtained in other ways.
         */
        final String uuid = UUID.randomUUID().toString();
        SPUtil.getInstance(Config.SP_APP_UUID_NAME).put("randomUUID", uuid);
        return uuid;
    }

    /**
     * Return a fixed immutable app uuid
     *
     * @return
     */
    @SuppressLint("MissingPermission")
    public static String getAppFixedUUID() {
        String randomUUID = SPUtil.getInstance(Config.SP_APP_UUID_NAME).getString("fixedUUID");
        if (!TextUtils.isEmpty(randomUUID)) {
            return randomUUID;
        }
        String serial = "unknown";
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if (ContextCompat.checkSelfPermission(FastApp.getContext(), Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                    serial = Build.getSerial();
                } else {
                    serial = Build.class.getField("SERIAL").get(null).toString();
                }
            } else {
                // https://developer.android.com/reference/android/os/Build
                serial = Build.SERIAL;
            }
        } catch (Exception ex) {
            LogUtil.logError(AppUtil.class, ex.toString());
        }
        String idShort = "";
        try {
            idShort = "35" + Build.BOARD.length() % 10
                    + Build.BRAND.length() % 10 + Build.CPU_ABI.length() % 10
                    + Build.DEVICE.length() % 10 + Build.DISPLAY.length() % 10
                    + Build.HOST.length() % 10 + Build.ID.length() % 10
                    + Build.MANUFACTURER.length() % 10 + Build.MODEL.length()
                    % 10 + Build.PRODUCT.length() % 10 + Build.TAGS.length()
                    % 10 + Build.TYPE.length() % 10 + Build.USER.length() % 10;
        } catch (Exception ex) {
            LogUtil.logError(AppUtil.class, ex.toString());
        }
        final String uuid = new UUID(idShort.hashCode(), serial.hashCode()).toString();
        SPUtil.getInstance(Config.SP_APP_UUID_NAME).put("fixedUUID", uuid);
        return uuid;
    }

    /**
     * Return app icon
     *
     * @return
     */
    public static Drawable getApplicationIcon() {
        try {
            PackageManager packageManager = getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
            return packageInfo.applicationInfo.loadIcon(packageManager);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.logError(AppUtil.class, e.toString());
        }
        return null;
    }

    /**
     * Whether the application is running in the background
     *
     * @return
     */
    public static boolean isRunningInBackground() {
        ActivityManager activityManager = getActivityManager();
        if (activityManager != null) {
            List<ActivityManager.RunningTaskInfo> taskList = activityManager.getRunningTasks(1);
            if (taskList != null && !taskList.isEmpty()) {
                ComponentName topActivity = taskList.get(0).topActivity;
                if (topActivity != null && !topActivity.getPackageName().equals(getPackageName())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Return all installed apps, no system apps
     *
     * @return
     */
    public static List<PackageInfo> getInstalledPackages() {
        List<PackageInfo> list = new ArrayList<>();
        PackageManager packageManager = getPackageManager();
        if (packageManager != null) {
            List<PackageInfo> packageInfoList = packageManager.getInstalledPackages(0);
            for (int i = 0; i < packageInfoList.size(); i++) {
                if ((packageInfoList.get(i).applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                    list.add(packageInfoList.get(i));
                }
            }
        }
        return list;
    }

    /**
     * Return to PackageManager
     *
     * @return
     */
    private static PackageManager getPackageManager() {
        if (FastApp.getContext() != null) {
            return FastApp.getContext().getPackageManager();
        } else {
            LogUtil.logWarn(AppUtil.class, "context is null");
            return null;
        }
    }

    /**
     * Return to ActivityManager
     *
     * @return
     */
    private static ActivityManager getActivityManager() {
        if (FastApp.getContext() != null) {
            return (ActivityManager) FastApp.getContext().getSystemService(Context.ACTIVITY_SERVICE);
        } else {
            LogUtil.logWarn(AppUtil.class, "context is null");
            return null;
        }
    }

}
