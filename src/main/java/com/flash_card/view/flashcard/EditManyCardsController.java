package com.flash_card.view.flashcard;

import com.flash_card.framework.ViewController;
import com.flash_card.view_model.entity.EntityManagerViewModel;
import com.flash_card.view_model.flashcard.EditFlashcardViewModel;
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
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.List;

/**
 * Controller class for managing and editing multiple flashcards in a flashcard set.
 * Provides functionality to navigate, edit, delete, and add flashcards.
 */
public class EditManyCardsController extends ViewController {
    /**
     * The maximum width for the term label in the flashcard display.
     */
    private static final int TERM_LABEL_MAX_WIDTH = 80;

    /**
     * The minimum width for the term label in the flashcard display.
     */
    private static final int TERM_LABEL_MIN_WIDTH = 80;

    /**
     * The maximum width for the definition label in the flashcard display.
     */
    private static final int DEFINITION_LABEL_MAX_WIDTH = 200;

    /**
     * The minimum width for the definition label in the flashcard display.
     */
    private static final int DEFINITION_LABEL_MIN_WIDTH = 200;
    /**
     * The ID of the flashcard set being managed.
     */
    private static int flashcardSetId;
    /**
     * The number of flashcards displayed per page.
     */
    private final int pageSize = 8;
    /**
     * EntityManager instance for database operations.
     */
    private final EntityManager entityManager = EntityManagerViewModel.getEntityManager();
    /**
     * ViewModel for managing flashcards in the flashcard set.
     */
    private final EditFlashcardViewModel viewModel = new EditFlashcardViewModel(entityManager);
    /**
     * Label for displaying the name of the flashcard set.
     */
    @FXML
    private Label setName;
    /**
     * Container for displaying flashcards in the current page.
     */
    @FXML
    private VBox flashcardsContainer;
    /**
     * The current page index for pagination.
     */
    private int currentPage = 0;
    /**
     * Icon for navigating to the previous page.
     */
    @FXML
    private ImageView backIcon;
    /**
     * Icon for navigating to the next page.
     */
    @FXML
    private ImageView nextIcon;
    /**
     * Button for navigating back to the homepage.
     */
    @FXML
    private Button backToHomepage;


    /**
     * Initializes the controller and loads the flashcard set name and flashcards.
     */
    @FXML
    private void initialize() {
        setReloadFxml("/com/flash_card/fxml/edit-many-cards.fxml");
        loadFlashcardSetName();
        loadFlashcards();
    }
    /**
     * Sets the ID of the flashcard set being managed and reloads the flashcards.
     *
     * @param setId the ID of the flashcard set
     */
    public void setFlashcardSetId(int setId) {
        EditManyCardsController.flashcardSetId = setId; //retrieve the setId from the previous page
        loadFlashcardSetName();
        loadFlashcards();
    }

    /**
     * Loads and displays the name of the flashcard set.
     */
    private void loadFlashcardSetName() {
        String name = viewModel.getSetName(flashcardSetId);
        if (name != null) {
            setName.setText(name);
        }
    }

