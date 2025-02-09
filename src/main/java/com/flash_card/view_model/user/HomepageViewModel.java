package com.flash_card.view_model.user;

import com.flash_card.model.dao.FlashcardSetDao;
import com.flash_card.model.dao.SharedSetsDao;
import com.flash_card.model.datasource.MariaDbJpaConnection;
import com.flash_card.model.entity.FlashcardSet;
import com.flash_card.model.entity.SharedSet;
import com.flash_card.view_model.flashcard_set.FlashcardSetViewModel;
import com.flash_card.view_model.flashcard_set.OwnFlashcardSetViewModel;
import com.flash_card.framework.SetViewModel;
import com.flash_card.view_model.flashcard_set.SharedFlashcardSetViewModel;
import jakarta.persistence.EntityManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

public class HomepageViewModel {
    private final ObservableList<OwnFlashcardSetViewModel> ownFlashcardList = FXCollections.observableArrayList();
    private final ObservableList<SharedFlashcardSetViewModel> sharedFlashcardList = FXCollections.observableArrayList();
    private final ObservableList<SetViewModel> flashcardList = FXCollections.observableArrayList();
    private FlashcardSetViewModel flashcardSetViewModel;
    private String userId;

    public HomepageViewModel(String userId, EntityManager entityManager) {
        this.userId = userId;
        flashcardSetViewModel = new FlashcardSetViewModel(userId, entityManager);
    }

    public void loadFlashcards(String userId) {
        List<FlashcardSet> ownFlashcardSets = flashcardSetViewModel.findOwnSets();
        List<FlashcardSet> sharedFlashcardSets = flashcardSetViewModel.findSharedSets();
        sharedFlashcardList.clear();
        ownFlashcardList.clear();
        ownFlashcardSets.stream()
                .map(OwnFlashcardSetViewModel::new)
                .forEach(ownFlashcardList::add);
        sharedFlashcardSets.stream()
                .map(SharedFlashcardSetViewModel::new)
                .forEach(sharedFlashcardList::add);

        flashcardList.addAll(ownFlashcardList);
        flashcardList.addAll(sharedFlashcardList);
    }

    public ObservableList<SetViewModel> getFlashcardList() {return flashcardList;}

    public ObservableList<OwnFlashcardSetViewModel> getOwnFlashcardList() {return ownFlashcardList;}

    public ObservableList<SharedFlashcardSetViewModel> getSharedFlashcardList() {return sharedFlashcardList;}

    public void deleteFlashcardSet(SetViewModel viewModel) {
        if (viewModel == null) return;

        if (viewModel.getType().equals("own")) {
            flashcardSetViewModel.deleteOwnFlashcardSet(viewModel.getSet());
            ownFlashcardList.remove(viewModel);
        } else {
            flashcardSetViewModel.deleteSharedFlashcardSet(userId, viewModel.getSet().getSetId());
            sharedFlashcardList.remove(viewModel);
        }
        flashcardList.remove(viewModel);

    }
}
