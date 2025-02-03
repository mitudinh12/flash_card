package com.flash_card.view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import com.flash_card.view_model.CreateFlashcardSetViewModel;
import org.w3c.dom.events.MouseEvent;

import java.io.IOException;

public class CreateFlashcardSetView {
    private Stage stage = HomeView.getStage();
    private CreateFlashcardSetViewModel viewModel;
    // FXML UI components
    @FXML
    private TextField setNameField, setDescriptionField, setTopicField;
    @FXML
    private Button createSetButton, cancelButton;

//    /*Guide to the code:
//    * 1. Add method to handle createSetButton click event
//    * 2. Initialize TextFields and Buttons
//    * 3. Bind the TextFields to the ViewModel
//    * 4. Set event when clicking the createSetButton to a viewModel method?
//    * 5. Change scene?
//    * 6. Create stage?
//    * */

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
            viewModel.addSet(name, description, topic);
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/flash_card/fxml/create_flashcard-in-set.fxml"));
                Parent homeRoot = loader.load();
                Scene scene = new Scene(homeRoot);
                stage.setScene(scene);
            } catch (IOException e) {
                e.printStackTrace();
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

    //Alert messages, for empty fields
    public void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }



}
