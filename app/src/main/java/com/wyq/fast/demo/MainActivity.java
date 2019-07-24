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
import com.wyq.fast.core.cipher.Cipher;
import com.wyq.fast.core.cipher.CipherFactory;
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
import com.wyq.fast.utils.NotificationUtil;
import com.wyq.fast.utils.ObjectValueUtil;
import com.wyq.fast.utils.PermissionUtil;
import com.wyq.fast.utils.PixelXmlMarkUtil;
import com.wyq.fast.utils.RandomUtil;
import com.wyq.fast.utils.SPUtil;
import com.wyq.fast.utils.ScreenUtil;
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
                        LogUtil.logDebug("通知栏权限是否开启：" + NotificationUtil.isNotificationEnabled());
                        break;
                }
            }
        });

        // 发送一个的空消息
        sendEmptyMessage(0);

        // 根据对象获取值
        String title = ObjectValueUtil.getString(getIntent(), "title");
        LogUtil.logDebug("title: " + title);

        // 获取一个长度为16位的随机key(数字+字母)
        final String randomKey = RandomUtil.getRandomNumberLetter(16);
        // 字符串加密or解密
        Cipher cipher = CipherFactory.create(Cipher.EncryptType.AES);
        String data = cipher.encryptString(randomKey, "哈哈哈哈哈哈哈哈");
        LogUtil.logDebug("加密数据: " + data);
        data = cipher.decryptString(randomKey, data);
        LogUtil.logDebug("解密数据: " + data);

        // 在主线程中Toast弹窗，注意：如果在同一时间调用多次，只会弹窗显示最后一次
        for (int i = 0; i < 10; i++) {
            ToastUtil.showShort("欢迎进入主界面，计数：" + i);
        }

        // 轻量级缓存工具
        final String spName = "Random";
        // 保存随机key
        SPUtil.getInstance(spName).put("random_key", randomKey);
        // 日志输出上一次保存的随机key
        LogUtil.logDebug("随机key：" + SPUtil.getInstance(spName).getString("random_key"));

        // 添加聊天未读数
        BadgeCountUtil.addBadgeCount("chat", 10);
        int chatCount = BadgeCountUtil.getBadgeCount("chat");
        // 设置新闻未读数
        BadgeCountUtil.setBadgeCount("news", 20);
        int newsCount = BadgeCountUtil.getBadgeCount("news");
        // 获取总的未读数
        int allBadgeCount = BadgeCountUtil.getAllBadgeCount();
        LogUtil.logDebug("聊天未读数:" + chatCount + "    新闻未读数:" + newsCount + "    总的未读数:" + allBadgeCount);

        // 获取App各种信息
        LogUtil.logDebug("包名：" + AppUtil.getPackageName() + "  版本名：" + AppUtil.getVersionName() + "  版本号：" + AppUtil.getVersionCode());
        // 清空数据或者卸载app会变
        LogUtil.logDebug(AppUtil.getAppUUID());
        // 清空数据或者卸载app不会改变
        LogUtil.logDebug(AppUtil.getAppFixedUUID());
    }

    /**
     * 异步任务
     */
    private void onFastAsyncTask() {
        // 判断当前异步任务是否不为空，并且正在运行
        if (builder != null && builder.isRunning()) {
            // 取消之前的异步任务
            builder.cancel(true);
        }
        builder = FastAsyncTask.newBuilder();
        builder.setDoInBackground(new OnDoInBackground<Integer, String>() {
            @Override
            public String doInBackground(Integer... integers) {
                return "屏幕宽高：" + ScreenUtil.getScreenWidth() + " x " + ScreenUtil.getScreenHeight();
            }
        });
        builder.setPostExecute(new OnPostExecute<String>() {
            @Override
            public void onPostExecute(String result) {
                ToastUtil.showLong("异步任务已完成：" + result);
                LogUtil.logDebug(getClass(), "异步任务已完成: " + result);
            }
        });
        builder.setCancelled(new OnCancelled<String>() {
            @Override
            public void onCancelled(String result) {
                ToastUtil.showShort("异步任务被取消：" + result);
                LogUtil.logDebug(getClass(), "异步任务被取消: " + result);
            }
        });
        // 允许在同一时刻有20个任务正在执行，并且最多能够存储400个任务
        Executor exec = new ThreadPoolExecutor(20, 400, 10,
                TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
        // 启动异步任务
        builder.start(exec, 100);
    }

    @OnClick({R.id.btnMortgageCalculator, R.id.btnCipher, R.id.btnPixelXmlMark})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnUtils:
                // 工具类演示
                onFastAsyncTask();
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
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !PermissionUtil.isHasPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    ToastUtil.showShort("请先允许程序写入外部存储的权限！");
                } else {
                    // 已经有了读写权限，开启一个子线程进行XML文件生成
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            // 生成屏幕分辨率(这里需要注意,6.0以上需要申请权限)
                            final String supportDimensions[] = {"240x320", "480x800", "720x1280", "1080x1920", "1080x2220", "1440x2560"};
                            /**
                             *
                             * @param UI效果图的基准宽度
                             * @param UI效果图的基准高度
                             * @param 自定义维度（输入需要适配的各个屏幕分辨率数组）
                             * @param 生成的像素大小是否支持负数
                             */
                            PixelXmlMarkUtil.markXmlSaveToSdCard(720, 1280, supportDimensions, true);
                            // 弹窗提醒
                            ToastUtil.showShort("屏幕分辨率文件已生成完毕，并保存在设备存储卡res目录下");
                        }
                    }).start();
                }
                break;
        }
    }

}
