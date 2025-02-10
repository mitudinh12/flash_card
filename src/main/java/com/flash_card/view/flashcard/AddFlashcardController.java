package com.flash_card.view.flashcard;

import com.flash_card.framework.ViewController;
import com.flash_card.view_model.entity.EntityManagerViewModel;
import com.flash_card.view_model.flashcard.CreateFlashcardViewModel;
import com.flash_card.view_model.user_auth.AuthSessionViewModel;
import jakarta.persistence.EntityManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;

import java.io.IOException;

public class AddFlashcardController extends ViewController {
    private final AuthSessionViewModel authSessionViewModel = AuthSessionViewModel.getInstance();
    private EntityManagerViewModel entityManagerViewModel = new EntityManagerViewModel();
    private EntityManager entityManager = entityManagerViewModel.getEntityManager();
    private final CreateFlashcardViewModel viewModel = new CreateFlashcardViewModel(authSessionViewModel.getVerifiedUserInfo().get("userId"), entityManager);
    private int flashcardSetId;

    @FXML
    private TextField termField;
    @FXML
    private TextField definitionField;

    @FXML
    public void handleSave() {
        if (termField.getText().isEmpty() || definitionField.getText().isEmpty()) {
            showAlert("Warning", "Please fill in both term and definition fields");
            return;
        }
        viewModel.saveFlashcard(termField.getText(), definitionField.getText(),flashcardSetId); //save flashcard and increase number of flashcards in flashcard set
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
}