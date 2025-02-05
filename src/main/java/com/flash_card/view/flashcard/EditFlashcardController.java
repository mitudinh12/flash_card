package com.flash_card.view.flashcard;

import com.flash_card.framework.ViewController;
import com.flash_card.model.entity.Flashcard;
import com.flash_card.view_model.flashcard.CreateFlashcardViewModel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

import java.io.IOException;

public class EditFlashcardController extends ViewController {
    @FXML
    private TextField termField;
    @FXML
    private TextField definitionField;

    private final CreateFlashcardViewModel viewModel = new CreateFlashcardViewModel();
    private int flashcardSetId;
    private Flashcard card;

    @FXML
    public void initialize() {
        if (card != null) {
            termField.setText(card.getTerm());
            definitionField.setText(card.getDefinition());
        }
    }

    @FXML
    public void handleSave() {
        if (termField.getText().isEmpty() || definitionField.getText().isEmpty()) {
            showAlert("Warning", "Please fill in both term and definition fields");
            return;
        }
        viewModel.updateFlashcard(card, termField.getText(), definitionField.getText());
        goToEditManyCardsPage();
    }

    @FXML
    public void handleCancel() {
        goToEditManyCardsPage();
    }

    private void goToEditManyCardsPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/flash_card/fxml/edit-many-cards.fxml"));
            Parent root = loader.load();
            EditManyCardsController controller = loader.getController();
            controller.setFlashcardSetId(flashcardSetId);
            Scene scene = termField.getScene();
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setFlashcardSetId(int flashcardSetId) {
        this.flashcardSetId = flashcardSetId;
    }

    public void setFlashcard(Flashcard card) {
        this.card = card;
        if (termField != null && definitionField != null) {
            termField.setText(card.getTerm());
            definitionField.setText(card.getDefinition());
        }
    }
}