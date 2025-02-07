package com.flash_card.framework;

import com.flash_card.view.homepage.HomePageController;
import com.flash_card.view_model.flashcard_set.DisplayFlashcardSetViewModel;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class FlashcardSetContainer extends HBox {
    private DisplayFlashcardSetViewModel viewModel;
    private HomePageController controller;

    public FlashcardSetContainer(DisplayFlashcardSetViewModel viewModel, HomePageController controller) {
        this.viewModel = viewModel;
        this.controller = controller;
        initializeUI();
    }

    private void initializeUI() {

        Label nameLabel = new Label();
        nameLabel.setId("name-label");
        nameLabel.textProperty().bind(viewModel.setNameProperty());

        Label topicLabel = new Label();
        topicLabel.setId("topic-label");
        topicLabel.textProperty().bind(viewModel.setTopicProperty());

        Label numberFlashcard = new Label();
        numberFlashcard.setId("number-flashcard");
        numberFlashcard.textProperty().bind(viewModel.setNumberFLashcard());

        Button actionButton = new Button("Action");
        actionButton.setId("action-button");
        actionButton.setOnAction(event -> showContextMenu(actionButton));

        HBox.setHgrow(nameLabel, Priority.ALWAYS);
        this.getChildren().addAll(nameLabel, topicLabel, numberFlashcard, actionButton);
        this.setId("flashcard-set-container");
        this.setAlignment(Pos.CENTER_LEFT);
//
    }

    private void showContextMenu(Button button) {
        ContextMenu menu = new ContextMenu();
        MenuItem study = new MenuItem("Study");
        MenuItem quiz = new MenuItem("Quiz");
        MenuItem edit = new MenuItem("Edit");
        MenuItem delete = new MenuItem("Delete");
        delete.setOnAction(event -> {
            controller.deleteFlashcardSet(viewModel);
        });
        MenuItem share = new MenuItem("Share");

        menu.getItems().addAll(study, quiz, edit, delete, share);
        menu.setId("action-list");
        menu.show(button, javafx.geometry.Side.BOTTOM, 0, 0);
    }
}
