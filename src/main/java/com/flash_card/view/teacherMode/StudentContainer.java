package com.flash_card.view.teacherMode;

import com.flash_card.view_model.teacher_mode.StudentViewModel;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public class StudentContainer extends HBox {
    private StudentViewModel viewModel;
    private ClassDetailController controller;
    private Label studentLabel;

    public StudentContainer(StudentViewModel viewModel, ClassDetailController controller) {
        this.controller = controller;
        this.viewModel = viewModel;
        initializeUI();
    }

    private void initializeUI() {
        studentLabel = new Label();
        studentLabel.setId("student-name-label");
        studentLabel.textProperty().bind(viewModel.getStudentName());

        Label email = new Label();
        email.setId("student-email-label");
        email.textProperty().bind(viewModel.getStudentName());

        Button deleteButton = new Button("Delete");
        deleteButton.setId("delete-button");
        deleteButton.setOnAction(event -> controller.deleteStudent(viewModel));

        HBox.setHgrow(studentLabel, Priority.ALWAYS);
        this.getChildren().addAll(studentLabel, email, deleteButton);
        this.setId("student-container");
        this.setAlignment(Pos.CENTER_LEFT);
    }

}
