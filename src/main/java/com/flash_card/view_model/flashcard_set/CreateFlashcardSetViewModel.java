package com.flash_card.view_model.flashcard_set;
import com.flash_card.model.dao.UserDao;
import com.flash_card.model.entity.FlashcardSet;
import com.flash_card.model.dao.FlashcardSetDao;
import com.flash_card.model.entity.User;
import jakarta.persistence.EntityManager;

/**
 * ViewModel for creating a new flashcard set.
 * Handles the logic for adding a flashcard set to the database.
 */
public class CreateFlashcardSetViewModel {
    /**
     * DAO for managing flashcard set database operations.
     */
    private final FlashcardSetDao flashcardSetDao;

    /**
     * DAO for managing user database operations.
     */
    private final UserDao userDao;

    /**
     * Constructs a CreateFlashcardSetViewModel with the specified EntityManager.
     *
     * @param em the EntityManager for database operations
     */
    public CreateFlashcardSetViewModel(EntityManager em) {
        flashcardSetDao = FlashcardSetDao.getInstance(em);
        userDao = UserDao.getInstance(em);
    }

    /**
     * Adds a new flashcard set to the database.
     *
     * @param name        the name of the flashcard set
     * @param setLanguage the language of the flashcard set
     * @param description the description of the flashcard set
     * @param topic       the topic of the flashcard set
     * @param userId      the ID of the user creating the flashcard set
     * @return the ID of the created flashcard set, or -1 if an error occurs
     */
    public int addSet(String name, String setLanguage, String description, String topic, String userId) {
        User user = userDao.findById(userId);
        try {
            FlashcardSet flashcardSet = new FlashcardSet(name, description, topic, user);
            flashcardSet.setSetLanguage(setLanguage);
            flashcardSetDao.persist(flashcardSet);
            return flashcardSet.getSetId(); //return the ID of created flashcardSet
        } catch (Exception e) {
            e.printStackTrace();
            return -1; //return -1 to show error
        }
    }
}
