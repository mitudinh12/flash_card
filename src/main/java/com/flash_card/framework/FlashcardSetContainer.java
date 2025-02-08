package com.flash_card.framework;

import com.flash_card.view.flashcardSet.EditFlashcardSetController;
import com.flash_card.view.homepage.HomePageController;
import com.flash_card.view_model.flashcard_set.OwnFlashcardSetViewModel;
import com.flash_card.view_model.flashcard_set.SetViewModel;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class FlashcardSetContainer extends HBox {
    private SetViewModel viewModel;
    private HomePageController controller;
    private Label nameLabel;

    public FlashcardSetContainer(SetViewModel viewModel, HomePageController controller) {
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

        Label numberFlashcard = new Label();
        numberFlashcard.setId("number-flashcard");
        numberFlashcard.textProperty().bind(viewModel.setNumberFlashcard());

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
        // study
        MenuItem study = new MenuItem("Study");
        // quiz
        MenuItem quiz = new MenuItem("Quiz");
        // edit
        MenuItem edit = new MenuItem("Edit");
        edit.setOnAction(event -> {
            gotoEditFlashcard();
        });
        // delete
        MenuItem delete = new MenuItem("Delete");
        delete.setOnAction(event -> {
            controller.deleteFlashcardSet(viewModel);
        });
        // share
        MenuItem share = new MenuItem("Share");
        share.setOnAction(event -> {
            controller.handleShare(viewModel.getSet().getSetId());
        });

        // conditional render
        if (viewModel.getType().equals("own")) {                        // action for own flashcard
            menu.getItems().addAll(study, quiz, edit, delete, share);
        } else {                                                        // action for shared flashcard
            menu.getItems().addAll(study, quiz, edit, delete);
        }

        menu.setId("action-list");
        menu.show(button, javafx.geometry.Side.BOTTOM, 0, 0);
    }

    public void gotoEditFlashcard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/flash_card/fxml/edit-set.fxml"));
            Parent root = loader.load();

            //pass the FlashcardSet data to the EditFlashcardSetController
            EditFlashcardSetController editSetController = loader.getController();
            editSetController.setFlashcardSet(
                    viewModel.getSet().getSetId(),
                    viewModel.getSet().getSetName(),
                    viewModel.getSet().getSetDescription(),
                    viewModel.getSet().getSetTopic());

            Scene scene = nameLabel.getScene();
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
