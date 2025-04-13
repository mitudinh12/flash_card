package com.flash_card.view.flashcardSet;

import com.flash_card.framework.ViewController;
import com.flash_card.view.flashcard.EditManyCardsController;
import com.flash_card.view_model.entity.EntityManagerViewModel;
import com.flash_card.view_model.flashcard_set.EditFlashcardSetViewModel;
import jakarta.persistence.EntityManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;

import java.io.IOException;

/**
 * Controller class for editing an existing flashcard set.
 */
public class EditFlashcardSetController extends ViewController {

    /** The entity manager for database operations. */
    private final EntityManager entityManager = EntityManagerViewModel.getEntityManager();

    /** The view model for editing flashcard sets. */
    private final EditFlashcardSetViewModel viewModel = new EditFlashcardSetViewModel(entityManager);

    /** The ID of the flashcard set being edited. */
    private int setId;

    /** The text field for entering the name of the flashcard set. */
    @FXML
    private TextField setNameField;

    /** The text field for entering the description of the flashcard set. */
    @FXML
    private TextField setDescriptionField;

    /** The text field for entering the topic of the flashcard set. */
    @FXML
    private TextField setTopicField;

    /**
     * Handles the action to edit the flashcards in the set.
     *
     * @param actionEvent the event triggered by the user
     */
    @FXML
    public void handleEditCards(final ActionEvent actionEvent) {
        goToEditManyCardsPage(setId, setNameField.getScene());
    }

    /**
     * Handles the action to save the edited flashcard set.
     *
     * @param actionEvent the event triggered by the user
     */
    @FXML
    public void handleSaveSet(final ActionEvent actionEvent) {
        String setName = setNameField.getText();
        String setDescription = setDescriptionField.getText();
        String setTopic = setTopicField.getText();
        if (setName.isEmpty() || setDescription.isEmpty() || setTopic.isEmpty()) {
            showAlert(
                    localization.getMessage("flashcardSet.warningTitle"),
                    localization.getMessage("flashcardSet.warningMessage")
            );
            return;
        }
        viewModel.saveFlashcardSet(setId, setName, setDescription, setTopic); //pass edited set info to view model
        goToPage("/com/flash_card/fxml/home.fxml", setNameField.getScene());
    }

    /**
     * Handles the action to cancel the editing process and return to the home page.
     *
     * @param actionEvent the event triggered by the user
     */
    @FXML
    public void handleCancel(final ActionEvent actionEvent) {
        goToPage("/com/flash_card/fxml/home.fxml", setNameField.getScene());
    }

    /**
     * Navigates to the page for editing multiple flashcards in the set.
     *
     * @param setIdParam the ID of the flashcard set
     * @param currentScene the current scene
     */
    public void goToEditManyCardsPage(final int setIdParam, final Scene currentScene) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/flash_card/fxml/edit-many-cards.fxml"));
            loader.setResources(localization.getBundle());
            Parent root = loader.load();
            EditManyCardsController controller = loader.getController();
            controller.setFlashcardSetId(setIdParam);
            currentScene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets the fields with the current flashcard set information.
     *
     * @param setIdParam the ID of the flashcard set
     * @param setName the name of the flashcard set
     * @param setDescription the description of the flashcard set
     * @param setTopic the topic of the flashcard set
     */
    public void setFlashcardSet(
            final int setIdParam,
            final String setName,
            final String setDescription,
            final String setTopic
    ) {
        this.setId = setIdParam;
        setNameField.setText(setName);
        setDescriptionField.setText(setDescription);
        setTopicField.setText(setTopic);
    }
}
