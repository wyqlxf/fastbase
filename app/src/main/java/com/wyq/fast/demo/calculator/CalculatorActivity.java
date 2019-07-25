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
package com.wyq.fast.demo.calculator;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.wyq.fast.core.calculator.Calculator;
import com.wyq.fast.core.calculator.MonthlyRepayment;
import com.wyq.fast.core.calculator.NumberOfLoans;
import com.wyq.fast.core.calculator.PayInterest;
import com.wyq.fast.core.calculator.TotalRepayment;
import com.wyq.fast.demo.BaseAppCompatActivity;
import com.wyq.fast.demo.R;
import com.wyq.fast.interfaces.calculator.CalculatorStrategy;
import com.wyq.fast.model.MCalculator;
import com.wyq.fast.utils.ToastUtil;
import com.wyq.fast.utils.ViewBindUtil;
import com.wyq.fast.utils.ViewBindUtil.BindView;

/**
 * Author: WangYongQi
 * Mortgage Calculator
 */

public class CalculatorActivity extends BaseAppCompatActivity implements TextWatcher {

    // 贷款类型控件
    @BindView(R.id.tvTypeBusiness)
    private TextView tvTypeBusiness;
    @BindView(R.id.tvTypeProvident)
    private TextView tvTypeProvident;
    @BindView(R.id.tvTypeCombination)
    private TextView tvTypeCombination;
    // 贷款类型 （默认商业贷款）
    private CalculatorStrategy.LoanType loanType = CalculatorStrategy.LoanType.BUSINESS;

    // 还款类型控件
    @BindView(R.id.tvWayAmount)
    private TextView tvWayAmount;
    @BindView(R.id.tvWayInterest)
    private TextView tvWayInterest;
    // 还款方式 （默认等额本息）
    private CalculatorStrategy.RepaymentWay repaymentWay = CalculatorStrategy.RepaymentWay.INTEREST;

    // 贷款总额输入框
    @BindView(R.id.etTotalLoan)
    private EditText etTotalLoan;
    @BindView(R.id.etProvidentLoan)
    private EditText etProvidentLoan;
    @BindView(R.id.llProvidentLoan)
    private LinearLayout llProvidentLoan;

    // 贷款期限（默认20年）
    private int mLoanTerm = 20;
    // 贷款期限控件
    @BindView(R.id.sbLoanTerm)
    private SeekBar sbLoanTerm;
    @BindView(R.id.tvLoanTerm)
    private TextView tvLoanTerm;

    // 贷款利率 (默认4.9%)
    private double mLoanRate = 4.9;
    // 贷款利率控件
    @BindView(R.id.tvLoanRate)
    private TextView tvLoanRate;
    @BindView(R.id.etPercentage)
    private EditText etPercentage;
    @BindView(R.id.etMultiple)
    private EditText etMultiple;
    @BindView(R.id.llLoanRate)
    private LinearLayout llLoanRate;

    // 结果控件
    @BindView(R.id.tvTotalRepayment)
    private TextView tvTotalRepayment;
    @BindView(R.id.tvLoanMonths)
    private TextView tvLoanMonths;
    @BindView(R.id.tvPayInterest)
    private TextView tvPayInterest;
    @BindView(R.id.tvMonthlyRepayment)
    private TextView tvMonthlyRepayment;
    @BindView(R.id.tvMonthlyRepaymentTab)
    private TextView tvMonthlyRepaymentTab;
    @BindView(R.id.llResult)
    private LinearLayout llResult;

