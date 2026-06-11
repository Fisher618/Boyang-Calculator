package com.boyang.calculator;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

/**
 * JavaFX 程序启动入口，负责加载主界面和全局样式。
 */
public class MainApp extends Application {

    private static final int WINDOW_WIDTH = 1100;
    private static final int WINDOW_HEIGHT = 700;

    /**
     * 创建主窗口并显示计算器界面。
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/boyang/calculator/fxml/main.fxml"));
        Scene scene = new Scene(loader.load(), WINDOW_WIDTH, WINDOW_HEIGHT);

        URL cssUrl = getClass().getResource("/com/boyang/calculator/css/apple-dark.css");
        if (cssUrl != null) {
            scene.getStylesheets().add(cssUrl.toExternalForm());
        }

        stage.setTitle("Apple Style Calculator");
        stage.setMinWidth(900);
        stage.setMinHeight(600);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * 应用程序主方法。
     */
    public static void main(String[] args) {
        launch(args);
    }
}
