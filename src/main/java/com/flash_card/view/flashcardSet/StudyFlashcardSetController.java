package com.flash_card.view.flashcardSet;

import com.flash_card.framework.ViewController;
import com.flash_card.view.flashcard.FlashcardView;
import com.flash_card.view_model.flashcard_set.StudyFlashcardSetViewModel;
import com.flash_card.view_model.entity.EntityManagerViewModel;
import com.flash_card.framework.DifficultyLevel;
import jakarta.persistence.EntityManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 * Controller for managing the study flashcard set view.
 * Handles the logic for displaying flashcards, updating difficulty levels, and navigating between flashcards.
 */
public class StudyFlashcardSetController extends ViewController {
    /**
     * EntityManager instance for database operations.
     */
    private final EntityManager entityManager = EntityManagerViewModel.getEntityManager();

    /**
     * ViewModel for managing study flashcard set data and logic.
     */
    protected final StudyFlashcardSetViewModel viewModel = new StudyFlashcardSetViewModel(entityManager);

    /**
     * Singleton instance of the StudySession.
     */
    protected StudySession session = StudySession.getInstance();

    /**
     * String for the highlighted button class.
     */
    private static final String HIGHLIGHTED_BUTTON_CLASS = "highlighted-button";

    /**
     * Label for displaying the current index of the flashcard being studied.
     */
    @FXML protected Label index;

    /**
     * Label for displaying the total number of flashcards in the set.
     */
    @FXML protected Label total;

    /**
     * Label for displaying the name of the flashcard set.
     */
    @FXML protected Label setName;

    /**
     * StackPane for navigating to the previous flashcard.
     */
    @FXML private StackPane backIcon;

    /**
     * StackPane for navigating to the next flashcard.
     */
    @FXML private StackPane nextIcon;

    /**
     * VBox container for displaying the flashcard content.
     */
    @FXML private VBox flashcardContainer;

    /**
     * Button for marking the current flashcard as "easy".
     */
    @FXML private Button easyButton;

    /**
     * Button for marking the current flashcard as "hard".
     */
    @FXML private Button hardButton;

    /**
     * Text element for displaying additional information or instructions.
     */
    @FXML private Text middleText;

    /**
     * Text element for displaying study instructions.
     */
    @FXML private Text instructionText;

    /**
     * ImageView for the shuffle icon, used to shuffle the flashcards.
     */
    @FXML protected ImageView shuffleIcon;

    /**
     * Initializes the controller and sets up the study flashcard set view.
     */
    @FXML
    public void initialize() {
        setReloadFxml("/com/flash_card/fxml/study-flashcard.fxml");
        setName.textProperty().bind(viewModel.setNameProperty());
        total.textProperty().bind(viewModel.totalProperty());
        index.textProperty().bind(viewModel.currentIndexProperty().add(1).asString());
        viewModel.startStudy(authSessionViewModel.getVerifiedUserInfo().get("userId"), session.getSetId());
        viewModel.loadFlashcards(session.getSetId(), session.getSetName());
        showFlashcard();
    }

    /**
     * Displays the current flashcard in the flashcard container.
     * Updates the visibility of controls based on the number of flashcards.
     */
    protected void showFlashcard() {
        flashcardContainer.getChildren().clear();
        boolean flashcardsEmpty = viewModel.getFlashcards().isEmpty();
        setFlashcardControlsVisibility(!flashcardsEmpty); //hide controls if no flashcards
        if (flashcardsEmpty) {
            Label messageLabel = new Label(localization.getMessage("flashcardSet.reStudy"));
            messageLabel.getStyleClass().add("message-label");
            flashcardContainer.setAlignment(Pos.TOP_CENTER);
            flashcardContainer.getChildren().add(messageLabel);
        } else {
            FlashcardView flashcardView = new FlashcardView(
                    viewModel.getCurrentFlashcard().getTerm(),
                    viewModel.getCurrentFlashcard().getDefinition()
            );
            flashcardContainer.getChildren().add(flashcardView);
            updateButtonStates();
        }
    }

    /**
     * Sets the visibility of flashcard controls based on the provided parameter.
     *
     * @param visible true to show controls, false to hide them
     */
    private void setFlashcardControlsVisibility(final boolean visible) {
        index.setVisible(visible);
        total.setVisible(visible);
        backIcon.setVisible(visible);
        nextIcon.setVisible(visible);
        easyButton.setVisible(visible);
        hardButton.setVisible(visible);
        middleText.setVisible(visible);
        shuffleIcon.setVisible(visible);
        instructionText.setVisible(visible);
    }

