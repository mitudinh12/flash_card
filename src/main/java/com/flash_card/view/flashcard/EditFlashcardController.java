package com.flash_card.view.flashcard;

import com.flash_card.framework.ViewController;
import com.flash_card.view_model.entity.EntityManagerViewModel;
import com.flash_card.view_model.flashcard.EditFlashcardViewModel;
import jakarta.persistence.EntityManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;

import java.io.IOException;
/**
 * Controller class for editing an existing flashcard in a flashcard set.
 * Handles user interactions and communicates with the ViewModel to update flashcards.
 */
public class EditFlashcardController extends ViewController {
    /**
     * The ID of the flashcard set to which the flashcard belongs.
     */
    private int flashcardSetId;
    /**
     * The ID of the flashcard being edited.
     */
    private int cardId;
    /**
     * EntityManager instance for database operations.
     */
    private final EntityManager entityManager = EntityManagerViewModel.getEntityManager();
    /**
     * ViewModel for editing flashcards.
     */
    private final EditFlashcardViewModel viewModel = new EditFlashcardViewModel(entityManager);
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
     * Loads the term and definition of the flashcard if the card ID is set.
     */
    @FXML
    public void initialize() {
        setReloadFxml("/com/flash_card/fxml/edit-flashcard.fxml");
        if (cardId != 0) {
            termField.setText(viewModel.term(cardId));
            definitionField.setText(viewModel.definition(cardId));
        }
    }
    /**
     * Handles the save action when the user clicks the save button.
     * Validates input fields and updates the flashcard in the database.
     */
    @FXML
    public void handleSave() {
        if (termField.getText().isEmpty() || definitionField.getText().isEmpty()) {
            showAlert("Warning", "Please fill in both term and definition fields");
            return;
        }
        viewModel.updateFlashcard(cardId, termField.getText(), definitionField.getText());
        goToEditManyCardsPage();
    }
    /**
     * Handles the save action when the user clicks the save button.
     * Validates input fields and updates the flashcard in the database.
     */
    @FXML
    public void handleCancel() {
        goToEditManyCardsPage();
    }

    /**
     * Handles the action when the user clicks the back button.
     * Navigates to the edit-many-cards page.
     */
    @Override
    protected void reloadCurrentView() {
        Scene scene = termField.getScene();
        Parent root = scene.getRoot();
        String fxmlFile = (String) root.getUserData();

        if (fxmlFile == null) {
            System.err.println("No FXML file path set in UserData.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            loader.setResources(localization.getBundle());
            Parent newRoot = loader.load();

            //pass cardId and flashcardSetId to the new controller
            EditFlashcardController controller = loader.getController();
            controller.setFlashcardId(cardId);
            controller.setFlashcardSetId(flashcardSetId);

            newRoot.setUserData(fxmlFile);
            scene.setRoot(newRoot);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
     * Sets the ID of the flashcard set to which the flashcard belongs.
     *
     * @param flashcardSetIdParam the ID of the flashcard set
     */
    public void setFlashcardSetId(final int flashcardSetIdParam) {
        this.flashcardSetId = flashcardSetIdParam;
    }
    /**
     * Sets the ID of the flashcard being edited.
     * Updates the term and definition fields if they are already initialized.
     *
     * @param cardIdParam the ID of the flashcard
     */
    public void setFlashcardId(final int cardIdParam) {
        this.cardId = cardIdParam;
        if (termField != null && definitionField != null) {
            termField.setText(viewModel.term(cardId));
            definitionField.setText(viewModel.definition(cardId));
        }
    }
}
