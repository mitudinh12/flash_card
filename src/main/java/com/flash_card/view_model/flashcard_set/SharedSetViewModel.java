package com.flash_card.view_model.flashcard_set;

import com.flash_card.model.dao.FlashcardSetDao;
import com.flash_card.model.dao.SharedSetsDao;
import com.flash_card.model.dao.UserDao;
import com.flash_card.model.datasource.MariaDbJpaConnection;
import com.flash_card.model.entity.FlashcardSet;
import com.flash_card.model.entity.SharedSet;
import com.flash_card.model.entity.User;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.ArrayList;

public class SharedSetViewModel {
    private SharedSetsDao sharedSetDao;
    private UserDao userDao;
    private FlashcardSetDao flashcardSetDao;
    private String userId;

    public SharedSetViewModel(String userId, EntityManager entityManager) {
        sharedSetDao = sharedSetDao.getInstance(entityManager);
        userDao = userDao.getInstance(entityManager);
        flashcardSetDao = flashcardSetDao.getInstance(entityManager);
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

    public boolean isUserAndSetShared(String email, int setId) {                        // check if this user is shared this set or not
        User user = userDao.findByEmail(email);
        return sharedSetDao.findBySetIdAndUserId(setId, user.getUserId()) != null;      // true if this user is shared this set
    }

}
