package com.boyang.calculator.controller;

import com.boyang.calculator.engine.ScientificCalculatorEngine;
import com.boyang.calculator.util.BigNumberUtil;
import com.boyang.calculator.util.ResultInteractionUtil;
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
    private boolean justEvaluated;

    /**
     * 初始化科学计算器显示。
     */
    @FXML
    private void initialize() {
        displayLabel.setTooltip(new Tooltip("点击复制结果，长结果点击查看"));
        displayLabel.setOnMouseClicked(event ->
                ResultInteractionUtil.handleResultClick(displayLabel, fullExpression, fullResult));
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
            case "x²" -> appendSquare();
            case "sqrt" -> appendFunction("sqrt");
            case "xʸ" -> appendOperator("^");
            case "sin", "cos", "tan", "ln", "log" -> appendFunction(text);
            case "π" -> appendToken("3.14159265358979323846264338327950288419716939937510");
            case "e" -> appendToken("2.71828182845904523536028747135266249775724709369995");
            case "Deg/Rad" -> expressionLabel.setText("Deg/Rad reserved");
            default -> appendToken(text);
        }
    }

    private void appendToken(String token) {
        if (justEvaluated || isError(fullResult)) {
            currentExpression = "";
            justEvaluated = false;
        }
        currentExpression += token;
        updateInputDisplay();
    }

    private void appendOperator(String operator) {
        if (currentExpression.isBlank() && !isError(fullResult)) {
            currentExpression = fullResult;
        }
        currentExpression += " " + operator + " ";
        justEvaluated = false;
        updateInputDisplay();
    }

    private void appendFunction(String functionName) {
        if (justEvaluated || isError(fullResult)) {
            currentExpression = "";
            justEvaluated = false;
        }
        currentExpression += functionName + "(";
        updateInputDisplay();
    }

    private void appendSquare() {
        if (currentExpression.isBlank()) {
            currentExpression = fullResult;
        }
        currentExpression += "^2";
        justEvaluated = false;
        updateInputDisplay();
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
            justEvaluated = true;
        }
    }

    private void clearAll() {
        currentExpression = "";
        fullExpression = "";
        justEvaluated = false;
        updateExpression("");
        updateResult("0");
    }

    private void deleteLast() {
        if (!currentExpression.isBlank()) {
            currentExpression = currentExpression.substring(0, currentExpression.length() - 1);
            justEvaluated = false;
            updateInputDisplay();
        }
    }

    private void updateInputDisplay() {
        fullExpression = currentExpression;
        updateExpression(currentExpression);
        displayLabel.setText(BigNumberUtil.toDisplayText(
                currentExpression.isBlank() ? "0" : currentExpression,
                DISPLAY_MAX_LENGTH
        ));
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

}
