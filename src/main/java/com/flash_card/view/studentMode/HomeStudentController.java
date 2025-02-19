package com.flash_card.view.studentMode;

import com.flash_card.framework.ViewController;
import com.flash_card.view_model.entity.EntityManagerViewModel;
import com.flash_card.view_model.teacher_mode.ClassRoomViewModel;
import com.flash_card.view_model.teacher_mode.HomeTeacherViewModel;
import com.flash_card.view_model.user_auth.AuthSessionViewModel;
import jakarta.persistence.EntityManager;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import java.util.ArrayList;
import java.util.List;

public class HomeStudentController extends ViewController {
    private AuthSessionViewModel authSessionViewModel = AuthSessionViewModel.getInstance();
    private EntityManager entityManager = EntityManagerViewModel.getEntityManager();
    private HomeTeacherViewModel homeTeacherViewModel = new HomeTeacherViewModel(authSessionViewModel.getVerifiedUserInfo().get("userId"), entityManager);
    private List<ClassRoomViewModel> classList = new ArrayList<>();
    private int currentPage = 0;
    private final int pageSize = 6;

    @FXML
    private Button createClassButton;

    @FXML
    private Button backButton;

    @FXML
    private Button nextButton;
    @FXML
    private ImageView backIcon;

    @FXML
    private ImageView nextIcon;

    @FXML
    private VBox listClassesUI;

    @FXML
    private void initialize() {
        setUserName();
        homeTeacherViewModel.loadClassrooms();
        classList = homeTeacherViewModel.getClassroomList();

        homeTeacherViewModel.getClassroomList().addListener((ListChangeListener<ClassRoomViewModel>) change -> {
            while (change.next()) {
                if (change.wasAdded() || change.wasRemoved()) {
                    resetPageToFirst();
                }
            }
        });

        updatePage();
    }

    private void updatePage() {
        listClassesUI.getChildren().clear();

        int start = currentPage * pageSize;
        int end = Math.min(start + pageSize, classList.size());
        for (int i = start; i < end; i++) {
            StudentClassContainer classUI = new StudentClassContainer(classList.get(i), this);
            listClassesUI.getChildren().add(classUI);
        }
        if (currentPage == 0) {
            backIcon.setVisible(false);
        } else {
            backIcon.setVisible(true);
        }
        if (end == classList.size()) {
            nextIcon.setVisible(false);
        } else {
            nextIcon.setVisible(true);
        }
    }

    @FXML
    private void goNext(ActionEvent event) {
        if ((currentPage + 1) * pageSize < classList.size()) {
            currentPage++;
            updatePage();
        }
    }

    @FXML
    private void goBack(ActionEvent event) {
        if (currentPage > 0) {
            currentPage--;
            updatePage();
        }
    }

    private void resetPageToFirst() {
        currentPage = 0;
        updatePage();
    }


    public void gotoClassDetailPage(int classId, String className) {
        /*FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/flash_card/fxml/student-class-detail.fxml"));
        try {
            Parent root = loader.load();
            StudentClassDetailController controller = loader.getController();
            controller.setClassId(classId);
            controller.setClassName(className);
            controller.initializeUI();
            Scene scene = listClassesUI.getScene();
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }
}
