package com.flash_card.view.flashcardPage;

import com.flash_card.framework.ViewController;
import com.flash_card.view_model.flashcard_set.EditFlashcardSetViewModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class EditFlashcardSetController extends ViewController {
    @FXML
    private TextField setNameField;
    @FXML
    private TextField setDescriptionField;
    @FXML
    private TextField setTopicField;

    private int setId;
    private EditFlashcardSetViewModel viewModel;

    public EditFlashcardSetController() {
        viewModel = new EditFlashcardSetViewModel();
    }

    //set the fields with the current set info
    public void setFlashcardSet(int setId, String setName, String setDescription, String setTopic) {
        this.setId = setId;
        setNameField.setText(setName);
        setDescriptionField.setText(setDescription);
        setTopicField.setText(setTopic);
    }

    @FXML
    public void handleEditCards(ActionEvent actionEvent) {
    }

    @FXML
    public void handleSaveSet(ActionEvent actionEvent) {
        String setName = setNameField.getText();
        String setDescription = setDescriptionField.getText();
        String setTopic = setTopicField.getText();
        if (setName.isEmpty() || setDescription.isEmpty() || setTopic.isEmpty()) {
            showAlert("Warning", "Please fill in all fields");
            return;
        }
        viewModel.saveFlashcardSet(setId, setName, setDescription, setTopic);//pass edited set info to view model
        goToPage("/com/flash_card/fxml/flashcard.fxml", setNameField.getScene());
    }

    @FXML
    public void handleCancel(ActionEvent actionEvent) {
        goToPage("/com/flash_card/fxml/flashcard.fxml", setNameField.getScene());
    }

    @FXML
    public void handleDeleteSet(ActionEvent actionEvent) {
        viewModel.deleteFlashcardSet(setId);
        goToPage("/com/flash_card/fxml/flashcard.fxml", setNameField.getScene());
    }
}