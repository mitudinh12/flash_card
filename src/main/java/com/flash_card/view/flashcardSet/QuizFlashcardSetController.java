package com.flash_card.view.flashcardSet;

import com.flash_card.framework.ViewController;
import com.flash_card.view_model.entity.EntityManagerViewModel;
import com.flash_card.view_model.flashcard_set.QuizFlashcardSetViewModel;
import com.flash_card.view_model.user_auth.AuthSessionViewModel;
import jakarta.persistence.EntityManager;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class QuizFlashcardSetController extends ViewController {
    private final EntityManager entityManager = EntityManagerViewModel.getEntityManager();
    private final QuizFlashcardSetViewModel viewModel = new QuizFlashcardSetViewModel(entityManager);
    private final AuthSessionViewModel authSessionViewModel = AuthSessionViewModel.getInstance();
    protected QuizSession quizSession = QuizSession.getInstance();

    protected int quizId;

    @FXML
    protected Label index, total, setName;
    @FXML
    private Button answerButton1, answerButton2, answerButton3, answerButton4;
    @FXML
    private Text term, instructionText;

    @FXML
    public void initialize() {
        setReloadFxml("/com/flash_card/fxml/quiz-flashcard.fxml");
        bindProperties();
        setAnswerButtonActions();
        setFlashcardSet(quizSession.getSetId(), quizSession.getSetName());
    }

    protected void bindProperties() {
        setName.textProperty().bind(viewModel.setNameProperty());
        total.textProperty().bind(viewModel.totalProperty());
        index.textProperty().bind(viewModel.currentIndexProperty().add(1).asString());
        term.textProperty().bind(viewModel.currentTermProperty());
        instructionText.textProperty().bind(viewModel.instructionTextProperty());

        answerButton1.textProperty().bind(viewModel.answer1Property());
        answerButton2.textProperty().bind(viewModel.answer2Property());
        answerButton3.textProperty().bind(viewModel.answer3Property());
        answerButton4.textProperty().bind(viewModel.answer4Property());
    }

    protected void setAnswerButtonActions() {
        answerButton1.setOnAction(event -> handleAnswer(answerButton1));
        answerButton2.setOnAction(event -> handleAnswer(answerButton2));
        answerButton3.setOnAction(event -> handleAnswer(answerButton3));
        answerButton4.setOnAction(event -> handleAnswer(answerButton4));
    }

    public void setFlashcardSet(int setId, String setName) {
        viewModel.loadFlashcards(setId, setName);
        viewModel.startQuiz(authSessionViewModel.getVerifiedUserInfo().get("userId"), setId);
        this.quizId = viewModel.getQuizId();
        showFlashcard();
    }

    public void handleClose(MouseEvent event) {
        viewModel.stopQuiz();
        goToPage("/com/flash_card/fxml/home.fxml", setName.getScene());
    }

    public void showFlashcard() {
        viewModel.loadQuestion();
    }

    public void handleAnswer(Button button) {
        boolean isCorrect = viewModel.isAnswerCorrect(button.getText());
        button.getStyleClass().add(isCorrect ? "correct-answer" : "wrong-answer");
        disableButtons(true, button);

        PauseTransition pause = new PauseTransition(Duration.seconds(0.5));
        pause.setOnFinished(event -> {
            resetButtonStyles();
            disableButtons(false, button);

            if (viewModel.isLastFlashcard()) {
                viewModel.finishQuiz();
                quizId = viewModel.getQuizId();
                goToResultPage();
            } else {
                viewModel.nextFlashcard();
            }
        });
        pause.play();
    }

    private void disableButtons(boolean disable, Button chosenButton) {
        answerButton1.setDisable(disable);
        answerButton2.setDisable(disable);
        answerButton3.setDisable(disable);
        answerButton4.setDisable(disable);
        chosenButton.setDisable(false);
    }

    private void resetButtonStyles() {
        for (Button button : new Button[]{answerButton1, answerButton2, answerButton3, answerButton4}) {
            button.getStyleClass().removeAll("correct-answer", "wrong-answer");
            button.getStyleClass().add("answer-buttons");
        }
    }

    protected void goToResultPage() {
        quizSession.setQuizId(quizId);
        goToPage("/com/flash_card/fxml/quiz-result.fxml", setName.getScene());
    }
}

