package com.boyang.calculator.model;

/**
 * 保存计算器当前输入和模式状态。
 */
public class CalculatorState {

    private String currentInput = "0";
    private String currentExpression = "";
    private CalculatorMode currentMode = CalculatorMode.BASIC;
    private boolean error;

    public String getCurrentInput() {
        return currentInput;
    }

    public void setCurrentInput(String currentInput) {
        this.currentInput = currentInput;
    }

    public String getCurrentExpression() {
        return currentExpression;
    }

    public void setCurrentExpression(String currentExpression) {
        this.currentExpression = currentExpression;
    }

    public CalculatorMode getCurrentMode() {
        return currentMode;
    }

    public void setCurrentMode(CalculatorMode currentMode) {
        this.currentMode = currentMode;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }
}
