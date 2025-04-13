package com.flash_card.view.flashcardSet;

import com.flash_card.framework.ViewController;
import com.flash_card.view.auth.LoginView;
import com.flash_card.view.flashcard.CreateFlashcardController;
import com.flash_card.view_model.entity.EntityManagerViewModel;
import jakarta.persistence.EntityManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import com.flash_card.view_model.flashcard_set.CreateFlashcardSetViewModel;
import com.flash_card.framework.Language;

import java.io.IOException;

/**
 * Controller class for creating a new flashcard set.
 */
public class CreateFlashcardSetView extends ViewController {
    /** The primary stage for the application. */
    private final Stage stage = LoginView.getStage();

    /** The entity manager for database operations. */
    private final EntityManager entityManager = EntityManagerViewModel.getEntityManager();

    /** The view model for creating flashcard sets. */
    private final CreateFlashcardSetViewModel viewModel = new CreateFlashcardSetViewModel(entityManager);

    /** The ID of the currently authenticated user. */
    private String userId;

    // FXML UI components
    /** The text field for entering the name of the flashcard set. */
    @FXML
    private TextField setNameField;

    /** The text field for entering the description of the flashcard set. */
    @FXML
    private TextField setDescriptionField;

    /** The text field for entering the topic of the flashcard set. */
    @FXML
    private TextField setTopicField;

    /** The button for creating the flashcard set. */
    @FXML
    private Button createSetButton;

    /** The button for canceling the operation. */
    @FXML
    private Button cancelButton;

    /** The dropdown for selecting the language of the flashcard set. */
    @FXML
    private ComboBox<String> languageDropdown;

    /**
     * Initializes the view and sets up event handlers.
     */
    @FXML
    private void initialize() {
        setupUIComponents();
        populateLanguageDropdown();
    }

    /**
     * Sets up the UI components and their event handlers.
     */
    private void setupUIComponents() {
        setReloadFxml("/com/flash_card/fxml/create-flashcard-set.fxml");
        createSetButton.setOnAction(event -> handleCreateSet());
        cancelButton.setOnAction(event -> handleCancel());
    }

    /**
     * Populates the language dropdown with available language options.
     */
    private void populateLanguageDropdown() {
        languageDropdown.getItems().addAll("English", "Suomi", "ภาษาไทย", "한국인", "Tiếng Việt");
        languageDropdown.setValue(localization.getMessage("language"));
    }

    /**
     * Handles the creation of a new flashcard set.
     */
    @FXML
    private void handleCreateSet() {
        String name = setNameField.getText();
        String languageDisplayName = languageDropdown.getValue();
        String description = setDescriptionField.getText();
        String topic = setTopicField.getText();
        userId = authSessionViewModel.getVerifiedUserInfo().get("userId");

        if (isInputValid(name, description, topic)) {
            createFlashcardSet(name, languageDisplayName, description, topic);
        } else {
            showAlert(
                    localization.getMessage("flashcardSet.warningTitle"),
                    localization.getMessage("flashcardSet.warningMessage")
            );
        }
    }

    /**
     * Validates the user input fields.
     *
     * @param name        the name of the flashcard set
     * @param description the description of the flashcard set
     * @param topic       the topic of the flashcard set
     * @return true if all inputs are valid, false otherwise
     */
    private boolean isInputValid(final String name, final String description, final String topic) {
        return !name.isEmpty() || !description.isEmpty() || !topic.isEmpty();
    }

    /**
     * Creates a new flashcard set and navigates to the flashcard creation view.
     *
     * @param name              the name of the flashcard set
     * @param languageDisplayName the display name of the selected language
     * @param description       the description of the flashcard set
     * @param topic             the topic of the flashcard set
     */
    private void createFlashcardSet(
            final String name,
            final String languageDisplayName,
            final String description,
            final String topic
    ) {
        try {
            String language = Language.fromDisplayName(languageDisplayName).name();
            int flashcardSetId = viewModel.addSet(name, language, description, topic, userId);

            if (flashcardSetId != -1) {
                navigateToFlashcardCreation(flashcardSetId);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Navigates to the flashcard creation view.
     *
     * @param flashcardSetId the ID of the created flashcard set
     * @throws IOException if the FXML file cannot be loaded
     */
    private void navigateToFlashcardCreation(final int flashcardSetId) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/flash_card/fxml/create-flashcard.fxml"));
        loader.setResources(localization.getBundle());
        Parent homeRoot = loader.load();

        CreateFlashcardController controller = loader.getController();
        controller.setFlashcardSetId(flashcardSetId);

        Scene scene = new Scene(homeRoot);
        stage.setScene(scene);
    }

    /**
     * Handles the cancel action and navigates back to the home view.
     */
    @FXML
    private void handleCancel() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/flash_card/fxml/home.fxml"));
            loader.setResources(localization.getBundle());
            Parent homeRoot = loader.load();

            Scene scene = new Scene(homeRoot);
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
