package com.flash_card.view;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class CreateFlashcardSetView {
    // FXML UI components
    @FXML
    private TextField setNameField, setDescriptionField, setTopicField;
    @FXML
    private Button createSetButton, cancelButton;

    /*Guide to the code:
    * 1. Add method to handle createSetButton click event
    * 2. Initialize TextFields and Buttons
    * 3. Bind the TextFields to the ViewModel
    * 4. Set event when clicking the createSetButton to a viewModel method?
    * 5. Change scene?
    * 6. Create stage?
    * */

    //Alert messages, for empty fields
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
