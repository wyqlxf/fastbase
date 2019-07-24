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

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.view.View;

import com.wyq.fast.core.asynctask.FastAsyncTask;
import com.wyq.fast.demo.calculator.CalculatorActivity;
import com.wyq.fast.demo.cipher.CipherActivity;
import com.wyq.fast.interfaces.asynctask.OnCancelled;
import com.wyq.fast.interfaces.asynctask.OnDoInBackground;
import com.wyq.fast.interfaces.asynctask.OnPostExecute;
import com.wyq.fast.interfaces.handler.OnHandlerListener;
import com.wyq.fast.receiver.NetworkChangeReceiver;
import com.wyq.fast.utils.AppUtil;
import com.wyq.fast.utils.BadgeCountUtil;
import com.wyq.fast.utils.LogUtil;
import com.wyq.fast.utils.NetWorkUtil;
import com.wyq.fast.utils.NotificationUtil;
import com.wyq.fast.utils.ObjectValueUtil;
import com.wyq.fast.utils.PermissionUtil;
import com.wyq.fast.utils.PixelXmlMarkUtil;
import com.wyq.fast.utils.ProcessUtil;
import com.wyq.fast.utils.RandomUtil;
import com.wyq.fast.utils.SPUtil;
import com.wyq.fast.utils.ScreenUtil;
import com.wyq.fast.utils.ThreadUtil;
import com.wyq.fast.utils.ToastUtil;
import com.wyq.fast.utils.ViewBindUtil;
import com.wyq.fast.utils.ViewBindUtil.OnClick;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Author: WangYongQi
 * main activity of this sample
 */

public class MainActivity extends BaseAppCompatActivity {

    // 异步任务
    private FastAsyncTask.Builder<Integer, String, String> builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 设置绑定
        ViewBindUtil.bind(this);
        // 注册网络变化监听
        registerNetworkListener(new NetworkChangeReceiver() {
            @Override
            protected void onNetworkChange(boolean isConnection) {
                if (isConnection) {
                    ToastUtil.showShort("检测到当前网络状态：有网络!");
                } else {
                    ToastUtil.showShort("检测到当前网络状态：无网络!");
                }
            }
        });

        // 注册Handler消息监听
        registerHandlerListener(new OnHandlerListener() {
            @Override
            public void onHandleMessage(Message msg) {
                switch (msg.what) {
                    case 0:
                        ToastUtil.showShort("收到一个延迟的空消息");
                        break;
                }
            }
        });

