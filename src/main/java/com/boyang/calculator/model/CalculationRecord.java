package com.boyang.calculator.model;

import java.time.LocalDateTime;

/**
 * 一条计算历史记录。
 */
public class CalculationRecord {

    private final String expression;
    private final String result;
    private final CalculatorMode mode;
    private final LocalDateTime timestamp;

    public CalculationRecord(String expression, String result, CalculatorMode mode, LocalDateTime timestamp) {
        this.expression = expression;
        this.result = result;
        this.mode = mode;
        this.timestamp = timestamp;
    }

    public String getExpression() {
        return expression;
    }

    public String getResult() {
        return result;
    }

    public CalculatorMode getMode() {
        return mode;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
