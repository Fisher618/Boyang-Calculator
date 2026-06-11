package com.boyang.calculator.controller;

import com.boyang.calculator.engine.ScientificCalculatorEngine;
import com.boyang.calculator.util.BigNumberUtil;
import com.boyang.calculator.util.FullResultDialog;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;

/**
 * 科学计算器控制器，表达式主路径使用 BigDecimal。
 */
public class ScientificController {

    private static final int DISPLAY_MAX_LENGTH = 35;

    @FXML
    private Label expressionLabel;

    @FXML
    private Label displayLabel;

    private final ScientificCalculatorEngine engine = new ScientificCalculatorEngine();

    private String currentExpression = "";
    private String fullExpression = "";
    private String fullResult = "0";

    /**
     * 初始化科学计算器显示。
     */
    @FXML
    private void initialize() {
        displayLabel.setTooltip(new Tooltip("点击查看完整结果"));
        displayLabel.setOnMouseClicked(event -> FullResultDialog.show(fullExpression, fullResult));
        updateExpression("");
        updateResult("0");
    }

    /**
     * 处理科学计算器按钮。
     */
    @FXML
    private void handleScientificAction(ActionEvent event) {
        String text = ((Button) event.getSource()).getText();
        switch (text) {
            case "AC" -> clearAll();
            case "DEL" -> deleteLast();
            case "=" -> evaluate();
            case "×" -> appendOperator("*");
            case "÷" -> appendOperator("/");
            case "x²" -> applySquare();
            case "sqrt" -> applySqrt();
            case "xʸ" -> appendOperator("^");
            case "sin", "cos", "tan", "ln", "log" -> applyApproximateFunction(text);
            case "π" -> appendToken("3.14159265358979323846264338327950288419716939937510");
            case "e" -> appendToken("2.71828182845904523536028747135266249775724709369995");
            case "Deg/Rad" -> expressionLabel.setText("Deg/Rad reserved");
            default -> appendToken(text);
        }
    }

    private void appendToken(String token) {
        if (isError(fullResult) || "0".equals(fullResult) && currentExpression.isBlank()) {
            currentExpression = "";
        }
        currentExpression += token;
        fullExpression = currentExpression;
        updateExpression(currentExpression);
        updateResult(currentExpression);
    }

    private void appendOperator(String operator) {
        if (currentExpression.isBlank() && !isError(fullResult)) {
            currentExpression = fullResult;
        }
        currentExpression += " " + operator + " ";
        fullExpression = currentExpression;
        updateExpression(currentExpression);
        updateResult(currentExpression);
    }

    private void evaluate() {
        if (currentExpression.isBlank()) {
            return;
        }
        fullExpression = currentExpression + " =";
        String result = engine.evaluateExpression(currentExpression);
        updateExpression(fullExpression);
        updateResult(result);
        if (!isError(result)) {
            currentExpression = result;
        }
    }

    private void applySquare() {
        String value = currentExpression.isBlank() ? fullResult : engine.evaluateExpression(currentExpression);
        String result = safeUnaryResult(() -> engine.square(value));
        fullExpression = "(" + value + ")²";
        updateExpression(fullExpression);
        updateResult(result);
        if (!isError(result)) {
            currentExpression = result;
        }
    }

    private void applySqrt() {
        String value = currentExpression.isBlank() ? fullResult : engine.evaluateExpression(currentExpression);
        String result = safeUnaryResult(() -> engine.sqrt(value));
        fullExpression = "sqrt(" + value + ")";
        updateExpression(fullExpression);
        updateResult(result);
        if (!isError(result)) {
            currentExpression = result;
        }
    }

    private void applyApproximateFunction(String functionName) {
        String value = currentExpression.isBlank() ? fullResult : engine.evaluateExpression(currentExpression);
        String result = safeUnaryResult(() -> engine.approximateFunction(functionName, value));
        fullExpression = functionName + "(" + value + ")";
        updateExpression(fullExpression);
        updateResult(result);
        if (!isError(result)) {
            currentExpression = result;
        }
    }

    private String safeUnaryResult(ResultSupplier supplier) {
        try {
            return supplier.get();
        } catch (RuntimeException exception) {
            return "Error: invalid expression";
        }
    }

    private void clearAll() {
        currentExpression = "";
        fullExpression = "";
        updateExpression("");
        updateResult("0");
    }

    private void deleteLast() {
        if (!currentExpression.isBlank()) {
            currentExpression = currentExpression.substring(0, currentExpression.length() - 1);
            fullExpression = currentExpression;
            updateExpression(currentExpression);
            updateResult(currentExpression.isBlank() ? "0" : currentExpression);
        }
    }

    private void updateExpression(String expression) {
        expressionLabel.setText(BigNumberUtil.toDisplayText(expression, DISPLAY_MAX_LENGTH));
    }

    private void updateResult(String result) {
        fullResult = result;
        displayLabel.setText(BigNumberUtil.toDisplayText(result, DISPLAY_MAX_LENGTH));
    }

    private boolean isError(String value) {
        return value != null && value.startsWith("Error");
    }

    @FunctionalInterface
    private interface ResultSupplier {
        String get();
    }
}
