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
    protected void goToResultPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/flash_card/fxml/student-quiz-result.fxml"));
            Parent root = loader.load();
            QuizResultController resultController = loader.getController();
            resultController.setResultView(quizId);
            Scene scene = setName.getScene();
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
