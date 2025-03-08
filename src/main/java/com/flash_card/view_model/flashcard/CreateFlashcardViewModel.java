package com.flash_card.view_model.flashcard;

import com.flash_card.model.dao.FlashcardDao;
import com.flash_card.model.dao.FlashcardSetDao;
import com.flash_card.model.dao.UserDao;
import com.flash_card.model.datasource.MariaDbJpaConnection;
import com.flash_card.model.entity.Flashcard;
import com.flash_card.model.entity.FlashcardSet;
import com. flash_card. framework. DifficultyLevel;
import com.flash_card.model.entity.User;
import com.flash_card.view_model.user_auth.AuthSessionViewModel;
import jakarta.persistence.EntityManager;

public class CreateFlashcardViewModel {
    private final FlashcardDao flashcardDao;
    private final UserDao userDao;
    private final FlashcardSetDao flashcardSetDao;
    private final String userId;

    public CreateFlashcardViewModel(String userId, EntityManager entityManager) {
        this.flashcardDao = FlashcardDao.getInstance(entityManager);
        this.userDao = UserDao.getInstance(entityManager);
        this.flashcardSetDao = FlashcardSetDao.getInstance(entityManager);
        this.userId = userId;
    }

    //SAVE FLASHCARD METHODS
    public FlashcardSet getCurrentFlashcardSet(int flashcardSetId) {
        return flashcardSetDao.findById(flashcardSetId);
    }

    public User getCurrentUser() {
        return userDao.findById(userId);
    }

    public void saveFlashcard(String term, String definition, int flashcardSetId) {
        Flashcard flashcard = new Flashcard(term, definition, getCurrentFlashcardSet(flashcardSetId), getCurrentUser());
        flashcardDao.persist(flashcard);

        //increase number of flashcards in the set by 1
        FlashcardSet flashcardSet = getCurrentFlashcardSet(flashcardSetId);
        flashcardSet.addNumberFlashcards();
        flashcardSetDao.update(flashcardSet);
    }

    //DELETE FLASHCARD IF EMPTY METHODS
    public boolean isFlashcardSetEmpty(int flashcardSetId) {
        return flashcardDao.findBySetId(flashcardSetId).isEmpty();
    }

    public void deleteFlashcardSetIfEmpty(int flashcardSetId) {
        if (isFlashcardSetEmpty(flashcardSetId)) {
            flashcardSetDao.delete(flashcardSetDao.findById(flashcardSetId));
        }
    }
}