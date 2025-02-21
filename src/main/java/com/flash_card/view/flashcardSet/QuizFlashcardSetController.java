package com.flash_card.view.flashcardSet;

import com.flash_card.framework.ViewController;
import com.flash_card.view_model.entity.EntityManagerViewModel;
import com.flash_card.view_model.flashcard_set.QuizFlashcardSetViewModel;
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
    @FXML private Text term, instructionText;

    @FXML
    public void initialize() {
        setName.textProperty().bind(viewModel.setNameProperty());
        total.textProperty().bind(viewModel.totalProperty());
        index.textProperty().bind(viewModel.currentIndexProperty().add(1).asString());
        term.textProperty().bind(viewModel.currentTermProperty());
        instructionText.textProperty().bind(viewModel.instructionTextProperty());
        answerButton1.textProperty().bind(viewModel.answer1Property());
//        answerButton2.textProperty().bind(viewModel.answer2Property());
//        answerButton3.textProperty().bind(viewModel.answer3Property());
//        answerButton4.textProperty().bind(viewModel.answer4Property());
    }

    public void setFlashcardSet(int setId, String setName) {
        this.setId = setId;
        viewModel.loadFlashcards(setId, setName);
        viewModel.startQuiz(authSessionViewModel.getVerifiedUserInfo().get("userId"), setId);
        showFlashcard();
    }

    public void handleClose(MouseEvent event) {
        viewModel.stopQuiz();
        goToPage("/com/flash_card/fxml/home.fxml", setName.getScene());
    }

    public void showFlashcard() {
        viewModel.loadQuestion();
    }
}
