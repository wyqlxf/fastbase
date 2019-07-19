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
package com.wyq.fast.core.calculator;

import com.wyq.fast.interfaces.calculator.CalculatorStrategy;
import com.wyq.fast.model.MCalculator;

/**
 * Author: WangYongQi
 * Monthly repayment specific strategy class
 */

public class MonthlyRepayment implements CalculatorStrategy {

    @Override
    public double doCalculator(MCalculator entity) {
        // Total loan month
        int months = entity.getLoanTerm() * 12;
        // Bank loan monthly interest rate
        double monthRate = entity.getLoanRate() / (100 * 12);
        // Provident fund loan monthly interest rate
        double providentMonthRate = entity.getProvidentRate() / (100 * 12);
        // Input result
        double result;
        // Commercial loan or provident fund loan
        if (entity.getRepaymentWay() == RepaymentWay.AMOUNT) {
            // Equal principal repayment method, calculate the total amount of repayment in the first month
            result = (entity.getTotalLoan() / months + entity.getTotalLoan() * monthRate);
        } else {
            // The same amount of principal and interest repayment method, calculate the total amount of monthly repayment
            result = (entity.getTotalLoan() * monthRate * Math.pow((1 + monthRate), months)) / (Math.pow((1 + monthRate), months) - 1);
        }
        if (entity.getLoanType() == LoanType.COMBINATION) {
            // Portfolio loans, distinguishing between commercial loans and provident fund loans
            if (entity.getRepaymentWay() == RepaymentWay.AMOUNT) {
                // Equal principal repayment method, calculate the total amount of repayment in the first month
                result = result + (entity.getProvidentLoan() / months + entity.getProvidentLoan() * providentMonthRate);
            } else {
                // The same amount of principal and interest repayment method, calculate the total amount of monthly repayment
                result = result + (entity.getProvidentLoan() * providentMonthRate * Math.pow((1 + providentMonthRate), months)) / (Math.pow((1 + providentMonthRate), months) - 1);
            }
        }
        return result;
    }

}
