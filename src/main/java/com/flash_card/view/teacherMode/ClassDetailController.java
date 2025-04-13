package com.flash_card.view.teacherMode;

import com.flash_card.framework.ViewController;
import com.flash_card.model.entity.FlashcardSet;
import com.flash_card.view_model.entity.EntityManagerViewModel;
import com.flash_card.view_model.flashcard_set.AssignedFlashcardSetViewModel;
import com.flash_card.view_model.teacher_mode.ClassDetailViewModel;
import com.flash_card.view_model.teacher_mode.TeacherViewModel;
import com.flash_card.view_model.teacher_mode.StudentViewModel;
import com.flash_card.view_model.user_auth.AuthSessionViewModel;
import jakarta.persistence.EntityManager;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.util.Pair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Controller for the class detail view in teacher mode.
 * <p>
 * This class manages the display of students and assigned flashcard sets in a selected class.
 * It supports adding students, assigning sets, pagination for both lists,
 * and navigating back to the teacher homepage.
 * </p>
 */
public class ClassDetailController extends ViewController {

    /**
     * ViewModel for accessing authenticated user session info.
     */
    private final AuthSessionViewModel authSessionViewModel = AuthSessionViewModel.getInstance();

    /**
     * EntityManager instance for DB operations.
     */
    private final EntityManager entityManager = EntityManagerViewModel.getEntityManager();

    /**
     * ViewModel for teacher-related operations.
     */
    private final TeacherViewModel teacherViewModel = new TeacherViewModel(
            authSessionViewModel.getVerifiedUserInfo().get("userId"), entityManager);

    /**
     * List of students in the current class.
     */
    private List<StudentViewModel> studentList = new ArrayList<>();

    /**
     * List of assigned flashcard sets in the current class.
     */
    private List<AssignedFlashcardSetViewModel> setList = new ArrayList<>();

    /**
     * ID of the current class being viewed.
     */
    private int classId;

    /**
     * Current page index for student pagination.
     */
    private int currentStudentPage = 0;

    /**
     * Current page index for flashcard set pagination.
     */
    private int currentSetPage = 0;

    /**
     * Number of items per page.
     */
    private static final int PAGE_SIZE = 4;

    /**
     * Padding value for layout containers.
     */
    private static final int PADDING = 20;

    /**
     * Padding for checkbox layout.
     */
    private static final int CHECKBOX_PADDING = 15;

    /**
     * Vertical spacing between form elements.
     */
    private static final int VERTICAL_SPACING = 10;

    /**
     * ViewModel that handles business logic for the class detail view.
     */
    private final ClassDetailViewModel classDetailViewModel = new ClassDetailViewModel(
            authSessionViewModel.getVerifiedUserInfo().get("userId"), entityManager);

    /**
     * VBox for displaying student UI cards.
     */
    @FXML
    private VBox listStudentsUI;

    /**
     * Label displaying the class name.
     */
    @FXML
    private Label className;

    /**
     * VBox for displaying flashcard set UI cards.
     */
    @FXML
    private VBox listSetsUI;

    /**
     * Button to go to the previous student page.
     */
    @FXML
    private ImageView backButtonStudent;

    /**
     * Button to go to the next student page.
     */
    @FXML
    private ImageView nextButtonStudent;

    /**
     * Button to go to the previous set page.
     */
    @FXML
    private ImageView backButtonSet;

    /**
     * Button to go to the next set page.
     */
    @FXML
    private ImageView nextButtonSet;

    /**
     * Initializes the view, loads students and sets, and sets up listeners for list changes.
     */
    public void initializeUI() {
        setReloadFxml("/com/flash_card/fxml/class-detail.fxml");
        classDetailViewModel.loadStudents(classId);
        studentList = classDetailViewModel.getStudentList();
        classDetailViewModel.loadSets(classId);
        setList = classDetailViewModel.getSetList();

        classDetailViewModel.getStudentList().addListener((ListChangeListener<StudentViewModel>) change -> {
            while (change.next()) {
                if (change.wasAdded() || change.wasRemoved()) {
                    resetStudentPageToFirst();
                }
            }
        });

        classDetailViewModel.getSetList().addListener((ListChangeListener<AssignedFlashcardSetViewModel>) change -> {
            while (change.next()) {
                if (change.wasAdded() || change.wasRemoved()) {
                    resetSetPageToFirst();
                }
            }
        });

        updatePage();
    }

