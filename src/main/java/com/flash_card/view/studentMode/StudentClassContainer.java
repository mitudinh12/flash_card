package com.flash_card.view.studentMode;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

import java.io.IOException;

public class StudentClassContainer extends HBox {
    public StudentClassContainer(int classId, String className, String teacherName, int numberSet, int numberStudent) {
        Label classNameLabel = new Label();
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
        viewButton.setOnAction(event -> goToClassDetail(classId, className, teacherName, classNameLabel.getScene()));

        HBox.setHgrow(classNameLabel, Priority.ALWAYS);
        this.getChildren().addAll(classNameLabel, teacherNameContainer, numberFlashcardContainer, numberStudentContainer, viewButton);
        this.setId("class-container");
        this.setAlignment(Pos.CENTER_LEFT);
    }

    private void goToClassDetail(int classId, String className, String teacherName, Scene scene) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/flash_card/fxml/student-class-details.fxml"));
            Parent root = loader.load();
            ClassDetailsViewController controller = loader.getController();
            controller.loadClass(classId, className, teacherName);
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
