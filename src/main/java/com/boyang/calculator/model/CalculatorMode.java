package com.boyang.calculator.model;

/**
 * 计算器模式枚举。
 */
public enum CalculatorMode {
    BASIC("Basic Calculator"),
    SCIENTIFIC("Scientific Calculator"),
    PROGRAMMER("Programmer Calculator");

    private final String displayName;

    CalculatorMode(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
