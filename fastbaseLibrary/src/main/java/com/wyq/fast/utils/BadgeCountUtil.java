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

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import com.wyq.fast.app.FastApp;

import java.util.Map;

/**
 * Author: WangYongQi
 * Badge Count tool class
 */

public final class BadgeCountUtil {

    // Cache name
    private final static String spName = "FastBadgeCount";

    /**
     * add the number of unread messages of the specified type
     *
     * @param key
     * @param count
     */
    public static void addBadgeCount(String key, int count) {
        count = count + getBadgeCount(key);
        SPUtil.getInstance(spName).put(key, count);
        updateBadgeCount();
    }

    /**
     * set the number of unread messages of the specified type
     *
     * @param key
     * @param count
     */
    public static void setBadgeCount(String key, int count) {
        SPUtil.getInstance(spName).put(key, count);
        updateBadgeCount();
    }

    /**
     * return the number of unread messages of the specified type
     *
     * @param key
     * @return
     */
    public static int getBadgeCount(String key) {
        return SPUtil.getInstance(spName).getInt(key, 0);
    }

    /**
     * return the number of unread messages of all types
     *
     * @return
     */
    public static int getAllBadgeCount() {
        int count = 0;
        Map<String, ?> map = SPUtil.getInstance(spName).getAll();
        if (map != null) {
            for (Object value : map.values()) {
                if (value instanceof Integer) {
                    count += (int) value;
                }
            }
        }
        return count;
    }

    /**
     * clear all types of unread messages
     */
    public static void clearAllBadgeCount() {
        SPUtil.getInstance(spName).clear();
        updateBadgeCount();
    }

    /**
     * update the number of unread messages
     */
    public static void updateBadgeCount() {
        final int count = getAllBadgeCount();
        if (count < 0) {
            return;
        }
        try {
            if (FastApp.getContext() != null) {
                Intent badgeIntent = new Intent("android.intent.action.BADGE_COUNT_UPDATE");
                badgeIntent.putExtra("badge_count", count);
                badgeIntent.putExtra("badge_count_package_name", FastApp.getContext().getPackageName());
                badgeIntent.putExtra("badge_count_class_name", getLauncherClassName(FastApp.getContext()));
                FastApp.getContext().sendBroadcast(badgeIntent);
            } else {
                LogUtil.logWarn(BadgeCountUtil.class, "context is null");
            }
        } catch (Exception ex) {
            LogUtil.logError(BadgeCountUtil.class, ex.toString());
        }
    }

    /**
     * Retrieve launcher activity name of the application from the context
     *
     * @param context The context of the application package.
     * @return launcher activity name of this application. From the "android:name" attribute.
     */
    private static String getLauncherClassName(Context context) {
        PackageManager packageManager = context.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        // To limit the components this Intent will resolve to, by setting an
        // explicit package name.
        intent.setPackage(context.getPackageName());
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        // All Application must have 1 Activity at least.
        // Launcher activity must be found!
        ResolveInfo info = packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
        // get a ResolveInfo containing ACTION_MAIN, CATEGORY_LAUNCHER
        // if there is no Activity which has filtered by CATEGORY_DEFAULT
        if (info == null) {
            info = packageManager.resolveActivity(intent, 0);
        }
        if (info == null) {
            return "";
        } else {
            return info.activityInfo.name;
        }
    }

}
