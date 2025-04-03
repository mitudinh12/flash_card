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
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.text.MessageFormat;
import java.util.Objects;

public class SetInClassContainer extends FlashcardSetContainer {
    private SetViewModel viewModel;
    private final EntityManager entityManager = EntityManagerViewModel.getEntityManager();
    private ProgressViewModel progressViewModel = new ProgressViewModel(entityManager);
    private final AuthSessionViewModel authSessionViewModel = AuthSessionViewModel.getInstance();
    private final String userId = authSessionViewModel.getVerifiedUserInfo().get("userId");
    private int setId;
    private final Localization localization = Localization.getInstance();

    public SetInClassContainer(SetViewModel viewModel) {
        super(viewModel, null);
        this.viewModel = viewModel;
        this.setId = viewModel.getSet().getSetId();
    }

    @Override
    public void showTrackProgressPopup() {
        Stage progressStage = new Stage();
        progressStage.initModality(Modality.APPLICATION_MODAL);
        progressStage.setTitle(localization.getMessage("student.yourProgress"));

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);

        int flashcardsStudied = progressViewModel.getStudiedFlashcards(userId, setId);
        int totalFlashcards = progressViewModel.getTotalFlashcards(setId);
        double quizPercentage = progressViewModel.calculateHighestQuizPercentage(userId, setId);
        String formatedQuizPercentage = localization.getNumberFormat().format(quizPercentage);
        String messageTemplate = localization.getMessage("student.quizHighestCorrectPercentage");
        String localizedQuizMessage = MessageFormat.format(messageTemplate, formatedQuizPercentage);

        Label flashcardsLabel = new Label(String.format(localization.getMessage("student.flashcardsStudied"), flashcardsStudied, totalFlashcards));
        Label quizPercentageLabel = new Label(localizedQuizMessage);
        System.out.println("Quiz percentage: " + localizedQuizMessage);
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
