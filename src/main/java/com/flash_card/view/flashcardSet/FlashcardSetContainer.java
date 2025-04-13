package com.flash_card.view.flashcardSet;

import com.flash_card.framework.SetViewModel;
import com.flash_card.localization.Localization;
import com.flash_card.view.homepage.HomePageController;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

import java.io.IOException;

/**
 * UI container for displaying a flashcard set.
 */
public class FlashcardSetContainer extends HBox {
    /** The view model for the flashcard set. */
    private final SetViewModel viewModel;

    /** The controller for the home page. */
    private final HomePageController controller;

    /** The label for displaying the name of the flashcard set. */
    private Label nameLabel;

    /** The controller for editing flashcard sets. */
    private final EditFlashcardSetController editSetController = new EditFlashcardSetController();

    /** Indicates whether the container is in student mode. */
    private boolean studentMode = false;

    /** The localization instance for retrieving localized messages. */
    private final Localization localization = Localization.getInstance();

    /** The minimum number of flashcards required for a quiz. */
    private static final int MIN_FLASHCARDS_FOR_QUIZ = 4;

    /**
     * Constructs a new FlashcardSetContainer.
     *
     * @param viewModelParam  the view model for the flashcard set
     * @param controllerParam the controller for the home page
     */
    public FlashcardSetContainer(final SetViewModel viewModelParam, final HomePageController controllerParam) {
        this.viewModel = viewModelParam;
        this.controller = controllerParam;
        initializeUI();
    }

    /**
     * Initializes the UI components for the flashcard set container.
     */
    protected void initializeUI() {

        nameLabel = new Label();
        nameLabel.setId("name-label");
        nameLabel.textProperty().bind(viewModel.setNameProperty());

        Label languageLabel = new Label();
        languageLabel.setId("language-label");
        languageLabel.textProperty().bind(viewModel.setLanguageProperty());

        Label topicLabel = new Label();
        topicLabel.setId("topic-label");
        topicLabel.textProperty().bind(viewModel.setTopicProperty());

        HBox numberFlashcardContainer = new HBox();
        Label numberFlashcard = new Label();
        Label term1 = new Label(" " + localization.getMessage("term"));
        Label term2 = new Label(" " + localization.getMessage("terms"));
        numberFlashcardContainer.setId("number-flashcard");
        numberFlashcard.textProperty().bind(viewModel.setNumberFlashcard());
        int numFlashcard = Integer.parseInt(viewModel.setNumberFlashcard().getValue());
        if (numFlashcard > 1) {
            numberFlashcardContainer.getChildren().addAll(numberFlashcard, term2);
        } else {
            numberFlashcardContainer.getChildren().addAll(numberFlashcard, term1);
        }
        numberFlashcardContainer.alignmentProperty().setValue(Pos.CENTER);


        Button actionButton = new Button(localization.getMessage("flashcardSet.actionButton"));
        actionButton.setId("action-button");
        actionButton.setOnAction(event -> showContextMenu(actionButton));

        HBox.setHgrow(nameLabel, Priority.ALWAYS);
        this.getChildren().addAll(nameLabel, languageLabel, topicLabel, numberFlashcardContainer, actionButton);
        this.setId("flashcard-set-container");
        if (this.viewModel.getType().equals("own")) {
            this.setOnMouseClicked(event ->
                    editSetController.goToEditManyCardsPage(
                            viewModel.getSet().getSetId(),
                            nameLabel.getScene()
                    )
            );
        }
        this.setAlignment(Pos.CENTER_LEFT);
    }

