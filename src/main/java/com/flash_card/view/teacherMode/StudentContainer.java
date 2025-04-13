package com.flash_card.view.teacherMode;

import com.flash_card.localization.Localization;
import com.flash_card.view_model.teacher_mode.StudentViewModel;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

/**
 * Represents a visual container in the UI to display student information
 * such as name and email, along with a remove button.
 * Used within the Class Detail view.
 */
public class StudentContainer extends HBox {

    /** The view model containing student data. */
    private final StudentViewModel viewModel;

    /** Reference to the controller handling class details and actions. */
    private final ClassDetailController controller;

    /** Label for displaying the student's full name. */
    private Label studentLabel;

    /** Localization instance for accessing localized messages. */
    private final Localization localization = Localization.getInstance();

    /**
     * Constructs a new StudentContainer with the specified view model and controller.
     *
     * @param currentViewModel  the view model for the student
     * @param currentController the controller managing the class detail logic
     */
    public StudentContainer(final StudentViewModel currentViewModel, final ClassDetailController currentController) {
        this.controller = currentController;
        this.viewModel = currentViewModel;
        initializeUI();
    }

    /**
     * Initializes the UI components including student name, email, and remove button.
     * Applies CSS IDs for styling and binds the data from the view model.
     */
    private void initializeUI() {
        studentLabel = new Label();
        studentLabel.setId("student-name-label");
        studentLabel.textProperty().bind(viewModel.getStudentName());

        Label email = new Label();
        email.setId("student-email-label");
        email.textProperty().bind(viewModel.getStudentEmail());

        Button deleteButton = new Button(localization.getMessage("teacher.buttonRemove"));
        deleteButton.setId("remove-button");
        deleteButton.setOnAction(event -> controller.deleteStudent(viewModel));

        HBox.setHgrow(studentLabel, Priority.ALWAYS);
        this.getChildren().addAll(studentLabel, email, deleteButton);
        this.setId("student-container");
        this.setAlignment(Pos.CENTER_LEFT);
    }

}
