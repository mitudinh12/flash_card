package com.flash_card.view.flashcardSet;

import com.flash_card.framework.ViewController;
import com.flash_card.localization.Localization;
import com.flash_card.view.flashcard.FlashcardView;
import com.flash_card.view_model.flashcard_set.StudyFlashcardSetViewModel;
import com.flash_card.view_model.entity.EntityManagerViewModel;
import com.flash_card.view_model.user_auth.AuthSessionViewModel;
import com.flash_card.framework.DifficultyLevel;
import jakarta.persistence.EntityManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;

public class StudyFlashcardSetController extends ViewController {
    private final EntityManager entityManager = EntityManagerViewModel.getEntityManager();
    protected final StudyFlashcardSetViewModel viewModel = new StudyFlashcardSetViewModel(entityManager);
    protected final AuthSessionViewModel authSessionViewModel = AuthSessionViewModel.getInstance();
    protected StudySession session = StudySession.getInstance();

    @FXML protected Label index, total, setName;
    @FXML private StackPane backIcon, nextIcon;
    @FXML private VBox flashcardContainer;
    @FXML private Button easyButton, hardButton;
    @FXML private Text middleText, instructionText;
    @FXML protected ImageView shuffleIcon;

    @FXML
    public void initialize() {
        setReloadFxml("/com/flash_card/fxml/study-flashcard.fxml");
        setName.textProperty().bind(viewModel.setNameProperty());
        total.textProperty().bind(viewModel.totalProperty());
        index.textProperty().bind(viewModel.currentIndexProperty().add(1).asString());
        viewModel.startStudy(authSessionViewModel.getVerifiedUserInfo().get("userId"), session.getSetId());
        viewModel.loadFlashcards(session.getSetId(), session.getSetName());
        showFlashcard();
    }

    protected void showFlashcard() {
        flashcardContainer.getChildren().clear();
        boolean flashcardsEmpty = viewModel.getFlashcards().isEmpty();
        setFlashcardControlsVisibility(!flashcardsEmpty); //hide controls if no flashcards
        if (flashcardsEmpty) {
            Label messageLabel = new Label(localization.getMessage("flashcardSet.reStudy"));
            messageLabel.getStyleClass().add("message-label");
            flashcardContainer.setAlignment(Pos.TOP_CENTER);
            flashcardContainer.getChildren().add(messageLabel);
        } else {
            FlashcardView flashcardView = new FlashcardView(viewModel.getCurrentFlashcard().getTerm(), viewModel.getCurrentFlashcard().getDefinition());
            flashcardContainer.getChildren().add(flashcardView);
            updateButtonStates();
        }
    }

    private void setFlashcardControlsVisibility(boolean visible) {
        index.setVisible(visible);
        total.setVisible(visible);
        backIcon.setVisible(visible);
        nextIcon.setVisible(visible);
        easyButton.setVisible(visible);
        hardButton.setVisible(visible);
        middleText.setVisible(visible);
        shuffleIcon.setVisible(visible);
        instructionText.setVisible(visible);
    }

    private void updateButtonStates() {
        backIcon.setVisible(viewModel.currentIndexProperty().get() != 0);
        nextIcon.setVisible(true);
        easyButton.getStyleClass().remove("highlighted-button");
        hardButton.getStyleClass().remove("highlighted-button");
        DifficultyLevel difficultyLevel = viewModel.getCurrentFlashcardDifficultLevel();
        if (difficultyLevel == DifficultyLevel.easy) {
            highlightButton(easyButton);
        } else if (difficultyLevel == DifficultyLevel.hard) {
            highlightButton(hardButton);
        }
    }

    private void highlightButton(Button button) {
        button.getStyleClass().add("highlighted-button");
    }

    @FXML
    public void handleClose(MouseEvent mouseEvent) {
        viewModel.endStudy();//end the study when press close and go to review page
        session.setViewModel(viewModel);
        goToReviewPage();
    }


    @FXML
    public void handleNext(MouseEvent mouseEvent) {
        if (viewModel.currentIndexProperty().get() < Integer.parseInt(viewModel.totalProperty().get()) - 1) {
            viewModel.nextFlashcard();
            showFlashcard();
        } else {
            viewModel.endStudy(); //end the study when after the last card
            session.setViewModel(viewModel);
            goToReviewPage();
        }
    }

    @FXML
    public void handleBack(MouseEvent mouseEvent) {
        viewModel.previousFlashcard();
        showFlashcard();
    }

    @FXML
    public void handleEasy(ActionEvent actionEvent) {
        viewModel.updateFlashcardLevel(DifficultyLevel.easy);
        updateButtonStates();
    }

    @FXML
    public void handleHard(ActionEvent actionEvent) {
        viewModel.updateFlashcardLevel(DifficultyLevel.hard);
        updateButtonStates();
    }

    @FXML
    public void handleReset(MouseEvent mouseEvent) {
        viewModel.resetAllFlashcardLevel();
        viewModel.currentIndexProperty().set(0);
        viewModel.loadFlashcards(session.getSetId(), viewModel.setNameProperty().get());
        showFlashcard();
    }

    @FXML
    public void handleShuffle(MouseEvent mouseEvent) {
        viewModel.shuffleFlashcards(); //shuffle cards and reset the index to show the first card
        showFlashcard(); //refresh the display to show the shuffled flashcards
    }

    protected void goToReviewPage() {
        goToPage("/com/flash_card/fxml/review-flashcard.fxml", easyButton.getScene());
    }
}