package com.flash_card.view.flashcardSet;

import com.flash_card.framework.SetViewModel;
import com.flash_card.view.homepage.HomePageController;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

import java.io.IOException;

public class FlashcardSetContainer extends HBox {
    private SetViewModel viewModel;
    private HomePageController controller;
    private Label nameLabel;
    private EditFlashcardSetController editSetController = new EditFlashcardSetController();
    private boolean studentMode = false;

    public FlashcardSetContainer(SetViewModel viewModel, HomePageController controller) {
        this.viewModel = viewModel;
        this.controller = controller;
        initializeUI();
    }

    protected void initializeUI() {

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


        Button actionButton = new Button("Action");
        actionButton.setId("action-button");
        actionButton.setOnAction(event -> showContextMenu(actionButton));

        HBox.setHgrow(nameLabel, Priority.ALWAYS);
        this.getChildren().addAll(nameLabel, topicLabel, numberFlashcardContainer, actionButton);
        this.setId("flashcard-set-container");
        if (this.viewModel.getType().equals("own")){
            this.setOnMouseClicked(event -> {
                editSetController.goToEditManyCardsPage(viewModel.getSet().getSetId(), nameLabel.getScene());
            });
        }        this.setAlignment(Pos.CENTER_LEFT);
//
    }

    protected void showContextMenu(Button button) {
        ContextMenu menu = new ContextMenu();
        // study
        MenuItem study = new MenuItem("Study");
        study.setOnAction(event -> {
            gotoStudyFlashcardSet(studentMode);
        });
        // quiz
        MenuItem quiz = new MenuItem("Quiz");
        quiz.setOnAction(e -> {
            goToQuizFlashcardSet(studentMode);
        });
        // edit
        MenuItem edit = new MenuItem("Edit");
        edit.setOnAction(event -> {
            gotoEditFlashcardSet();
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
        } else if (viewModel.getType().equals("assigned")) {       // action for assigned flashcard
            studentMode = true;
            menu.getItems().addAll(study, quiz);
        }
        else {                                                        // action for shared flashcard
            menu.getItems().addAll(study, quiz, delete);
        }

        menu.setId("action-list");
        menu.show(button, javafx.geometry.Side.BOTTOM, 0, 0);
    }

    public void gotoEditFlashcardSet() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/flash_card/fxml/edit-set.fxml"));
            Parent root = loader.load();

            //pass the FlashcardSet data to the EditFlashcardSetController
            EditFlashcardSetController editSetController = loader.getController();
            editSetController.setFlashcardSet(
                    viewModel.getSet().getSetId(),viewModel.getSet().getSetName(),
                    viewModel.getSet().getSetDescription(),
                    viewModel.getSet().getSetTopic());

            Scene scene = nameLabel.getScene();
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void gotoStudyFlashcardSet(boolean isStudentMode) {
        try {
            FXMLLoader loader;
            if (isStudentMode) {
                loader = new FXMLLoader(getClass().getResource("/com/flash_card/fxml/student-study-flashcard.fxml"));
            } else {
                loader = new FXMLLoader(getClass().getResource("/com/flash_card/fxml/study-flashcard.fxml"));
            }
            Parent root = loader.load();

            //pass the FlashcardSet data to the StudyFlashcardSetController
            StudyFlashcardSetController studySetController = loader.getController();
            studySetController.setFlashcardSet(viewModel.getSet().getSetId(), viewModel.getSet().getSetName());

            Scene scene = nameLabel.getScene();
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void goToQuizFlashcardSet(boolean isStudentMode) {
        if (viewModel.getSet().getNumberFlashcards() < 4) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("You need at least 4 flashcards to start a quiz");
            alert.showAndWait();
        } else {
            try {
                FXMLLoader loader;
                if (isStudentMode) {
                    loader = new FXMLLoader(getClass().getResource("/com/flash_card/fxml/student-quiz-flashcard.fxml"));
                } else {
                    loader = new FXMLLoader(getClass().getResource("/com/flash_card/fxml/quiz-flashcard.fxml"));
                }
                Parent root = loader.load();
                QuizFlashcardSetController quizSetController = loader.getController();
                quizSetController.setFlashcardSet(viewModel.getSet().getSetId(), viewModel.getSet().getSetName());

                Scene scene = nameLabel.getScene();
                scene.setRoot(root);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
