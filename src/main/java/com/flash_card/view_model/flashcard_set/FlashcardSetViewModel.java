package com.flash_card.view_model.flashcard_set;

import com.flash_card.model.dao.FlashcardSetDao;
import com.flash_card.model.dao.SharedSetsDao;
import com.flash_card.model.entity.FlashcardSet;
import com.flash_card.model.entity.SharedSet;
import jakarta.persistence.EntityManager;

import java.util.ArrayList;
import java.util.List;

/**
 * ViewModel for managing flashcard sets.
 * Provides methods to retrieve, delete, and manage both owned and shared flashcard sets.
 */
public class FlashcardSetViewModel {
    /**
     * The EntityManager for database operations.
     */
    private final EntityManager entityManager;

    /**
     * DAO for managing flashcard set database operations.
     */
    private final FlashcardSetDao flashcardSetDao;

    /**
     * DAO for managing shared flashcard set database operations.
     */
    private final SharedSetsDao sharedSetsDao;

    /**
     * The ID of the user associated with this ViewModel.
     */
    private final String userId;

    /**
     * Constructs a FlashcardSetViewModel with the specified user ID and EntityManager.
     *
     * @param userIdParam        the ID of the user
     * @param entityManagerParam the EntityManager for database operations
     */
    public FlashcardSetViewModel(final String userIdParam, final EntityManager entityManagerParam) {
        this.entityManager = entityManagerParam;
        flashcardSetDao = FlashcardSetDao.getInstance(entityManager);
        sharedSetsDao = SharedSetsDao.getInstance(entityManager);
        this.userId = userIdParam;
    }

    /**
     * Finds all flashcard sets shared with the user.
     *
     * @return a list of shared flashcard sets
     */
    public List<FlashcardSet> findSharedSets() {
        List<SharedSet> sharedSets = sharedSetsDao.findByUserId(userId);
        List<FlashcardSet> sharedFlashcardSets = new ArrayList<>();
        for (SharedSet sharedSet : sharedSets) {
            sharedFlashcardSets.add(sharedSet.getFlashcardSet());
        }
        return sharedFlashcardSets;
    }

    /**
     * Finds all flashcard sets owned by the user.
     *
     * @return a list of the user's own flashcard sets
     */
    public List<FlashcardSet> findOwnSets() {
        return flashcardSetDao.findByUserId(userId);
    }

    /**
     * Deletes a flashcard set owned by the user.
     *
     * @param flashcardSet the flashcard set to delete
     */
    public void deleteOwnFlashcardSet(final FlashcardSet flashcardSet) {
        flashcardSetDao.delete(flashcardSet);
    }

    /**
     * Deletes a shared flashcard set for the user.
     *
     * @param userIdParam the ID of the user
     * @param setId  the ID of the shared flashcard set
     */
    public void deleteSharedFlashcardSet(final String userIdParam, final int setId) {
        SharedSet sharedSet = sharedSetsDao.findBySetIdAndUserId(setId, userIdParam);
        sharedSetsDao.delete(sharedSet);
    }
}
