package com.flash_card.view.teacherMode;

import com.flash_card.framework.ViewController;
import com.flash_card.view_model.entity.EntityManagerViewModel;
import com.flash_card.view_model.teacher_mode.TeacherViewModel;
import com.flash_card.view_model.user_auth.AuthSessionViewModel;
import jakarta.persistence.EntityManager;
import javafx.fxml.FXML;

import javafx.scene.control.TextField;


/**
 * Controller for editing a classroom's information.
 * <p>
 * This controller allows teachers to update the name and description of an existing class.
 * It pre-fills the form fields based on data passed in from the previous view,
 * validates user input, and updates the database through the {@link TeacherViewModel}.
 * </p>
 */
public class EditClassController extends ViewController {

    /**
     * EntityManager instance for database operations.
     */
    private final EntityManager entityManager = EntityManagerViewModel.getEntityManager();

    /**
     * Singleton for accessing current authenticated session info.
     */
    private final AuthSessionViewModel authSessionViewModel = AuthSessionViewModel.getInstance();

    /**
     * ID of the classroom being edited.
     */
    private static int classId;

    /**
     * Name of the classroom being edited.
     */
    private static String className;

    /**
     * Description of the classroom being edited.
     */
    private static String classDescription;

    /**
     * User ID of the currently logged-in teacher.
     */
    private final String userId = authSessionViewModel.getVerifiedUserInfo().get("userId");

    /**
     * ViewModel responsible for teacher-related operations.
     */
    private final TeacherViewModel viewModel = new TeacherViewModel(userId, entityManager);

    /**
     * Text field for editing the class name.
     */
    @FXML
    private TextField classNameField;

    /**
     * Text field for editing the class description.
     */
    @FXML
    private TextField classDescriptionField;

    /**
     * Initializes the view by setting the reload path and filling form fields with current data.
     */
    @FXML
    public void initialize() {
        setReloadFxml("/com/flash_card/fxml/edit-class.fxml");
        classNameField.setText(className);
        classDescriptionField.setText(classDescription);
    }

    /**
     * Sets the class information to be edited.
     *
     * @param currentClassId          the class ID
     * @param currentClassName        the class name
     * @param currentClassDescription the class description
     */
    public void setClassRoom(final int currentClassId,
                             final String currentClassName,
                             final String currentClassDescription) {
        classId = currentClassId;
        className = currentClassName;
        classDescription = currentClassDescription;
        classNameField.setText(className);
        classDescriptionField.setText(classDescription);
    }

    /**
     * Handles saving the updated class details.
     * <p>
     * If either field is empty, an alert is shown.
     * Otherwise, the class is updated and user is redirected to the teacher homepage.
     * </p>
     */
    @FXML
    public void handleSaveClass() {
        String currentClassName = classNameField.getText();
        String currentClassDescription = classDescriptionField.getText();
        if (currentClassName.isEmpty() || currentClassDescription.isEmpty()) {
            showAlert(localization.getMessage("teacher.warning"),
                    localization.getMessage("teacher.warningMessage"));
            return;
        }
        int result = viewModel.editClass(classId, currentClassName, currentClassDescription);
        if (result != 1) {
            showAlert(localization.getMessage("teacher.error"),
                    localization.getMessage("update.errorMessage"));
        } else {
            goToPage("/com/flash_card/fxml/teacher-mode.fxml", classNameField.getScene());
        }
    }

    /**
     * Handles canceling the edit and returning to the teacher mode page.
     */
    @FXML
    public void handleCancel() {
        goToPage("/com/flash_card/fxml/teacher-mode.fxml", classNameField.getScene());
    }

    /**
     * Handles navigating back to the teacher mode page.
     */
    @FXML
    public void handleBack() {
        goToPage("/com/flash_card/fxml/teacher-mode.fxml", classNameField.getScene());
    }
}
