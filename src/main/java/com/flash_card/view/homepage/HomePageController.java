package com.flash_card.view.homepage;

import com.flash_card.model.dao.FlashcardSetDao;
import com.flash_card.model.dao.SharedSetsDao;
import com.flash_card.view.flashcardSet.FlashcardSetContainer;
import com.flash_card.framework.ViewController;
import com.flash_card.view_model.entity.EntityManagerViewModel;
import com.flash_card.view_model.flashcard_set.OwnFlashcardSetViewModel;
import com.flash_card.framework.SetViewModel;
import com.flash_card.view_model.flashcard_set.SharedFlashcardSetViewModel;
import com.flash_card.view_model.flashcard_set.SharedSetViewModel;
import com.flash_card.view_model.user.HomepageViewModel;
import com.flash_card.view_model.user_auth.AuthSessionViewModel;
import jakarta.persistence.EntityManager;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.image.ImageView;


public class HomePageController extends ViewController {
    private static final Logger log = LoggerFactory.getLogger(HomePageController.class);
    private final AuthSessionViewModel authSessionViewModel = AuthSessionViewModel.getInstance();
    private EntityManager entityManager = EntityManagerViewModel.getEntityManager();
    private List<OwnFlashcardSetViewModel> ownFlashcardList;
    private List<SharedFlashcardSetViewModel> sharedFlashcardList;
    private List<SetViewModel> flashcardList = new ArrayList<>();
    private int currentPage = 0;
    private final int pageSize = 8;
    private HomepageViewModel homepageViewModel = new HomepageViewModel(authSessionViewModel.getVerifiedUserInfo().get("userId"), entityManager);
    private SharedSetViewModel sharedSetViewModel = new SharedSetViewModel(authSessionViewModel.getVerifiedUserInfo().get("userId"), entityManager);

    @FXML
    protected Label userName;

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
        setUserName();
        homepageViewModel.loadFlashcards(authSessionViewModel.getVerifiedUserInfo().get("userId"));
        flashcardList = homepageViewModel.getFlashcardList();

        // listen for changes in flashcardList to automatically update UI
        homepageViewModel.getFlashcardList().addListener((ListChangeListener<SetViewModel>) change -> {
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

    public void deleteFlashcardSet(SetViewModel setViewModel) {
        if (setViewModel == null) return;
        homepageViewModel.deleteFlashcardSet(setViewModel);
    }

    public void handleShare(int setId ) {
        Stage newStage = new Stage();
        newStage.setTitle("FLASHCARDS SHARING");
        HBox hBox = new HBox();
        hBox.setPadding(new Insets(10, 10, 10, 10));

        Label emailLabel = new Label("Enter email: ");
        TextField emailField = new TextField();
        Button shareButton = new Button("Share");

        hBox.getChildren().addAll(emailLabel, emailField, shareButton);
        Scene scene = new Scene(hBox);
        newStage.setScene(scene);
        newStage.initModality(Modality.APPLICATION_MODAL);
        newStage.show();

        shareButton.setOnAction(event -> {
            if (!sharedSetViewModel.isUserValid(emailField.getText())) {
                showAlert("Invalid email", "The email you entered is not valid. Please try again.");
                return;
            } else if (sharedSetViewModel.isUserAndSetShared(emailField.getText(), setId)) {
                showAlert("Invalid sharing","This Flashcard set is already shared with this user");
            }
            else {
                sharedSetViewModel.saveSharedFlashcardSet(emailField.getText(), setId);
                showAlert("Success", "The flashcard set has been shared successfully!");
            }
            newStage.close();
        });

    }
}
