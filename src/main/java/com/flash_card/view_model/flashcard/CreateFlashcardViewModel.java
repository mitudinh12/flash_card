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

public class CreateFlashcardViewModel {
    private final FlashcardDao flashcardDao = FlashcardDao.getInstance(MariaDbJpaConnection.getInstance());
    private final UserDao userDao = UserDao.getInstance(MariaDbJpaConnection.getInstance());
    private final FlashcardSetDao flashcardSetDao = FlashcardSetDao.getInstance(MariaDbJpaConnection.getInstance());
    private final AuthSessionViewModel authSessionViewModel = AuthSessionViewModel.getInstance();

    //SAVE FLASHCARD METHODS
    private FlashcardSet getCurrentFlashcardSet(int flashcardSetId) {
        return flashcardSetDao.findById(flashcardSetId);
    }

    private User getCurrentUser() {
        String userId = authSessionViewModel.getVerifiedUserInfo().get("userId");
        return userDao.findById(userId);
    }
    public void saveFlashcard(String term, String definition, int flashcardSetId) {
        Flashcard flashcard = new Flashcard(term, definition, DifficultyLevel.hard, getCurrentFlashcardSet(flashcardSetId), getCurrentUser());
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