    /**
     * Loads and displays the flashcards in the current page.
     */
    private void loadFlashcards() {
        List<Integer> flashcardIds = viewModel.getFlashcardIdsBySetId(flashcardSetId);
        flashcardsContainer.getChildren().clear();
        int start = currentPage * pageSize;
        int end = Math.min(start + pageSize, flashcardIds.size());
        for (int i = start; i < end; i++) {
            int flashcardId = flashcardIds.get(i);
            HBox flashcardBox = new HBox();
            flashcardBox.getStyleClass().add("hbox-flashcard");
            flashcardBox.setAlignment(Pos.CENTER_LEFT);

            Button editButton = new Button(localization.getMessage("flashcard.edit"));
            editButton.getStyleClass().add("edit-action-button");
            Button deleteButton = new Button(localization.getMessage("delete"));
            deleteButton.getStyleClass().add("delete-action-button");

            editButton.setOnAction(event -> handleEditFlashcard(flashcardId));
            deleteButton.setOnAction(event -> handleDeleteFlashcard(flashcardId));

            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);

            Label termLabel = new Label(viewModel.term(flashcardId));
            termLabel.getStyleClass().add("term-label");
            termLabel.setMaxWidth(TERM_LABEL_MAX_WIDTH);
            termLabel.setMinWidth(TERM_LABEL_MIN_WIDTH);

            Label definitionLabel = new Label(viewModel.definition(flashcardId));
            definitionLabel.getStyleClass().add("definition-label");
            definitionLabel.setMaxWidth(DEFINITION_LABEL_MAX_WIDTH);
            definitionLabel.setMinWidth(DEFINITION_LABEL_MIN_WIDTH);

            flashcardBox.getChildren().addAll(termLabel, definitionLabel, spacer, editButton, deleteButton);
            flashcardsContainer.getChildren().add(flashcardBox);
        }
        updateNavigationIcons(flashcardIds.size());
    }
    /**
     * Updates the visibility of navigation icons based on the current page and total flashcards.
     *
     * @param totalFlashcards the total number of flashcards in the set
     */
    private void updateNavigationIcons(int totalFlashcards) {
        backIcon.setVisible(currentPage > 0);
        nextIcon.setVisible((currentPage + 1) * pageSize < totalFlashcards);
    }
    /**
     * Navigates to the next page of flashcards.
     *
     * @param event the action event triggered by the user
     */
    @FXML
    private void goNext(ActionEvent event) {
        if ((currentPage + 1) * pageSize < viewModel.getFlashcardIdsBySetId(flashcardSetId).size()) {
            currentPage++;
            loadFlashcards();
        }
    }
    /**
     * Navigates to the previous page of flashcards.
     *
     * @param event the action event triggered by the user
     */
    @FXML
    private void goBack(ActionEvent event) {
        if (currentPage > 0) {
            currentPage--;
            loadFlashcards();
        }
    }
    /**
     * Handles the action to add a new flashcard to the set.
     */
    @FXML
    private void handleAddCard() {
        goToAddFlashcardPage();
    }
    /**
     * Handles the action to edit an existing flashcard.
     *
     * @param flashcardId the ID of the flashcard to edit
     */
    @FXML
    private void handleEditFlashcard(int flashcardId) {
        goToEditFlashcardPage(flashcardId);
    }

    /**
     * Handles the action to delete a flashcard from the set.
     *
     * @param flashcardId the ID of the flashcard to delete
     */
    @FXML
    private void handleDeleteFlashcard(int flashcardId) {
        if (viewModel.isLastFlashcard(flashcardSetId)) {
            showAlert(localization.getMessage("flashcard.warning"), localization.getMessage(
                    "flashcard.lastCardDeleteWarning"));
        } else {
            //delete the flashcard and decrease the number of flashcards in the set
            viewModel.deleteFlashcard(flashcardId, flashcardSetId);
        }
        loadFlashcards(); //reload the flashcards
    }
    /**
     * Navigates to the add-flashcard page for adding a new flashcard.
     */
    private void goToAddFlashcardPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/com/flash_card/fxml/add-flashcard.fxml"));
            loader.setResources(localization.getBundle());
            Parent root = loader.load();
            AddFlashcardController controller = loader.getController();
            controller.setFlashcardSetId(flashcardSetId); // pass the flashcardSetId to the next flashcard
            Scene scene = setName.getScene();
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Navigates to the edit-flashcard page for editing an existing flashcard.
     *
     * @param flashcardId the ID of the flashcard to edit
     */
    private void goToEditFlashcardPage(int flashcardId) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/com/flash_card/fxml/edit-flashcard.fxml"));
            loader.setResources(localization.getBundle());
            Parent root = loader.load();
            EditFlashcardController controller = loader.getController();
            controller.setFlashcardSetId(flashcardSetId);
            controller.setFlashcardId(flashcardId); //pass the cardId and setId to the controller
            Scene scene = setName.getScene();
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

