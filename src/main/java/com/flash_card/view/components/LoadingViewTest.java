package com.flash_card.view.components;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class LoadingViewTest extends Application {
    @Override
    public void start(Stage primaryStage) {
        LoadingView loadingView = new LoadingView();

        Scene scene = new Scene(loadingView, 400, 200);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}