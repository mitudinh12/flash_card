package com.flash_card.view.flashcardSet;

import com.flash_card.framework.ViewController;
import com.flash_card.view.flashcard.FlashcardView;
import com.flash_card.view_model.flashcard_set.StudyFlashcardSetViewModel;
import com.flash_card.view_model.entity.EntityManagerViewModel;
import jakarta.persistence.EntityManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class StudyFlashcardSetController extends ViewController {
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

    private final EntityManager entityManager = EntityManagerViewModel.getEntityManager();
    private final StudyFlashcardSetViewModel viewModel = new StudyFlashcardSetViewModel(entityManager);
    private int setId;
    @FXML
    public void initialize() {
        setName.textProperty().bind(viewModel.setNameProperty());
        total.textProperty().bind(viewModel.totalProperty());
        index.textProperty().bind(viewModel.currentIndexProperty().add(1).asString());
    }

    public void setFlashcardSet(int setId, String setName) {
        this.setId = setId;
        viewModel.loadFlashcards(setId, setName);
        showFlashcard();
    }

    private void showFlashcard() {
        flashcardContainer.getChildren().clear();
        FlashcardView flashcardView = new FlashcardView(viewModel.getCurrentFlashcard().getTerm(), viewModel.getCurrentFlashcard().getDefinition());
        flashcardContainer.getChildren().add(flashcardView);
        updateButtonStates();
    }

    private void updateButtonStates() {
        backIcon.setVisible(viewModel.currentIndexProperty().get() != 0);
        nextIcon.setVisible(true);
    }

    @FXML
    public void handleNext(MouseEvent mouseEvent) {
        if (viewModel.currentIndexProperty().get() < Integer.parseInt(viewModel.totalProperty().get()) - 1) {
            viewModel.nextFlashcard();
            showFlashcard();
        } else {
            goToReviewPage();
        }
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

    @FXML
    public void handleBack(MouseEvent mouseEvent) {
        viewModel.previousFlashcard();
        showFlashcard();
    }

    @FXML
    public void handelEasy(ActionEvent actionEvent) {
    }

    @FXML
    public void handleHard(ActionEvent actionEvent) {
    }

    @FXML
    public void handleReset(MouseEvent mouseEvent) {
    }

    @FXML
    public void handleShuffle(MouseEvent mouseEvent) {
    }
}