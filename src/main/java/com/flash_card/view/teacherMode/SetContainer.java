package com.flash_card.view.teacherMode;

import com.flash_card.view_model.flashcard_set.AssignedFlashcardSetViewModel;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public class SetContainer extends HBox {
    private AssignedFlashcardSetViewModel viewModel;
    private Label nameLabel;
    private ClassDetailController controller;


    public SetContainer(AssignedFlashcardSetViewModel viewModel, ClassDetailController controller) {
        this.viewModel = viewModel;
        this.controller = controller;
        initializeUI();
    }

    private void initializeUI() {

        nameLabel = new Label();
        nameLabel.setId("name-label");
        nameLabel.textProperty().bind(viewModel.setNameProperty());

        Label topicLabel = new Label();
        topicLabel.setId("topic-label");
        topicLabel.textProperty().bind(viewModel.setTopicProperty());

        HBox numberFlashcardContainer = new HBox();
        Label numberFlashcard = new Label();
        Label term1 = new Label(" term");
        Label term2 = new Label(" terms");
        numberFlashcardContainer.setId("number-flashcard");
        numberFlashcard.textProperty().bind(viewModel.setNumberFlashcard());
        int numFlashcard = Integer.parseInt(viewModel.setNumberFlashcard().getValue());
        if (numFlashcard > 1) {
            numberFlashcardContainer.getChildren().addAll(numberFlashcard, term2);
        } else {
            numberFlashcardContainer.getChildren().addAll(numberFlashcard, term1);
        }
        numberFlashcardContainer.alignmentProperty().setValue(Pos.CENTER);

        Button actionButton = new Button("Remove");
        actionButton.setId("action-button");
        actionButton.setOnAction(event -> {
            controller.deleteAssignedSet(viewModel);
        });

        HBox.setHgrow(nameLabel, Priority.ALWAYS);
        this.getChildren().addAll(nameLabel, topicLabel, numberFlashcardContainer, actionButton);
        this.setId("flashcard-set-container");
        this.setAlignment(Pos.CENTER_LEFT);
    }
}
