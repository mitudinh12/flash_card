package com.flash_card.view.studentMode;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public class StudentClassContainer extends HBox {
    private Label classNameLabel;

    public StudentClassContainer(String className, String teacherName, int numberSet, int numberStudent) {
        classNameLabel = new Label();
        classNameLabel.setId("class-name-label");
        classNameLabel.setText(className);

        HBox teacherNameContainer = new HBox();
        teacherNameContainer.setId("teacher-name-container");
        Label teacherNameLabel = new Label();
        teacherNameLabel.setId("teacher-name-label");
        teacherNameLabel.setText(teacherName);
        teacherNameContainer.getChildren().add(teacherNameLabel);

        HBox numberFlashcardContainer = new HBox();
        numberFlashcardContainer.setId("number-set-container");
        Label numberFlashcard = new Label();
        numberFlashcard.setId("number-flashcard-label");
        numberFlashcard.setText(String.valueOf(numberSet));
        Label setLabel1 = new Label("  set");
        Label setLabel2 = new Label("  sets");
        if (numberSet > 0) {
            numberFlashcardContainer.getChildren().addAll(numberFlashcard, setLabel2);
        } else {
            numberFlashcardContainer.getChildren().addAll(numberFlashcard, setLabel1);
        }

        HBox numberStudentContainer = new HBox();
        numberStudentContainer.setId("number-student-container");
        Label numberStudentLabel = new Label();
        numberStudentLabel.setId("number-student-label");
        numberStudentLabel.setText(String.valueOf(numberStudent));
        Label studentLabel1 = new Label("  student");
        Label studentLabel2 = new Label("  students");
        if (numberStudent > 1) {
            numberStudentContainer.getChildren().addAll(numberStudentLabel, studentLabel2);
        } else {
            numberStudentContainer.getChildren().addAll(numberStudentLabel, studentLabel1);
        }

        Button viewButton = new Button("View");
        viewButton.setId("edit-button");

        HBox.setHgrow(classNameLabel, Priority.ALWAYS);
        this.getChildren().addAll(classNameLabel, teacherNameContainer, numberFlashcardContainer, numberStudentContainer, viewButton);
        this.setId("class-container");
        this.setAlignment(Pos.CENTER_LEFT);
    }
}
