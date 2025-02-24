package com.flash_card.view.studentMode;

import com.flash_card.view.flashcardSet.ReviewFlashcardSetController;
import com.flash_card.view.flashcardSet.StudyFlashcardSetController;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;

public class StudentStudyFlashcardSetController extends StudyFlashcardSetController {
    @Override
    protected void goToReviewPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/flash_card/fxml/student-review-flashcard.fxml"));
            Parent root = loader.load();
            ReviewFlashcardSetController reviewController = loader.getController();
            reviewController.setFlashcardSet(setId, viewModel.setNameProperty().get());
            Scene scene = shuffleIcon.getScene();
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
