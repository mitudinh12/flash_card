package com.flash_card.view.flashcardSet;

import com.flash_card.framework.ViewController;
import com.flash_card.view_model.entity.EntityManagerViewModel;
import com.flash_card.view_model.flashcard_set.QuizFlashcardSetViewModel;
import com.flash_card.view_model.flashcard_set.QuizResultViewModel;
import com.flash_card.view_model.user_auth.AuthSessionViewModel;
import jakarta.persistence.EntityManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;

import javafx.event.ActionEvent;
import java.io.IOException;

public class QuizResultController extends ViewController {
    private final EntityManager entityManager = EntityManagerViewModel.getEntityManager();

    @FXML
    public Label setNameLabel;
    @FXML
    public Label quizTimeLabel;
    @FXML
    public PieChart pieChart;

    private int setId;
    private String setName;

    public void setResultView(int quizId) {
        QuizResultViewModel quizResultViewModel = new QuizResultViewModel(entityManager, quizId);

        //TODO: get data from quizResultViewModel
        this.setId = quizResultViewModel.getFlashcardSetId();
        this.setName = quizResultViewModel.getSetName();
        int correctCount = quizResultViewModel.getTotalCorrect();
        int wrongCount = quizResultViewModel.getTotalWrong();
        quizTimeLabel.textProperty().bind(quizResultViewModel.quizTimeProperty());

        setNameLabel.setText(setName);
        PieChart.Data correctData = new PieChart.Data("Correct (" + correctCount + ")", correctCount);
        PieChart.Data wrongData = new PieChart.Data("Wrong (" + wrongCount + ")", wrongCount);
        pieChart.getData().clear();
        pieChart.getData().addAll(correctData, wrongData);
    }

    @FXML
    public void handleStudy(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/flash_card/fxml/study-flashcard.fxml"));
            Parent root = loader.load();

            //pass data back to studyFlashcardController
            StudyFlashcardSetController studySetController = loader.getController();
            studySetController.setFlashcardSet(setId, setName);
            Scene scene = setNameLabel.getScene();
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void goToHome(ActionEvent actionEvent) {
        goToPage("/com/flash_card/fxml/home.fxml", setNameLabel.getScene());
    }


}
