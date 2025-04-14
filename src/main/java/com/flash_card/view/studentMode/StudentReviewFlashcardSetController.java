package com.flash_card.view.studentMode;

import com.flash_card.view.flashcardSet.ReviewFlashcardSetController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
/**
 * Controller class for managing the review flashcard set in student mode.
 * Extends the functionality of `ReviewFlashcardSetController` to customize behavior for students.
 */
public class StudentReviewFlashcardSetController extends ReviewFlashcardSetController {
    /**
     * Initializes the controller by setting up the FXML file and loading the flashcard set.
     * Configures the review flashcard set based on the session's set ID and name.
     */
    @Override
    public void initialize() {
        setReloadFxml("/com/flash_card/fxml/student-review-flashcard.fxml");
        setFlashcardSet(session.getSetId(), session.getSetName());
    }
    /**
     * Navigates back to the student home page when the corresponding button is clicked.
     *
     * @param event the action event triggered by the button click
     */
    @FXML
    public void goToStudentHome(final ActionEvent event) {
        goToPage("/com/flash_card/fxml/student-class.fxml", setNameLabel.getScene());
    }
}
