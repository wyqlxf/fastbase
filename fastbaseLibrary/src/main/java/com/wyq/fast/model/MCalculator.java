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
package com.wyq.fast.model;

import com.wyq.fast.interfaces.calculator.CalculatorStrategy;
import com.wyq.fast.utils.LogUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: WangYongQi
 * Mortgage Calculator Entity Class
 */

public class MCalculator<T> implements Serializable {

    // Total loan (unit: 10,000 yuan)
    private double totalLoan;
    // Total provident fund loans, used in commercial loan and provident fund loan portfolio models
    private double providentLoan;
    // Loan type (commercial / provident fund / portfolio)
    private CalculatorStrategy.LoanType loanType;
    // Repayment method (equal principal and interest / equal principal)
    private CalculatorStrategy.RepaymentWay repaymentWay;
    // Loan term (unit: year)
    private int loanTerm;
    // Loan interest rate, bank interest rate or provident fund interest rate (example: 4.9)
    private double loanRate;
    // Provident fund interest rate, commercial loan and provident fund loan combination mode (currently fixed at 3.25)
    private double providentRate = 3.25;

    // Custom entity object
    private T entity;

    public double getTotalLoan() {
        return totalLoan;
    }

    public void setTotalLoan(double totalLoan) {
        this.totalLoan = totalLoan;
    }

    public double getProvidentLoan() {
        return providentLoan;
    }

    public void setProvidentLoan(double providentLoan) {
        this.providentLoan = providentLoan;
    }

    public CalculatorStrategy.LoanType getLoanType() {
        return loanType;
    }

    public void setLoanType(CalculatorStrategy.LoanType loanType) {
        this.loanType = loanType;
    }

    public CalculatorStrategy.RepaymentWay getRepaymentWay() {
        return repaymentWay;
    }

    public void setRepaymentWay(CalculatorStrategy.RepaymentWay repaymentWay) {
        this.repaymentWay = repaymentWay;
    }

    public int getLoanTerm() {
        return loanTerm;
    }

    public void setLoanTerm(int loanTerm) {
        this.loanTerm = loanTerm;
    }

    public double getLoanRate() {
        return loanRate;
    }

    public void setLoanRate(double loanRate) {
        this.loanRate = loanRate;
    }

    public double getProvidentRate() {
        return providentRate;
    }

    public void setProvidentRate(double providentRate) {
        this.providentRate = providentRate;
    }

    public T getEntity() {
        return entity;
    }

    public void setEntity(T entity) {
        this.entity = entity;
    }

    @Override
    public String toString() {
        return "MCalculator{" +
                "totalLoan=" + totalLoan +
                ", providentLoan=" + providentLoan +
                ", loanType=" + loanType +
                ", repaymentWay=" + repaymentWay +
                ", loanTerm=" + loanTerm +
                ", loanRate=" + loanRate +
                ", providentRate=" + providentRate +
                '}';
    }

    /**
     * Object copy
     *
     * @return
     */
    public MCalculator copy() {
        MCalculator calculator = new MCalculator();
        calculator.setTotalLoan(totalLoan);
        calculator.setProvidentLoan(providentLoan);
        calculator.setLoanType(loanType);
        calculator.setRepaymentWay(repaymentWay);
        calculator.setLoanTerm(loanTerm);
        calculator.setLoanRate(loanRate);
        calculator.setProvidentRate(providentRate);
        return calculator;
    }

