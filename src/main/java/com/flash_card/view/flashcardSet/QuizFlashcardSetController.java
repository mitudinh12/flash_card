package com.flash_card.view.flashcardSet;

import com.flash_card.framework.ViewController;
import com.flash_card.view_model.entity.EntityManagerViewModel;
import com.flash_card.view_model.flashcard_set.QuizFlashcardSetViewModel;
import jakarta.persistence.EntityManager;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.util.Duration;

/**
 * Controller for managing the quiz flashcard set view.
 */
public class QuizFlashcardSetController extends ViewController {

    /**
     * EntityManager instance for database operations.
     */
    private final EntityManager entityManager = EntityManagerViewModel.getEntityManager();

    /**
     * ViewModel for managing quiz flashcard set data and logic.
     */
    private final QuizFlashcardSetViewModel viewModel = new QuizFlashcardSetViewModel(entityManager);

    /**
     * Singleton instance of the QuizSession.
     */
    protected QuizSession quizSession = QuizSession.getInstance();

    /**
     * ID of the current quiz session.
     */
    protected int quizId;

    /**
     * Duration for the pause between flashcard transitions.
     */
    private static final double PAUSE_DURATION_SECONDS = 0.5;


    /**
     * Label for displaying the current flashcard index.
     */
    @FXML
    protected Label index;

    /**
     * Label for displaying the total number of flashcards.
     */
    @FXML
    protected Label total;

    /**
     * Label for displaying the name of the flashcard set.
     */
    @FXML
    protected Label setName;

    /**
     * Button for the first answer option.
     */
    @FXML
    private Button answerButton1;

    /**
     * Button for the second answer option.
     */
    @FXML
    private Button answerButton2;

    /**
     * Button for the third answer option.
     */
    @FXML
    private Button answerButton3;

    /**
     * Button for the fourth answer option.
     */
    @FXML
    private Button answerButton4;

    /**
     * Text element for displaying the current flashcard term.
     */
    @FXML
    private Text term;

    /**
     * Text element for displaying instructions to the user.
     */
    @FXML
    private Text instructionText;

    /**
     * Initializes the controller and binds UI components to the view model.
     */
    @FXML
    public void initialize() {
        setReloadFxml("/com/flash_card/fxml/quiz-flashcard.fxml");
        bindProperties();
        setAnswerButtonActions();
        setFlashcardSet(quizSession.getSetId(), quizSession.getSetName());
    }

    /**
     * Binds UI properties to the view model.
     */
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

    /**
     * Sets actions for the answer buttons.
     */
    protected void setAnswerButtonActions() {
        answerButton1.setOnAction(event -> handleAnswer(answerButton1));
        answerButton2.setOnAction(event -> handleAnswer(answerButton2));
        answerButton3.setOnAction(event -> handleAnswer(answerButton3));
        answerButton4.setOnAction(event -> handleAnswer(answerButton4));
    }

    /**
     * Loads the flashcard set and starts the quiz.
     *
     * @param setId   the ID of the flashcard set
     * @param setNameParam the name of the flashcard set
     */
    public void setFlashcardSet(final int setId, final String setNameParam) {
        viewModel.loadFlashcards(setId, setNameParam);
        viewModel.startQuiz(authSessionViewModel.getVerifiedUserInfo().get("userId"), setId);
        this.quizId = viewModel.getQuizId();
        showFlashcard();
    }

    /**
     * Handles the close event and navigates back to the home page.
     *
     * @param event the mouse event
     */
    public void handleClose(final MouseEvent event) {
        viewModel.stopQuiz();
        goToPage("/com/flash_card/fxml/home.fxml", setName.getScene());
    }

    /**
     * Displays the current flashcard question.
     */
    public void showFlashcard() {
        viewModel.loadQuestion();
    }

    /**
     * Handles the user's answer selection.
     *
     * @param button the button representing the selected answer
     */
    public void handleAnswer(final Button button) {
        boolean isCorrect = viewModel.isAnswerCorrect(button.getText());
        button.getStyleClass().add(isCorrect ? "correct-answer" : "wrong-answer");
        disableButtons(true, button);

        PauseTransition pause = new PauseTransition(Duration.seconds(PAUSE_DURATION_SECONDS));
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

    /**
     * Disables or enables the answer buttons.
     *
     * @param disable      whether to disable the buttons
     * @param chosenButton the button that remains enabled
     */
    private void disableButtons(final boolean disable, final Button chosenButton) {
        answerButton1.setDisable(disable);
        answerButton2.setDisable(disable);
        answerButton3.setDisable(disable);
        answerButton4.setDisable(disable);
        chosenButton.setDisable(false);
    }

    /**
     * Resets the styles of the answer buttons.
     */
    private void resetButtonStyles() {
        for (Button button : new Button[]{answerButton1, answerButton2, answerButton3, answerButton4}) {
            button.getStyleClass().removeAll("correct-answer", "wrong-answer");
            button.getStyleClass().add("answer-buttons");
        }
    }

    /**
     * Navigates to the quiz result page.
     */
    protected void goToResultPage() {
        quizSession.setQuizId(quizId);
        goToPage("/com/flash_card/fxml/quiz-result.fxml", setName.getScene());
    }
}

