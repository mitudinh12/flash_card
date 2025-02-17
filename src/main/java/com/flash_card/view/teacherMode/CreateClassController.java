package com.flash_card.view.teacherMode;

import com.flash_card.framework.ViewController;
import com.flash_card.view_model.entity.EntityManagerViewModel;
import com.flash_card.view_model.teacher_mode.TeacherViewModel;
import com.flash_card.view_model.user_auth.AuthSessionViewModel;
import jakarta.persistence.EntityManager;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class CreateClassController extends ViewController {
    private final AuthSessionViewModel authSessionViewModel = AuthSessionViewModel.getInstance();
    private String teacherId = authSessionViewModel.getVerifiedUserInfo().get("userId");
    private EntityManager entityManager = EntityManagerViewModel.getEntityManager();
    private final TeacherViewModel viewModel = new TeacherViewModel(teacherId, entityManager);

    @FXML
    private TextField classNameField;

    @FXML
    private TextField classDescriptionField;

    @FXML
    public void handleCreateClass() {
        if (classNameField.getText().isEmpty() || classDescriptionField.getText().isEmpty()) {
            showAlert("Warning", "Please fill in both class name and description fields");
            return;
        }
        int result = viewModel.addClass(classNameField.getText(), classDescriptionField.getText());
        if (result != -1) {
            showAlert("Success", "Class created successfully");
            System.out.println("Class created. Create new one");
            clearFields();
        } else {
            showAlert("Error", "Error in creating new class");
        }
    }

    private void clearFields() {
        classNameField.setText("");
        classDescriptionField.setText("");
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