    /**
     * returns a collection of calculation details
     *
     * @return
     */
    public List<MCalculatorDetail> getCalculatorDetails() {
        List<MCalculatorDetail> list = new ArrayList<>();
        try {
            MCalculator entity = copy();
            // Total loan month
            final int months = entity.getLoanTerm() * 12;
            // Bank loan monthly interest rate
            final double monthRate = entity.getLoanRate() / (100 * 12);
            // Provident fund loan monthly interest rate
            final double monthRateProvident = entity.getProvidentRate() / (100 * 12);
            // Total amount of loans (ten thousand yuan, unified unit yuan)
            final double mTotalLoan = entity.getTotalLoan() * 10000;
            entity.setTotalLoan(mTotalLoan);
            // Total amount of provident fund loans (ten thousand yuan, unified unit yuan)
            final double mProvidentLoan = entity.getProvidentLoan() * 10000;
            entity.setProvidentLoan(mProvidentLoan);
            for (int i = 0; i < months; i++) {
                // Period
                int number = (i + 1);
                // Repayment of principal and interest
                double money;
                // Repayment of principal
                double principal;
                // Repayment of interest
                double interest;
                // Remaining principal
                double remainingMoney = entity.getTotalLoan();
                // Commercial loan or provident fund loan
                if (entity.getRepaymentWay() == CalculatorStrategy.RepaymentWay.AMOUNT) {
                    // Equal principal repayment method, monthly repayment principal (the same as the principal of each month, the interest is not the same)
                    principal = mTotalLoan / months;
                    interest = entity.getTotalLoan() * monthRate;
                    money = principal + interest;
                    remainingMoney = entity.getTotalLoan() - principal;
                    entity.setTotalLoan(remainingMoney);
                } else {
                    // The same amount of principal and interest repayment method, calculate the total amount of monthly repayment (the monthly principal is not the same, the interest is not the same)
                    interest = remainingMoney * monthRate;
                    money = (mTotalLoan * monthRate * Math.pow((1 + monthRate), months)) / (Math.pow((1 + monthRate), months) - 1);
                    principal = money - interest;
                    remainingMoney = entity.getTotalLoan() - principal;
                    entity.setTotalLoan(remainingMoney);
                }

                // Repayment of principal and interest
                double moneyProvident;
                // Repayment of principal
                double principalProvident;
                // Repayment of interest
                double interestProvident;
                // Remaining principal
                double remainingMoneyProvident = entity.getProvidentLoan();
                // Portfolio loans, distinguishing between commercial loans and provident fund loans
                if (entity.getLoanType() == CalculatorStrategy.LoanType.COMBINATION) {
                    if (entity.getRepaymentWay() == CalculatorStrategy.RepaymentWay.AMOUNT) {
                        // Equal principal repayment method, monthly repayment principal (the same as the principal of each month, the interest is not the same)
                        principalProvident = mProvidentLoan / months;
                        interestProvident = entity.getProvidentLoan() * monthRateProvident;
                        moneyProvident = principalProvident + interestProvident;
                        remainingMoneyProvident = entity.getProvidentLoan() - principalProvident;
                        entity.setProvidentLoan(remainingMoneyProvident);
                    } else {
                        // The same amount of principal and interest repayment method, calculate the total amount of monthly repayment (the monthly principal is not the same, the interest is not the same)
                        interestProvident = remainingMoneyProvident * monthRateProvident;
                        moneyProvident = (mProvidentLoan * monthRateProvident * Math.pow((1 + monthRateProvident), months)) / (Math.pow((1 + monthRateProvident), months) - 1);
                        principalProvident = moneyProvident - interestProvident;
                        remainingMoneyProvident = entity.getProvidentLoan() - principalProvident;
                        entity.setProvidentLoan(remainingMoneyProvident);
                    }
                    money = money + moneyProvident;
                    principal = principal + principalProvident;
                    interest = interest + interestProvident;
                    remainingMoney = remainingMoney + remainingMoneyProvident;
                }
                MCalculatorDetail detail = new MCalculatorDetail();
                detail.setNumber(number);
                detail.setMoney(formatDouble(money));
                detail.setPrincipal(formatDouble(principal));
                detail.setInterest(formatDouble(interest));
                detail.setRemainingMoney(formatDouble(remainingMoney));
                list.add(detail);
            }
        } catch (Exception ex) {
            LogUtil.logDebug(MCalculator.class, "Error getting calculation details," + ex.toString());
        }
        return list;
    }

    /**
     * Format conversion
     *
     * @param value
     * @return
     */
    private double formatDouble(double value) {
        return (double) Math.round(value * 100) / 100;
    }

}
