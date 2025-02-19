package com.flash_card.view.studentMode;

import com.flash_card.view_model.teacher_mode.ClassRoomViewModel;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public class StudentClassContainer extends HBox {
    private HomeStudentController controller;
    private ClassRoomViewModel viewModel;
    private Label classNameLabel;

    public StudentClassContainer(ClassRoomViewModel viewModel, HomeStudentController controller) {
        this.controller = controller;
        this.viewModel = viewModel;
        initializeUI();
    }

    private void initializeUI() {
        classNameLabel = new Label();
        classNameLabel.setId("class-name-label");
        classNameLabel.textProperty().bind(viewModel.getClassName());

        HBox numberStudentContainer = new HBox();
        numberStudentContainer.setId("number-student-container");
        Label numberStudent = new Label();
        numberStudent.setId("number-student-label");
        numberStudent.textProperty().bind(viewModel.getNumberStudents());
        Label studentLabel1 = new Label("  student");
        Label studentLabel2 = new Label("  students");
        int numStudent = Integer.parseInt(viewModel.getNumberStudents().getValue());
        if (numStudent > 1) {
            numberStudentContainer.getChildren().addAll(numberStudent, studentLabel2);
        } else {
            numberStudentContainer.getChildren().addAll(numberStudent, studentLabel1);
        }
        HBox numberFlashcardContainer = new HBox();
        numberFlashcardContainer.setId("number-set-container");
        Label numberFlashcard = new Label();
        numberFlashcard.setId("number-flashcard-label");
        numberFlashcard.textProperty().bind(viewModel.getNumberSets());
        Label setLabel1 = new Label("  set");
        Label setLabel2 = new Label("  sets");
        int numSet = Integer.parseInt(viewModel.getNumberSets().getValue());
        if (numSet > 0) {
            numberFlashcardContainer.getChildren().addAll(numberFlashcard, setLabel2);
        } else {
            numberFlashcardContainer.getChildren().addAll(numberFlashcard, setLabel1);
        }

        Button viewButton = new Button("View");
        viewButton.setId("edit-button");

        HBox.setHgrow(classNameLabel, Priority.ALWAYS);
        this.getChildren().addAll(classNameLabel, numberStudentContainer, numberFlashcardContainer, viewButton);
        this.setId("class-container");
        this.setAlignment(Pos.CENTER_LEFT);
        this.setOnMouseClicked(event -> controller.gotoClassDetailPage(viewModel.getClassId(), viewModel.getClassName().getValue()));
    }
}
