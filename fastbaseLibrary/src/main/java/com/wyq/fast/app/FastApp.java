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

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.view.Gravity;

import com.wyq.fast.model.NoticeChannel;
import com.wyq.fast.utils.LogUtil;

import java.util.List;

/**
 * Author: WangYongQi
 * Application initialization entry
 */

public class FastApp {

    // Declare a application
    private static Application application;
    // Whether to enable debug mode for log output
    private static boolean debugLog = false;

    // Declare a toast XOffset
    private static int toastXOffset = 0;
    // Declare a toast YOffset
    private static int toastYOffset = 120;
    // Declare a toast Gravity
    private static int toastGravity = Gravity.BOTTOM | Gravity.CENTER;

    /**
     * initialization app
     *
     * @param context
     */
    public static void init(Application context) {
        if (context == null) {
            LogUtil.logWarn("initialization of FastApp failed, context cannot be empty");
            return;
        }
        application = context;
    }

    /**
     * Set whether to enable debug log mode
     *
     * @param enabled
     */
    public static void setLogEnabled(boolean enabled) {
        debugLog = enabled;
    }

    /**
     * return is enabled debug log
     *
     * @return
     */
    public static boolean isDebugLog() {
        return debugLog;
    }

    /**
     * return a context
     *
     * @return
     */
    public static Application getContext() {
        return application;
    }

    /**
     * Create a notification bar channel
     *
     * @param list
     */
    public static void setNotificationChannel(List<NoticeChannel> list) {
        if (application == null) {
            LogUtil.logWarn("Please initialize FastApp first");
            return;
        }
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        // Register the channel with the system; you can't change the importance
        // or other notification behaviors after this
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (list != null && list.size() > 0) {
                NotificationManager notificationManager = application.getSystemService(NotificationManager.class);
                for (int i = 0; i < list.size(); i++) {
                    NoticeChannel noticeChannel = list.get(i);
                    if (noticeChannel != null) {
                        NotificationChannel channelChat = new NotificationChannel(noticeChannel.getId(), noticeChannel.getName(), NotificationManager.IMPORTANCE_DEFAULT);
                        channelChat.setDescription(noticeChannel.getDescription());
                        notificationManager.createNotificationChannel(channelChat);
                    }
                }
            }
        }
    }

    /**
     * return toast XOffset
     *
     * @return
     */
    public static int getToastXOffset() {
        return toastXOffset;
    }

    /**
     * return toast YOffset
     *
     * @return
     */
    public static int getToastYOffset() {
        return toastYOffset;
    }

    /**
     * return toast Gravity
     *
     * @return
     */
    public static int getToastGravity() {
        return toastGravity;
    }

    /**
     * Set Toast the gravity.
     *
     * @param gravity
     * @param xOffset
     * @param yOffset
     */
    public static void setToastGravity(final int gravity, final int xOffset, final int yOffset) {
        toastGravity = gravity;
        toastXOffset = xOffset;
        toastYOffset = yOffset;
    }

}
