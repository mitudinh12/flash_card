package com.flash_card.view_model.user;

import com.flash_card.model.dao.FlashcardSetDao;
import com.flash_card.model.dao.SharedSetsDao;
import com.flash_card.model.datasource.MariaDbJpaConnection;
import com.flash_card.model.entity.FlashcardSet;
import com.flash_card.model.entity.SharedSet;
import com.flash_card.view_model.flashcard_set.OwnFlashcardSetViewModel;
import com.flash_card.framework.SetViewModel;
import com.flash_card.view_model.flashcard_set.SharedFlashcardSetViewModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

public class HomepageViewModel {
    private final FlashcardSetDao flashcardSetDAO = FlashcardSetDao.getInstance(MariaDbJpaConnection.getInstance());
    private final SharedSetsDao sharedSetsDao = SharedSetsDao.getInstance(MariaDbJpaConnection.getInstance());
    private final ObservableList<OwnFlashcardSetViewModel> ownFlashcardList = FXCollections.observableArrayList();
    private final ObservableList<SharedFlashcardSetViewModel> sharedFlashcardList = FXCollections.observableArrayList();
    private final ObservableList<SetViewModel> flashcardList = FXCollections.observableArrayList();
    private String userId;

    public HomepageViewModel(String userId) {
        this.userId = userId;
        loadFlashcards(userId);
    }

    private void loadFlashcards(String userId) {
        List<FlashcardSet> ownFlashcardSets = flashcardSetDAO.findAllSetsByCreatorId(userId);
        List<SharedSet> sharedSets = sharedSetsDao.findByUserId(userId);
        List<FlashcardSet> sharedFlashcardSets = new ArrayList<>();
        for (SharedSet sharedSet : sharedSets) {
            sharedFlashcardSets.add(sharedSet.getFlashcardSet());
        }

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

    public void deleteFlashcardSet(SetViewModel viewModel) {
        if (viewModel == null) return;

        if (viewModel.getType().equals("own")) {
            flashcardSetDAO.delete(viewModel.getSet());
            ownFlashcardList.remove(viewModel);
        } else {
            SharedSet sharedSet = sharedSetsDao.findBySetIdAndUserId(viewModel.getSet().getSetId(), userId);
            sharedSetsDao.delete(sharedSet);
            sharedFlashcardList.remove(viewModel);
        }
        flashcardList.remove(viewModel);

    }
}
