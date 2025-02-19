package com.flash_card.view.studentMode;

import com.flash_card.framework.ViewController;
import com.flash_card.view_model.entity.EntityManagerViewModel;
import com.flash_card.view_model.student_mode.HomeStudentViewModel;
import com.flash_card.view_model.user_auth.AuthSessionViewModel;
import jakarta.persistence.EntityManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.util.Map;

public class HomeStudentController extends ViewController {
    private EntityManager entityManager = EntityManagerViewModel.getEntityManager();
    private final AuthSessionViewModel authSessionViewModel = AuthSessionViewModel.getInstance();
    private HomeStudentViewModel viewModel = new HomeStudentViewModel(authSessionViewModel.getVerifiedUserInfo().get("userId"), entityManager);
    private int currentPage = 0;
    private final int pageSize = 8;

    @FXML
    private ImageView backIcon, nextIcon;
    @FXML
    private VBox listClassesUI;

    @FXML
    private void initialize() {
        setUserName();
        viewModel.loadClasses();
        displayClassInfo();
    }

    private void displayClassInfo() {
        listClassesUI.getChildren().clear();
        for (Map<String, Object> classInfo : viewModel.getClassInfo()) {
            String className = (String) classInfo.get("className");
            String teacherName = (String) classInfo.get("teacherName");
            int numberSet = (int) classInfo.get("numberSet");
            int numberStudent = (int) classInfo.get("numberStudent");
            StudentClassContainer classContainer = new StudentClassContainer(className, teacherName, numberSet, numberStudent);
            listClassesUI.getChildren().add(classContainer);
        }
    }


    @FXML
    private void goNext(ActionEvent event) {
    }

    @FXML
    private void goBack(ActionEvent event) {
    }
}
