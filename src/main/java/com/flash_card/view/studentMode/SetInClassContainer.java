package com.flash_card.view.studentMode;

import com.flash_card.framework.SetViewModel;
import com.flash_card.view.flashcardSet.FlashcardSetContainer;
import com.flash_card.view_model.entity.EntityManagerViewModel;
import com.flash_card.view_model.flashcard_set.ProgressViewModel;
import com.flash_card.view_model.user_auth.AuthSessionViewModel;
import jakarta.persistence.EntityManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.Objects;

public class SetInClassContainer extends FlashcardSetContainer {
    private SetViewModel viewModel;
    private final EntityManager entityManager = EntityManagerViewModel.getEntityManager();
    private ProgressViewModel progressViewModel = new ProgressViewModel(entityManager);
    private final AuthSessionViewModel authSessionViewModel = AuthSessionViewModel.getInstance();
    private final String userId = authSessionViewModel.getVerifiedUserInfo().get("userId");
    private int setId;
    public SetInClassContainer(SetViewModel viewModel) {
        super(viewModel, null);
        this.viewModel = viewModel;
        this.setId = viewModel.getSet().getSetId();
    }

    @Override
    public void showTrackProgressPopup() {
        Stage progressStage = new Stage();
        progressStage.initModality(Modality.APPLICATION_MODAL);
        progressStage.setTitle("Your Progress");

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);

        int flashcardsStudied = progressViewModel.getStudiedFlashcards(userId, setId);
        int totalFlashcards = progressViewModel.getTotalFlashcards(setId);
        double quizPercentage = progressViewModel.calculateHighestQuizPercentage(userId, setId);

        Label flashcardsLabel = new Label(String.format("Flashcards studied: %d/%d", flashcardsStudied, totalFlashcards));
        Label quizPercentageLabel = new Label(String.format("Quiz highest correct percentage: %.2f%%", quizPercentage));
        flashcardsLabel.getStyleClass().add("assign-label");
        quizPercentageLabel.getStyleClass().add("assign-label");

        layout.getChildren().addAll(flashcardsLabel, quizPercentageLabel);

        Scene scene = new Scene(layout, 500, 150);
        String css = Objects.requireNonNull(getClass().getResource("/com/flash_card/styles/styles.css").toExternalForm());
        if (!css.isEmpty()) {
            scene.getStylesheets().add(css);
        }
        progressStage.setScene(scene);
        progressStage.showAndWait();
    }
}
