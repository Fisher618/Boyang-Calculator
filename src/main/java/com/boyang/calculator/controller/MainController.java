package com.boyang.calculator.controller;

import com.boyang.calculator.model.CalculatorMode;
import com.boyang.calculator.util.AnimationUtil;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.io.IOException;

/**
 * 主控制器，负责模式切换、页面加载和历史面板显示控制。
 */
public class MainController {

    private static final double WIDE_LAYOUT_THRESHOLD = 1350;

    @FXML
    private StackPane mainRoot;

    @FXML
    private StackPane calculatorContent;

    @FXML
    private VBox historyPanel;

    @FXML
    private VBox historyContent;

    @FXML
    private Label modeTitle;

    @FXML
    private Button historyButton;

    @FXML
    private StackPane modeDrawerOverlay;

    @FXML
    private VBox modeDrawer;

    @FXML
    private Button basicModeButton;

    @FXML
    private Button scientificModeButton;

    @FXML
    private Button programmerModeButton;

    private CalculatorMode currentMode = CalculatorMode.BASIC;
    private boolean historyPanelAnimating;
    private boolean modeDrawerAnimating;
    private boolean wideLayout;
    private TranslateTransition historyTransition;

    /**
     * 初始化主界面，默认加载基础计算器。
     */
    @FXML
    private void initialize() {
        loadHistoryPanel();
        switchToBasic();
        setHistoryPanelVisible(false);
        setModeDrawerVisible(false);
        mainRoot.widthProperty().addListener((observable, oldWidth, newWidth) ->
                updateResponsiveLayout(newWidth.doubleValue()));
    }

    /**
     * 切换到基础计算器模式。
     */
    @FXML
    public void switchToBasic() {
        currentMode = CalculatorMode.BASIC;
        modeTitle.setText("Basic Calculator");
        loadCalculatorView("basic.fxml");
        updateSelectedMode();
        closeModeDrawer();
    }

    /**
     * 切换到科学计算器模式。
     */
    @FXML
    public void switchToScientific() {
        currentMode = CalculatorMode.SCIENTIFIC;
        modeTitle.setText("Scientific Calculator");
        loadCalculatorView("scientific.fxml");
        updateSelectedMode();
        closeModeDrawer();
    }

    /**
     * 切换到程序员计算器模式。
     */
    @FXML
    public void switchToProgrammer() {
        currentMode = CalculatorMode.PROGRAMMER;
        modeTitle.setText("Programmer Calculator");
        loadCalculatorView("programmer.fxml");
        updateSelectedMode();
        closeModeDrawer();
    }

    @FXML
    public void toggleModeDrawer() {
        if (modeDrawerAnimating) {
            return;
        }
        if (modeDrawerOverlay.isVisible()) {
            closeModeDrawer();
        } else {
            showModeDrawer();
        }
    }

    @FXML
    public void closeModeDrawer() {
        if (!modeDrawerOverlay.isVisible() || modeDrawerAnimating) {
            return;
        }
        modeDrawerAnimating = true;

        TranslateTransition slide = createDrawerSlide(-modeDrawer.getPrefWidth());
        FadeTransition fade = createDrawerFade(0);
        ParallelTransition transition = new ParallelTransition(slide, fade);
        transition.setOnFinished(event -> {
            setModeDrawerVisible(false);
            modeDrawer.setTranslateX(0);
            modeDrawerOverlay.setOpacity(1);
            modeDrawerAnimating = false;
        });
        transition.play();
    }

    /**
     * 显示或隐藏历史记录面板。
     */
    @FXML
    public void toggleHistoryPanel() {
        if (historyPanelAnimating) {
            return;
        }
        if (historyPanel.isVisible()) {
            hideHistoryPanel();
        } else {
            showHistoryPanel();
        }
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

    private void updateResponsiveLayout(double width) {
        boolean shouldUseWideLayout = width >= WIDE_LAYOUT_THRESHOLD;
        if (wideLayout == shouldUseWideLayout) {
            return;
        }

        wideLayout = shouldUseWideLayout;
        if (historyTransition != null) {
            historyTransition.stop();
        }
        historyPanelAnimating = false;
        historyPanel.setTranslateX(0);
        setHistoryPanelVisible(wideLayout);
        historyButton.setVisible(!wideLayout);
        historyButton.setManaged(!wideLayout);
    }

    private void showHistoryPanel() {
        historyPanelAnimating = true;
        setHistoryPanelVisible(true);
        historyPanel.setTranslateX(-historyPanel.getPrefWidth());

        historyTransition = createHistoryTransition(0);
        historyTransition.setOnFinished(event -> historyPanelAnimating = false);
        historyTransition.play();
    }

    private void hideHistoryPanel() {
        historyPanelAnimating = true;

        historyTransition = createHistoryTransition(-historyPanel.getWidth());
        historyTransition.setOnFinished(event -> {
            setHistoryPanelVisible(false);
            historyPanel.setTranslateX(0);
            historyPanelAnimating = false;
        });
        historyTransition.play();
    }

    private TranslateTransition createHistoryTransition(double targetX) {
        TranslateTransition transition = new TranslateTransition(Duration.millis(220), historyPanel);
        transition.setToX(targetX);
        transition.setInterpolator(Interpolator.EASE_BOTH);
        return transition;
    }

    private void showModeDrawer() {
        modeDrawerAnimating = true;
        setModeDrawerVisible(true);
        modeDrawer.setTranslateX(-modeDrawer.getPrefWidth());
        modeDrawerOverlay.setOpacity(0);

        TranslateTransition slide = createDrawerSlide(0);
        FadeTransition fade = createDrawerFade(1);
        ParallelTransition transition = new ParallelTransition(slide, fade);
        transition.setOnFinished(event -> modeDrawerAnimating = false);
        transition.play();
    }

    private TranslateTransition createDrawerSlide(double targetX) {
        TranslateTransition transition = new TranslateTransition(Duration.millis(240), modeDrawer);
        transition.setToX(targetX);
        transition.setInterpolator(Interpolator.EASE_BOTH);
        return transition;
    }

    private FadeTransition createDrawerFade(double targetOpacity) {
        FadeTransition transition = new FadeTransition(Duration.millis(180), modeDrawerOverlay);
        transition.setToValue(targetOpacity);
        return transition;
    }

    private void setModeDrawerVisible(boolean visible) {
        modeDrawerOverlay.setVisible(visible);
        modeDrawerOverlay.setManaged(visible);
    }

    private void updateSelectedMode() {
        setSelected(basicModeButton, currentMode == CalculatorMode.BASIC);
        setSelected(scientificModeButton, currentMode == CalculatorMode.SCIENTIFIC);
        setSelected(programmerModeButton, currentMode == CalculatorMode.PROGRAMMER);
    }

    private void setSelected(Button button, boolean selected) {
        button.getStyleClass().remove("drawer-mode-button-selected");
        if (selected) {
            button.getStyleClass().add("drawer-mode-button-selected");
        }
    }

    public CalculatorMode getCurrentMode() {
        return currentMode;
    }
}