    /**
     * Reloads the current view with updated data.
     */
    @Override
    protected void reloadCurrentView() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/flash_card/fxml/class-detail.fxml"));
        loader.setResources(localization.getBundle());
        try {
            Parent root = loader.load();
            ClassDetailController controller = loader.getController();
            controller.setClassId(classId);
            controller.setClassName(className.getText());
            controller.initializeUI();
            Scene scene = languageComboBox.getScene();
            scene.setRoot(root);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets the class name in the UI.
     *
     * @param name The name of the class to be displayed.
     */
    public void setClassName(final String name) {
        className.setText(name);
    }

    /**
     * Updates the UI pagination for both student and flashcard set lists.
     */
    private void updatePage() {
        listStudentsUI.getChildren().clear();
        listSetsUI.getChildren().clear();

        int studentStart = currentStudentPage * PAGE_SIZE;
        int studentEnd = Math.min((currentStudentPage + 1) * PAGE_SIZE, studentList.size());
        for (int i = studentStart; i < studentEnd; i++) {
            StudentContainer studentUI = new StudentContainer(studentList.get(i), this);
            listStudentsUI.getChildren().add(studentUI);
        }

        int setStart = currentSetPage * PAGE_SIZE;
        int setEnd = Math.min((currentSetPage + 1) * PAGE_SIZE, setList.size());
        for (int i = setStart; i < setEnd; i++) {
            SetContainer setUI = new SetContainer(setList.get(i), this);
            listSetsUI.getChildren().add(setUI);
        }

        backButtonStudent.setVisible(currentStudentPage != 0);

        nextButtonStudent.setVisible(studentEnd != studentList.size());

        backButtonSet.setVisible(currentSetPage != 0);

        nextButtonSet.setVisible(setEnd != setList.size());
        System.out.println(backButtonSet.isVisible());

    }

    /**
     * Advances to the next page of students.
     *
     * @param event the action event
     */
    @FXML
    private void goNextStudent(final ActionEvent event) {
        if ((currentStudentPage + 1) * PAGE_SIZE < studentList.size()) {
            currentStudentPage++;
            updatePage();
        }
    }

    /**
     * Goes back to the previous page of students.
     *
     * @param event the action event
     */
    @FXML
    private void goBackStudent(final ActionEvent event) {
        if (currentStudentPage > 0) {
            currentStudentPage--;
            updatePage();
        }
    }

    /**
     * Advances to the next page of flashcard sets.
     *
     * @param event the action event
     */
    @FXML
    private void goNextSet(final ActionEvent event) {
        if ((currentSetPage + 1) * PAGE_SIZE < setList.size()) {
            currentSetPage++;
            updatePage();
        }
    }

    /**
     * Goes back to the previous page of flashcard sets.
     *
     * @param event the action event
     */
    @FXML
    private void goBackSet(final ActionEvent event) {
        if (currentSetPage > 0) {
            currentSetPage--;
            updatePage();
        }
    }

    /**
     * Resets the student pagination to the first page.
     */
    private void resetStudentPageToFirst() {
        currentStudentPage = 0;
        updatePage();
    }

    /**
     * Resets the flashcard set pagination to the first page.
     */
    private void resetSetPageToFirst() {
        currentSetPage = 0;
        updatePage();
    }

    /**
     * Opens a modal to assign unassigned flashcard sets to the current class.
     */
    @FXML
    private void handleAssignSet() {
        Stage addSetStage = new Stage();
        addSetStage.initModality(Modality.APPLICATION_MODAL);
        addSetStage.setTitle(localization.getMessage("teacher.titleAssignSet"));

        VBox layout = new VBox();
        layout.getStyleClass().add("layout-check-boxes");
        layout.setPadding(new Insets(PADDING));

        Label label = new Label(localization.getMessage("teacher.labelAssignSet"));
        label.getStyleClass().add("assign-label");
        layout.getChildren().add(label);

        VBox checkboxes = new VBox();
        checkboxes.setPadding(new Insets(CHECKBOX_PADDING));
        layout.getChildren().add(checkboxes);

        List<Pair<Integer, CheckBox>> checkBoxes = new ArrayList<>();

        for (FlashcardSet set : teacherViewModel.getAllUnassignedSetsByClassIdAndTeacherId(classId)) {
            String content = set.getSetName()
                    + " - " + set.getSetTopic() + " - "
                    + set.getNumberFlashcards() + " flashcards";
            CheckBox checkBox = new CheckBox(content);
            checkBoxes.add(new Pair<>(set.getSetId(), checkBox));
            checkBox.getStyleClass().add("check-box");
            checkboxes.getChildren().add(checkBox);
        }

        Button assignButton = new Button(localization.getMessage("teacher.assignButton"));
        assignButton.getStyleClass().add("confirm-assign-button");
        assignButton.setOnAction(e -> {
            List<Integer> selectedSets = new ArrayList<>();
            for (Pair<Integer, CheckBox> checkBox : checkBoxes) {
                CheckBox box = checkBox.getValue();
                if (box.isSelected()) {
                    selectedSets.add(checkBox.getKey());
                }
            }
            addSetStage.close();
            int result = classDetailViewModel.assignSets(selectedSets, classId);
            if (result == -1) {
                showAlert(localization.getMessage("home.error"),
                        localization.getMessage("teacher.errorAssign.message"));
            } else {
                updatePage();
            }

        });
        layout.getChildren().add(assignButton);

        Scene scene = new Scene(layout);
        String css = Objects.requireNonNull(getClass()
                .getResource("/com/flash_card/styles/styles.css").toExternalForm());
        if (!css.isEmpty()) {
            scene.getStylesheets().add(css);
        }
        addSetStage.setScene(scene);
        addSetStage.showAndWait();
    }

    /**
     * Opens a modal to add a new student to the class.
     */
    @FXML
    private void handleAddStudent() {
        Stage newStage = new Stage();
        newStage.setTitle(localization.getMessage("teacher.titleAddStudent"));
        VBox layout = new VBox();
        layout.getStyleClass().add("layout-check-boxes");
        layout.alignmentProperty().set(javafx.geometry.Pos.CENTER);
        layout.setPadding(new Insets(PADDING));
        layout.setSpacing(VERTICAL_SPACING);

        Label emailLabel = new Label(localization.getMessage("teacher.labelAddStudent"));
        emailLabel.getStyleClass().add("assign-label");

        TextField emailField = new TextField();
        emailField.setId("email-field");

        Button shareButton = new Button(localization.getMessage("teacher.addButton"));
        shareButton.getStyleClass().add("confirm-assign-button");

        layout.getChildren().addAll(emailLabel, emailField, shareButton);
        Scene scene = new Scene(layout);
        String css = Objects.requireNonNull(getClass()
                .getResource("/com/flash_card/styles/styles.css")
                .toExternalForm());
        if (!css.isEmpty()) {
            scene.getStylesheets().add(css);
        }
        newStage.setScene(scene);
        newStage.initModality(Modality.APPLICATION_MODAL);
        newStage.show();

        shareButton.setOnAction(event -> {
            if (!teacherViewModel.isUserValid(emailField.getText())) {
                showAlert(localization.getMessage("teacher.errorAdd1"),
                        localization.getMessage("teacher.errorAdd1.message"));
                return;
            } else if (teacherViewModel.isStudentAdded(classId, emailField.getText())) {
                showAlert(localization.getMessage("teacher.errorAdd2"),
                        localization.getMessage("teacher.errorAdd2.message"));
            } else {
                int result = teacherViewModel.addStudent(classId, emailField.getText());
                if (result != 1) {
                    showAlert(localization.getMessage("teacher.error"),
                            localization.getMessage("teacher.errorAdd.message"));
                } else {
                    classDetailViewModel.loadStudents(classId);
                    updatePage();
                }
            }
            newStage.close();
        });
    }

    /**
     * Sets the ID of the class currently being viewed.
     *
     * @param currentClassId the class ID
     */
    @FXML
    public void setClassId(final int currentClassId) {
        this.classId = currentClassId;
    }

    /**
     * Deletes the specified assigned set from the class.
     *
     * @param viewModel the assigned set to delete
     */
    public void deleteAssignedSet(final AssignedFlashcardSetViewModel viewModel) {
        int result = classDetailViewModel.deleteSet(classId, viewModel);
        if (result == 0) {
            showAlert(localization.getMessage("teacher.error"),
                    localization.getMessage("teacher.errorDelete.message"));
        } else {
            updatePage();
        }
    }

    /**
     * Deletes the specified student from the class.
     *
     * @param viewModel the student to remove
     */
    public void deleteStudent(final StudentViewModel viewModel) {
        String result = classDetailViewModel.deleteStudent(classId, viewModel);
        if (result != null) {
            showAlert(localization.getMessage("teacher.error"), result);
        }
    }

    /**
     * Navigates back to the teacher homepage view.
     */
    public void goBackToTeacherHome() {
        goToPage("/com/flash_card/fxml/teacher-mode.fxml", listStudentsUI.getScene());
    }

    /**
     * Returns the ID of the currently viewed class.
     *
     * @return the class ID
     */
    public int getClassId() {
        return classId;
    }
}
