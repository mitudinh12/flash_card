package com.flash_card.view.flashcardSet;

import com.flash_card.framework.ViewController;
import com.flash_card.localization.Localization;
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
    private StudyFlashcardSetViewModel viewModel;
    private final AuthSessionViewModel authSessionViewModel = AuthSessionViewModel.getInstance();
    protected StudySession session = StudySession.getInstance();

    @FXML
    public Label setNameLabel;
    @FXML
    public Label studyTimeLabel;
    @FXML
    public Label studiedNumLabel;
    @FXML
    public PieChart pieChart;

    @FXML
    public void initialize() {
        setReloadFxml("/com/flash_card/fxml/review-flashcard.fxml");
        setFlashcardSet(session.getSetId(), session.getSetName());
    }

    public void setFlashcardSet(int setId, String setName) {

        setNameLabel.setText(setName);
        String userId = authSessionViewModel.getVerifiedUserInfo().get("userId");
        this.viewModel = session.getViewModel();

        //load flashcards and update study details
        viewModel.loadFlashcards(setId, setName);
        viewModel.updateStudyDetails(userId, setId);
        studyTimeLabel.textProperty().bind(viewModel.studyTimeProperty());
        studiedNumLabel.textProperty().bind(viewModel.studiedNumProperty());

        //calculate data and display piechart
        int totalCards = viewModel.getTotalFlashcards();
        int studiedCards = viewModel.getStudiedFlashcards();
        int leftCards = totalCards - studiedCards;
        PieChart.Data studiedData = new PieChart.Data(localization.getMessage("flashcardSet.studied") +" (" + studiedCards + ")", studiedCards);
        PieChart.Data leftData = new PieChart.Data(localization.getMessage("flashcardSet.left")+" (" + leftCards + ")", leftCards);
        pieChart.getData().clear();
        pieChart.getData().addAll(studiedData, leftData);
    }

    @FXML
    public void handleReview(ActionEvent actionEvent) {
        goToPage("/com/flash_card/fxml/study-flashcard.fxml", setNameLabel.getScene());
    }

    @FXML
    public void handleQuiz(ActionEvent actionEvent) {
        if (viewModel.getTotalFlashcards() < 4) {
            showAlert(localization.getMessage("flashcardSet.errorTitle"),localization.getMessage("flashcardSet.errorQuizSet"));
        } else {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/flash_card/fxml/quiz-flashcard.fxml"));
                loader.setResources(localization.getBundle());
                Parent root = loader.load();
                QuizFlashcardSetController quizSetController = loader.getController();
                quizSetController.setFlashcardSet(session.getSetId(), session.getSetName());

                Scene scene = setNameLabel.getScene();
                scene.setRoot(root);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @FXML
    public void goToHome(ActionEvent actionEvent) {
        session.clear();
        goToPage("/com/flash_card/fxml/home.fxml", setNameLabel.getScene());
    }
}