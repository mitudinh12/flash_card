package com.flash_card.view.flashcardSet;

import com.flash_card.framework.ViewController;
import com.flash_card.view_model.entity.EntityManagerViewModel;
import com.flash_card.view_model.flashcard_set.QuizResultViewModel;
import jakarta.persistence.EntityManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;

/**
 * Controller for managing the quiz result view.
 * Displays the results of a completed quiz, including a pie chart and quiz details.
 */
public class QuizResultController extends ViewController {
    /**
     * EntityManager instance for database operations.
     */
    private final EntityManager entityManager = EntityManagerViewModel.getEntityManager();

    /**
     * Label for displaying the name of the flashcard set.
     */
    @FXML
    public Label setNameLabel;

    /**
     * Label for displaying the total quiz time.
     */
    @FXML
    public Label quizTimeLabel;

    /**
     * Pie chart for visualizing the quiz results (correct vs. wrong answers).
     */
    @FXML
    public PieChart pieChart;

    /**
     * Singleton instance of the QuizSession.
     */
    protected QuizSession quizSession = QuizSession.getInstance();

    /**
     * Initializes the controller and sets up the result view.
     */
    @FXML
    public void initialize() {
        setReloadFxml("/com/flash_card/fxml/quiz-result.fxml");
        setResultView(quizSession.getQuizId());
    }

    /**
     * Configures the result view with quiz data.
     *
     * @param quizId the ID of the completed quiz
     */
    public void setResultView(int quizId) {
        QuizResultViewModel quizResultViewModel = new QuizResultViewModel(entityManager, quizId);
        String setName = quizResultViewModel.getSetName();
        int correctCount = quizResultViewModel.getTotalCorrect();
        int wrongCount = quizResultViewModel.getTotalWrong();
        quizTimeLabel.textProperty().bind(quizResultViewModel.quizTimeProperty());

        setNameLabel.setText(setName);
        PieChart.Data correctData = new PieChart.Data(localization.getMessage("flashcardSet.correct") + "( " + correctCount + ")", correctCount);
        PieChart.Data wrongData = new PieChart.Data(localization.getMessage("flashcardSet.wrong") + " (" + wrongCount + ")", wrongCount);
        pieChart.getData().clear();
        pieChart.getData().addAll(correctData, wrongData);
    }

    /**
     * Handles the "Study" button action.
     * Navigates to the study flashcard view for further review.
     *
     * @param actionEvent the event triggered by the user
     */
    @FXML
    public void handleStudy(ActionEvent actionEvent) {
        StudySession session = StudySession.getInstance();
        session.setSetId(quizSession.getSetId());
        session.setSetName(quizSession.getSetName());
        quizSession.clear();
        goToPage("/com/flash_card/fxml/study-flashcard.fxml", setNameLabel.getScene());
    }

    /**
     * Handles the "Home" button action.
     * Navigates back to the home page and clears the quiz session.
     *
     * @param actionEvent the event triggered by the user
     */
    @FXML
    public void goToHome(ActionEvent actionEvent) {
        quizSession.clear();
        goToPage("/com/flash_card/fxml/home.fxml", setNameLabel.getScene());
    }
}
