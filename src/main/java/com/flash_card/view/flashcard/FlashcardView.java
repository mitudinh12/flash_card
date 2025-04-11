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
/**
 * A custom view for displaying a flashcard with a term and definition.
 * Provides functionality to flip between the term and definition with animations.
 */
public class FlashcardView extends StackPane {
    /**
     * Font size for the term and definition text.
     */
    private static final int FONT_SIZE = 24;

    /**
     * Padding for the wrapping width of the text.
     */
    private static final int TEXT_PADDING = 20;

    /**
     * Duration for the rotate transition animation in seconds.
     */
    private static final double ROTATE_DURATION = 0.5;

    /**
     * Duration for the fade transition animation in seconds.
     */
    private static final double FADE_DURATION = 0.25;

    /**
     * Angle for the first half of the rotation animation.
     */
    private static final int ROTATE_OUT_ANGLE = 90;

    /**
     * Starting angle for the second half of the rotation animation.
     */
    private static final int ROTATE_IN_START_ANGLE = 270;

    /**
     * Ending angle for the second half of the rotation animation.
     */
    private static final int ROTATE_IN_END_ANGLE = 360;
    /**
     * The text node for displaying the term of the flashcard.
     */
    private final Text termText;
    /**
     * The text node for displaying the definition of the flashcard.
     */
    private final Text definitionText;
    /**
     * A flag indicating whether the term side of the flashcard is currently being displayed.
     */
    private boolean showingTerm;
    /**
     * Constructs a new FlashcardView with the specified term and definition.
     *
     * @param term       the term to display on the flashcard
     * @param definition the definition to display on the flashcard
     */
    public FlashcardView(String term, String definition) {
        termText = new Text(term);
        termText.setFill(Color.WHITE);
        termText.setFont(Font.font(FONT_SIZE));
        termText.setTextAlignment(TextAlignment.CENTER);
        termText.setStyle("-fx-font-weight: bold;");
        termText.wrappingWidthProperty().bind(widthProperty().subtract(TEXT_PADDING));

        definitionText = new Text(definition);
        definitionText.setFill(Color.DARKBLUE);
        definitionText.setFont(Font.font(FONT_SIZE));
        definitionText.setTextAlignment(TextAlignment.CENTER);
        definitionText.setStyle("-fx-font-weight: bold;");
        definitionText.wrappingWidthProperty().bind(widthProperty().subtract(TEXT_PADDING));

        showingTerm = true;

        setStyle("-fx-background-color: #00AAFF; -fx-pref-height: 400px; -fx-background-radius: 10; -fx-alignment: center;");
        getChildren().add(termText);

        setOnMouseClicked(event -> flip());
    }
    /**
     * Flips the flashcard to show the other side (term or definition).
     * Includes a rotate and fade animation during the flip.
     */
    private void flip() {
        RotateTransition rotateOut = new RotateTransition(Duration.seconds(ROTATE_DURATION), this);
        rotateOut.setAxis(new Point3D(0, 1, 0));
        rotateOut.setFromAngle(0);
        rotateOut.setToAngle(ROTATE_OUT_ANGLE);
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

            RotateTransition rotateIn = new RotateTransition(Duration.seconds(ROTATE_DURATION), this);
            rotateIn.setAxis(new Point3D(0, 1, 0));
            rotateIn.setFromAngle(ROTATE_IN_START_ANGLE);
            rotateIn.setToAngle(ROTATE_IN_END_ANGLE);
            rotateIn.play();
        });

        FadeTransition fadeOut = new FadeTransition(Duration.seconds(FADE_DURATION), this);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        fadeOut.setOnFinished(event -> {
            rotateOut.play();
            FadeTransition fadeIn = new FadeTransition(Duration.seconds(FADE_DURATION), this);
            fadeIn.setFromValue(0.0);
            fadeIn.setToValue(1.0);
            fadeIn.play();
        });

        fadeOut.play();
    }
}
