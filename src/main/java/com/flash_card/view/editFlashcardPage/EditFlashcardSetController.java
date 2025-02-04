package com.flash_card.view.editFlashcardPage;

import com.flash_card.framework.ViewController;
import com.flash_card.model.entity.FlashcardSet;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class EditFlashcardSetController extends ViewController {

    @FXML
    private TextField setNameField;
    @FXML
    private TextField setDescriptionField;
    @FXML
    private TextField setTopicField;

    public void setFlashcardSet(FlashcardSet set) {
    }

    @FXML
    private void handleSave() {
    }
}