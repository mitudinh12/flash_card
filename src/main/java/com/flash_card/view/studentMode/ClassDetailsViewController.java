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
        this.className.setText(className);
        this.teacherName.setText(teacherName);
        viewModel.loadClass(classId);
        flashcardList = viewModel.loadSets();
        updatePage();
    }

    private void updatePage() {
        flashcardSetList.getChildren().clear();

        int start = currentPage * pageSize;
        int end = Math.min(start + pageSize, flashcardList.size());

        for (int i = start; i < end; i++) {
            System.out.println(flashcardList.get(i).getSet().getSetName());
            SetInClassContainer flashcardUI = new SetInClassContainer(flashcardList.get(i));

            flashcardSetList.getChildren().add(flashcardUI);
        }
        if (currentPage == 0) {
            backIcon.setVisible(false);
        } else {
            backIcon.setVisible(true);
        }
        if (end >= flashcardList.size()) {
            nextIcon.setVisible(false);
        } else {
            nextIcon.setVisible(true);
        }
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