    /**
     * Updates the states of the buttons based on the current flashcard index and difficulty level.
     */
    private void updateButtonStates() {
        backIcon.setVisible(viewModel.currentIndexProperty().get() != 0);
        nextIcon.setVisible(true);
        easyButton.getStyleClass().remove(HIGHLIGHTED_BUTTON_CLASS);
        hardButton.getStyleClass().remove(HIGHLIGHTED_BUTTON_CLASS);
        DifficultyLevel difficultyLevel = viewModel.getCurrentFlashcardDifficultLevel();
        if (difficultyLevel == DifficultyLevel.easy) {
            highlightButton(easyButton);
        } else if (difficultyLevel == DifficultyLevel.hard) {
            highlightButton(hardButton);
        }
    }

    /**
     * Highlights the specified button by adding a CSS style class.
     *
     * @param button the button to highlight
     */
    private void highlightButton(final Button button) {
        button.getStyleClass().add(HIGHLIGHTED_BUTTON_CLASS);
    }

    /**
     * Handles the event when the user clicks on the close icon.
     * Ends the study session and navigates to the review page.
     *
     * @param mouseEvent the mouse event triggered by clicking the icon
     */
    @FXML
    public void handleClose(final MouseEvent mouseEvent) {
        viewModel.endStudy(); //end the study when press close and go to review page
        session.setViewModel(viewModel);
        goToReviewPage();
    }

    /**
     * Handles the event when the user clicks on the next flashcard icon.
     * If there are more flashcards, it loads the next one; otherwise, it ends the study.
     *
     * @param mouseEvent the mouse event triggered by clicking the icon
     */
    @FXML
    public void handleNext(final MouseEvent mouseEvent) {
        if (viewModel.currentIndexProperty().get() < Integer.parseInt(viewModel.totalProperty().get()) - 1) {
            viewModel.nextFlashcard();
            showFlashcard();
        } else {
            viewModel.endStudy(); //end the study when after the last card
            session.setViewModel(viewModel);
            goToReviewPage();
        }
    }

    /**
     * Handles the event when the user clicks on the previous flashcard icon.
     * Loads the previous flashcard if available.
     *
     * @param mouseEvent the mouse event triggered by clicking the icon
     */
    @FXML
    public void handleBack(final MouseEvent mouseEvent) {
        viewModel.previousFlashcard();
        showFlashcard();
    }

    /**
     * Handles the event when the user clicks on the "easy" button.
     * Updates the difficulty level of the current flashcard to "easy".
     *
     * @param actionEvent the action event triggered by clicking the button
     */
    @FXML
    public void handleEasy(final ActionEvent actionEvent) {
        viewModel.updateFlashcardLevel(DifficultyLevel.easy);
        updateButtonStates();
    }

    /**
     * Handles the event when the user clicks on the "hard" button.
     * Updates the difficulty level of the current flashcard to "hard".
     *
     * @param actionEvent the action event triggered by clicking the button
     */
    @FXML
    public void handleHard(final ActionEvent actionEvent) {
        viewModel.updateFlashcardLevel(DifficultyLevel.hard);
        updateButtonStates();
    }

    /**
     * Handles the event when the user clicks on the "reset" button.
     * Resets all flashcard levels and reloads the flashcards.
     *
     * @param mouseEvent the mouse event triggered by clicking the button
     */
    @FXML
    public void handleReset(final MouseEvent mouseEvent) {
        viewModel.resetAllFlashcardLevel();
        viewModel.currentIndexProperty().set(0);
        viewModel.loadFlashcards(session.getSetId(), viewModel.setNameProperty().get());
        showFlashcard();
    }

    /**
     * Handles the event when the user clicks on the "shuffle" icon.
     * Shuffles the flashcards and resets the index to show the first card.
     *
     * @param mouseEvent the mouse event triggered by clicking the icon
     */
    @FXML
    public void handleShuffle(final MouseEvent mouseEvent) {
        viewModel.shuffleFlashcards(); //shuffle cards and reset the index to show the first card
        showFlashcard(); //refresh the display to show the shuffled flashcards
    }

    /**
     * Navigates to the review page after ending the study session.
     */
    protected void goToReviewPage() {
        goToPage("/com/flash_card/fxml/review-flashcard.fxml", easyButton.getScene());
    }
}
