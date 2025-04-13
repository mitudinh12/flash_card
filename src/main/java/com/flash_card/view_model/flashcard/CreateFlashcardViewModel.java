package com.flash_card.view_model.flashcard;

import com.flash_card.model.dao.FlashcardDao;
import com.flash_card.model.dao.FlashcardSetDao;
import com.flash_card.model.dao.UserDao;
import com.flash_card.model.entity.Flashcard;
import com.flash_card.model.entity.FlashcardSet;
import com.flash_card.model.entity.User;
import jakarta.persistence.EntityManager;
/**
 * ViewModel class for managing the creation of flashcards.
 * Provides methods to save and delete flashcards and manage flashcard sets.
 */
public class CreateFlashcardViewModel {
    /**
     * DAO for managing {@link Flashcard} entities.
     */
    private final FlashcardDao flashcardDao;
    /**
     * DAO for managing {@link User} entities.
     */
    private final UserDao userDao;
    /**
     * DAO for managing {@link FlashcardSet} entities.
     */
    private final FlashcardSetDao flashcardSetDao;
    /**
     * The ID of the current user.
     */
    private final String userId;
    /**
     * Constructor to initialize the ViewModel with the DAOs and user ID.
     *
     * @param userIdParam        the ID of the current user
     * @param entityManager the {@link EntityManager} instance for database operations
     */
    public CreateFlashcardViewModel(final String userIdParam, final EntityManager entityManager) {
        this.flashcardDao = FlashcardDao.getInstance(entityManager);
        this.userDao = UserDao.getInstance(entityManager);
        this.flashcardSetDao = FlashcardSetDao.getInstance(entityManager);
        this.userId = userIdParam;
    }

    /**
     * Retrieves the current {@link FlashcardSet} by its ID.
     *
     * @param flashcardSetId the ID of the flashcard set
     * @return the {@link FlashcardSet} entity
     */
    public FlashcardSet getCurrentFlashcardSet(final int flashcardSetId) {
        return flashcardSetDao.findById(flashcardSetId);
    }
    /**
     * Retrieves the current {@link User} entity.
     *
     * @return the {@link User} entity
     */
    public User getCurrentUser() {
        return userDao.findById(userId);
    }
    /**
     * Saves a new {@link Flashcard} to the database and updates the flashcard set.
     *
     * @param term            the term of the flashcard
     * @param definition      the definition of the flashcard
     * @param flashcardSetId  the ID of the flashcard set
     */
    public void saveFlashcard(final String term, final String definition, final int flashcardSetId) {
        Flashcard flashcard = new Flashcard(term, definition, getCurrentFlashcardSet(flashcardSetId), getCurrentUser());
        flashcardDao.persist(flashcard);

        //increase number of flashcards in the set by 1
        FlashcardSet flashcardSet = getCurrentFlashcardSet(flashcardSetId);
        flashcardSet.addNumberFlashcards();
        flashcardSetDao.update(flashcardSet);
    }

    /**
     * Checks if a flashcard set is empty.
     *
     * @param flashcardSetId the ID of the flashcard set
     * @return true if the flashcard set is empty, false otherwise
     */
    public boolean isFlashcardSetEmpty(final int flashcardSetId) {
        return flashcardDao.findBySetId(flashcardSetId).isEmpty();
    }
    /**
     * Deletes a flashcard set if it is empty.
     *
     * @param flashcardSetId the ID of the flashcard set
     */
    public void deleteFlashcardSetIfEmpty(final int flashcardSetId) {
        if (isFlashcardSetEmpty(flashcardSetId)) {
            flashcardSetDao.delete(flashcardSetDao.findById(flashcardSetId));
        }
    }
}
