package com.boyang.calculator.controller;

import com.boyang.calculator.engine.ProgrammerCalculatorEngine;
import com.boyang.calculator.model.NumberBase;
import com.boyang.calculator.util.BigNumberUtil;
import com.boyang.calculator.util.ResultInteractionUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;

/**
 * 程序员计算器控制器，使用 BigInteger 处理超大整数。
 */
public class ProgrammerController {

    private static final int DISPLAY_MAX_LENGTH = 48;

    @FXML
    private Label expressionLabel;

    @FXML
    private Label hexValue;

    @FXML
    private Label decValue;

    @FXML
    private Label octValue;

    @FXML
    private Label binValue;

    private final ProgrammerCalculatorEngine engine = new ProgrammerCalculatorEngine();

    private NumberBase activeBase = NumberBase.DEC;
    private String currentInput = "0";
    private String leftOperand = "0";
    private String pendingOperator;
    private String fullExpression = "";
    private String fullResult = "0";
    private boolean waitingForNextNumber;

    /**
     * 初始化各进制显示。
     */
    @FXML
    private void initialize() {
        setupResultLabel(hexValue, NumberBase.HEX);
        setupResultLabel(decValue, NumberBase.DEC);
        setupResultLabel(octValue, NumberBase.OCT);
        setupResultLabel(binValue, NumberBase.BIN);
        updateExpression("");
        updateBaseValues();
    }

    /**
     * 处理程序员计算器按钮。
     */
    @FXML
    private void handleProgrammerAction(ActionEvent event) {
        String text = ((Button) event.getSource()).getText();
        try {
            switch (text) {
                case "HEX", "DEC", "OCT", "BIN" -> switchBase(NumberBase.valueOf(text));
                case "AC" -> clearAll();
                case "DEL" -> deleteLast();
                case "AND", "OR", "XOR", "<<", ">>", "+", "-", "×" -> handleOperator(normalizeOperator(text));
                case "NOT" -> applyNot();
                case "=" -> handleEquals();
                default -> appendDigit(text);
            }
        } catch (RuntimeException exception) {
            fullResult = "Error: invalid programmer input";
            updateDisplayError();
        }
    }

    private void switchBase(NumberBase newBase) {
        if (newBase == activeBase) {
            return;
        }
        boolean wasWaitingForNextNumber = waitingForNextNumber;
        if (pendingOperator != null) {
            leftOperand = engine.convert(leftOperand, activeBase, newBase);
        }
        currentInput = engine.convert(currentInput, activeBase, newBase);
        activeBase = newBase;
        fullResult = currentInput;
        waitingForNextNumber = wasWaitingForNextNumber;
        updateCurrentExpression();
        updateBaseValues();
    }

    private void appendDigit(String digit) {
        if (!isDigitAllowed(digit)) {
            return;
        }
        if (waitingForNextNumber || "0".equals(currentInput) || isError(currentInput)) {
            currentInput = digit;
            waitingForNextNumber = false;
        } else {
            currentInput += digit;
        }
        fullResult = currentInput;
        updateCurrentExpression();
        updateBaseValues();
    }

    private void handleOperator(String operator) {
        if (pendingOperator != null && !waitingForNextNumber) {
            currentInput = engine.calculate(leftOperand, pendingOperator, currentInput, activeBase);
        }
        leftOperand = currentInput;
        pendingOperator = operator;
        waitingForNextNumber = true;
        fullExpression = leftOperand + " " + operator;
        fullResult = currentInput;
        updateExpression(fullExpression);
        updateBaseValues();
    }

    private void handleEquals() {
        if (pendingOperator == null) {
            return;
        }
        String rightOperand = currentInput;
        fullExpression = leftOperand + " " + pendingOperator + " " + rightOperand + " =";
        currentInput = engine.calculate(leftOperand, pendingOperator, rightOperand, activeBase);
        fullResult = currentInput;
        pendingOperator = null;
        waitingForNextNumber = true;
        updateExpression(fullExpression);
        updateBaseValues();
    }

    private void applyNot() {
        fullExpression = "NOT " + currentInput;
        currentInput = engine.not(currentInput, activeBase);
        fullResult = currentInput;
        waitingForNextNumber = true;
        updateExpression(fullExpression);
        updateBaseValues();
    }

    private void clearAll() {
        currentInput = "0";
        leftOperand = "0";
        pendingOperator = null;
        fullExpression = "";
        fullResult = "0";
        waitingForNextNumber = false;
        updateExpression("");
        updateBaseValues();
    }

    private void deleteLast() {
        if (waitingForNextNumber || isError(currentInput)) {
            return;
        }
        if (currentInput.length() <= 1 || (currentInput.length() == 2 && currentInput.startsWith("-"))) {
            currentInput = "0";
        } else {
            currentInput = currentInput.substring(0, currentInput.length() - 1);
        }
        fullResult = currentInput;
        updateCurrentExpression();
        updateBaseValues();
    }

    private void updateCurrentExpression() {
        if (pendingOperator == null) {
            fullExpression = currentInput;
        } else if (waitingForNextNumber) {
            fullExpression = leftOperand + " " + pendingOperator;
        } else {
            fullExpression = leftOperand + " " + pendingOperator + " " + currentInput;
        }
        updateExpression(fullExpression);
    }

    private void updateExpression(String expression) {
        expressionLabel.setText(BigNumberUtil.toDisplayText(expression, DISPLAY_MAX_LENGTH));
    }

    private void updateBaseValues() {
        try {
            hexValue.setText(BigNumberUtil.toDisplayText(engine.convert(currentInput, activeBase, NumberBase.HEX), DISPLAY_MAX_LENGTH));
            decValue.setText(BigNumberUtil.toDisplayText(engine.convert(currentInput, activeBase, NumberBase.DEC), DISPLAY_MAX_LENGTH));
            octValue.setText(BigNumberUtil.toDisplayText(engine.convert(currentInput, activeBase, NumberBase.OCT), DISPLAY_MAX_LENGTH));
            binValue.setText(BigNumberUtil.toDisplayText(engine.convert(currentInput, activeBase, NumberBase.BIN), DISPLAY_MAX_LENGTH));
        } catch (RuntimeException exception) {
            updateDisplayError();
        }
    }

    private void updateDisplayError() {
        hexValue.setText("Error");
        decValue.setText("Error");
        octValue.setText("Error");
        binValue.setText("Error");
    }

    private void setupResultLabel(Label label, NumberBase base) {
        label.setTooltip(new Tooltip("点击复制结果，长结果点击查看"));
        label.setOnMouseClicked(event -> {
            String result = isError(fullResult) ? fullResult : engine.convert(currentInput, activeBase, base);
            ResultInteractionUtil.handleResultClick(label, fullExpression, result);
        });
    }

    private boolean isDigitAllowed(String digit) {
        if (digit.length() != 1) {
            return false;
        }
        return Character.digit(digit.charAt(0), activeBase.getRadix()) >= 0;
    }

    private boolean isError(String value) {
        return value != null && value.startsWith("Error");
    }

    private String normalizeOperator(String operator) {
        return "×".equals(operator) ? "*" : operator;
    }
}
