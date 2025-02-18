package com.flash_card.view.flashcardSet;

import com.flash_card.framework.ViewController;
import com.flash_card.view.flashcard.FlashcardView;
import com.flash_card.view_model.flashcard_set.StudyFlashcardSetViewModel;
import com.flash_card.view_model.entity.EntityManagerViewModel;
import com.flash_card.view_model.user_auth.AuthSessionViewModel;
import com.flash_card.framework.DifficultyLevel;
import jakarta.persistence.EntityManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class StudyFlashcardSetController extends ViewController {
    private final EntityManager entityManager = EntityManagerViewModel.getEntityManager();
    private final StudyFlashcardSetViewModel viewModel = new StudyFlashcardSetViewModel(entityManager);
    private final AuthSessionViewModel authSessionViewModel = AuthSessionViewModel.getInstance();
    private int setId;

    @FXML
    public Label index;
    @FXML
    public Label total;
    @FXML
    public Label setName;
    @FXML
    public StackPane backIcon;
    @FXML
    public StackPane nextIcon;
    @FXML
    private VBox flashcardContainer;
    @FXML
    private Button easyButton;
    @FXML
    private Button hardButton;

    @FXML
    public void initialize() {
        setName.textProperty().bind(viewModel.setNameProperty());
        total.textProperty().bind(viewModel.totalProperty());
        index.textProperty().bind(viewModel.currentIndexProperty().add(1).asString());
    }

    //pass the setId and setName to the viewModel to start the study when click study or review
    public void setFlashcardSet(int setId, String setName) {
        this.setId = setId;
        viewModel.loadFlashcards(setId, setName);
        viewModel.startStudy(authSessionViewModel.getVerifiedUserInfo().get("userId"), setId);
        showFlashcard();
    }

    private void showFlashcard() {
        flashcardContainer.getChildren().clear();
        FlashcardView flashcardView = new FlashcardView(viewModel.getCurrentFlashcard().getTerm(), viewModel.getCurrentFlashcard().getDefinition());
        flashcardContainer.getChildren().add(flashcardView);
        updateButtonStates();
    }

    //update both back/next button and easy/hard button states
    private void updateButtonStates() {
        //set visible for back/next button
        backIcon.setVisible(viewModel.currentIndexProperty().get() != 0);
        nextIcon.setVisible(true);

        //highlight the button based on current card's difficulty level
        DifficultyLevel difficultyLevel = viewModel.getCurrentFlashcard().getDifficultLevel();
        easyButton.getStyleClass().remove("highlighted-button");
        hardButton.getStyleClass().remove("highlighted-button");
        if (difficultyLevel == DifficultyLevel.easy) {
            easyButton.getStyleClass().add("highlighted-button");
        } else if (difficultyLevel == DifficultyLevel.hard) {
            hardButton.getStyleClass().add("highlighted-button");
        }
    }

    @FXML
    public void handleClose(MouseEvent mouseEvent) {
        goToReviewPage();
        viewModel.endStudy(); //end the study when press close and go to review page
    }

    @FXML
    public void handleNext(MouseEvent mouseEvent) {
        if (viewModel.currentIndexProperty().get() < Integer.parseInt(viewModel.totalProperty().get()) - 1) {
            viewModel.nextFlashcard();
            showFlashcard();
        } else {
            goToReviewPage();
            viewModel.endStudy(); //end the study when after the last card
        }
    }

    @FXML
    public void handleBack(MouseEvent mouseEvent) {
        viewModel.previousFlashcard();
        showFlashcard();
    }

    @FXML
    public void handelEasy(ActionEvent actionEvent) {
        viewModel.updateFlashcardLevel(DifficultyLevel.easy);
        easyButton.getStyleClass().remove("highlighted-button");
        hardButton.getStyleClass().remove("highlighted-button");
        easyButton.getStyleClass().add("highlighted-button");
    }

    @FXML
    public void handleHard(ActionEvent actionEvent) {
        viewModel.updateFlashcardLevel(DifficultyLevel.hard);
        easyButton.getStyleClass().remove("highlighted-button");
        hardButton.getStyleClass().remove("highlighted-button");
        hardButton.getStyleClass().add("highlighted-button");
    }

    @FXML
    public void handleReset(MouseEvent mouseEvent) {
        viewModel.resetAllFlashcardLevel(); //reset all flashcard levels to hard
        viewModel.currentIndexProperty().set(0); //reset the card index to show the first card
        viewModel.loadFlashcards(setId, viewModel.setNameProperty().get()); //reload the flashcards
        index.textProperty().bind(viewModel.currentIndexProperty().add(1).asString());
        total.textProperty().bind(viewModel.totalProperty());
        showFlashcard();
    }

    @FXML
    public void handleShuffle(MouseEvent mouseEvent) {
    }
    private void goToReviewPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/flash_card/fxml/review-flashcard.fxml"));
            Parent root = loader.load();
            ReviewFlashcardSetController reviewController = loader.getController();
            reviewController.setFlashcardSet(setId, viewModel.setNameProperty().get());
            Stage stage = (Stage) flashcardContainer.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}