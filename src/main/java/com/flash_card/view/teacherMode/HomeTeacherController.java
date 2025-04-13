package com.flash_card.view.teacherMode;

import com.flash_card.framework.ViewController;
import com.flash_card.view_model.entity.EntityManagerViewModel;
import com.flash_card.view_model.teacher_mode.ClassRoomViewModel;
import com.flash_card.view_model.teacher_mode.HomeTeacherViewModel;
import com.flash_card.view_model.user_auth.AuthSessionViewModel;
import jakarta.persistence.EntityManager;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller class responsible for managing the teacher home page view.
 * <p>
 * Displays a paginated list of classrooms managed by the currently logged-in teacher.
 * Allows teachers to navigate between pages, create a new class, or view details of a selected class.
 * </p>
 */
public class HomeTeacherController extends ViewController {

    /** Authenticated session for the currently logged-in user. */
    private final AuthSessionViewModel authSessionViewModel = AuthSessionViewModel.getInstance();

    /** EntityManager for database access. */
    private final EntityManager entityManager = EntityManagerViewModel.getEntityManager();

    /** ViewModel for accessing and modifying teacher data. */
    private final HomeTeacherViewModel homeTeacherViewModel =
            new HomeTeacherViewModel(authSessionViewModel.getVerifiedUserInfo().get("userId"), entityManager);

    /** List of classroom view models for display. */
    private List<ClassRoomViewModel> classList = new ArrayList<>();

    /** Current page index in pagination. */
    private int currentPage = 0;

    /** Number of classrooms to show per page. */
    private static final int PAGE_SIZE = 6;

    /** Button for creating a new class. */
    @FXML
    private Button createClassButton;

    /** Navigation button to go to previous page. */
    @FXML
    private Button backButton;

    /** Navigation button to go to next page. */
    @FXML
    private Button nextButton;

    /** Icon shown when back navigation is available. */
    @FXML
    private ImageView backIcon;

    /** Icon shown when next navigation is available. */
    @FXML
    private ImageView nextIcon;

    /** VBox container for displaying classroom cards. */
    @FXML
    private VBox listClassesUI;

    /**
     * Initializes the teacher home view, loads classrooms and sets up pagination.
     */
    @FXML
    private void initialize() {
        setUserName();
        setReloadFxml("/com/flash_card/fxml/teacher-mode.fxml");
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

    /**
     * Updates the classroom list display for the current page.
     */
    private void updatePage() {
        listClassesUI.getChildren().clear();

        int start = currentPage * PAGE_SIZE;
        int end = Math.min(start + PAGE_SIZE, classList.size());

        for (int i = start; i < end; i++) {
            ClassContainer classUI = new ClassContainer(classList.get(i), this);
            listClassesUI.getChildren().add(classUI);
        }

        backIcon.setVisible(currentPage != 0);
        nextIcon.setVisible(end < classList.size());
    }

    /**
     * Handles the event for clicking the "Next" button in pagination.
     *
     * @param event The action event triggered by the user.
     */
    @FXML
    private void goNext(final ActionEvent event) {
        if ((currentPage + 1) * PAGE_SIZE < classList.size()) {
            currentPage++;
            updatePage();
        }
    }

    /**
     * Handles the event for clicking the "Back" button in pagination.
     *
     * @param event The action event triggered by the user.
     */
    @FXML
    private void goBack(final ActionEvent event) {
        if (currentPage > 0) {
            currentPage--;
            updatePage();
        }
    }

    /**
     * Resets the pagination to show the first page of results.
     */
    private void resetPageToFirst() {
        currentPage = 0;
        updatePage();
    }

    /**
     * Navigates to the "Create Class" view when the button is clicked.
     */
    @FXML
    private void gotoCreateClass() {
        goToPage("/com/flash_card/fxml/create-class.fxml", createClassButton.getScene());
    }

    /**
     * Deletes a classroom from the list if it exists and updates the UI.
     *
     * @param classRoomViewModel The classroom to delete.
     */
    public void deleteClass(final ClassRoomViewModel classRoomViewModel) {
        if (classRoomViewModel == null) {
            return;
        }
        int result = homeTeacherViewModel.deleteClass(classRoomViewModel);
        if (result != 1) {
            showAlert("Error", "Error in deleting class");
        }
    }

    /**
     * Navigates to the detail page for a selected classroom.
     *
     * @param classId    ID of the class to view.
     * @param className  Name of the class to display.
     */
    public void gotoClassDetailPage(final int classId, final String className) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/flash_card/fxml/class-detail.fxml"));
        loader.setResources(localization.getBundle());
        try {
            Parent root = loader.load();
            ClassDetailController controller = loader.getController();
            controller.setClassId(classId);
            controller.setClassName(className);
            controller.initializeUI();
            Scene scene = listClassesUI.getScene();
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
