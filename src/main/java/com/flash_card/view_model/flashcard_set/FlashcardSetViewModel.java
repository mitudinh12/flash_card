package com.flash_card.view_model.flashcard_set;

import com.flash_card.model.dao.FlashcardSetDao;
import com.flash_card.model.dao.SharedSetsDao;
import com.flash_card.model.datasource.MariaDbJpaConnection;
import com.flash_card.model.entity.FlashcardSet;
import com.flash_card.model.entity.SharedSet;
import com.flash_card.view_model.entity.EntityManagerViewModel;
import com.flash_card.view_model.user_auth.AuthSessionViewModel;
import jakarta.persistence.EntityManager;

import java.util.ArrayList;
import java.util.List;

public class FlashcardSetViewModel {
    EntityManager entityManager;
    FlashcardSetDao flashcardSetDao;
    SharedSetsDao sharedSetsDao;
    String userId;

    public FlashcardSetViewModel(String userId, EntityManager entityManager) {
        this.entityManager = entityManager;
        flashcardSetDao = FlashcardSetDao.getInstance(entityManager);
        sharedSetsDao = SharedSetsDao.getInstance(entityManager);
        this.userId = userId;
    }

    public List<FlashcardSet> findSharedSets() {
        List<SharedSet> sharedSets = sharedSetsDao.findByUserId(userId);
        List<FlashcardSet> sharedFlashcardSets = new ArrayList<>();
        for (SharedSet sharedSet : sharedSets) {
            sharedFlashcardSets.add(sharedSet.getFlashcardSet());
        }
        return sharedFlashcardSets;
    }

    public List<FlashcardSet> findOwnSets() {
        return flashcardSetDao.findByUserId(userId);
    }

    public void deleteOwnFlashcardSet(FlashcardSet flashcardSet) {
        flashcardSetDao.delete(flashcardSet);
    }

    public void deleteSharedFlashcardSet(String userId, int setId) {
        SharedSet sharedSet = sharedSetsDao.findBySetIdAndUserId(setId, userId);
        sharedSetsDao.delete(sharedSet);
    }
}
