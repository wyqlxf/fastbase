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

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.Button;

import com.wyq.fast.core.asynctask.FastAsyncTask;
import com.wyq.fast.core.calculator.Calculator;
import com.wyq.fast.core.calculator.MonthlyRepayment;
import com.wyq.fast.core.calculator.NumberOfLoans;
import com.wyq.fast.core.calculator.PayInterest;
import com.wyq.fast.core.calculator.TotalRepayment;
import com.wyq.fast.core.cipher.Cipher;
import com.wyq.fast.core.cipher.CipherFactory;
import com.wyq.fast.interfaces.asynctask.OnCancelled;
import com.wyq.fast.interfaces.asynctask.OnDoInBackground;
import com.wyq.fast.interfaces.asynctask.OnPostExecute;
import com.wyq.fast.interfaces.asynctask.OnPublishProgress;
import com.wyq.fast.interfaces.calculator.CalculatorStrategy;
import com.wyq.fast.interfaces.handler.OnHandlerListener;
import com.wyq.fast.model.MCalculator;
import com.wyq.fast.model.MCalculatorDetail;
import com.wyq.fast.receiver.NetworkChangeReceiver;
import com.wyq.fast.utils.BadgeCountUtil;
import com.wyq.fast.utils.LogUtil;
import com.wyq.fast.utils.NotificationUtil;
import com.wyq.fast.utils.ObjectValueUtil;
import com.wyq.fast.utils.PixelXmlMarkUtil;
import com.wyq.fast.utils.RandomUtil;
import com.wyq.fast.utils.SPUtil;
import com.wyq.fast.utils.ScreenUtil;
import com.wyq.fast.utils.ToastUtil;
import com.wyq.fast.utils.ViewBindUtil;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Author: WangYongQi
 * main activity of this sample
 */

public class MainActivity extends BaseAppCompatActivity {

    @ViewBindUtil.BindView(R.id.btnClick)
    private Button btnClick;

    // 异步任务
    private FastAsyncTask.Builder<String, String, String> builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 注册网络变化监听
        registerNetworkListener(new NetworkChangeReceiver() {
            @Override
            protected void onNetworkChange(boolean isConnection) {
                if (isConnection) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                // 这里先休眠三秒
                                Thread.sleep(3000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            // 在子线程中Toast弹窗，注意：如果在同一时间调用多次，只会弹窗显示最后一次
                            ToastUtil.showShort("当前有网络!");
                        }
                    }).start();
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

        // 设置绑定
        ViewBindUtil.bind(this);

        // 发送一个的空消息
        sendEmptyMessage(0);

        // 根据对象获取值
        String title = ObjectValueUtil.getString(getIntent(), "title");
        LogUtil.logDebug("title: " + title);

        // 生成屏幕分辨率(这里需要注意,6.0以上需要申请权限)
        PixelXmlMarkUtil.markXmlSaveToSdCard(720, 1280, "480x800,720x1280,1080x1920", true);

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
        builder.setDoInBackground(new OnDoInBackground<String, String, String>() {
            @Override
            public String doInBackground(OnPublishProgress<String> publishProgress, String... strings) {
                // 房贷计算器，做房贷计算
                MCalculator entity = new MCalculator();
                // 设置总贷款：100万
                entity.setTotalLoan(100);
                // 设置公积金贷款总额：100万(商贷和公积金贷款组合模式下使用)
                entity.setProvidentLoan(90);
                // 设置贷款类型为：商业贷款
                entity.setLoanType(CalculatorStrategy.LoanType.BUSINESS);
                // 设置还款方式为：等额本金
                entity.setRepaymentWay(CalculatorStrategy.RepaymentWay.AMOUNT);
                // 设置贷款期限为：10年
                entity.setLoanTerm(10);
                // 设置贷款利率为：4.9
                entity.setLoanRate(4.9f);
                // 开始计算
                Calculator calculator;
                // 还款总额(单位：万元)
                calculator = new Calculator(new TotalRepayment());
                double totalRepayment = calculator.calculatorResult(entity);
                // 贷款月数
                calculator = new Calculator(new NumberOfLoans());
                double numberOfLoans = calculator.calculatorResult(entity);
                // 支付利息(单位：万元)
                calculator = new Calculator(new PayInterest());
                double payInterest = calculator.calculatorResult(entity);
                // 每月还款 / 首月还款 (单位：万元，需要x10000转成元)
                calculator = new Calculator(new MonthlyRepayment());
                double monthlyRepayment = calculator.calculatorResult(entity);
                LogUtil.logDebug("还款总额: " + totalRepayment + " 万元");
                LogUtil.logDebug("贷款月数: " + numberOfLoans + " 月");
                LogUtil.logDebug("支付利息: " + payInterest + " 万元");
                LogUtil.logDebug("每月还款: " + monthlyRepayment + " 元");
                // 输出更多详情
                List<MCalculatorDetail> list = entity.getCalculatorDetails();
                for (int i = 0; i < list.size(); i++) {
                    // 输出： 期次、偿还本息、偿还本金、偿还利息、剩余本金
                    LogUtil.logDebug("更多详情: " + list.get(i).toString());
                }
                try {
                    Thread.sleep(3 * 1000);
                } catch (Exception ex) {
                }
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
        builder.start(exec);
    }


    @ViewBindUtil.OnClick(R.id.btnClick)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnClick:
                // 启动异步任务
                onFastAsyncTask();
                // 清空所有未读数
                BadgeCountUtil.clearAllBadgeCount();
                break;
        }
    }

}