    /**
     * Displays the context menu for the flashcard set.
     *
     * @param button the button that triggers the context menu
     */
    protected void showContextMenu(final Button button) {
        ContextMenu menu = new ContextMenu();
        // study
        MenuItem study = new MenuItem(localization.getMessage("flashcardSet.studyAction"));
        study.setOnAction(event ->
            gotoStudyFlashcardSet(studentMode));
        // quiz
        MenuItem quiz = new MenuItem(localization.getMessage("flashcardSet.quizAction"));
        quiz.setOnAction(e -> goToQuizFlashcardSet(studentMode));
        // edit
        MenuItem edit = new MenuItem(localization.getMessage("flashcardSet.editAction"));
        edit.setOnAction(event -> gotoEditFlashcardSet());
        // delete
        MenuItem delete = new MenuItem(localization.getMessage("flashcardSet.deleteAction"));
        delete.setOnAction(event -> controller.deleteFlashcardSet(viewModel));
        // share
        MenuItem share = new MenuItem(localization.getMessage("flashcardSet.shareAction"));
        share.setOnAction(event -> controller.handleShare(viewModel.getSet().getSetId()));
        //track progress
        MenuItem trackProgress = new MenuItem(localization.getMessage("flashcardSet.trackProgress"));
        trackProgress.setOnAction(event -> showTrackProgressPopup());

        // conditional render
        if (viewModel.getType().equals("own")) {                        // action for own flashcard
            menu.getItems().addAll(study, quiz, edit, delete, share);
        } else if (viewModel.getType().equals("assigned")) {       // action for assigned flashcard
            studentMode = true;
            menu.getItems().addAll(study, quiz, trackProgress);
        } else {                                                        // action for shared flashcard
            menu.getItems().addAll(study, quiz, delete);
        }
        menu.setId("action-list");
        menu.show(button, javafx.geometry.Side.BOTTOM, 0, 0);
    }

    /**
     * Navigates to the edit flashcard set page.
     */
    public void gotoEditFlashcardSet() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/flash_card/fxml/edit-set.fxml"));
            loader.setResources(localization.getBundle());
            Parent root = loader.load();

            //pass the FlashcardSet data to the EditFlashcardSetController
            EditFlashcardSetController localEditSetController = loader.getController();
            localEditSetController.setFlashcardSet(
                    viewModel.getSet().getSetId(), viewModel.getSet().getSetName(),
                    viewModel.getSet().getSetDescription(),
                    viewModel.getSet().getSetTopic());

            Scene scene = nameLabel.getScene();
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Navigates to the study flashcard set page.
     *
     * @param isStudentMode whether the user is in student mode
     */
    public void gotoStudyFlashcardSet(final boolean isStudentMode) {
        try {
            FXMLLoader loader;
            if (isStudentMode) {
                loader = new FXMLLoader(getClass().getResource("/com/flash_card/fxml/student-study-flashcard.fxml"));
            } else {
                loader = new FXMLLoader(getClass().getResource("/com/flash_card/fxml/study-flashcard.fxml"));
            }
            loader.setResources(localization.getBundle());

            StudySession session = StudySession.getInstance();
            session.setSetId(viewModel.getSet().getSetId());
            session.setSetName(viewModel.getSet().getSetName());
            Parent root = loader.load();

            Scene scene = nameLabel.getScene();
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Navigates to the quiz flashcard set page.
     *
     * @param isStudentMode whether the user is in student mode
     */
    public void goToQuizFlashcardSet(final boolean isStudentMode) {
        if (viewModel.getSet().getNumberFlashcards() < MIN_FLASHCARDS_FOR_QUIZ) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(localization.getMessage("flashcardSet.errorTitle"));
            alert.setHeaderText(null);
            alert.setContentText(localization.getMessage("flashcardSet.errorQuizSet"));
            alert.showAndWait();
        } else {
            try {
                FXMLLoader loader;
                if (isStudentMode) {
                    loader = new FXMLLoader(getClass().getResource("/com/flash_card/fxml/student-quiz-flashcard.fxml"));
                } else {
                    loader = new FXMLLoader(getClass().getResource("/com/flash_card/fxml/quiz-flashcard.fxml"));
                }
                loader.setResources(localization.getBundle());

                QuizSession quizSession = QuizSession.getInstance();
                quizSession.setSetId(viewModel.getSet().getSetId());
                quizSession.setSetName(viewModel.getSet().getSetName());

                Parent root = loader.load();

                Scene scene = nameLabel.getScene();
                scene.setRoot(root);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Displays a popup for tracking progress.
     */
    protected void showTrackProgressPopup() {
        // TODO Implementation for showing track progress popup
    }
}
