package com.flash_card.view.teacherMode;

import com.flash_card.framework.ViewController;
import com.flash_card.model.entity.FlashcardSet;
import com.flash_card.view_model.entity.EntityManagerViewModel;
import com.flash_card.view_model.flashcard_set.AssignedFlashcardSetViewModel;
import com.flash_card.view_model.teacher_mode.*;
import com.flash_card.view_model.user_auth.AuthSessionViewModel;
import jakarta.persistence.EntityManager;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ClassDetailController extends ViewController {
    private AuthSessionViewModel authSessionViewModel = AuthSessionViewModel.getInstance();
    private EntityManager entityManager = EntityManagerViewModel.getEntityManager();
    private TeacherViewModel teacherViewModel = new TeacherViewModel(authSessionViewModel.getVerifiedUserInfo().get("userId"), entityManager);
    private List<StudentViewModel> studentList = new ArrayList<>();
    private List<AssignedFlashcardSetViewModel> setList = new ArrayList<>();
    private int classId;
    private int currentStudentPage = 0;
    private int currentSetPage = 0;
    private final int pageSize = 4;
    private ClassDetailViewModel classDetailViewModel = new ClassDetailViewModel(authSessionViewModel.getVerifiedUserInfo().get("userId"), entityManager);


    @FXML
    private VBox listStudentsUI;

    @FXML
    private Label className;

    @FXML
    private VBox listSetsUI;

    @FXML
    private ImageView backButtonStudent;

    @FXML
    private ImageView nextButtonStudent;

    @FXML
    private ImageView backButtonSet;

    @FXML
    private ImageView nextButtonSet;

    public void initializeUI() {
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

    public void setClassName(String name) {
        className.setText(name);
    }

    private void updatePage() {
        listStudentsUI.getChildren().clear();
        listSetsUI.getChildren().clear();

        int studentStart = currentStudentPage * pageSize;
        int studentEnd = Math.min((currentStudentPage + 1) * pageSize, studentList.size());
        for (int i = studentStart; i < studentEnd; i++) {
            StudentContainer studentUI = new StudentContainer(studentList.get(i), this);
            listStudentsUI.getChildren().add(studentUI);
        }

        int setStart = currentSetPage * pageSize;
        int setEnd = Math.min((currentSetPage + 1) * pageSize, setList.size());
        for (int i = setStart; i < setEnd; i++) {
            SetContainer setUI = new SetContainer(setList.get(i), this);
            listSetsUI.getChildren().add(setUI);
        }

        if (currentStudentPage == 0) {
            backButtonStudent.setVisible(false);
        } else {
            backButtonStudent.setVisible(true);
        }

        if (studentEnd == studentList.size()) {
            nextButtonStudent.setVisible(false);
        } else {
            nextButtonStudent.setVisible(true);
        }

        if (currentSetPage == 0) {
            backButtonSet.setVisible(false);
        } else {
            backButtonSet.setVisible(true);
        }

        if (setEnd == setList.size()) {
            nextButtonSet.setVisible(false);
        } else {
            nextButtonSet.setVisible(true);
        }
        System.out.println(backButtonSet.isVisible());

    }

    @FXML
    private void goNextStudent(ActionEvent event) {
        if ((currentStudentPage + 1) * pageSize < studentList.size()) {
            currentStudentPage++;
            updatePage();
        }
    }

    @FXML
    private void goBackStudent(ActionEvent event) {
        if (currentStudentPage > 0) {
            currentStudentPage--;
            updatePage();
        }
    }

    @FXML
    private void goNextSet(ActionEvent event) {
        if ((currentSetPage + 1) * pageSize < setList.size()) {
            currentSetPage++;
            updatePage();
        }
    }

    @FXML
    private void goBackSet(ActionEvent event) {
        if (currentSetPage > 0) {
            currentSetPage--;
            updatePage();
        }
    }

    private void resetStudentPageToFirst() {
        currentStudentPage = 0;
        updatePage();
    }

    private void resetSetPageToFirst() {
        currentSetPage = 0;
        updatePage();
    }

    @FXML
    private void gotoTrackProgress() {}

    @FXML
    private void handleAssignSet() {
        Stage addSetStage = new Stage();
        addSetStage.initModality(Modality.APPLICATION_MODAL);
        addSetStage.setTitle("Assign Flashcard Set");

        VBox layout = new VBox();
        layout.getStyleClass().add("layout-check-boxes");
        layout.setPadding(new Insets(20));

        Label label = new Label("Select the flashcard sets you want to assign");
        label.getStyleClass().add("assign-label");
        layout.getChildren().add(label);

        VBox checkboxes = new VBox();
        checkboxes.setPadding(new Insets(15));
        layout.getChildren().add(checkboxes);

        List<Pair<Integer, CheckBox>> checkBoxes = new ArrayList<>();

        for (FlashcardSet set : teacherViewModel.getAllUnassignedSetsByClassIdAndTeacherId(classId)) {
            String content = set.getSetName() + " - " + set.getSetTopic() + " - " + set.getNumberFlashcards() + " flashcards";
            CheckBox checkBox = new CheckBox(content);
            checkBoxes.add(new Pair<>(set.getSetId(), checkBox));
            checkBox.getStyleClass().add("check-box");
            checkboxes.getChildren().add(checkBox);
        }

        Button assignButton = new Button("Assign");
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
                showAlert("Error", "Error in assigning flashcard sets");
            } else {
                updatePage();
            }

        });
        layout.getChildren().add(assignButton);

        Scene scene = new Scene(layout);
        String css = Objects.requireNonNull(getClass().getResource("/com/flash_card/styles/styles.css").toExternalForm());
        if (!css.isEmpty()) {
            scene.getStylesheets().add(css);
        }
        addSetStage.setScene(scene);
        addSetStage.showAndWait();
    }

    @FXML
    private void handleAddStudent() {
        Stage newStage = new Stage();
        newStage.setTitle("ADD NEW STUDENT");
        VBox layout = new VBox();
        layout.getStyleClass().add("layout-check-boxes");
        layout.alignmentProperty().set(javafx.geometry.Pos.CENTER);
        layout.setPadding(new Insets(20));
        layout.setSpacing(10);

        Label emailLabel = new Label("Enter student email");
        emailLabel.getStyleClass().add("assign-label");

        TextField emailField = new TextField();
        emailField.setId("email-field");

        Button shareButton = new Button("Add Student");
        shareButton.getStyleClass().add("confirm-assign-button");

        layout.getChildren().addAll(emailLabel, emailField, shareButton);
        Scene scene = new Scene(layout);
        String css = Objects.requireNonNull(getClass().getResource("/com/flash_card/styles/styles.css").toExternalForm());
        if (!css.isEmpty()) {
            scene.getStylesheets().add(css);
        }
        newStage.setScene(scene);
        newStage.initModality(Modality.APPLICATION_MODAL);
        newStage.show();

        shareButton.setOnAction(event -> {
            if (!teacherViewModel.isUserValid(emailField.getText())) {
                showAlert("Invalid email", "The email you entered is not valid. Please try again.");
                return;
            } else if (teacherViewModel.isStudentAdded(classId, emailField.getText())) {
                showAlert("Invalid sharing","This student is already added to this class");
            }
            else {
                int result = teacherViewModel.addStudent(classId, emailField.getText());
                if (result != 1) {
                    showAlert("Error", "Error in adding student to class");
                } else {
                    classDetailViewModel.loadStudents(classId);
                    updatePage();
                }
            }
            newStage.close();
        });
    }

    @FXML
    public void setClassId(int classId) {
        this.classId = classId;
    }

    public void deleteAssignedSet(AssignedFlashcardSetViewModel viewModel) {
        int result = classDetailViewModel.deleteSet(classId, viewModel);
        if (result == 0) {
            showAlert("Error", "Error in deleting flashcard set");
        }  else {
            updatePage();
        }
    }

    public void deleteStudent(StudentViewModel viewModel) {
        String result = classDetailViewModel.deleteStudent(classId, viewModel);
        if (result != null) {
            showAlert("Error", result);
        }
    }

    public void goBackToTeacherHome() {
        goToPage("/com/flash_card/fxml/teacher-mode.fxml", listStudentsUI.getScene());
    }

    public int getClassId() {
        return classId;
    }

}
