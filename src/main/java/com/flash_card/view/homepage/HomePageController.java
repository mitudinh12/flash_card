package com.flash_card.view.homepage;

import com.flash_card.localization.Localization;
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
import java.util.Objects;

import javafx.scene.image.ImageView;

/**
 * Controller for the user homepage view.
 * <p>
 * Manages the display of flashcard sets (owned and shared), pagination,
 * and sharing/deleting functionality. Uses localization for messages
 * and interacts with the authenticated session and database layer.
 */
public class HomePageController extends ViewController {

    /**
     * Logger instance for logging errors and information.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(HomePageController.class);

    /**
     * ViewModel for accessing authenticated user session data.
     */
    private final AuthSessionViewModel authSessionViewModel = AuthSessionViewModel.getInstance();

    /**
     * Entity manager for database interaction.
     */
    private final EntityManager entityManager = EntityManagerViewModel.getEntityManager();

    /**
     * Localization instance for loading localized strings.
     */
    private final Localization localization = Localization.getInstance();

    /**
     * List of the user's owned flashcard sets.
     */
    private List<OwnFlashcardSetViewModel> ownFlashcardList;

    /**
     * List of flashcard sets shared with the user.
     */
    private List<SharedFlashcardSetViewModel> sharedFlashcardList;

    /**
     * Combined list of flashcard sets to display.
     */
    private List<SetViewModel> flashcardList = new ArrayList<>();

    /**
     * Current page index in pagination.
     */
    private int currentPage = 0;

    /**
     * Number of flashcard sets shown per page.
     */
    private final int pageSize = 8;

    /**
     * ViewModel for loading and managing the user's flashcard sets.
     */
    private final HomepageViewModel homepageViewModel = new HomepageViewModel(
            authSessionViewModel.getVerifiedUserInfo().get("userId"),
            entityManager
    );

    /**
     * ViewModel for managing flashcard sharing logic.
     */
    private final SharedSetViewModel sharedSetViewModel = new SharedSetViewModel(
            authSessionViewModel.getVerifiedUserInfo().get("userId"),
            entityManager
    );

    /**
     * Padding size (in pixels) for the share dialog layout.
     */
    private static final int SHARE_DIALOG_PADDING = 20;

    /**
     * Vertical spacing (in pixels) between components in the share dialog layout.
     */
    private static final int SHARE_DIALOG_SPACING = 10;

    /**
     * Label displaying the logged-in user's name.
     */
    @FXML
    private Label userName;

    /**
     * Button to log out of the session.
     */
    @FXML
    private Button logoutButton;

    /**
     * Button to go to the previous flashcard page.
     */
    @FXML
    private Button backButton;

    /**
     * Button to go to the next flashcard page.
     */
    @FXML
    private Button nextButton;

    /**
     * Icon displayed on the back button.
     */
    @FXML
    private ImageView backIcon;

    /**
     * Icon displayed on the next button.
     */
    @FXML
    private ImageView nextIcon;

    /**
     * VBox container that holds all displayed flashcard sets.
     */
    @FXML
    private VBox listFlashcards;

    /**
     * HBox container wrapping flashcard UI.
     */
    @FXML
    private HBox flashcardSetContainer;

    /**
     * Initializes the homepage, loads flashcards, sets user info, and adds list change listener.
     */
    @FXML
    private void initialize() {
        setReloadFxml("/com/flash_card/fxml/home.fxml");
        setUserName();
        homepageViewModel.loadFlashcards(authSessionViewModel.getVerifiedUserInfo().get("userId"));
        flashcardList = homepageViewModel.getFlashcardList();

        homepageViewModel.getFlashcardList().addListener((ListChangeListener<SetViewModel>) change -> {
            while (change.next()) {
                if (change.wasRemoved() || change.wasAdded()) {
                    resetPageToFirst();
                }
            }
        });

        updatePage();
    }

    /**
     * Updates the flashcard list UI based on the current page.
     */
    private void updatePage() {
        listFlashcards.getChildren().clear();

        int start = currentPage * pageSize;
        int end = Math.min(start + pageSize, flashcardList.size());

        for (int i = start; i < end; i++) {
            FlashcardSetContainer flashcardUI = new FlashcardSetContainer(flashcardList.get(i), this);
            listFlashcards.getChildren().add(flashcardUI);
        }

        backIcon.setVisible(currentPage > 0);
        nextIcon.setVisible(end < flashcardList.size());
    }

    /**
     * Navigates to the next flashcard page.
     *
     * @param event the action event
     */
    @FXML
    private void goNext(final ActionEvent event) {
        if ((currentPage + 1) * pageSize < flashcardList.size()) {
            currentPage++;
            updatePage();
        }
    }

    /**
     * Navigates to the previous flashcard page.
     *
     * @param event the action event
     */
    @FXML
    private void goBack(final ActionEvent event) {
        if (currentPage > 0) {
            currentPage--;
            updatePage();
        }
    }

    /**
     * Resets pagination to the first page and updates the UI.
     */
    private void resetPageToFirst() {
        currentPage = 0;
        updatePage();
    }

    /**
     * Deletes the selected flashcard set.
     *
     * @param setViewModel the flashcard set to delete
     */
    public void deleteFlashcardSet(final SetViewModel setViewModel) {
        if (setViewModel == null) {
            return;
        }
        homepageViewModel.deleteFlashcardSet(setViewModel);
    }

    /**
     * Opens a modal to share a flashcard set with another user via email.
     *
     * @param setId the ID of the set to be shared
     */
    public void handleShare(final int setId) {
        Stage newStage = new Stage();
        newStage.setTitle(localization.getMessage("home.TitleShare"));
        VBox layout = new VBox();
        layout.getStyleClass().add("layout-check-boxes");
        layout.alignmentProperty().set(javafx.geometry.Pos.CENTER);
        layout.setPadding(new Insets(SHARE_DIALOG_PADDING));
        layout.setSpacing(SHARE_DIALOG_SPACING);

        Label emailLabel = new Label(localization.getMessage("home.shareLabel"));
        emailLabel.getStyleClass().add("assign-label");

        TextField emailField = new TextField();
        emailField.setId("email-field");

        Button shareButton = new Button(localization.getMessage("home.shareButton"));
        shareButton.getStyleClass().add("confirm-assign-button");

        layout.getChildren().addAll(emailLabel, emailField, shareButton);
        Scene scene = new Scene(layout);
        String css = "";
        try {
            css = Objects.requireNonNull(getClass().getResource("/com/flash_card/styles/styles.css").toExternalForm());
            if (!css.isEmpty()) {
                scene.getStylesheets().add(css);
            }
        } catch (NullPointerException e) {
            LOGGER.error("Error loading CSS file: {}", e.getMessage());
        }
        if (!css.isEmpty()) {
            scene.getStylesheets().add(css);
        }

        newStage.setScene(scene);
        newStage.initModality(Modality.APPLICATION_MODAL);
        newStage.show();

        shareButton.setOnAction(event -> {
            if (!sharedSetViewModel.isUserValid(emailField.getText())) {
                showAlert(localization.getMessage("home.invalid.email"),
                        localization.getMessage("home.invalid.email.message"));
                return;
            } else if (sharedSetViewModel.isUserAndSetShared(emailField.getText(), setId)) {
                showAlert(localization.getMessage("home.invalid.sharing"),
                        localization.getMessage("home.invalid.sharing.message"));
            } else {
                sharedSetViewModel.saveSharedFlashcardSet(emailField.getText(), setId);
                showAlert(localization.getMessage("home.share.success"),
                        localization.getMessage("home.share.success.message"));
            }
            newStage.close();
        });
    }
}
