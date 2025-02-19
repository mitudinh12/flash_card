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

import java.util.List;
import java.util.Map;

public class HomeStudentController extends ViewController {
    private EntityManager entityManager = EntityManagerViewModel.getEntityManager();
    private final AuthSessionViewModel authSessionViewModel = AuthSessionViewModel.getInstance();
    private HomeStudentViewModel viewModel = new HomeStudentViewModel(authSessionViewModel.getVerifiedUserInfo().get("userId"), entityManager);
    private int currentPage = 0;
    private final int pageSize = 8;
    private List<Map<String, Object>> classInfoList;

    @FXML
    private ImageView backIcon, nextIcon;
    @FXML
    private VBox classList;

    @FXML
    private void initialize() {
        setUserName();
        viewModel.loadClasses();
        classInfoList = viewModel.getClassInfo(); //list of classes info: each class map with classId, className, teacherName, numberSet, numberStudent
        displayClassInfo();
        updateIconsVisibility();
    }

    private void displayClassInfo() {
        classList.getChildren().clear();
        int start = currentPage * pageSize;
        int end = Math.min(start + pageSize, classInfoList.size());
        for (int i = start; i < end; i++) {
            Map<String, Object> classInfo = classInfoList.get(i);
            int classId = (int) classInfo.get("classId");
            String className = (String) classInfo.get("className");
            String teacherName = (String) classInfo.get("teacherName");
            int numberSet = (int) classInfo.get("numberSet");
            int numberStudent = (int) classInfo.get("numberStudent");
            StudentClassContainer classContainer = new StudentClassContainer(classId, className, teacherName, numberSet, numberStudent);
            classList.getChildren().add(classContainer);
        }
    }

    private void updateIconsVisibility() {
        backIcon.setVisible(currentPage > 0);
        nextIcon.setVisible((currentPage + 1) * pageSize < classInfoList.size());
    }

    @FXML
    private void goNext(ActionEvent event) {
        if ((currentPage + 1) * pageSize < classInfoList.size()) {
            currentPage++;
            displayClassInfo();
            updateIconsVisibility();
        }
    }

    @FXML
    private void goBack(ActionEvent event) {
        if (currentPage > 0) {
            currentPage--;
            displayClassInfo();
            updateIconsVisibility();
        }
    }
}
