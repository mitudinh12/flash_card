package com.flash_card.view.studentMode;

import com.flash_card.view.flashcardSet.QuizResultController;
import javafx.event.ActionEvent;

public class StudentQuizResultController extends QuizResultController {

    public void goToStudentHome(ActionEvent event) {
        goToPage("/com/flash_card/fxml/student-class.fxml", setNameLabel.getScene());
    }
}
