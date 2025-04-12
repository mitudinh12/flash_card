package com.flash_card.view.studentMode;

import com.flash_card.view.flashcardSet.QuizResultController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
/**
 * Controller class for managing the quiz result view in student mode.
 * Extends the functionality of `QuizResultController` to customize behavior for students.
 */
public class StudentQuizResultController extends QuizResultController {
    /**
     * Initializes the controller by setting up the FXML file and loading the quiz results.
     * Configures the result view based on the quiz session's quiz ID.
     */
    @Override
    public void initialize() {
        setReloadFxml("/com/flash_card/fxml/student-quiz-result.fxml");
        setResultView(quizSession.getQuizId());
    }
    /**
     * Navigates back to the student home page when the corresponding button is clicked.
     *
     * @param event the action event triggered by the button click
     */
    @FXML
    public void goToStudentHome(ActionEvent event) {
        goToPage("/com/flash_card/fxml/student-class.fxml", setNameLabel.getScene());
    }
}
