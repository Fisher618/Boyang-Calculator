package com.boyang.calculator.engine;

import com.boyang.calculator.util.BigNumberUtil;

import java.math.BigDecimal;

/**
 * 基础计算引擎，使用 BigDecimal 支持超大数和高精度小数。
 */
public class BasicCalculatorEngine {

    /**
     * 根据运算符计算两个字符串数字，结果以完整十进制字符串返回。
     */
    public String calculate(String left, String operator, String right) {
        try {
            BigDecimal leftValue = new BigDecimal(left);
            BigDecimal rightValue = new BigDecimal(right);
            BigDecimal result = switch (operator) {
                case "+" -> leftValue.add(rightValue);
                case "-" -> leftValue.subtract(rightValue);
                case "*" -> leftValue.multiply(rightValue);
                case "/" -> BigNumberUtil.safeDivide(leftValue, rightValue);
                case "%" -> leftValue.remainder(rightValue);
                default -> throw new IllegalArgumentException("Unsupported operator: " + operator);
            };
            return BigNumberUtil.formatDecimal(result);
        } catch (ArithmeticException exception) {
            return "Error: division by zero";
        } catch (NumberFormatException exception) {
            return "Error: invalid number";
        }
    }

    /**
     * 计算百分数。
     */
    public String percent(String value) {
        try {
            BigDecimal result = new BigDecimal(value).movePointLeft(2);
            return BigNumberUtil.formatDecimal(result);
        } catch (NumberFormatException exception) {
            return "Error: invalid number";
        }
    }

    /**
     * 切换正负号。
     */
    public String toggleSign(String value) {
        try {
            return BigNumberUtil.formatDecimal(new BigDecimal(value).negate());
        } catch (NumberFormatException exception) {
            return "Error: invalid number";
        }
    }
}
