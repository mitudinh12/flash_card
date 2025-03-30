package com.flash_card.view.studentMode;

import com.flash_card.view.flashcardSet.ReviewFlashcardSetController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class StudentReviewFlashcardSetController extends ReviewFlashcardSetController {
    @Override
    public void initialize() {
        setReloadFxml("/com/flash_card/fxml/student-review-flashcard.fxml");
        setFlashcardSet(session.getSetId(), session.getSetName());
    }

    @FXML
    public void goToStudentHome(ActionEvent event) {
        goToPage("/com/flash_card/fxml/student-class.fxml", setNameLabel.getScene());
    }
}
