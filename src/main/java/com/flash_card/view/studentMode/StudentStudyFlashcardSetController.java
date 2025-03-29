package com.flash_card.view.studentMode;

import com.flash_card.view.flashcardSet.ReviewFlashcardSetController;
import com.flash_card.view.flashcardSet.StudyFlashcardSetController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;

public class StudentStudyFlashcardSetController extends StudyFlashcardSetController {

    @Override
    public void initialize() {
        setReloadFxml("/com/flash_card/fxml/student-study-flashcard.fxml");
        setName.textProperty().bind(viewModel.setNameProperty());
        total.textProperty().bind(viewModel.totalProperty());
        index.textProperty().bind(viewModel.currentIndexProperty().add(1).asString());
        viewModel.startStudy(authSessionViewModel.getVerifiedUserInfo().get("userId"), session.getSetId());
        viewModel.loadFlashcards(session.getSetId(), session.getSetName());
        showFlashcard();
    }
    @Override
    protected void goToReviewPage() {
        goToPage("/com/flash_card/fxml/student-review-flashcard.fxml", shuffleIcon.getScene());
    }
}
