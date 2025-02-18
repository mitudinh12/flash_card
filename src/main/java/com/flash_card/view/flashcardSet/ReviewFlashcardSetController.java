package com.flash_card.view.flashcardSet;

import com.flash_card.framework.DifficultyLevel;
import com.flash_card.framework.ViewController;
import com.flash_card.view_model.entity.EntityManagerViewModel;
import com.flash_card.view_model.flashcard_set.StudyFlashcardSetViewModel;
import com.flash_card.view_model.user_auth.AuthSessionViewModel;
import jakarta.persistence.EntityManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;

import java.io.IOException;

public class ReviewFlashcardSetController extends ViewController {
    private final EntityManager entityManager = EntityManagerViewModel.getEntityManager();
    private final StudyFlashcardSetViewModel viewModel = new StudyFlashcardSetViewModel(entityManager);
    private final AuthSessionViewModel authSessionViewModel = AuthSessionViewModel.getInstance();
    @FXML
    public Label setNameLabel;
    @FXML
    public Label studyTimeLabel;
    @FXML
    public Label studiedNumLabel;
    @FXML
    public PieChart pieChart;

    private int setId;
    private String setName;

    public void setFlashcardSet(int setId, String setName) {
        this.setId = setId;
        this.setName = setName;
        setNameLabel.setText(setName);
        String userId = authSessionViewModel.getVerifiedUserInfo().get("userId");

        // Load flashcards
        viewModel.loadFlashcards(setId, setName);
        viewModel.updateStudyDetails(userId, setId);
        studyTimeLabel.textProperty().bind(viewModel.studyTimeProperty());
        studiedNumLabel.textProperty().bind(viewModel.studiedNumProperty());

        //calculate studied and left cards
        int totalCards = viewModel.getTotalFlashcards();
        int studiedCards = viewModel.getStudiedFlashcards();
        int leftCards = totalCards - studiedCards;

        //create PieChart data
        PieChart.Data studiedData = new PieChart.Data("Studied (" + studiedCards + ")", studiedCards);
        PieChart.Data leftData = new PieChart.Data("Left (" + leftCards + ")", leftCards);

        //sdd data to PieChart
        pieChart.getData().clear();
        pieChart.getData().addAll(studiedData, leftData);
    }

    @FXML
    public void handleReview(ActionEvent actionEvent) {
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
    public void handleQuiz(ActionEvent actionEvent) {
    }

    @FXML
    public void goToHome(ActionEvent actionEvent) {
        goToPage("/com/flash_card/fxml/home.fxml", setNameLabel.getScene());
    }
}