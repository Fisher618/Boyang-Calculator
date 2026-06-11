package com.boyang.calculator.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 * 历史记录控制器，当前版本显示简单占位内容。
 */
public class HistoryController {

    @FXML
    private VBox historyList;

    /**
     * 初始化历史记录面板。
     */
    @FXML
    private void initialize() {
        showEmptyMessage();
    }

    /**
     * 清空历史记录显示。
     */
    @FXML
    private void clearHistory() {
        showEmptyMessage();
    }

    private void showEmptyMessage() {
        historyList.getChildren().setAll(new Label("暂无历史记录"));
    }
}
