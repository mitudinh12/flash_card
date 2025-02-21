package com.flash_card.view.flashcardSet;

import com.flash_card.framework.ViewController;
import com.flash_card.view_model.entity.EntityManagerViewModel;
import com.flash_card.view_model.flashcard_set.QuizFlashcardSetViewModel;
import com.flash_card.view_model.flashcard_set.StudyFlashcardSetViewModel;
import com.flash_card.view_model.user_auth.AuthSessionViewModel;
import jakarta.persistence.EntityManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

public class QuizFlashcardSetController extends ViewController {
    private final EntityManager entityManager = EntityManagerViewModel.getEntityManager();
    private final QuizFlashcardSetViewModel viewModel = new QuizFlashcardSetViewModel(entityManager);
    private final AuthSessionViewModel authSessionViewModel = AuthSessionViewModel.getInstance();
    private int setId;

    @FXML private Label index, total, setName;
    @FXML private Button answerButton1, answerButton2, answerButton3, answerButton4;
    @FXML private Text term, instructionText2;

    @FXML
    public void initialize() {

    }

    public void setFlashcardSet(int setId, String setName) {
        this.setId = setId;
        viewModel.loadFlashcards(setId, setName);
        viewModel.startQuiz(authSessionViewModel.getVerifiedUserInfo().get("userId"), setId);
    }

    public void handleClose(MouseEvent event) {
    }
}
