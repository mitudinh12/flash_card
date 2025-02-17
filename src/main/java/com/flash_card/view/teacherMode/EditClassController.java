package com.flash_card.view.teacherMode;

import com.flash_card.framework.ViewController;
import com.flash_card.view_model.entity.EntityManagerViewModel;
import com.flash_card.view_model.teacher_mode.TeacherViewModel;
import com.flash_card.view_model.user_auth.AuthSessionViewModel;
import jakarta.persistence.EntityManager;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class EditClassController extends ViewController {

    private EntityManager entityManager = EntityManagerViewModel.getEntityManager();
    private AuthSessionViewModel authSessionViewModel = AuthSessionViewModel.getInstance();
    private int classId;
    private String userId = authSessionViewModel.getVerifiedUserInfo().get("userId");
    private TeacherViewModel viewModel = new TeacherViewModel(userId, entityManager);

    @FXML
    private TextField classNameField;

    @FXML
    private TextField classDescriptionField;

    public void setClassRoom(int classId, String className, String classDescription) {
        this.classId = classId;
        classNameField.setText(className);
        classDescriptionField.setText(classDescription);
    }

    @FXML
    public void handleSaveClass() {
        String className = classNameField.getText();
        String classDescription = classDescriptionField.getText();
        if (className.isEmpty() || classDescription.isEmpty()) {
            showAlert("Warning", "Please fill in all fields");
            return;
        }
        int result = viewModel.editClass(classId, className, classDescription);
        if (result != 1) {
            showAlert("Error", "Error in updating class");
        } else {
            goToPage("/com/flash_card/fxml/teacher-mode.fxml", classNameField.getScene());
        }
    }

    @FXML
    public void handleCancel() {
        goToPage("/com/flash_card/fxml/teacher-mode.fxml", classNameField.getScene());
    }

    @FXML
    public void handleBack() {
        goToPage("/com/flash_card/fxml/teacher-mode.fxml", classNameField.getScene());
    }
}
