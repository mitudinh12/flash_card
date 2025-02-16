package com.flash_card.view.teacherMode;

import com.flash_card.framework.SetViewModel;
import com.flash_card.framework.ViewController;
import com.flash_card.view.flashcardSet.FlashcardSetContainer;
import com.flash_card.view_model.entity.EntityManagerViewModel;
import com.flash_card.view_model.flashcard_set.AssignedFlashcardSetViewModel;
import com.flash_card.view_model.teacher_mode.*;
import com.flash_card.view_model.user_auth.AuthSessionViewModel;
import jakarta.persistence.EntityManager;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

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
    private ClassDetailViewModel classDetailViewModel = new ClassDetailViewModel(classId, authSessionViewModel.getVerifiedUserInfo().get("userId"), entityManager);


    @FXML
    private VBox listStudentsUI;

    @FXML
    private VBox listSetsUI;

    @FXML
    private Button backButtonStudent;

    @FXML
    private Button nextButtonStudent;

    @FXML
    private Button backButtonSet;

    @FXML
    private Button nextButtonSet;

    @FXML
    private void initialize() {
        classDetailViewModel.loadStudents();
        studentList = classDetailViewModel.getStudentList();
        classDetailViewModel.loadSets();
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

    private void updatePage() {
        listStudentsUI.getChildren().clear();
        listSetsUI.getChildren().clear();

        int start = currentStudentPage * pageSize;
        int end = Math.min((currentStudentPage + 1) * pageSize, studentList.size());
        for (int i = start; i < end; i++) {
            StudentContainer studentUI = new StudentContainer(studentList.get(i), this);
        }

        start = currentSetPage * pageSize;
        end = Math.min((currentSetPage + 1) * pageSize, setList.size());
        for (int i = start; i < end; i++) {
            SetContainer setUI = new SetContainer(setList.get(i));
        }

        if (currentStudentPage == 0) {
            backButtonStudent.setVisible(false);
        } else {
            backButtonStudent.setVisible(true);
        }

        if (end == studentList.size()) {
            nextButtonStudent.setVisible(false);
        } else {
            nextButtonStudent.setVisible(true);
        }

        if (currentSetPage == 0) {
            backButtonSet.setVisible(false);
        } else {
            backButtonSet.setVisible(true);
        }

        if (end == setList.size()) {
            nextButtonSet.setVisible(false);
        } else {
            nextButtonSet.setVisible(true);
        }

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
    private void handleAssignSet() {}

    @FXML
    private void handleAddStudent() {}

    @FXML
    public void setClassId(int classId) {
        this.classId = classId;
    }

    public void deleteStudent(StudentViewModel viewModel) {
        String result = classDetailViewModel.deleteStudent(viewModel);
        if (result != null) {
            showAlert("Error", result);
        }
    }

}
