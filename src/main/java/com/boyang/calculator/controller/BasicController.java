package com.boyang.calculator.controller;

import com.boyang.calculator.engine.BasicCalculatorEngine;
import com.boyang.calculator.model.CalculatorMode;
import com.boyang.calculator.util.BigNumberUtil;
import com.boyang.calculator.util.FullResultDialog;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;

/**
 * 基础计算器控制器，处理按钮点击和显示更新。
 */
public class BasicController {

    private static final int DISPLAY_MAX_LENGTH = 35;

    @FXML
    private Label expressionLabel;

    @FXML
    private Label displayLabel;

    private final BasicCalculatorEngine engine = new BasicCalculatorEngine();

    private String leftOperand = "0";
    private String currentInput = "0";
    private String pendingOperator;
    private String fullExpression = "";
    private String fullResult = "0";
    private boolean waitingForNextNumber;

    /**
     * 初始化基础计算器显示。
     */
    @FXML
    private void initialize() {
        displayLabel.setTooltip(new Tooltip("点击查看完整结果"));
        displayLabel.setOnMouseClicked(event -> FullResultDialog.show(fullExpression, fullResult));
        clearAll();
    }

    /**
     * 处理数字按钮输入。
     */
    @FXML
    private void handleDigit(ActionEvent event) {
        String digit = getButtonText(event);
        if (waitingForNextNumber || "0".equals(currentInput) || isError(currentInput)) {
            currentInput = digit;
            waitingForNextNumber = false;
        } else {
            currentInput += digit;
        }
        updateResult(currentInput);
    }

    /**
     * 处理小数点按钮输入。
     */
    @FXML
    private void handleDecimal() {
        if (waitingForNextNumber || isError(currentInput)) {
            currentInput = "0.";
            waitingForNextNumber = false;
        } else if (!currentInput.contains(".")) {
            currentInput += ".";
        }
        updateResult(currentInput);
    }

    /**
     * 处理四则运算符。
     */
    @FXML
    private void handleOperator(ActionEvent event) {
        String operator = normalizeOperator(getButtonText(event));
        if (pendingOperator != null && !waitingForNextNumber) {
            currentInput = calculate(leftOperand, pendingOperator, currentInput);
            updateResult(currentInput);
            if (isError(currentInput)) {
                return;
            }
        }

        leftOperand = currentInput;
        pendingOperator = operator;
        waitingForNextNumber = true;
        fullExpression = leftOperand + " " + displayOperator(operator);
        expressionLabel.setText(BigNumberUtil.toDisplayText(fullExpression, DISPLAY_MAX_LENGTH));
    }

    /**
     * 计算当前表达式结果。
     */
    @FXML
    private void handleEquals() {
        if (pendingOperator == null) {
            return;
        }

        String rightOperand = currentInput;
        fullExpression = leftOperand + " " + displayOperator(pendingOperator) + " " + rightOperand + " =";
        currentInput = calculate(leftOperand, pendingOperator, rightOperand);
        updateResult(currentInput);
        expressionLabel.setText(BigNumberUtil.toDisplayText(fullExpression, DISPLAY_MAX_LENGTH));
        leftOperand = currentInput;
        pendingOperator = null;
        waitingForNextNumber = true;
    }

    /**
     * 清空当前输入和表达式。
     */
    @FXML
    private void clearAll() {
        leftOperand = "0";
        currentInput = "0";
        pendingOperator = null;
        fullExpression = "";
        waitingForNextNumber = false;
        expressionLabel.setText("");
        updateResult("0");
    }

    /**
     * 删除当前输入的最后一位。
     */
    @FXML
    private void deleteLast() {
        if (waitingForNextNumber || isError(currentInput)) {
            return;
        }
        if (currentInput.length() <= 1 || (currentInput.length() == 2 && currentInput.startsWith("-"))) {
            currentInput = "0";
        } else {
            currentInput = currentInput.substring(0, currentInput.length() - 1);
        }
        updateResult(currentInput);
    }

    /**
     * 切换当前数字的正负号。
     */
    @FXML
    private void toggleSign() {
        if ("0".equals(currentInput) || isError(currentInput)) {
            return;
        }
        currentInput = engine.toggleSign(currentInput);
        updateResult(currentInput);
    }

    /**
     * 将当前数字转换为百分数。
     */
    @FXML
    private void applyPercent() {
        if (isError(currentInput)) {
            return;
        }
        currentInput = engine.percent(currentInput);
        updateResult(currentInput);
    }

    private String calculate(String left, String operator, String right) {
        String result = engine.calculate(left, operator, right);
        if (isError(result)) {
            expressionLabel.setText(CalculatorMode.BASIC.getDisplayName());
            pendingOperator = null;
            waitingForNextNumber = true;
        }
        return result;
    }

    private void updateResult(String result) {
        fullResult = result;
        displayLabel.setText(BigNumberUtil.toDisplayText(result, DISPLAY_MAX_LENGTH));
    }

    private boolean isError(String value) {
        return value != null && value.startsWith("Error");
    }

    private String getButtonText(ActionEvent event) {
        Button button = (Button) event.getSource();
        return button.getText();
    }

    private String normalizeOperator(String operator) {
        return switch (operator) {
            case "×" -> "*";
            case "÷" -> "/";
            default -> operator;
        };
    }

    private String displayOperator(String operator) {
        return switch (operator) {
            case "*" -> "×";
            case "/" -> "÷";
            default -> operator;
        };
    }
}
