package com.flash_card.view.components;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class LoadingView extends StackPane {
    public LoadingView() {
        ProgressIndicator progressIndicator = new ProgressIndicator();
        Label loadingLabel = new Label("Loading...");
        VBox loadingPane = new VBox();
        loadingPane.setAlignment(Pos.CENTER);
        loadingPane.getChildren().addAll(progressIndicator, loadingLabel);
        loadingPane.setStyle("-fx-font-size: 20pt; -fx-text-fill: white; -fx-background-color: transparent;");
        progressIndicator.setStyle("-fx-progress-color: #01971F;");
        setStyle("-fx-background-color: transparent;");
        getChildren().add(loadingPane);
    }
}
