package com.flash_card.view.teacherMode;

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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.*;

public class SetContainer extends HBox {
    private AssignedFlashcardSetViewModel viewModel;
    private Label nameLabel;
    private ClassDetailController controller;
    private final EntityManager entityManager = EntityManagerViewModel.getEntityManager();
    private final ProgressViewModel progressViewModel = new ProgressViewModel(entityManager);
    private int setId;


    public SetContainer(AssignedFlashcardSetViewModel viewModel, ClassDetailController controller) {
        this.viewModel = viewModel;
        this.controller = controller;
        this.setId = viewModel.getSet().getSetId();
        initializeUI();
    }

    private void initializeUI() {

        nameLabel = new Label();
        nameLabel.setId("name-label2");
        nameLabel.textProperty().bind(viewModel.setNameProperty());

        Label topicLabel = new Label();
        topicLabel.setId("topic-label2");
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
        Button progressButton = new Button("Track Progress");
        progressButton.setId("action-button");
        actionButton.setId("action-button");
        actionButton.setOnAction(event -> {
            controller.deleteAssignedSet(viewModel);
        });

        progressButton.setOnAction(event -> {
            showAllStudentsProgressPopup();
        });

        HBox.setHgrow(nameLabel, Priority.ALWAYS);
        this.getChildren().addAll(nameLabel, topicLabel, numberFlashcardContainer, progressButton, actionButton);
        this.setId("flashcard-set-container");
        this.setAlignment(Pos.CENTER_LEFT);
    }

    private void showAllStudentsProgressPopup() {
        Stage progressStage = new Stage();
        progressStage.initModality(Modality.APPLICATION_MODAL);
        progressStage.setTitle("Students' Progress");

        TableView<Map<String, Object>> tableView = new TableView<>();

        TableColumn<Map<String, Object>, String> nameColumn = new TableColumn<>("Student Name");
        nameColumn.setCellValueFactory(data -> new SimpleStringProperty((String) data.getValue().get("studentName")));

        TableColumn<Map<String, Object>, String> flashcardsColumn = new TableColumn<>("Studied/Total Flashcards");
        flashcardsColumn.setCellValueFactory(data -> new SimpleStringProperty((String) data.getValue().get("flashcardsProgress")));

        TableColumn<Map<String, Object>, Double> quizPercentageColumn = new TableColumn<>("Highest Quiz Percentage");
        quizPercentageColumn.setCellValueFactory(data -> new SimpleDoubleProperty((Double) data.getValue().get("highestQuizPercentage")).asObject());

        tableView.getColumns().addAll(nameColumn, flashcardsColumn, quizPercentageColumn);

        List<Map<String, Object>> studentProgressList = progressViewModel.getStudentProgressList(controller.getClassId(), setId);
        tableView.getItems().addAll(studentProgressList);

        VBox layout = new VBox(tableView);
        layout.setPadding(new Insets(20));

        Scene scene = new Scene(layout, 600, 400);
        String css = Objects.requireNonNull(getClass().getResource("/com/flash_card/styles/styles.css").toExternalForm());
        if (!css.isEmpty()) {
            scene.getStylesheets().add(css);
        }
        progressStage.setScene(scene);
        progressStage.showAndWait();
    }

}
