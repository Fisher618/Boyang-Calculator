package com.boyang.calculator.model;

/**
 * 数字进制枚举。
 */
public enum NumberBase {
    BIN(2),
    OCT(8),
    DEC(10),
    HEX(16);

    private final int radix;

    NumberBase(int radix) {
        this.radix = radix;
    }

    public int getRadix() {
        return radix;
    }
}
