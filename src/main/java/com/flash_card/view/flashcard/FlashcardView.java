package com.flash_card.view.flashcard;

import javafx.animation.FadeTransition;
import javafx.animation.RotateTransition;
import javafx.geometry.Point3D;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

public class FlashcardView extends StackPane {
    private final Text termText;
    private final Text definitionText;
    private boolean showingTerm;

    public FlashcardView(String term, String definition) {
        termText = new Text(term);
        termText.setFill(Color.WHITE);
        termText.setFont(Font.font(24));
        termText.setTextAlignment(TextAlignment.CENTER);
        termText.setStyle("-fx-font-weight: bold;");
        termText.wrappingWidthProperty().bind(widthProperty().subtract(20));

        definitionText = new Text(definition);
        definitionText.setFill(Color.DARKBLUE);
        definitionText.setFont(Font.font(24));
        definitionText.setTextAlignment(TextAlignment.CENTER);
        definitionText.setStyle("-fx-font-weight: bold;");
        definitionText.wrappingWidthProperty().bind(widthProperty().subtract(20));

        showingTerm = true;

        setStyle("-fx-background-color: #00AAFF; -fx-pref-height: 400px; -fx-background-radius: 10; -fx-alignment: center;");
        getChildren().add(termText);

        setOnMouseClicked(event -> flip());
    }

    private void flip() {
        RotateTransition rotateOut = new RotateTransition(Duration.seconds(0.5), this);
        rotateOut.setAxis(new Point3D(0, 1, 0));
        rotateOut.setFromAngle(0);
        rotateOut.setToAngle(90);
        rotateOut.setOnFinished(event -> {
            getChildren().clear();
            if (showingTerm) {
                getChildren().add(definitionText);
                setStyle("-fx-background-color: white; -fx-pref-height: 400px; -fx-background-radius: 10; -fx-alignment: center;");
            } else {
                getChildren().add(termText);
                setStyle("-fx-background-color: #00AAFF; -fx-pref-height: 400px; -fx-background-radius: 10; -fx-alignment: center;");
            }
            showingTerm = !showingTerm;

            RotateTransition rotateIn = new RotateTransition(Duration.seconds(0.5), this);
            rotateIn.setAxis(new Point3D(0, 1, 0));
            rotateIn.setFromAngle(270);
            rotateIn.setToAngle(360);
            rotateIn.play();
        });

        FadeTransition fadeOut = new FadeTransition(Duration.seconds(0.25), this);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        fadeOut.setOnFinished(event -> {
            rotateOut.play();
            FadeTransition fadeIn = new FadeTransition(Duration.seconds(0.25), this);
            fadeIn.setFromValue(0.0);
            fadeIn.setToValue(1.0);
            fadeIn.play();
        });

        fadeOut.play();
    }
}