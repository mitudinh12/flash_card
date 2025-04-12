package com.flash_card.view.studentMode;

import com.flash_card.framework.SetViewModel;
import com.flash_card.framework.ViewController;
import com.flash_card.view_model.entity.EntityManagerViewModel;
import com.flash_card.view_model.student_mode.ClassDetailsViewModel;
import jakarta.persistence.EntityManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;
/**
 * Controller class for managing the details of a class in student mode.
 * Displays class information and the list of flashcard sets associated with the class.
 */
public class ClassDetailsViewController extends ViewController {
    /**
     * The number of flashcard sets displayed per page.
     */
    private final int pageSize = 8;
    /**
     * EntityManager instance for database operations.
     */
    private final EntityManager entityManager = EntityManagerViewModel.getEntityManager();
    /**
     * ViewModel for managing class details and flashcard sets.
     */
    private final ClassDetailsViewModel viewModel = new ClassDetailsViewModel(entityManager);
    /**
     * The ID of the class being displayed.
     */
    private int classId;
    /**
     * The name of the class being displayed.
     */
    private String classNameStr;
    /**
     * The name of the teacher for the class being displayed.
     */
    private String teacherNameStr;
    /**
     * The current page index for pagination.
     */
    private int currentPage = 0;
    /**
     * The list of flashcard sets associated with the class.
     */
    private List<SetViewModel> flashcardList = new ArrayList<>();
    /**
     * Label for displaying the class name.
     */
    @FXML
    private Label className;
    /**
     * Text node for displaying the teacher's name.
     */
    @FXML
    private Text teacherName;
    /**
     * Container for displaying the list of flashcard sets.
     */
    @FXML
    private VBox flashcardSetList;
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
     * Initializes the controller and loads the class details and flashcard sets.
     */
    @FXML
    private void initialize() {
        setReloadFxml("/com/flash_card/fxml/student-class-details.fxml");
        setUserName();
        classId = StudentClassSession.getInstance().getClassId();
        classNameStr = StudentClassSession.getInstance().getClassName();
        teacherNameStr = StudentClassSession.getInstance().getTeacherName();
        if (classId != 0) {
            setClassDetails(classNameStr, teacherNameStr);
            loadFlashcardSets();
            updateNavigationControls();
        }
    }
    /**
     * Sets the class name and teacher name in the UI.
     *
     * @param classNameParam   the name of the class
     * @param teacherNameParam the name of the teacher
     */
    private void setClassDetails(String classNameParam, String teacherNameParam) {
        this.className.setText(classNameParam);
        this.teacherName.setText(teacherNameParam);
    }
    /**
     * Loads the flashcard sets associated with the class.
     */
    private void loadFlashcardSets() {
        viewModel.loadClass(classId);
        flashcardList = viewModel.loadSets();
        updatePage();
    }
    /**
     * Updates the current page of flashcard sets in the UI.
     */
    private void updatePage() {
        flashcardSetList.getChildren().clear();
        populateFlashcardList();
        updateNavigationControls();
    }
    /**
     * Populates the flashcard set list for the current page.
     */
    private void populateFlashcardList() {
        int start = currentPage * pageSize;
        int end = Math.min(start + pageSize, flashcardList.size());

        for (int i = start; i < end; i++) {
            flashcardSetList.getChildren().add(new SetInClassContainer(flashcardList.get(i)));
        }
    }
    /**
     * Updates the visibility of navigation controls based on the current page and total flashcard sets.
     */
    private void updateNavigationControls() {
        backIcon.setVisible(currentPage > 0);
        nextIcon.setVisible((currentPage + 1) * pageSize < flashcardList.size());
    }
    /**
     * Navigates to the next page of flashcard sets.
     *
     * @param event the action event triggered by the user
     */
    @FXML
    private void goNext(ActionEvent event) {
        if ((currentPage + 1) * pageSize < flashcardList.size()) {
            currentPage++;
            updatePage();
        }
    }
    /**
     * Navigates to the previous page of flashcard sets.
     *
     * @param event the action event triggered by the user
     */
    @FXML
    private void goBack(ActionEvent event) {
        if (currentPage > 0) {
            currentPage--;
            updatePage();
        }
    }
    /**
     * Navigates back to the student home page.
     *
     * @param event the action event triggered by the user
     */
    @FXML
    private void goBackToStudentHome(ActionEvent event) {
        goToPage("/com/flash_card/fxml/student-class.fxml", className.getScene());
    }
}

