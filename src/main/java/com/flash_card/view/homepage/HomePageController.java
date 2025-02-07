package com.flash_card.view.homepage;

import com.flash_card.framework.FlashcardSetContainer;
import com.flash_card.view.auth.LoginView;
import com.flash_card.framework.ViewController;
import com.flash_card.view_model.flashcard_set.DisplayFlashcardSetViewModel;
import com.flash_card.view_model.user.HomepageViewModel;
import com.flash_card.view_model.user_auth.AuthSessionViewModel;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import javafx.scene.image.ImageView;


public class HomePageController extends ViewController {
    private static final Logger log = LoggerFactory.getLogger(HomePageController.class);
    private final AuthSessionViewModel authSessionViewModel = AuthSessionViewModel.getInstance();
    private List<DisplayFlashcardSetViewModel> flashcardList;
    private int currentPage = 0;
    private final int pageSize = 3;
    private HomepageViewModel homepageViewModel = new HomepageViewModel(authSessionViewModel.getVerifiedUserInfo().get("userId"));

    @FXML
    private Label userName;

    @FXML
    private Button logoutButton;

    @FXML
    private Button backButton;

    @FXML
    private Button nextButton;

    @FXML
    private ImageView backIcon;

    @FXML
    private ImageView nextIcon;

    @FXML
    private VBox listFlashcards;

    @FXML
    private void initialize() {
        setUserName(authSessionViewModel.getVerifiedUserInfo().get("firstName"));
        flashcardList = homepageViewModel.getFlashcardList();
        updatePage();

        // listen for changes in flashcardList to automatically update UI
        homepageViewModel.getFlashcardList().addListener((ListChangeListener<DisplayFlashcardSetViewModel>) change -> {
            while (change.next()) {
                if (change.wasRemoved() || change.wasAdded()) {
                    resetPageToFirst();
                }
            }
        });

        updatePage();
    }

    @FXML
    private HBox flashcardSetContainer;

    public void setUserName(String name) {
        userName.setText("Hi, " + name);
    }

    private void updatePage() {
        listFlashcards.getChildren().clear();

        int start = currentPage * pageSize;
        int end = Math.min(start + pageSize, flashcardList.size());

        for (int i = start; i < end; i++) {
            FlashcardSetContainer flashcardUI = new FlashcardSetContainer(flashcardList.get(i), this);
            listFlashcards.getChildren().add(flashcardUI);
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

    private void resetPageToFirst() {
        currentPage = 0;
        updatePage();
    }

    public void deleteFlashcardSet(DisplayFlashcardSetViewModel flashcardSetViewModel) {
        if (flashcardSetViewModel == null) return;
        homepageViewModel.deleteFlashcardSet(flashcardSetViewModel);
    }
}
