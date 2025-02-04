package com.flash_card.view.create_flashcard_set;

import com.flash_card.framework.ViewController;
import com.flash_card.view.auth.LoginView;
import com.flash_card.view.createFlashcardPage.CreateFlashcardController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import com.flash_card.view_model.flashcard_set.CreateFlashcardSetViewModel;

import java.io.IOException;

public class CreateFlashcardSetView extends ViewController {
    private Stage stage = LoginView.getStage();
    private CreateFlashcardSetViewModel viewModel;
    // FXML UI components
    @FXML
    private TextField setNameField, setDescriptionField, setTopicField;
    @FXML
    private Button createSetButton, cancelButton;

    public CreateFlashcardSetView() {
        this.viewModel = new CreateFlashcardSetViewModel(this);
    }

    @FXML
    private void initialize() {
        createSetButton.setOnAction(event -> handleCreateSet());
        cancelButton.setOnAction(event -> handleCancel());
    }
    @FXML
    private void handleCreateSet() {
        String name = setNameField.getText();
        String description = setDescriptionField.getText();
        String topic = setTopicField.getText();
        if (!name.isEmpty() || !description.isEmpty() || !topic.isEmpty()) {
            int flashcardSetId = viewModel.addSet(name, description, topic);
            if (flashcardSetId != -1) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/flash_card/fxml/create-flashcard.fxml"));
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
            showAlert("Warning", "Please fill in all fields");
        }
    }

    @FXML
    private void handleCancel() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/flash_card/fxml/home.fxml"));
            Parent homeRoot = loader.load();
            Scene scene = new Scene(homeRoot);
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
