package com.boyang.calculator.util;

import javafx.animation.PauseTransition;
import javafx.geometry.Bounds;
import javafx.scene.control.Label;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.text.Text;
import javafx.stage.Popup;
import javafx.util.Duration;

public final class ResultInteractionUtil {

    private ResultInteractionUtil() {
    }

    public static void handleResultClick(Label source, String expression, String result) {
        if (isTruncated(source, result)) {
            FullResultDialog.show(expression, result);
            return;
        }

        ClipboardContent content = new ClipboardContent();
        content.putString(result == null ? "" : result);
        Clipboard.getSystemClipboard().setContent(content);
        showCopiedFeedback(source);
    }

    private static boolean isTruncated(Label source, String result) {
        String fullResult = result == null ? "" : result;
        String displayedText = source.getText() == null ? "" : source.getText();
        if (!displayedText.equals(fullResult)) {
            return true;
        }

        double availableWidth = source.getWidth()
                - source.getInsets().getLeft()
                - source.getInsets().getRight();
        if (availableWidth <= 0) {
            return false;
        }

        Text measurement = new Text(fullResult);
        measurement.setFont(source.getFont());
        return measurement.getLayoutBounds().getWidth() > availableWidth;
    }

    private static void showCopiedFeedback(Label source) {
        Bounds bounds = source.localToScreen(source.getBoundsInLocal());
        if (bounds == null) {
            return;
        }

        Label message = new Label("计算结果已复制到剪切板");
        message.setStyle("""
                -fx-background-color: #2c2c2e;
                -fx-background-radius: 6px;
                -fx-text-fill: #f5f5f7;
                -fx-font-family: "Segoe UI", "Microsoft YaHei", sans-serif;
                -fx-font-size: 13px;
                -fx-padding: 8px 12px;
                """);

        Popup popup = new Popup();
        popup.setAutoFix(true);
        popup.setAutoHide(true);
        popup.getContent().add(message);
        popup.show(source, bounds.getMinX(), bounds.getMaxY() + 8);
        popup.setAnchorX(bounds.getMinX() + (bounds.getWidth() - popup.getWidth()) / 2);

        PauseTransition delay = new PauseTransition(Duration.millis(1400));
        delay.setOnFinished(event -> popup.hide());
        delay.play();
    }
}
