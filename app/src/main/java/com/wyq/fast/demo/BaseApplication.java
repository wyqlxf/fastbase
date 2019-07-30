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
package com.wyq.fast.demo;

import android.app.Application;
import android.view.Gravity;

import com.wyq.fast.app.AppRunningCallbacks;
import com.wyq.fast.app.FastApp;
import com.wyq.fast.interfaces.running.OnAppRunningListener;
import com.wyq.fast.model.NoticeChannel;
import com.wyq.fast.utils.LogUtil;
import com.wyq.fast.utils.ProcessUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: WangYongQi
 * Application entry
 */

public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // 判断当前是否为主进程，避免app多个进程，多次调用
        if (ProcessUtil.isMainProcess(this)) {
            // 初始化app
            FastApp.init(this);
            // 是否开启日志
            FastApp.setLogEnabled(true);
            // 创建通知栏渠道
            FastApp.setNotificationChannel(getNoticeChannelList());
            // 设置全局Toast弹窗重力位置
            FastApp.setToastGravity(Gravity.CENTER, 0, 0);
            // 添加App运行监听
            AppRunningCallbacks.getInstance().addAppRunningListener(new OnAppRunningListener() {
                @Override
                public void onRunningInForeground() {
                    LogUtil.logDebug("app 运行在前台");
                }

                @Override
                public void onRunningInBackground() {
                    LogUtil.logDebug("app 运行在后台");
                }
            });
        }
    }

    /**
     * 获取通知栏渠道列表
     *
     * @return
     */
    private List<NoticeChannel> getNoticeChannelList() {
        List<NoticeChannel> list = new ArrayList<>();
        NoticeChannel noticeChannel;
        noticeChannel = new NoticeChannel();
        noticeChannel.setId("Chat");
        noticeChannel.setName("即时通讯");
        noticeChannel.setDescription("聊天消息通知");
        list.add(noticeChannel);

        noticeChannel = new NoticeChannel();
        noticeChannel.setId("Subscription");
        noticeChannel.setName("消息订阅");
        noticeChannel.setDescription("订阅消息资讯");
        list.add(noticeChannel);
        return list;
    }

}
