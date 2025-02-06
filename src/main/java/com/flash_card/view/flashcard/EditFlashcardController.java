package com.flash_card.view.flashcard;

import com.flash_card.framework.ViewController;
import com.flash_card.view_model.flashcard.EditFlashcardViewModel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import java.io.IOException;

public class EditFlashcardController extends ViewController {
    @FXML
    private TextField termField;
    @FXML
    private TextField definitionField;

    private final EditFlashcardViewModel viewModel = new EditFlashcardViewModel();
    private int flashcardSetId;
    private int cardId = 0;

    @FXML
    public void initialize() {
        if (cardId != 0) {
            termField.setText(viewModel.term(cardId));
            definitionField.setText(viewModel.definition(cardId));
        }
    }

    @FXML
    public void handleSave() {
        if (termField.getText().isEmpty() || definitionField.getText().isEmpty()) {
            showAlert("Warning", "Please fill in both term and definition fields");
            return;
        }
        viewModel.updateFlashcard(cardId, termField.getText(), definitionField.getText());
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

    public void setFlashcardId(int cardId) {
        this.cardId = cardId;
        if (termField != null && definitionField != null) {
            termField.setText(viewModel.term(cardId));
            definitionField.setText(viewModel.definition(cardId));
        }
    }
}