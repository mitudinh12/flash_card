package com.flash_card.view.teacherMode;

import com.flash_card.localization.Localization;
import com.flash_card.view_model.entity.EntityManagerViewModel;
import com.flash_card.view_model.flashcard_set.AssignedFlashcardSetViewModel;
import com.flash_card.view_model.flashcard_set.ProgressViewModel;
import jakarta.persistence.EntityManager;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Represents a UI component for displaying an assigned flashcard set
 * in a teacher's class detail view.
 * <p>
 * This component includes the set name, topic, number of flashcards,
 * and buttons for removing the set and viewing students' progress.
 * </p>
 */
public class SetContainer extends HBox {

    /** The view model for the assigned flashcard set. */
    private final AssignedFlashcardSetViewModel viewModel;

    /** Label for displaying the set name. */
    private Label nameLabel;

    /** Reference to the controller of the class detail view. */
    private final ClassDetailController controller;

    /** EntityManager for database interactions. */
    private final EntityManager entityManager = EntityManagerViewModel.getEntityManager();

    /** View model for retrieving progress-related data. */
    private final ProgressViewModel progressViewModel = new ProgressViewModel(entityManager);

    /** The ID of the flashcard set. */
    private final int setId;

    /** The default padding around the progress popup. */
    private static final int POPUP_PADDING = 20;

    /** The default width of the progress popup window. */
    private static final int POPUP_WIDTH = 600;

    /** The default height of the progress popup window. */
    private static final int POPUP_HEIGHT = 400;

    /** Localization utility instance. */
    private final Localization localization = Localization.getInstance();

    /**
     * Constructs a SetContainer with the given flashcard set and controller.
     *
     * @param currentViewModel  the view model representing the assigned flashcard set.
     * @param currentController the controller handling class detail logic.
     */
    public SetContainer(final AssignedFlashcardSetViewModel currentViewModel, final ClassDetailController currentController) {
        this.viewModel = currentViewModel;
        this.controller = currentController;
        this.setId = viewModel.getSet().getSetId();
        initializeUI();
    }

    /**
     * Initializes the UI components of the container, including labels and buttons.
     */
    private void initializeUI() {
        nameLabel = new Label();
        nameLabel.setId("name-label2");
        nameLabel.textProperty().bind(viewModel.setNameProperty());

        Label topicLabel = new Label();
        topicLabel.setId("topic-label2");
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
        numberFlashcardContainer.setAlignment(Pos.CENTER);

        Button actionButton = new Button(localization.getMessage("teacher.buttonRemove"));
        actionButton.setId("action-button");
        actionButton.setOnAction(event -> controller.deleteAssignedSet(viewModel));

        Button progressButton = new Button(localization.getMessage("teacher.buttonProgress"));
        progressButton.setId("action-button");
        progressButton.setOnAction(event -> showAllStudentsProgressPopup());

        HBox.setHgrow(nameLabel, Priority.ALWAYS);
        this.getChildren().addAll(nameLabel, topicLabel, numberFlashcardContainer, progressButton, actionButton);
        this.setId("flashcard-set-container");
        this.setAlignment(Pos.CENTER_LEFT);
    }

    /**
     * Opens a popup window displaying a table of all students' progress
     * on the current flashcard set.
     */
    private void showAllStudentsProgressPopup() {
        Stage progressStage = new Stage();
        progressStage.initModality(Modality.APPLICATION_MODAL);
        progressStage.setTitle(localization.getMessage("teacher.progress"));

        TableView<Map<String, Object>> tableView = new TableView<>();

        TableColumn<Map<String, Object>, String> nameColumn = new TableColumn<>(localization.getMessage("teacher.studentName"));
        nameColumn.setCellValueFactory(data -> new SimpleStringProperty((String) data.getValue().get("studentName")));

        TableColumn<Map<String, Object>, String> flashcardsColumn = new TableColumn<>(localization.getMessage("teacher.columnStudy"));
        flashcardsColumn.setCellValueFactory(data -> new SimpleStringProperty((String) data.getValue().get("flashcardsProgress")));

        TableColumn<Map<String, Object>, Double> quizPercentageColumn = new TableColumn<>(localization.getMessage("teacher.columnQuiz"));
        quizPercentageColumn.setCellValueFactory(data ->
                new SimpleDoubleProperty((Double) data.getValue().get("highestQuizPercentage")).asObject());

        quizPercentageColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(final Double item, final boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText("");
                } else {
                    setText(localization.getNumberFormat().format(item));
                }
            }
        });

        tableView.getColumns().addAll(nameColumn, flashcardsColumn, quizPercentageColumn);

        List<Map<String, Object>> studentProgressList = progressViewModel.getStudentProgressList(controller.getClassId(), setId);
        tableView.getItems().addAll(studentProgressList);

        ScrollPane scrollPane = new ScrollPane(tableView);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);

        VBox layout = new VBox(scrollPane);
        layout.setPadding(new Insets(POPUP_PADDING));

        Scene scene = new Scene(layout, POPUP_WIDTH, POPUP_HEIGHT);
        String css = Objects.requireNonNull(getClass().getResource("/com/flash_card/styles/styles.css").toExternalForm());
        if (!css.isEmpty()) {
            scene.getStylesheets().add(css);
        }

        progressStage.setScene(scene);
        progressStage.showAndWait();
    }
}
