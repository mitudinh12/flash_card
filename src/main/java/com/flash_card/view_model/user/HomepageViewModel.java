package com.flash_card.view_model.user;

import com.flash_card.model.dao.FlashcardSetDao;
import com.flash_card.model.entity.FlashcardSet;
import com.flash_card.view_model.flashcard_set.DisplayFlashcardSetViewModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.List;

public class HomepageViewModel {
    private final FlashcardSetDao flashcardSetDAO = FlashcardSetDao.getInstance();
    private final ObservableList<DisplayFlashcardSetViewModel> flashcardList = FXCollections.observableArrayList();

    public HomepageViewModel(String userId) {
        loadFlashcards(userId);
    }

    private void loadFlashcards(String userId) {
        List<FlashcardSet> entities = flashcardSetDAO.findAllSetsByCreatorId(userId);

        flashcardList.clear();
        entities.stream()
                .map(DisplayFlashcardSetViewModel::new)
                .forEach(flashcardList::add);
    }

    public ObservableList<DisplayFlashcardSetViewModel> getFlashcardList() {
        return flashcardList;
    }

    public void deleteFlashcardSet( DisplayFlashcardSetViewModel viewModel) {
        if (viewModel == null) return;

        flashcardSetDAO.delete(viewModel.getSet());

        flashcardList.remove(viewModel);
    }
}