        // 延时发送一个的空消息
        sendEmptyMessageDelayed(0, 3000);
    }

    @OnClick({R.id.btnUtils, R.id.btnMortgageCalculator, R.id.btnCipher, R.id.btnPixelXmlMark})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnUtils:
                // 判断当前异步任务是否不为空，并且正在运行
                if (builder != null && builder.isRunning()) {
                    // 取消之前的异步任务
                    builder.cancel(true);
                }
                builder = FastAsyncTask.newBuilder();
                builder.setDoInBackground(new OnDoInBackground<Integer, String>() {
                    @Override
                    public String doInBackground(Integer... integers) {
                        try {
                            ToastUtil.showShort("休眠3秒后请查看日志:TagFast，欢迎狂点");
                            // 休眠3秒
                            Thread.sleep(3000);
                        } catch (Exception ex) {
                        }
                        return "" + System.currentTimeMillis();
                    }
                });
                builder.setPostExecute(new OnPostExecute<String>() {
                    @Override
                    public void onPostExecute(String result) {
                        LogUtil.logDebug("异步任务已完成: " + result);
                        // 普通工具类演示
                        utilToLog();
                    }
                });
                builder.setCancelled(new OnCancelled<String>() {
                    @Override
                    public void onCancelled(String result) {
                        LogUtil.logDebug("异步任务被取消: " + result);
                    }
                });
                // 设置在同一时刻可以有30个任务正在执行，并且最多能够存储300个任务
                Executor exec = new ThreadPoolExecutor(30, 300, 10,
                        TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
                // 启动异步任务
                builder.start(exec);
                // 跳转工具类界面
                break;

            case R.id.btnMortgageCalculator:
                // 房贷计算器
                startActivity(new Intent(this, CalculatorActivity.class));
                break;

            case R.id.btnCipher:
                // 字符串加密解密
                startActivity(new Intent(this, CipherActivity.class));
                break;

            case R.id.btnPixelXmlMark:
                // 生成屏幕分辨率(这里需要注意,6.0以上需要申请权限)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !PermissionUtil.isHasPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    ToastUtil.showShort("请先允许程序写入外部存储的权限！");
                } else {
                    // 已经有了读写权限，开启一个子线程去制作xml文件，可以复制这些文件到你的主项目进行屏幕分辨率适配
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            final String supportDimensions[] = {"240x320", "480x800", "720x1280", "1080x1920", "1080x2220", "1440x2560"};
                            /**
                             *
                             * @param UI效果图的基准宽度
                             * @param UI效果图的基准高度
                             * @param 自定义维度（输入需要适配的各个屏幕分辨率数组）
                             * @param 生成的像素大小是否支持负数
                             */
                            PixelXmlMarkUtil.markXmlSaveToSdCard(720, 1280, supportDimensions, true);
                            // 弹窗提醒 (允许在任意线程中弹窗)
                            ToastUtil.showShort("屏幕分辨率文件已生成完毕，并保存在设备存储卡res目录下");
                        }
                    }).start();
                }
                break;
        }
    }

    /**
     * 调用工具类方法，并打印日志
     */
    private void utilToLog() {
        StringBuffer buffer = new StringBuffer();
        String log;
        /**
         * AppUtil类
         */
        log = "应用程序包名：" + AppUtil.getPackageName() + "\n";
        buffer.append(log);
        LogUtil.logDebug(log);
        log = "应用程序版本名：" + AppUtil.getVersionName() + "\n";
        buffer.append(log);
        LogUtil.logDebug(log);
        log = "应用程序版本号：" + AppUtil.getVersionCode() + "\n";
        buffer.append(log);
        LogUtil.logDebug(log);
        log = "清空数据或卸载应用才会发生变化的AppUUID：" + AppUtil.getAppUUID() + "\n";
        buffer.append(log);
        LogUtil.logDebug(log);
        log = "清空数据或卸载应用都不会发生变化的AppUUID：" + AppUtil.getAppFixedUUID() + "\n";
        buffer.append(log);
        LogUtil.logDebug(log);

        /**
         * BadgeCountUtil类
         */
        // 清除所有的应用消息未读数
        BadgeCountUtil.clearAllBadgeCount();
        // 设置应用 “chat”类型的消息未读数量
        BadgeCountUtil.setBadgeCount("chat", 1);
        // 设置应用 “news”类型的消息未读数量
        BadgeCountUtil.setBadgeCount("news", 2);
        // 获取应用 “chat”类型的消息未读数量
        final int chatCount = BadgeCountUtil.getBadgeCount("chat");
        log = "“chat”类型的消息未读数量：" + chatCount + "\n";
        buffer.append(log);
        LogUtil.logDebug(log);
        // 获取应用 “news”类型的消息未读数量
        final int newsCount = BadgeCountUtil.getBadgeCount("news");
        log = "“news”类型的消息未读数量：" + newsCount + "\n";
        buffer.append(log);
        LogUtil.logDebug(log);
        // 获取应用总的未读消息数
        final int totalCount = BadgeCountUtil.getAllBadgeCount();
        log = "获取应用总的未读消息数：" + totalCount + "\n";
        buffer.append(log);
        LogUtil.logDebug(log);
        // 更新应用桌面消息未读数
        BadgeCountUtil.updateBadgeCount();

        /**
         * NetWorkUtil
         */
        log = "获取当前网络类型：" + NetWorkUtil.getNetworkType() + "\n";
        buffer.append(log);
        LogUtil.logDebug(log);
        log = "当前是否有网络：" + NetWorkUtil.isNetworkConnected() + "\n";
        buffer.append(log);
        LogUtil.logDebug(log);
        log = "当前WIFI是否已打开：" + NetWorkUtil.isWifiEnabled() + "\n";
        buffer.append(log);
        LogUtil.logDebug(log);
        log = "当前网络是否为WIFI：" + NetWorkUtil.isWifiConnected() + "\n";
        buffer.append(log);
        LogUtil.logDebug(log);
        log = "当前网络是否设置了代理：" + NetWorkUtil.isWifiProxy() + "\n";
        buffer.append(log);
        LogUtil.logDebug(log);

        /**
         * NotificationUtil类
         */
        log = "通知栏权限是否开启：" + NotificationUtil.isNotificationEnabled() + "\n";
        buffer.append(log);
        LogUtil.logDebug(log);

        /**
         * ObjectValueUtil类
         */
        Bundle bundle = new Bundle();
        bundle.putString("title", "this is bundle title");
        final String title = ObjectValueUtil.getString(bundle, "title");
        log = title + "\n";
        buffer.append(log);
        LogUtil.logDebug(log);

        /**
         * ProcessUtil类
         */
        log = "当前是否为主进程: " + ProcessUtil.isMainProcess(getContext()) + "\n";
        buffer.append(log);
        LogUtil.logDebug(log);
        log = "获取当前进程名称: " + ProcessUtil.getCurProcessName() + "\n";
        buffer.append(log);
        LogUtil.logDebug(log);

        /**
         * RandomUtil类
         */
        // 获取一个长度为16位的随机key(纯数字)
        String randomKey = RandomUtil.getRandomNumber(16);
        log = "随机key(纯数字): " + randomKey + "\n";
        buffer.append(log);
        LogUtil.logDebug(log);
        // 获取一个长度为32位的随机key(数字+字母)
        randomKey = RandomUtil.getRandomNumberLetter(32);
        log = "随机key(数字+字母): " + randomKey + "\n";
        buffer.append(log);
        LogUtil.logDebug(log);

        /**
         * SPUtil类
         */
        final String spName = "Random";
        // 保存随机key
        SPUtil.getInstance(spName).put("random_key", randomKey);
        // 获取随机Key
        randomKey = SPUtil.getInstance(spName).getString("random_key");
        log = "随机key：" + randomKey + "\n";
        buffer.append(log);
        LogUtil.logDebug(log);

        /**
         * ScreenUtil类
         */
        log = "屏幕宽度：" + ScreenUtil.getScreenWidth() + "\n";
        buffer.append(log);
        LogUtil.logDebug(log);
        log = "屏幕高度：" + ScreenUtil.getScreenHeight() + "\n";
        buffer.append(log);
        LogUtil.logDebug(log);
        log = "状态栏高度：" + ScreenUtil.getStatusBarHeight() + "\n";
        buffer.append(log);
        LogUtil.logDebug(log);

        /**
         * ThreadUtil类
         */
        log = "当前是否为主线程: " + ThreadUtil.isMainThread() + "\n";
        buffer.append(log);
        LogUtil.logDebug(log);
    }

}
