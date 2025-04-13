package com.flash_card.view_model.flashcard_set;

import com.flash_card.model.dao.FlashcardSetDao;
import com.flash_card.model.dao.SharedSetsDao;
import com.flash_card.model.dao.UserDao;
import com.flash_card.model.entity.FlashcardSet;
import com.flash_card.model.entity.SharedSet;
import com.flash_card.model.entity.User;
import jakarta.persistence.EntityManager;

/**
 * ViewModel for managing shared flashcard sets.
 * Provides methods to save, validate, and check shared flashcard sets for users.
 */
public class SharedSetViewModel {
    /**
     * DAO for managing shared sets-related database operations.
     */
    private final SharedSetsDao sharedSetDao;

    /**
     * DAO for managing user-related database operations.
     */
    private final UserDao userDao;

    /**
     * DAO for managing flashcard set-related database operations.
     */
    private final FlashcardSetDao flashcardSetDao;

    /**
     * The ID of the user associated with this ViewModel.
     */
    private final String userId;

    /**
     * Constructor for SharedSetViewModel.
     *
     * @param userIdParam        the ID of the user
     * @param entityManager the EntityManager for database operations
     */
    public SharedSetViewModel(final String userIdParam, final EntityManager entityManager) {
        this.sharedSetDao = SharedSetsDao.getInstance(entityManager);
        this.userDao = UserDao.getInstance(entityManager);
        this.flashcardSetDao = FlashcardSetDao.getInstance(entityManager);
        this.userId = userIdParam;
    }

    /**
     * Saves a shared flashcard set for a user identified by their email.
     *
     * @param email the email of the user to share the set with
     * @param setId the ID of the flashcard set to be shared
     */
    public void saveSharedFlashcardSet(final String email, final int setId) {
        User user = userDao.findByEmail(email);
        FlashcardSet flashcardSet = flashcardSetDao.findById(setId);

        if (user != null && flashcardSet != null) {
            sharedSetDao.persist(new SharedSet(user, flashcardSet));
        }
    }

    /**
     * Checks if a user with the specified email exists.
     *
     * @param email the email of the user to validate
     * @return true if the user exists, false otherwise
     */
    public boolean isUserValid(final String email) {
        return userDao.findByEmail(email) != null; //true if user exists, false otherwise
    }

    /**
     * Checks if a specific flashcard set is already shared with a user identified by their email.
     *
     * @param email the email of the user
     * @param setId the ID of the flashcard set
     * @return true if the set is already shared with the user, false otherwise
     */
    public boolean isUserAndSetShared(
            final String email,
            final int setId
    ) {                       // check if this user is shared this set or not
        User user = userDao.findByEmail(email);
        assert user != null;
        return sharedSetDao.findBySetIdAndUserId(
                setId,
                user.getUserId()
        ) != null;      // true if this user is shared this set
    }
}
