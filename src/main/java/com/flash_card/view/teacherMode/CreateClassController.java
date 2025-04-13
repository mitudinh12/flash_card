package com.flash_card.view.teacherMode;

import com.flash_card.framework.ViewController;
import com.flash_card.view_model.entity.EntityManagerViewModel;
import com.flash_card.view_model.teacher_mode.TeacherViewModel;
import com.flash_card.view_model.user_auth.AuthSessionViewModel;
import jakarta.persistence.EntityManager;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

/**
 * Controller for creating a new classroom in the teacher mode view.
 * <p>
 * This controller handles the user interface for creating a new class,
 * validating input fields, and storing the new class in the database.
 * </p>
 */
public class CreateClassController extends ViewController {

    /** Authenticated teacher's session info. */
    private final AuthSessionViewModel authSessionViewModel = AuthSessionViewModel.getInstance();

    /** ID of the currently logged-in teacher. */
    private final String teacherId = authSessionViewModel.getVerifiedUserInfo().get("userId");

    /** EntityManager instance for database operations. */
    private final EntityManager entityManager = EntityManagerViewModel.getEntityManager();

    /** ViewModel for teacher-specific logic. */
    private final TeacherViewModel viewModel = new TeacherViewModel(teacherId, entityManager);

    /** TextField input for the class name. */
    @FXML
    private TextField classNameField;

    /** TextField input for the class description. */
    @FXML
    private TextField classDescriptionField;

    /**
     * Initializes the view and sets the reload FXML path.
     */
    @FXML
    public void initialize() {
        setReloadFxml("/com/flash_card/fxml/create-class.fxml");
    }

    /**
     * Handles the creation of a new class.
     * <p>
     * Validates that both name and description are filled out.
     * If successful, shows a success alert and clears the fields.
     * </p>
     */
    @FXML
    public void handleCreateClass() {
        if (classNameField.getText().isEmpty() || classDescriptionField.getText().isEmpty()) {
            showAlert(localization.getMessage("teacher.warning"),
                    localization.getMessage("teacher.warningMessage"));
            return;
        }

        int result = viewModel.addClass(classNameField.getText(), classDescriptionField.getText());
        if (result != -1) {
            showAlert(localization.getMessage("teacher.success"),
                    localization.getMessage("teacher.successMessage"));
            clearFields();
        } else {
            showAlert(localization.getMessage("teacher.error"),
                    localization.getMessage("teacher.errorCreateClass"));
        }
    }

    /**
     * Clears the input fields after a class has been created.
     */
    private void clearFields() {
        classNameField.setText("");
        classDescriptionField.setText("");
    }

    /**
     * Handles canceling the creation and returns to the teacher mode page.
     */
    @FXML
    public void handleCancel() {
        goToPage("/com/flash_card/fxml/teacher-mode.fxml", classNameField.getScene());
    }

    /**
     * Handles the back button action and returns to the teacher mode page.
     */
    @FXML
    public void handleBack() {
        goToPage("/com/flash_card/fxml/teacher-mode.fxml", classNameField.getScene());
    }
}
