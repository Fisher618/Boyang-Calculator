package com.boyang.calculator.util;

import javafx.animation.FadeTransition;
import javafx.scene.Node;
import javafx.util.Duration;

/**
 * JavaFX 动画工具类，封装简单淡入淡出效果。
 */
public final class AnimationUtil {

    private AnimationUtil() {
    }

    /**
     * 对节点执行淡入动画。
     */
    public static void fadeIn(Node node) {
        FadeTransition transition = new FadeTransition(Duration.millis(160), node);
        transition.setFromValue(0);
        transition.setToValue(1);
        transition.play();
    }

    /**
     * 对节点执行淡出动画。
     */
    public static void fadeOut(Node node) {
        FadeTransition transition = new FadeTransition(Duration.millis(160), node);
        transition.setFromValue(1);
        transition.setToValue(0);
        transition.play();
    }

    /**
     * 滑入动画占位方法。
     */
    public static void slideIn(Node node) {
        fadeIn(node);
    }

    /**
     * 滑出动画占位方法。
     */
    public static void slideOut(Node node) {
        fadeOut(node);
    }
}
