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

/**
 * Controller class for managing the home view in student mode.
 * Displays a list of classes and provides navigation for pagination.
 */
public class HomeStudentController extends ViewController {
    /**
     * ViewModel for managing user authentication sessions.
     */
    private final AuthSessionViewModel authSessionViewModel = AuthSessionViewModel.getInstance();
    /**
     * The number of classes displayed per page.
     */
    private final int pageSize = 8;
    /**
     * EntityManager instance for database operations.
     */
    private final EntityManager entityManager = EntityManagerViewModel.getEntityManager();
    /**
     * ViewModel for managing the home view in student mode.
     */
    private final HomeStudentViewModel viewModel = new HomeStudentViewModel(
            authSessionViewModel.getVerifiedUserInfo().get("userId"), entityManager);
    /**
     * The current page index for pagination.
     */
    private int currentPage = 0;
    /**
     * The list of class information to be displayed.
     */
    private List<Map<String, Object>> classInfoList;
    /**
     * Icon for navigating to the previous page.
     */
    @FXML
    private ImageView backIcon;

    /**
     * Icon for navigating to the next page.
     */
    @FXML
    private ImageView nextIcon;
    /**
     * Container for displaying the list of classes.
     */
    @FXML
    private VBox classList;
    /**
     * Initializes the controller and loads the class information.
     */
    @FXML
    private void initialize() {
        setReloadFxml("/com/flash_card/fxml/student-class.fxml");
        setUserName();
        viewModel.loadClasses();
        classInfoList = viewModel.getClassInfo(); //list of classes info: each class map with classId, className, teacherName, numberSet, numberStudent
        displayClassInfo();
        updateIconsVisibility();
    }
    /**
     * Displays the class information for the current page.
     */
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
            classContainer.setOnMouseClicked(event -> classContainer.goToClassDetail(classId, className, teacherName, classList.getScene()));
            classList.getChildren().add(classContainer);
        }
    }
    /**
     * Updates the visibility of the navigation icons based on the current page and total classes.
     */
    private void updateIconsVisibility() {
        backIcon.setVisible(currentPage > 0);
        nextIcon.setVisible((currentPage + 1) * pageSize < classInfoList.size());
    }
    /**
     * Navigates to the next page of classes.
     *
     * @param event the action event triggered by the user
     */
    @FXML
    private void goNext(ActionEvent event) {
        if ((currentPage + 1) * pageSize < classInfoList.size()) {
            currentPage++;
            displayClassInfo();
            updateIconsVisibility();
        }
    }
    /**
     * Navigates to the previous page of classes.
     *
     * @param event the action event triggered by the user
     */
    @FXML
    private void goBack(ActionEvent event) {
        if (currentPage > 0) {
            currentPage--;
            displayClassInfo();
            updateIconsVisibility();
        }
    }
}
