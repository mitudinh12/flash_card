package com.flash_card.view_model.flashcard_set;

import com.flash_card.model.dao.FlashcardSetDao;
import com.flash_card.model.entity.FlashcardSet;
import jakarta.persistence.EntityManager;

/**
 * ViewModel for editing an existing flashcard set.
 * Handles the logic for updating flashcard set details in the database.
 */
public class EditFlashcardSetViewModel {

    /**
     * DAO for managing flashcard set database operations.
     */
    private final FlashcardSetDao flashcardSetDao;

    /**
     * Constructs an EditFlashcardSetViewModel with the specified EntityManager.
     *
     * @param entityManager the EntityManager for database operations
     */
    public EditFlashcardSetViewModel(EntityManager entityManager) {
        flashcardSetDao = FlashcardSetDao.getInstance(entityManager);
    }

    /**
     * Updates the details of an existing flashcard set in the database.
     *
     * @param setId          the ID of the flashcard set to update
     * @param setName        the new name of the flashcard set
     * @param setDescription the new description of the flashcard set
     * @param setTopic       the new topic of the flashcard set
     */
    public void saveFlashcardSet(int setId, String setName, String setDescription, String setTopic) {
        FlashcardSet flashcardSet = flashcardSetDao.findById(setId);
        if (flashcardSet != null) {
            flashcardSet.setSetName(setName);
            flashcardSet.setSetDescription(setDescription);
            flashcardSet.setSetTopic(setTopic);
            flashcardSetDao.update(flashcardSet);
        }
    }
}
