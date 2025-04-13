package com.flash_card.view.teacherMode;

import com.flash_card.framework.ViewController;
import com.flash_card.view_model.entity.EntityManagerViewModel;
import com.flash_card.view_model.teacher_mode.ClassRoomViewModel;
import com.flash_card.view_model.teacher_mode.TeacherViewModel;
import com.flash_card.view_model.user_auth.AuthSessionViewModel;
import jakarta.persistence.EntityManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;

public class EditClassController extends ViewController {

    private EntityManager entityManager = EntityManagerViewModel.getEntityManager();
    private AuthSessionViewModel authSessionViewModel = AuthSessionViewModel.getInstance();
    private static int classId;
    private static String className;
    private static String classDescription;
    private String userId = authSessionViewModel.getVerifiedUserInfo().get("userId");
    private TeacherViewModel viewModel = new TeacherViewModel(userId, entityManager);

    @FXML
    private TextField classNameField;

    @FXML
    private TextField classDescriptionField;

    @FXML
    public void initialize() {
        setReloadFxml("/com/flash_card/fxml/edit-class.fxml");
        classNameField.setText(className);
        classDescriptionField.setText(classDescription);
    }

    public void setClassRoom(int currentClassId, String currentClassName, String currentClassDescription) {
        classId = currentClassId;
        className = currentClassName;
        classDescription = currentClassDescription;
        classNameField.setText(className);
        classDescriptionField.setText(classDescription);
    }

    @FXML
    public void handleSaveClass() {
        String className = classNameField.getText();
        String classDescription = classDescriptionField.getText();
        if (className.isEmpty() || classDescription.isEmpty()) {
            showAlert(localization.getMessage("teacher.warning"), localization.getMessage("teacher.warningMessage"));
            return;
        }
        int result = viewModel.editClass(classId, className, classDescription);
        if (result != 1) {
            showAlert(localization.getMessage("teacher.error"), localization.getMessage("update.errorMessage"));
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

