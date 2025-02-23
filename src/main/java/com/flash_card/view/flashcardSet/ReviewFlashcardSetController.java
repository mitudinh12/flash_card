package com.flash_card.view.flashcardSet;

import com.flash_card.framework.TriviaQuestionGenerator;
import com.flash_card.framework.ViewController;
import com.flash_card.view_model.entity.EntityManagerViewModel;
import com.flash_card.view_model.flashcard_set.QuizFlashcardSetViewModel;
import com.flash_card.view_model.flashcard_set.StudyFlashcardSetViewModel;
import com.flash_card.view_model.user_auth.AuthSessionViewModel;
import jakarta.persistence.EntityManager;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class ReviewFlashcardSetController extends ViewController {
    private final EntityManager entityManager = EntityManagerViewModel.getEntityManager();
    private final StudyFlashcardSetViewModel viewModel = new StudyFlashcardSetViewModel(entityManager);
    private final AuthSessionViewModel authSessionViewModel = AuthSessionViewModel.getInstance();
    private final QuizFlashcardSetViewModel quizViewModel = new QuizFlashcardSetViewModel(entityManager);

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

        //load flashcards and update study details
        viewModel.loadFlashcards(setId, setName);
        viewModel.updateStudyDetails(userId, setId);
        studyTimeLabel.textProperty().bind(viewModel.studyTimeProperty());
        studiedNumLabel.textProperty().bind(viewModel.studiedNumProperty());

        //calculate data and display piechart
        int totalCards = viewModel.getTotalFlashcards();
        int studiedCards = viewModel.getStudiedFlashcards();
        int leftCards = totalCards - studiedCards;
        PieChart.Data studiedData = new PieChart.Data("Studied (" + studiedCards + ")", studiedCards);
        PieChart.Data leftData = new PieChart.Data("Left (" + leftCards + ")", leftCards);
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
        Stage loadingStage = showLoading();

        new Thread(() -> {
            // Get fake answers in the background
            TriviaQuestionGenerator triviaQuestionGenerator = TriviaQuestionGenerator.getInstance();
            quizViewModel.setQuizTopic(setId);
            triviaQuestionGenerator.generateFakeAnswers(quizViewModel.getQuizTopic());

            Platform.runLater(() -> {
                loadingStage.close(); // Close loading view
                //Open quiz flashcard view after trivia questions are generated
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/flash_card/fxml/quiz-flashcard.fxml"));
                    Parent root = loader.load();
                    QuizFlashcardSetController quizSetController = loader.getController();
                    quizSetController.setFlashcardSet(setId, setName);

                    Scene scene = setNameLabel.getScene();
                    scene.setRoot(root);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }).start();
    }

    @FXML
    public void goToHome(ActionEvent actionEvent) {
        goToPage("/com/flash_card/fxml/home.fxml", setNameLabel.getScene());
    }
}