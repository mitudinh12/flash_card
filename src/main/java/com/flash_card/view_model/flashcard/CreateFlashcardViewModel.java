package com.flash_card.view_model.flashcard;

import com.flash_card.model.dao.FlashcardDao;
import com.flash_card.model.dao.FlashcardSetDao;
import com.flash_card.model.dao.UserDao;
import com.flash_card.model.entity.Flashcard;
import com.flash_card.model.entity.FlashcardSet;
import com. flash_card. framework. DifficultyLevel;
import com.flash_card.model.entity.User;
import com.flash_card.view_model.user_auth.AuthSessionViewModel;

public class CreateFlashcardViewModel {
    private final FlashcardDao flashcardDao = FlashcardDao.getInstance();
    private final UserDao userDao = UserDao.getInstance();
    private final FlashcardSetDao flashcardSetDao = FlashcardSetDao.getInstance();
    private final AuthSessionViewModel authSessionViewModel = AuthSessionViewModel.getInstance();

    public void saveFlashcard(String term, String definition, int flashcardSetId) {
        Flashcard flashcard = new Flashcard(term, definition, DifficultyLevel.hard, getCurrentFlashcardSet(flashcardSetId), getCurrentUser());
        flashcardDao.persist(flashcard);
    }

    public boolean isFlashcardSetEmpty(int flashcardSetId) {
        return flashcardDao.findBySetId(flashcardSetId).isEmpty();
    }

    public void deleteFlashcardSetIfEmpty(int flashcardSetId) {
        if (isFlashcardSetEmpty(flashcardSetId)) {
            flashcardSetDao.delete(flashcardSetDao.findById(flashcardSetId));
        }
    }

    private FlashcardSet getCurrentFlashcardSet(int flashcardSetId) {
        return flashcardSetDao.findById(flashcardSetId);
    }

    private User getCurrentUser() {
        String userId = authSessionViewModel.getVerifiedUserInfo().get("userId");
        return userDao.findById(userId);
    }
}