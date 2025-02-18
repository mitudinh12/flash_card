package com.flash_card.view.flashcardSet;

import com.flash_card.framework.ViewController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;

import java.io.IOException;

public class ReviewFlashcardSetController extends ViewController {
    @FXML
    public Label setNameLabel;
    @FXML
    public Label studyTimeLabel;
    @FXML
    public Label studiedNumLabel;

    private int setId;
    private String setName;

    public void setFlashcardSet(int setId, String setName) {
        this.setId = setId;
        this.setName = setName;
        setNameLabel.setText(setName);
    }

    @FXML
    public void handleReview(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/flash_card/fxml/study-flashcard.fxml"));
            Parent root = loader.load();

            StudyFlashcardSetController studySetController = loader.getController();
            studySetController.setFlashcardSet(setId, setName);

            Scene scene = setNameLabel.getScene();
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleQuiz(ActionEvent actionEvent) {
    }

    @FXML
    public void goToHome(ActionEvent actionEvent) {
        goToPage("/com/flash_card/fxml/home.fxml", setNameLabel.getScene());
    }
}