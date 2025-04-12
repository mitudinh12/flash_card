package com.flash_card.view.flashcardSet;

import com.flash_card.framework.ViewController;
import com.flash_card.view_model.flashcard_set.StudyFlashcardSetViewModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;

import java.io.IOException;

/**
 * Controller for managing the review flashcard set view.
 * Handles the logic for displaying study progress, navigating to study or quiz views, and updating study details.
 */
public class ReviewFlashcardSetController extends ViewController {

    /**
     * ViewModel for managing study flashcard set data and logic.
     */
    private StudyFlashcardSetViewModel viewModel;

    /**
     * Singleton instance of the StudySession.
     */
    protected StudySession session = StudySession.getInstance();

    /**
     * Label for displaying the name of the flashcard set.
     */
    @FXML
    public Label setNameLabel;

    /**
     * Label for displaying the total study time.
     */
    @FXML
    public Label studyTimeLabel;

    /**
     * Label for displaying the number of studied flashcards.
     */
    @FXML
    public Label studiedNumLabel;

    /**
     * Pie chart for visualizing study progress (studied vs. remaining flashcards).
     */
    @FXML
    public PieChart pieChart;

    /**
     * Initializes the controller and sets up the review flashcard set view.
     */
    @FXML
    public void initialize() {
        setReloadFxml("/com/flash_card/fxml/review-flashcard.fxml");
        setFlashcardSet(session.getSetId(), session.getSetName());
    }

    /**
     * Configures the flashcard set for review and updates study details.
     *
     * @param setId   the ID of the flashcard set
     * @param setName the name of the flashcard set
     */
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
        PieChart.Data studiedData = new PieChart.Data(localization.getMessage("flashcardSet.studied") + " (" + studiedCards + ")", studiedCards);
        PieChart.Data leftData = new PieChart.Data(localization.getMessage("flashcardSet.left") + " (" + leftCards + ")", leftCards);
        pieChart.getData().clear();
        pieChart.getData().addAll(studiedData, leftData);
    }

    /**
     * Handles the "Review" button action.
     * Navigates to the study flashcard view for further review.
     *
     * @param actionEvent the event triggered by the user
     */
    @FXML
    public void handleReview(ActionEvent actionEvent) {
        goToPage("/com/flash_card/fxml/study-flashcard.fxml", setNameLabel.getScene());
    }

    /**
     * Handles the "Quiz" button action.
     * Navigates to the quiz flashcard view if the flashcard set has enough cards.
     * Displays an error alert if the set has fewer than 4 cards.
     *
     * @param actionEvent the event triggered by the user
     */
    @FXML
    public void handleQuiz(ActionEvent actionEvent) {
        if (viewModel.getTotalFlashcards() < 4) {
            showAlert(localization.getMessage("flashcardSet.errorTitle"), localization.getMessage("flashcardSet.errorQuizSet"));
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

    /**
     * Handles the "Home" button action.
     * Navigates back to the home page and clears the study session.
     *
     * @param actionEvent the event triggered by the user
     */
    @FXML
    public void goToHome(ActionEvent actionEvent) {
        session.clear();
        goToPage("/com/flash_card/fxml/home.fxml", setNameLabel.getScene());
    }
}
