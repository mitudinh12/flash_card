package com.flash_card.view.studentMode;

import com.flash_card.view.flashcardSet.QuizFlashcardSetController;
import com.flash_card.view.flashcardSet.QuizResultController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import java.io.IOException;

public class StudentQuizFlashcardSetController extends QuizFlashcardSetController {

    @Override
    public void initialize() {
        setReloadFxml("/com/flash_card/fxml/student-quiz-flashcard.fxml");
        bindProperties();
        setAnswerButtonActions();
        setFlashcardSet(quizSession.getSetId(), quizSession.getSetName());
    }

    @Override
    protected void goToResultPage() {
        quizSession.setQuizId(quizId);
        goToPage("/com/flash_card/fxml/student-quiz-result.fxml", setName.getScene());
    }
}
