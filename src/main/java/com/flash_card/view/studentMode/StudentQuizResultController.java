package com.flash_card.view.studentMode;

import com.flash_card.view.flashcardSet.QuizResultController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class StudentQuizResultController extends QuizResultController {
    @Override
    public void initialize() {
        setReloadFxml("/com/flash_card/fxml/student-quiz-result.fxml");
        setResultView(quizSession.getQuizId());
    }

    @FXML
    public void goToStudentHome(ActionEvent event) {
        goToPage("/com/flash_card/fxml/student-class.fxml", setNameLabel.getScene());
    }
}
