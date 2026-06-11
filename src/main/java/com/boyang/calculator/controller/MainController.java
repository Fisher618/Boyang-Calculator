package com.boyang.calculator.controller;

import com.boyang.calculator.model.CalculatorMode;
import com.boyang.calculator.util.AnimationUtil;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.io.IOException;

/**
 * 主控制器，负责模式切换、页面加载和历史面板显示控制。
 */
public class MainController {

    @FXML
    private StackPane calculatorContent;

    @FXML
    private VBox historyPanel;

    @FXML
    private VBox historyContent;

    @FXML
    private Label modeTitle;

    private CalculatorMode currentMode = CalculatorMode.BASIC;

    /**
     * 初始化主界面，默认加载基础计算器。
     */
    @FXML
    private void initialize() {
        loadHistoryPanel();
        switchToBasic();
        setHistoryPanelVisible(true);
    }

    /**
     * 切换到基础计算器模式。
     */
    @FXML
    public void switchToBasic() {
        currentMode = CalculatorMode.BASIC;
        modeTitle.setText("Basic Calculator");
        loadCalculatorView("basic.fxml");
    }

    /**
     * 切换到科学计算器模式。
     */
    @FXML
    public void switchToScientific() {
        currentMode = CalculatorMode.SCIENTIFIC;
        modeTitle.setText("Scientific Calculator");
        loadCalculatorView("scientific.fxml");
    }

    /**
     * 切换到程序员计算器模式。
     */
    @FXML
    public void switchToProgrammer() {
        currentMode = CalculatorMode.PROGRAMMER;
        modeTitle.setText("Programmer Calculator");
        loadCalculatorView("programmer.fxml");
    }

    /**
     * 显示或隐藏历史记录面板。
     */
    @FXML
    public void toggleHistoryPanel() {
        setHistoryPanelVisible(!historyPanel.isVisible());
    }

    private void loadCalculatorView(String fxmlName) {
        try {
            Parent view = loadFxml(fxmlName);
            calculatorContent.getChildren().setAll(view);
            AnimationUtil.fadeIn(view);
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to load calculator view: " + fxmlName, exception);
        }
    }

    private void loadHistoryPanel() {
        try {
            Parent view = loadFxml("history.fxml");
            historyContent.getChildren().setAll(view);
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to load history view.", exception);
        }
    }

    private Parent loadFxml(String fxmlName) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/boyang/calculator/fxml/" + fxmlName));
        return loader.load();
    }

    private void setHistoryPanelVisible(boolean visible) {
        historyPanel.setVisible(visible);
        historyPanel.setManaged(visible);
    }

    public CalculatorMode getCurrentMode() {
        return currentMode;
    }
}
