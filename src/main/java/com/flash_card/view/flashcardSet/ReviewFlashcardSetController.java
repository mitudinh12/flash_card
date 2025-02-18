package com.flash_card.view.flashcardSet;

import com.flash_card.framework.ViewController;
import javafx.fxml.FXML;

import javafx.scene.control.Label;

public class ReviewFlashcardSetController extends ViewController {
    @FXML
    public Label setNameLabel;
    private int setId;
    private String setName;

    public void setFlashcardSet(int setId, String setName) {
        this.setId = setId;
        this.setName = setName;
        setNameLabel.setText(setName);
    }
}