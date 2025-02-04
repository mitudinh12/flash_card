package com.flash_card.view_model.flashcard;

import com.flash_card.model.dao.FlashcardDao;
import com.flash_card.model.dao.FlashcardSetDao;
import com.flash_card.model.dao.UserDao;
import com.flash_card.model.entity.Flashcard;
import com.flash_card.model.entity.FlashcardSet;
import com.flash_card.model.entity.DifficultyLevel;
import com.flash_card.model.entity.User;
import com.flash_card.view_model.user_auth.AuthSessionViewModel;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class CreateFlashcardViewModel {
    private final StringProperty term = new SimpleStringProperty();
    private final StringProperty definition = new SimpleStringProperty();
    private final FlashcardDao flashcardDao = FlashcardDao.getInstance();
    private final UserDao userDao = UserDao.getInstance();
    private final FlashcardSetDao flashcardSetDao = FlashcardSetDao.getInstance();
    private final AuthSessionViewModel authSessionViewModel = AuthSessionViewModel.getInstance();

    public StringProperty termProperty() {
        return term;
    }

    public StringProperty definitionProperty() {
        return definition;
    }

    public void saveFlashcard(int flashcardSetId) {
        Flashcard flashcard = new Flashcard(term.get(), definition.get(), DifficultyLevel.HARD, getCurrentFlashcardSet(flashcardSetId), getCurrentUser());
        flashcardDao.persist(flashcard);
    }

    private FlashcardSet getCurrentFlashcardSet(int flashcardSetId) {
        return flashcardSetDao.findById(flashcardSetId);
    }

    private User getCurrentUser() {
        String userId = authSessionViewModel.getVerifiedUserInfo().get("userId");
        return userDao.findById(userId);
    }
}