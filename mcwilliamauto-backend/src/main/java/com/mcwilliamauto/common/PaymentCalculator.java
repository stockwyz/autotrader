package com.mcwilliamauto.common;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 贷款月供/双周供计算工具
 * 月供 = P * r(1+r)^n / [(1+r)^n - 1]
 * P: 贷款本金  r: 月利率  n: 分期数(月)
 */
public class PaymentCalculator {

    /** 默认年利率(%)，实际项目应从 finance_setting 表读取，此处提供合理默认值 */
    public static final double DEFAULT_ANNUAL_RATE = 7.99;
    public static final int DEFAULT_TERM_MONTHS = 72;

    public static BigDecimal calcBiweekly(BigDecimal price, Integer termMonths) {
        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO;
        }
        int n = (termMonths == null || termMonths <= 0) ? DEFAULT_TERM_MONTHS : termMonths;
        double r = DEFAULT_ANNUAL_RATE / 100 / 12;
        double p = price.doubleValue();

        double monthly;
        if (r == 0) {
            monthly = p / n;
        } else {
            monthly = p * r * Math.pow(1 + r, n) / (Math.pow(1 + r, n) - 1);
        }
        double biweekly = monthly * 12 / 26;
        return BigDecimal.valueOf(biweekly).setScale(2, RoundingMode.HALF_UP);
    }
}
