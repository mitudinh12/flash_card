package com.flash_card.view_model.flashcard_set;

import com.flash_card.model.dao.FlashcardSetDao;
import com.flash_card.model.dao.SharedSetsDao;
import com.flash_card.model.dao.UserDao;
import com.flash_card.model.entity.FlashcardSet;
import com.flash_card.model.entity.SharedSet;
import com.flash_card.model.entity.User;

import java.util.List;
import java.util.ArrayList;

public class SharedSetViewModel {
    private SharedSetsDao sharedSetDao;
    private UserDao userDao;
    private FlashcardSetDao flashcardSetDao;
    private String userId;

    public SharedSetViewModel( String userId) {
        sharedSetDao = sharedSetDao.getInstance();
        userDao = userDao.getInstance();
        flashcardSetDao = flashcardSetDao.getInstance();
        this.userId = userId;
    }

    public void saveSharedFlashcardSet(String email, int setId) {
        User user = userDao.findByEmail(email);
        FlashcardSet flashcardSet = flashcardSetDao.findById(setId);

        if (user != null && flashcardSet != null) {
            sharedSetDao.persist(new SharedSet(user, flashcardSet));
        }
    }

    public boolean isUserValid(String email) {
        return userDao.findByEmail(email) != null; //true if user exists, false otherwise
    }

    public List<FlashcardSet> getSharedFlashcardSets() {
        List<SharedSet> sharedSets = sharedSetDao.findByUserId(userId);
        List<FlashcardSet> flashcardSets = new ArrayList<>();
        for (SharedSet sharedSet : sharedSets) {
            flashcardSets.add(sharedSet.getFlashcardSet());
        }
        return flashcardSets;
    }

    public void deleteSharedFlashcardSet(int setId) {
        SharedSet sharedSet = sharedSetDao.findBySetIdAndUserId(setId, userId);
        if (sharedSet != null) {
            sharedSetDao.delete(sharedSet);
        }
    }
}
