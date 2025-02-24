package com.flash_card.view.studentMode;

import com.flash_card.framework.SetViewModel;
import com.flash_card.view.flashcardSet.FlashcardSetContainer;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;

public class SetInClassContainer extends FlashcardSetContainer {
    private SetViewModel viewModel;
    public SetInClassContainer(SetViewModel viewModel) {
        super(viewModel, null);
        this.viewModel = viewModel;
    }

    @Override

    @Override
    public void showContextMenu(Button button) {
        ContextMenu menu = new ContextMenu();

        MenuItem study = new MenuItem("Study");
        study.setOnAction(event -> {
            gotoStudyFlashcardSet();
        });

        MenuItem quiz = new MenuItem("Quiz");
        quiz.setOnAction(e -> {
            goToQuizFlashcardSet();
        });
    }
}
