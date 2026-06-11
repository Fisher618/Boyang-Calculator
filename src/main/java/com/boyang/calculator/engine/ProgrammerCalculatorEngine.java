package com.boyang.calculator.engine;

import com.boyang.calculator.model.NumberBase;

import java.math.BigInteger;

/**
 * 程序员计算引擎，使用 BigInteger 支持任意长度整数。
 */
public class ProgrammerCalculatorEngine {

    /**
     * 在 BIN/OCT/DEC/HEX 之间转换任意长度整数。
     */
    public String convert(String value, NumberBase fromBase, NumberBase toBase) {
        BigInteger number = parse(value, fromBase);
        return format(number, toBase);
    }

    /**
     * 执行 BigInteger 位运算。
     */
    public String calculate(String left, String operator, String right, NumberBase base) {
        BigInteger leftValue = parse(left, base);
        BigInteger result = switch (operator) {
            case "AND" -> leftValue.and(parse(right, base));
            case "OR" -> leftValue.or(parse(right, base));
            case "XOR" -> leftValue.xor(parse(right, base));
            case "<<" -> leftValue.shiftLeft(parseShiftCount(right));
            case ">>" -> leftValue.shiftRight(parseShiftCount(right));
            case "+" -> leftValue.add(parse(right, base));
            case "-" -> leftValue.subtract(parse(right, base));
            case "*" -> leftValue.multiply(parse(right, base));
            default -> throw new IllegalArgumentException("Unsupported programmer operator: " + operator);
        };
        return format(result, base);
    }

    /**
     * 执行 BigInteger 按位取反。
     */
    public String not(String value, NumberBase base) {
        return format(parse(value, base).not(), base);
    }

    /**
     * 将指定进制字符串解析为 BigInteger。
     */
    public BigInteger parse(String value, NumberBase base) {
        String normalized = normalize(value);
        if (normalized.isBlank() || "-".equals(normalized)) {
            return BigInteger.ZERO;
        }
        return new BigInteger(normalized, base.getRadix());
    }

    /**
     * 将 BigInteger 格式化为指定进制字符串。
     */
    public String format(BigInteger value, NumberBase base) {
        return value.toString(base.getRadix()).toUpperCase();
    }

    private int parseShiftCount(String value) {
        BigInteger shift = parse(value, NumberBase.DEC);
        if (shift.signum() < 0) {
            throw new IllegalArgumentException("Shift count cannot be negative.");
        }
        return shift.intValueExact();
    }

    private String normalize(String value) {
        return value == null ? "0" : value.trim().replace("_", "").toUpperCase();
    }
}
