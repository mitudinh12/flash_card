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
 * Controller class for creating flashcards in a flashcard set.
 * Handles user interactions and communicates with the ViewModel to manage flashcards.
 */
public class CreateFlashcardController extends ViewController {
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
     * Initializes the controller and sets the FXML reload path for app language consistency.
     */
    @FXML
    private void initialize() {
        setReloadFxml("/com/flash_card/fxml/create-flashcard.fxml");
    }
    /**
     * Handles the action to create a new flashcard.
     * Validates input fields and saves the flashcard to the database.
     * Navigates to the create-flashcard page for adding another flashcard.
     */
    @FXML
    public void handleCreateFlashcard() {
        if (termField.getText().isEmpty() || definitionField.getText().isEmpty()) {
            showAlert("Warning", "Please fill in both term and definition fields");
            return;
        }
        viewModel.saveFlashcard(termField.getText(), definitionField.getText(), flashcardSetId);
        System.out.println("Flashcard saved. Create new one");
        goToCreateFlashcardPage();
    }
    /**
     * Handles the save action when the user clicks the save button.
     * Validates input fields, saves the flashcard, and navigates back to the home page.
     */
    @FXML
    public void handleSave() {
        if (termField.getText().isEmpty() || definitionField.getText().isEmpty()) {
            showAlert("Warning", "Please fill in both term and definition fields");
            return;
        }
        viewModel.saveFlashcard(termField.getText(), definitionField.getText(), flashcardSetId);
        System.out.println("Flashcard saved. Back to Flashcard Page");
        clearFlashcardSetId();
        goToPage("/com/flash_card/fxml/home.fxml", termField.getScene());
    }
    /**
     * Handles the cancel action when the user clicks the cancel button.
     * Deletes the flashcard set if it is empty and navigates back to the home page.
     */
    @FXML
    public void handleCancel() {
        viewModel.deleteFlashcardSetIfEmpty(flashcardSetId); //delete flashcard set if there's no flashcard in it
        System.out.println("Cancel");
        clearFlashcardSetId();
        goToPage("/com/flash_card/fxml/home.fxml", termField.getScene());
    }
    /**
     * Navigates to the create-flashcard page for adding another flashcard.
     */
    private void goToCreateFlashcardPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/flash_card/fxml/create-flashcard.fxml"));
            loader.setResources(localization.getBundle());
            Parent root = loader.load();
            CreateFlashcardController controller = loader.getController();
            controller.setFlashcardSetId(flashcardSetId); // pass the flashcardSetId to the next flashcard
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
    public void setFlashcardSetId(int flashcardSetIdParam) {
        CreateFlashcardController.flashcardSetId = flashcardSetIdParam;
    }
    /**
     * Clears the flashcard set ID when the user saves or cancels.
     */
    private void clearFlashcardSetId() {
        CreateFlashcardController.flashcardSetId = 0; //clear the flashcardSetId when clicked save or cancel
    }
}
