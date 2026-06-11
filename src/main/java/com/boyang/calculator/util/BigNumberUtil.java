package com.boyang.calculator.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;

/**
 * 大数格式化和高精度运算辅助工具。
 */
public final class BigNumberUtil {

    public static final MathContext MC = new MathContext(80, RoundingMode.HALF_UP);
    public static final int DIVIDE_SCALE = 80;
    private static final String ELLIPSIS = "...";

    private BigNumberUtil() {
    }

    /**
     * 格式化 BigDecimal，去除无意义末尾 0，并避免科学计数法。
     */
    public static String formatDecimal(BigDecimal value) {
        if (value == null) {
            return "0";
        }
        BigDecimal normalized = value.stripTrailingZeros();
        if (BigDecimal.ZERO.compareTo(normalized) == 0) {
            return "0";
        }
        return normalized.toPlainString();
    }

    /**
     * 格式化 BigInteger。
     */
    public static String formatInteger(BigInteger value) {
        return value == null ? "0" : value.toString();
    }

    /**
     * 判断 BigDecimal 是否为整数。
     */
    public static boolean isInteger(BigDecimal value) {
        return value != null && value.stripTrailingZeros().scale() <= 0;
    }

    /**
     * 统一处理除法精度。
     */
    public static BigDecimal safeDivide(BigDecimal a, BigDecimal b) {
        if (BigDecimal.ZERO.compareTo(b) == 0) {
            throw new ArithmeticException("Division by zero.");
        }
        return a.divide(b, DIVIDE_SCALE, RoundingMode.HALF_UP);
    }

    /**
     * 将过长文本缩略为显示文本，不改变真实结果。
     */
    public static String toDisplayText(String fullText, int maxLength) {
        if (fullText == null || fullText.length() <= maxLength) {
            return fullText;
        }
        if (maxLength <= ELLIPSIS.length()) {
            return ELLIPSIS;
        }
        return fullText.substring(0, maxLength - ELLIPSIS.length()) + ELLIPSIS;
    }

    /**
     * 从字符串构造 BigDecimal，确保计算路径不经过浮点数。
     */
    public static BigDecimal parseDecimal(String value) {
        return new BigDecimal(value);
    }
}
