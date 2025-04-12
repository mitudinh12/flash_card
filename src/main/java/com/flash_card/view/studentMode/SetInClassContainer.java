package com.flash_card.view.studentMode;

import com.flash_card.framework.SetViewModel;
import com.flash_card.localization.Localization;
import com.flash_card.view.flashcardSet.FlashcardSetContainer;
import com.flash_card.view_model.entity.EntityManagerViewModel;
import com.flash_card.view_model.flashcard_set.ProgressViewModel;
import com.flash_card.view_model.user_auth.AuthSessionViewModel;
import jakarta.persistence.EntityManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.text.MessageFormat;
import java.util.Objects;

/**
 * A container for displaying a flashcard set within a class.
 * Extends the functionality of `FlashcardSetContainer` to include progress tracking.
 */
public class SetInClassContainer extends FlashcardSetContainer {
    /**
     * Spacing between elements in the VBox layout.
     */
    private static final int VBOX_SPACING = 10;

    /**
     * Padding for the VBox layout.
     */
    private static final int VBOX_PADDING = 20;

    /**
     * Width of the progress popup window.
     */
    private static final int POPUP_WIDTH = 500;

    /**
     * Height of the progress popup window.
     */
    private static final int POPUP_HEIGHT = 150;
    /**
     * EntityManager instance for database operations.
     */
    private final EntityManager entityManager = EntityManagerViewModel.getEntityManager();
    /**
     * ViewModel for managing user authentication sessions.
     */
    private final AuthSessionViewModel authSessionViewModel = AuthSessionViewModel.getInstance();
    /**
     * The ID of the authenticated user.
     */
    private final String userId = authSessionViewModel.getVerifiedUserInfo().get("userId");
    /**
     * Localization instance for retrieving localized messages.
     */
    private final Localization localization = Localization.getInstance();
    /**
     * ViewModel for managing the flashcard set.
     */
    private final SetViewModel viewModel;
    /**
     * ViewModel for tracking progress of the flashcard set.
     */
    private final ProgressViewModel progressViewModel = new ProgressViewModel(entityManager);
    /**
     * The ID of the flashcard set.
     */
    private final int setId;
    /**
     * Constructs a new `SetInClassContainer` with the specified flashcard set ViewModel.
     *
     * @param viewModelParam the ViewModel for the flashcard set
     */
    public SetInClassContainer(final SetViewModel viewModelParam) {
        super(viewModelParam, null);
        this.viewModel = viewModelParam;
        this.setId = viewModel.getSet().getSetId();
    }
    /**
     * Displays a popup showing the user's progress for the flashcard set.
     * Includes the number of studied flashcards and the highest quiz percentage.
     */
    @Override
    public void showTrackProgressPopup() {
        Stage progressStage = new Stage();
        progressStage.initModality(Modality.APPLICATION_MODAL);
        progressStage.setTitle(localization.getMessage("student.yourProgress"));

        VBox layout = new VBox(VBOX_SPACING);
        layout.setPadding(new Insets(VBOX_PADDING));
        layout.setAlignment(Pos.CENTER);

        int flashcardsStudied = progressViewModel.getStudiedFlashcards(userId, setId);
        int totalFlashcards = progressViewModel.getTotalFlashcards(setId);
        double quizPercentage = progressViewModel.calculateHighestQuizPercentage(userId, setId);
        String formatedQuizPercentage = localization.getNumberFormat().format(quizPercentage);
        String messageTemplate = localization.getMessage("student.quizHighestCorrectPercentage");
        String localizedQuizMessage = MessageFormat.format(messageTemplate, formatedQuizPercentage);

        Label flashcardsLabel = new Label(String.format(
                localization.getMessage("student.flashcardsStudied"), flashcardsStudied, totalFlashcards));
        Label quizPercentageLabel = new Label(localizedQuizMessage);
        System.out.println("Quiz percentage: " + localizedQuizMessage);
        flashcardsLabel.getStyleClass().add("assign-label");
        quizPercentageLabel.getStyleClass().add("assign-label");

        layout.getChildren().addAll(flashcardsLabel, quizPercentageLabel);

        Scene scene = new Scene(layout, POPUP_WIDTH, POPUP_HEIGHT);
        String css = Objects.requireNonNull(getClass().getResource(
                "/com/flash_card/styles/styles.css").toExternalForm());
        if (!css.isEmpty()) {
            scene.getStylesheets().add(css);
        }
        progressStage.setScene(scene);
        progressStage.showAndWait();
    }
}
