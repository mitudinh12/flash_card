package com.flash_card.view.flashcardSet;

import com.flash_card.framework.SetViewModel;
import com.flash_card.view.components.LoadingView;
import com.flash_card.view.homepage.HomePageController;
import com.flash_card.view_model.flashcard_set.TriviaQuestionGenerator;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.List;

public class FlashcardSetContainer extends HBox {
    private SetViewModel viewModel;
    private HomePageController controller;
    private Label nameLabel;
    private EditFlashcardSetController editSetController = new EditFlashcardSetController();
    private TriviaQuestionGenerator triviaQuestionGenerator = TriviaQuestionGenerator.getInstance();

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

    private void showContextMenu(Button button) {
        ContextMenu menu = new ContextMenu();
        // study
        MenuItem study = new MenuItem("Study");
        study.setOnAction(event -> {
            gotoStudyFlashcardSet();
        });
        // quiz
        MenuItem quiz = new MenuItem("Quiz");
        quiz.setOnAction(e -> {
            goToQuizFlashcardSet();
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
        } else {                                                        // action for shared flashcard
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

    public void gotoStudyFlashcardSet() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/flash_card/fxml/study-flashcard.fxml"));
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

    private void goToQuizFlashcardSet() {
        Stage loadingStage = showLoading();

        new Thread(() -> {
            // Get fake answers in the background
            triviaQuestionGenerator.generateFakeAnswers(viewModel.getSet().getSetTopic());

            Platform.runLater(() -> {
                loadingStage.close(); // Close loading view
                //Open quiz flashcard view after trivia questions are generated
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/flash_card/fxml/quiz-flashcard.fxml"));
                    Parent root = loader.load();
                    QuizFlashcardSetController quizSetController = loader.getController();
                    quizSetController.setFlashcardSet(viewModel.getSet().getSetId(), viewModel.getSet().getSetName());

                    Scene scene = nameLabel.getScene();
                    scene.setRoot(root);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }).start();
    }


    private Stage showLoading() {
        Stage newStage = new Stage();
        LoadingView loadingView = new LoadingView();
        Scene scene = new Scene(loadingView, 400, 200);
        scene.setFill(null);
        newStage.initStyle(StageStyle.TRANSPARENT);
        newStage.setScene(scene);
        newStage.setX(650);
        newStage.setY(300);
        newStage.show();
        return newStage;
    }
}