    // 计算实体类
    private static final MCalculator mCalculatorEntity = new MCalculator();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);
        // 设置绑定
        ViewBindUtil.bind(this);
        // 贷款期限拖动监听
        sbLoanTerm.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // SeekBar值发生改变时候触发，progress的值0~29
                mLoanTerm = progress + 1;
                tvLoanTerm.setText(mLoanTerm + "年");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // 开始触摸动作时触发
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // 结束触摸动作时触发
            }
        });
        // 贷款利率控件
        etPercentage.addTextChangedListener(this);
        etMultiple.addTextChangedListener(this);
        tvLoanRate.setText(mLoanRate + "%");
    }

    /**
     * 点击贷款类型
     *
     * @param v
     */
    public void onClickType(View v) {
        tvTypeBusiness.setTextColor(getResources().getColor(R.color.colorText));
        tvTypeProvident.setTextColor(getResources().getColor(R.color.colorText));
        tvTypeCombination.setTextColor(getResources().getColor(R.color.colorText));
        tvTypeBusiness.setBackgroundResource(0);
        tvTypeProvident.setBackgroundResource(0);
        tvTypeCombination.setBackgroundResource(0);
        switch (v.getId()) {
            case R.id.tvTypeBusiness:
                // 商业
                loanType = CalculatorStrategy.LoanType.BUSINESS;
                tvTypeBusiness.setBackgroundResource(R.drawable.btn_type_color);
                tvTypeBusiness.setTextColor(getResources().getColor(R.color.colorYellow));
                llLoanRate.setVisibility(View.VISIBLE);
                llProvidentLoan.setVisibility(View.GONE);
                etTotalLoan.setHint("");
                doLoanRate();
                break;
            case R.id.tvTypeProvident:
                // 公积金
                loanType = CalculatorStrategy.LoanType.PROVIDENT;
                tvTypeProvident.setBackgroundResource(R.drawable.btn_type_color);
                tvTypeProvident.setTextColor(getResources().getColor(R.color.colorYellow));
                llLoanRate.setVisibility(View.GONE);
                llProvidentLoan.setVisibility(View.GONE);
                etTotalLoan.setHint("");
                mLoanRate = 3.25;
                tvLoanRate.setText(mLoanRate + "%");
                break;
            case R.id.tvTypeCombination:
                // 组合
                loanType = CalculatorStrategy.LoanType.COMBINATION;
                tvTypeCombination.setBackgroundResource(R.drawable.btn_type_color);
                tvTypeCombination.setTextColor(getResources().getColor(R.color.colorYellow));
                llLoanRate.setVisibility(View.VISIBLE);
                llProvidentLoan.setVisibility(View.VISIBLE);
                etTotalLoan.setHint("商业贷款  ");
                doLoanRate();
                break;
        }
    }

    /**
     * 点击还款方式
     *
     * @param v
     */
    public void onClickWay(View v) {
        tvWayAmount.setBackgroundResource(0);
        tvWayAmount.setTextColor(getResources().getColor(R.color.colorText));
        tvWayInterest.setBackgroundResource(0);
        tvWayInterest.setTextColor(getResources().getColor(R.color.colorText));
        switch (v.getId()) {
            case R.id.tvWayInterest:
                // 等额本息
                repaymentWay = CalculatorStrategy.RepaymentWay.INTEREST;
                tvWayInterest.setBackgroundResource(R.drawable.btn_type_color);
                tvWayInterest.setTextColor(getResources().getColor(R.color.colorYellow));
                break;
            case R.id.tvWayAmount:
                // 等额本金
                repaymentWay = CalculatorStrategy.RepaymentWay.AMOUNT;
                tvWayAmount.setBackgroundResource(R.drawable.btn_type_color);
                tvWayAmount.setTextColor(getResources().getColor(R.color.colorYellow));
                break;
        }
    }

    /**
     * 点击开始计算
     *
     * @param v
     */
    public void onClickCalculator(View v) {
        if (v.getId() == R.id.btnCalculator) {
            double totalLoan = 0;
            try {
                String text = etTotalLoan.getText().toString();
                totalLoan = Double.parseDouble(text);
            } catch (Exception ex) {
            }
            double providentLoan = 0;
            try {
                String text = etProvidentLoan.getText().toString();
                providentLoan = Double.parseDouble(text);
            } catch (Exception ex) {
            }
            // 贷款总额大于0
            if (totalLoan > 0) {
                // 进行房贷计算
                mCalculatorEntity.setTotalLoan(totalLoan);
                mCalculatorEntity.setProvidentLoan(providentLoan);
                mCalculatorEntity.setLoanType(loanType);
                mCalculatorEntity.setRepaymentWay(repaymentWay);
                mCalculatorEntity.setLoanTerm(mLoanTerm);
                mCalculatorEntity.setLoanRate(mLoanRate);
                doCalculator(mCalculatorEntity);
            } else {
                ToastUtil.showShort("请输入正确的贷款总额!");
            }
        }
    }

    /**
     * 点击更多详情
     *
     * @param v
     */
    public void onClickMore(View v) {
        if (v.getId() == R.id.fabMore) {
            double totalLoan = 0;
            try {
                String text = etTotalLoan.getText().toString();
                totalLoan = Double.parseDouble(text);
            } catch (Exception ex) {
            }
            double providentLoan = 0;
            try {
                String text = etProvidentLoan.getText().toString();
                providentLoan = Double.parseDouble(text);
            } catch (Exception ex) {
            }
            // 商业贷款和公积金贷款只有利率不一样
            MCalculator entity = new MCalculator();
            entity.setTotalLoan(totalLoan);
            entity.setProvidentLoan(providentLoan);
            entity.setLoanType(loanType);
            entity.setRepaymentWay(repaymentWay);
            entity.setLoanTerm(mLoanTerm);
            entity.setLoanRate(mLoanRate);
            Intent intent = new Intent();
            intent.setClass(this, MoreDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("calculator", entity);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    /**
     * 做计算
     *
     * @param entity
     */
    private void doCalculator(MCalculator entity) {
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

        // 将计算结果打上标签展示到界面上去
        if (repaymentWay == CalculatorStrategy.RepaymentWay.AMOUNT) {
            // 等额本金
            tvMonthlyRepaymentTab.setText("首月还款");
        } else {
            // 等额本息
            tvMonthlyRepaymentTab.setText("每月还款");
        }
        // 最终结果
        tvTotalRepayment.setText(formatDouble(totalRepayment) + "  万元");
        tvLoanMonths.setText((int) numberOfLoans + "  月");
        tvPayInterest.setText(formatDouble(payInterest) + "  万元");
        tvMonthlyRepayment.setText(formatDouble(monthlyRepayment * 10000) + "  元");
        llResult.setVisibility(View.VISIBLE);
        findViewById(R.id.fabMore).setVisibility(View.VISIBLE);
    }

    /**
     * 展示贷款利率
     */
    private void doLoanRate() {
        try {
            String strPercentage = etPercentage.getText().toString();
            String strMultiple = etMultiple.getText().toString();
            double percentage = Double.parseDouble(strPercentage);
            double multiple = Double.parseDouble(strMultiple);
            mLoanRate = formatDouble(percentage * multiple);
            tvLoanRate.setText(mLoanRate + "%");
        } catch (Exception ex) {
        }
    }

    /**
     * 格式化
     *
     * @param value
     * @return
     */
    private double formatDouble(double value) {
        return (double) Math.round(value * 100) / 100;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        doLoanRate();
    }

}
