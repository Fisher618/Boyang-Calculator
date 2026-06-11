package com.boyang.calculator.util;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;

/**
 * 完整计算结果查看窗口。
 */
public final class FullResultDialog {

    private FullResultDialog() {
    }

    /**
     * 显示完整表达式和完整结果，并提供复制结果能力。
     */
    public static void show(String expression, String result) {
        Stage stage = new Stage();
        stage.setTitle("完整计算结果");
        stage.initModality(Modality.NONE);

        TextArea expressionArea = createTextArea(expression == null || expression.isBlank() ? "无" : expression);
        TextArea resultArea = createTextArea(result == null || result.isBlank() ? "0" : result);
        resultArea.setPrefRowCount(8);

        Button copyButton = new Button("复制结果");
        copyButton.getStyleClass().addAll("dialog-button", "dialog-primary-button");
        copyButton.setOnAction(event -> {
            ClipboardContent content = new ClipboardContent();
            content.putString(resultArea.getText());
            Clipboard.getSystemClipboard().setContent(content);
        });

        Button closeButton = new Button("关闭");
        closeButton.getStyleClass().add("dialog-button");
        closeButton.setOnAction(event -> stage.close());

        HBox buttonBar = new HBox(10, copyButton, closeButton);
        buttonBar.setAlignment(Pos.CENTER_RIGHT);

        Label expressionTitle = createTitleLabel("表达式：");
        Label resultTitle = createTitleLabel("结果：");

        VBox root = new VBox(
                12,
                expressionTitle,
                expressionArea,
                resultTitle,
                resultArea,
                buttonBar
        );
        root.getStyleClass().add("full-result-dialog");
        root.setPadding(new Insets(20));
        VBox.setVgrow(resultArea, Priority.ALWAYS);

        Scene scene = new Scene(root, 680, 460);
        scene.setFill(Color.web("#050505"));
        URL cssUrl = FullResultDialog.class.getResource("/com/boyang/calculator/css/apple-dark.css");
        if (cssUrl != null) {
            scene.getStylesheets().add(cssUrl.toExternalForm());
        }
        stage.setScene(scene);
        stage.show();
    }

    private static TextArea createTextArea(String text) {
        TextArea textArea = new TextArea(text);
        textArea.setEditable(false);
        textArea.setWrapText(false);
        textArea.setPrefRowCount(3);
        textArea.getStyleClass().add("full-result-text-area");
        return textArea;
    }

    private static Label createTitleLabel(String text) {
        Label label = new Label(text);
        label.getStyleClass().add("dialog-section-title");
        return label;
    }
}
