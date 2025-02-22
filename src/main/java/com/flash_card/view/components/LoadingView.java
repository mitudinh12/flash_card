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
        // Create a spinning wheel to show the loading progress
        ProgressIndicator progressIndicator = new ProgressIndicator();

        // Create a label for the loading message
        Label loadingLabel = new Label("Loading...");

        // Create a vertical box to hold the spinning wheel and the loading message
        VBox loadingPane = new VBox();
        loadingPane.setAlignment(Pos.CENTER);
        loadingPane.getChildren().addAll(progressIndicator, loadingLabel);

        // Set the style of the spinning wheel and the loading message
        loadingPane.setStyle("-fx-font-size: 32pt; -fx-text-fill: white; -fx-background-color: black;");
        progressIndicator.setStyle("-fx-progress-color: white;");

        // Add the vertical box to the stack pane
        getChildren().add(loadingPane);
    }
}
