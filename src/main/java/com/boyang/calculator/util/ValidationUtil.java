package com.boyang.calculator.util;

import com.boyang.calculator.model.NumberBase;

import java.math.BigDecimal;

/**
 * 输入校验工具类。
 */
public final class ValidationUtil {

    private ValidationUtil() {
    }

    /**
     * 判断字符串是否为十进制数字。
     */
    public static boolean isNumber(String value) {
        if (value == null || value.isBlank()) {
            return false;
        }
        try {
            new BigDecimal(value);
            return true;
        } catch (NumberFormatException exception) {
            return false;
        }
    }

    /**
     * 判断字符是否可在指定进制下输入。
     */
    public static boolean isValidDigitForBase(char digit, NumberBase base) {
        return Character.digit(digit, base.getRadix()) >= 0;
    }
}
