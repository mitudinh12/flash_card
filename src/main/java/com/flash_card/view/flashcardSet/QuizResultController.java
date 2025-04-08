package com.flash_card.view.flashcardSet;

import com.flash_card.framework.ViewController;
import com.flash_card.view_model.entity.EntityManagerViewModel;
import com.flash_card.view_model.flashcard_set.QuizResultViewModel;
import jakarta.persistence.EntityManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;

public class QuizResultController extends ViewController {
    private final EntityManager entityManager = EntityManagerViewModel.getEntityManager();
    @FXML
    public Label setNameLabel;
    @FXML
    public Label quizTimeLabel;
    @FXML
    public PieChart pieChart;
    protected QuizSession quizSession = QuizSession.getInstance();
    private String setName;

    @FXML
    public void initialize() {
        setReloadFxml("/com/flash_card/fxml/quiz-result.fxml");
        setResultView(quizSession.getQuizId());
    }

    public void setResultView(int quizId) {
        QuizResultViewModel quizResultViewModel = new QuizResultViewModel(entityManager, quizId);
        this.setName = quizResultViewModel.getSetName();
        int correctCount = quizResultViewModel.getTotalCorrect();
        int wrongCount = quizResultViewModel.getTotalWrong();
        quizTimeLabel.textProperty().bind(quizResultViewModel.quizTimeProperty());

        setNameLabel.setText(setName);
        PieChart.Data correctData = new PieChart.Data(localization.getMessage("flashcardSet.correct") + "( " + correctCount + ")", correctCount);
        PieChart.Data wrongData = new PieChart.Data(localization.getMessage("flashcardSet.wrong") + " (" + wrongCount + ")", wrongCount);
        pieChart.getData().clear();
        pieChart.getData().addAll(correctData, wrongData);
    }

    @FXML
    public void handleStudy(ActionEvent actionEvent) {
        StudySession session = StudySession.getInstance();
        session.setSetId(quizSession.getSetId());
        session.setSetName(quizSession.getSetName());
        goToPage("/com/flash_card/fxml/study-flashcard.fxml", setNameLabel.getScene());
    }

    @FXML
    public void goToHome(ActionEvent actionEvent) {
        quizSession.clear();
        goToPage("/com/flash_card/fxml/home.fxml", setNameLabel.getScene());
    }
}
