package com.flash_card.view.studentMode;

import com.flash_card.view.flashcardSet.ReviewFlashcardSetController;
import javafx.event.ActionEvent;

public class StudentReviewFlashcardSetController extends ReviewFlashcardSetController {
    public void goToStudentHome(ActionEvent event) {
        goToPage("/com/flash_card/fxml/student-class.fxml", setNameLabel.getScene());
    }
}
