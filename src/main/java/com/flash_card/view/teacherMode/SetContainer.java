package com.flash_card.view.teacherMode;

import com.flash_card.framework.SetViewModel;
import com.flash_card.view.flashcardSet.EditFlashcardSetController;
import com.flash_card.view.homepage.HomePageController;
import com.flash_card.view_model.flashcard_set.AssignedFlashcardSetViewModel;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public class SetContainer extends HBox {
    private AssignedFlashcardSetViewModel viewModel;
    private Label nameLabel;
    private EditFlashcardSetController editSetController = new EditFlashcardSetController();


    public SetContainer(AssignedFlashcardSetViewModel viewModel) {
        this.viewModel = viewModel;
        initializeUI();
    }

    private void initializeUI() {

        nameLabel = new Label();
        nameLabel.setId("name-label");
        nameLabel.textProperty().bind(viewModel.setNameProperty());

        Label topicLabel = new Label();
        topicLabel.setId("topic-label");
        topicLabel.textProperty().bind(viewModel.setTopicProperty());

        Label numberFlashcard = new Label();
        numberFlashcard.setId("number-flashcard");
        numberFlashcard.textProperty().bind(viewModel.setNumberFlashcard());

        Button actionButton = new Button("Delete");
        actionButton.setId("action-button");

        HBox.setHgrow(nameLabel, Priority.ALWAYS);
        this.getChildren().addAll(nameLabel, topicLabel, numberFlashcard, actionButton);
        this.setId("flashcard-set-container");
        this.setAlignment(Pos.CENTER_LEFT);
    }
}
