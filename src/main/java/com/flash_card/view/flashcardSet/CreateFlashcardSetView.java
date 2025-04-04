package com.flash_card.view.flashcardSet;

import com.flash_card.framework.ViewController;
import com.flash_card.localization.Localization;
import com.flash_card.model.dao.UserDao;
import com.flash_card.model.datasource.MariaDbJpaConnection;
import com.flash_card.model.entity.User;
import com.flash_card.view.auth.LoginView;
import com.flash_card.view.flashcard.CreateFlashcardController;
import com.flash_card.view_model.entity.EntityManagerViewModel;
import com.flash_card.view_model.user_auth.AuthSessionViewModel;
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

public class CreateFlashcardSetView extends ViewController {
    private Stage stage = LoginView.getStage();
    private EntityManager entityManager = EntityManagerViewModel.getEntityManager();
    private CreateFlashcardSetViewModel viewModel = new CreateFlashcardSetViewModel(entityManager);
    private AuthSessionViewModel authSessionViewModel = AuthSessionViewModel.getInstance();
    private Localization localization = Localization.getInstance();
    private String userId;

    // FXML UI components
    @FXML
    private TextField setNameField, setDescriptionField, setTopicField;
    @FXML
    private Button createSetButton, cancelButton;
    @FXML
    public ComboBox<String> languageDropdown;

    @FXML
    private void initialize() {
        setReloadFxml("/com/flash_card/fxml/create-flashcard-set.fxml");
        createSetButton.setOnAction(event -> handleCreateSet());
        cancelButton.setOnAction(event -> handleCancel());

        languageDropdown.getItems().addAll("English", "Suomi", "ภาษาไทย", "한국인", "Tiếng Việt");
        languageDropdown.setValue(localization.getMessage("language"));
    }
    @FXML
    private void handleCreateSet() {
        String name = setNameField.getText();
        String languageDisplayName = languageDropdown.getValue();
        String description = setDescriptionField.getText();
        String topic = setTopicField.getText();
        userId = authSessionViewModel.getVerifiedUserInfo().get("userId");
        if (!name.isEmpty() || !description.isEmpty() || !topic.isEmpty()) {
            String language = Language.fromDisplayName(languageDisplayName).name();
            int flashcardSetId = viewModel.addSet(name, language, description, topic, userId);
            if (flashcardSetId != -1) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/flash_card/fxml/create-flashcard.fxml"));
                    loader.setResources(localization.getBundle());
                    Parent homeRoot = loader.load();
                    CreateFlashcardController controller = loader.getController(); //load the controller of CreateFlashcard
                    controller.setFlashcardSetId(flashcardSetId); //pass setId to the controller
                    Scene scene = new Scene(homeRoot);
                    stage.setScene(scene);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        else {
            showAlert(localization.getMessage("flashcardSet.warningTitle"), localization.getMessage("flashcardSet.warningMessage"));
        }
    }

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