package com.flash_card.view.studentMode;

import com.flash_card.framework.SetViewModel;
import com.flash_card.framework.ViewController;
import com.flash_card.view_model.entity.EntityManagerViewModel;
import com.flash_card.view_model.student_mode.ClassDetailsViewModel;
import com.flash_card.view_model.user_auth.AuthSessionViewModel;
import jakarta.persistence.EntityManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;

public class ClassDetailsViewController extends ViewController {
    private EntityManager entityManager = EntityManagerViewModel.getEntityManager();
    private final AuthSessionViewModel authSessionViewModel = AuthSessionViewModel.getInstance();
    private ClassDetailsViewModel viewModel = new ClassDetailsViewModel(entityManager);
    private int classId;

    private int currentPage = 0;
    private final int pageSize = 8;
    private List<SetViewModel> flashcardList = new ArrayList<>();

    @FXML
    private Label className;
    @FXML
    private Text teacherName;
    @FXML
    private VBox flashcardSetList;
    @FXML
    private ImageView backIcon, nextIcon;

    public void loadClass(int classId, String className, String teacherName) {
        this.classId = classId;
        setClassDetails(className, teacherName);
        loadFlashcardSets();
        updateNavigationControls();
    }

    private void setClassDetails(String className, String teacherName) {
        this.className.setText(className);
        this.teacherName.setText(teacherName);
    }

    private void loadFlashcardSets() {
        viewModel.loadClass(classId);
        flashcardList = viewModel.loadSets();
        updatePage();
    }

    private void updatePage() {
        flashcardSetList.getChildren().clear();
        populateFlashcardList();
        updateNavigationControls();
    }

    private void populateFlashcardList() {
        int start = currentPage * pageSize;
        int end = Math.min(start + pageSize, flashcardList.size());

        for (int i = start; i < end; i++) {
            flashcardSetList.getChildren().add(new SetInClassContainer(flashcardList.get(i)));
        }
    }

    private void updateNavigationControls() {
        backIcon.setVisible(currentPage > 0);
        nextIcon.setVisible((currentPage + 1) * pageSize < flashcardList.size());
    }

    @FXML
    private void goNext(ActionEvent event) {
        if ((currentPage + 1) * pageSize < flashcardList.size()) {
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
}

