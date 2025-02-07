package com.flash_card.view_model.flashcard_set;

import com.flash_card.model.dao.FlashcardSetDao;
import com.flash_card.model.entity.FlashcardSet;
import com.flash_card.view_model.user_auth.AuthSessionViewModel;

import java.util.List;

public class DisplayFlashcardSetViewModel {
    private AuthSessionViewModel authSessionViewModel;
    private FlashcardSetDao flashcardSetDao = FlashcardSetDao.getInstance();

    public DisplayFlashcardSetViewModel() {
        authSessionViewModel = AuthSessionViewModel.getInstance();
        flashcardSetDao = FlashcardSetDao.getInstance();
    }

    public List<FlashcardSet> findOwnSets(String userId) {
        return flashcardSetDao.findByUserId(userId);
    }
}
