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
/**
 * Controller class for adding a new flashcard to a flashcard set.
 * Handles user interactions and extended from the ViewModel to save flashcards.
 */
public class AddFlashcardController extends ViewController {
    /**
     * The ID of the flashcard set to which the flashcard will be added.
     */
    private static int flashcardSetId;
    /**
     * ViewModel for managing user authentication sessions.
     */
    private final AuthSessionViewModel authSessionViewModel = AuthSessionViewModel.getInstance();
    /**
     * EntityManager instance for database operations.
     */
    private final EntityManager entityManager = EntityManagerViewModel.getEntityManager();
    /**
     * ViewModel for creating flashcards.
     */
    private final CreateFlashcardViewModel viewModel = new CreateFlashcardViewModel(
            authSessionViewModel.getVerifiedUserInfo().get("userId"), entityManager);
    /**
     * TextField for entering the term of the flashcard.
     */
    @FXML
    private TextField termField;
    /**
     * TextField for entering the definition of the flashcard.
     */
    @FXML
    private TextField definitionField;
    /**
     * Initializes the controller and sets the FXML reload path.
     */
    @FXML
    private void initialize() {
        setReloadFxml("/com/flash_card/fxml/add-flashcard.fxml");
    }
    /**
     * Handles the save action when the user clicks the save button.
     * Validates input fields and saves the flashcard to the database.
     */
    @FXML
    public void handleSave() {
        if (termField.getText().isEmpty() || definitionField.getText().isEmpty()) {
            showAlert("Warning", "Please fill in both term and definition fields");
            return;
        }
        //save flashcard and increase number of flashcards in flashcard set
        viewModel.saveFlashcard(termField.getText(), definitionField.getText(), flashcardSetId);
        goToEditManyCardsPage();
    }
    /**
     * Handles the cancel action when the user clicks the cancel button.
     * Navigates back to the edit-many-cards page.
     */
    @FXML
    public void handleCancel() {
        goToEditManyCardsPage();
    }
    /**
     * Navigates to the edit-many-cards page.
     */
    private void goToEditManyCardsPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/flash_card/fxml/edit-many-cards.fxml"));
            loader.setResources(localization.getBundle());
            Parent root = loader.load();
            EditManyCardsController controller = loader.getController();
            controller.setFlashcardSetId(flashcardSetId);
            Scene scene = termField.getScene();
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Sets the ID of the flashcard set to which the flashcard will be added.
     *
     * @param flashcardSetIdParam the ID of the flashcard set
     */
    public void setFlashcardSetId(final int flashcardSetIdParam) {
        AddFlashcardController.flashcardSetId = flashcardSetIdParam;
    }
}
