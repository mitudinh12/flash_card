package com.flash_card.view.flashcard;

import com.flash_card.framework.ViewController;
import com.flash_card.view_model.flashcard.CreateFlashcardViewModel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;

import java.io.IOException;

public class CreateFlashcardController extends ViewController {
    @FXML
    private TextField termField;
    @FXML
    private TextField definitionField;

    private final CreateFlashcardViewModel viewModel = new CreateFlashcardViewModel();
    private int flashcardSetId;

    @FXML
    public void handleCreateFlashcard() {
        if (termField.getText().isEmpty() || definitionField.getText().isEmpty()) {
            showAlert("Warning", "Please fill in both term and definition fields");
            return;
        }
        viewModel.saveFlashcard(termField.getText(), definitionField.getText(),flashcardSetId);
        System.out.println("Flashcard saved. Create new one");
        goToCreateFlashcardPage();
    }

    @FXML
    public void handleSave() {
        if (termField.getText().isEmpty() || definitionField.getText().isEmpty()) {
            showAlert("Warning", "Please fill in both term and definition fields");
            return;
        }
        viewModel.saveFlashcard(termField.getText(), definitionField.getText(),flashcardSetId);
        System.out.println("Flashcard saved. Back to Flashcard Page");
        clearFlashcardSetId();
        goToPage("/com/flash_card/fxml/flashcard.fxml", termField.getScene());
    }

    @FXML
    public void handleCancel() {
        viewModel.deleteFlashcardSetIfEmpty(flashcardSetId); //delete flashcard set if there's no flashcard in it
        System.out.println("Cancel");
        clearFlashcardSetId();
        goToPage("/com/flash_card/fxml/home.fxml", termField.getScene());
    }

    private void goToCreateFlashcardPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/flash_card/fxml/create-flashcard.fxml"));
            Parent root = loader.load();
            CreateFlashcardController controller = loader.getController();
            controller.setFlashcardSetId(flashcardSetId); // pass the flashcardSetId to the next flashcard
            Scene scene = termField.getScene();
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setFlashcardSetId(int flashcardSetId) {
        this.flashcardSetId = flashcardSetId;
    }

    private void clearFlashcardSetId() {
        this.flashcardSetId = 0; //clear the flashcardSetId when clicked save or cancel
    }
}