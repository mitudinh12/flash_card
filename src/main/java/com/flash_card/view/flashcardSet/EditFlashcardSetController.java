package com.flash_card.view.flashcardSet;

import com.flash_card.framework.ViewController;
import com.flash_card.view.flashcard.EditManyCardsController;
import com.flash_card.view_model.flashcard_set.EditFlashcardSetViewModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;

import java.io.IOException;

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
        goToEditManyCardsPage(setId);
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

    private void goToEditManyCardsPage(int setId) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/flash_card/fxml/edit-many-cards.fxml"));
            Parent root = loader.load();
            EditManyCardsController controller = loader.getController();
            controller.setFlashcardSetId(setId);
            Scene scene = setNameField.getScene();
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}