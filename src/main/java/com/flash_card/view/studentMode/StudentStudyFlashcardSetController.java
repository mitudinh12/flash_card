package com.flash_card.view.studentMode;

import com.flash_card.view.flashcardSet.StudyFlashcardSetController;
/**
 * Controller class for managing the study flashcard set in student mode.
 * Extends the functionality of `StudyFlashcardSetController` to customize behavior for students.
 */
public class StudentStudyFlashcardSetController extends StudyFlashcardSetController {
    /**
     * Initializes the controller by setting up the FXML file, binding properties,
     * starting the study session, loading flashcards, and displaying the first flashcard.
     */
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
    /**
     * Navigates to the review page for the current flashcard set.
     * Loads the review page FXML and sets it as the current scene.
     */
    @Override
    protected void goToReviewPage() {
        goToPage("/com/flash_card/fxml/student-review-flashcard.fxml", shuffleIcon.getScene());
    }
}
