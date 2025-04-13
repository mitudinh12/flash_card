package com.flash_card.view.teacherMode;

import com.flash_card.localization.Localization;
import com.flash_card.view_model.teacher_mode.ClassRoomViewModel;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

import java.io.IOException;

/**
 * A UI component representing a single classroom in teacher mode.
 * <p>
 * Displays the classroom name, number of students, number of flashcard sets,
 * and provides edit and delete buttons.
 * Clicking on the component navigates to the class detail view.
 */
public class ClassContainer extends HBox {

    /**
     * Controller that manages navigation and class operations.
     */
    private HomeTeacherController controller;

    /**
     * ViewModel representing the classroom's data.
     */
    private ClassRoomViewModel viewModel;

    /**
     * Label displaying the classroom name.
     */
    private Label classNameLabel;

    /**
     * Localization instance for translating UI text.
     */
    private final Localization localization = Localization.getInstance();

    /**
     * Constructs a new {@code ClassContainer} for the specified classroom and controller.
     *
     * @param currentViewModel  the ViewModel representing the classroom
     * @param currentController the controller managing the view
     */
    public ClassContainer(final ClassRoomViewModel currentViewModel, final HomeTeacherController currentController) {
        this.controller = currentController;
        this.viewModel = currentViewModel;
        initializeUI();
    }

    /**
     * Initializes the UI components for the classroom container.
     */
    private void initializeUI() {
        classNameLabel = new Label();
        classNameLabel.setId("class-name-label");
        classNameLabel.textProperty().bind(viewModel.getClassName());

        // Student count container
        HBox numberStudentContainer = new HBox();
        numberStudentContainer.setId("number-student-container");
        Label numberStudent = new Label();
        numberStudent.setId("number-student-label");
        numberStudent.textProperty().bind(viewModel.getNumberStudents());
        Label studentLabel1 = new Label(" " + localization.getMessage("student"));
        Label studentLabel2 = new Label("  " + localization.getMessage("students"));
        int numStudent = Integer.parseInt(viewModel.getNumberStudents().getValue());
        if (numStudent > 1) {
            numberStudentContainer.getChildren().addAll(numberStudent, studentLabel2);
        } else {
            numberStudentContainer.getChildren().addAll(numberStudent, studentLabel1);
        }

        // Flashcard set count container
        HBox numberFlashcardContainer = new HBox();
        numberFlashcardContainer.setId("number-set-container");
        Label numberFlashcard = new Label();
        numberFlashcard.setId("number-flashcard-label");
        numberFlashcard.textProperty().bind(viewModel.getNumberSets());
        Label setLabel1 = new Label("  " + localization.getMessage("set"));
        Label setLabel2 = new Label("  " + localization.getMessage("sets"));
        int numSet = Integer.parseInt(viewModel.getNumberSets().getValue());
        if (numSet > 0) {
            numberFlashcardContainer.getChildren().addAll(numberFlashcard, setLabel2);
        } else {
            numberFlashcardContainer.getChildren().addAll(numberFlashcard, setLabel1);
        }

        // Edit button
        Button editButton = new Button(localization.getMessage("edit"));
        editButton.setId("edit-button");
        editButton.setOnAction(event -> gotoEditClassPage());

        // Delete button
        Button deleteButton = new Button(localization.getMessage("delete"));
        deleteButton.setId("delete-button");
        deleteButton.setOnAction(event -> controller.deleteClass(viewModel));

        HBox.setHgrow(classNameLabel, Priority.ALWAYS);
        this.getChildren().addAll(
                classNameLabel,
                numberStudentContainer,
                numberFlashcardContainer,
                editButton,
                deleteButton
        );
        this.setId("class-container");
        this.setAlignment(Pos.CENTER_LEFT);

        // Navigate to class detail page on click
        this.setOnMouseClicked(event ->
                controller.gotoClassDetailPage(viewModel.getClassId(), viewModel.getClassName().getValue())
        );
    }

    /**
     * Navigates to the edit class page for the current class.
     * Loads the edit FXML and sets the classroom values in the controller.
     */
    public void gotoEditClassPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/flash_card/fxml/edit-class.fxml"));
            loader.setResources(localization.getBundle());
            Parent root = loader.load();
            EditClassController controllerEdit = loader.getController();
            controllerEdit.setClassRoom(
                    viewModel.getClassId(),
                    viewModel.getClassName().getValue(),
                    viewModel.getClassDescription()
            );
            Scene scene = classNameLabel.getScene();
            scene.setRoot(root);
            controllerEdit.setReloadFxml("/com/flash_card/fxml/class-detail.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
