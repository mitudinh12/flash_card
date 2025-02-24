package com.flash_card.view_model.flashcard_set;
import com.flash_card.model.dao.UserDao;
import com.flash_card.model.datasource.MariaDbJpaConnection;
import com.flash_card.model.entity.FlashcardSet;
import com.flash_card.model.dao.FlashcardSetDao;
import com.flash_card.model.entity.User;
import com.flash_card.view.flashcardSet.CreateFlashcardSetView;
import com.flash_card.view_model.user_auth.AuthSessionViewModel;
import jakarta.persistence.EntityManager;

public class CreateFlashcardSetViewModel {
    private FlashcardSetDao flashcardSetDao;
    private UserDao userDao;

    public CreateFlashcardSetViewModel(EntityManager em) {
        flashcardSetDao = FlashcardSetDao.getInstance(em);
        userDao = UserDao.getInstance(em);
    }

    public int addSet(String name, String description, String topic, String userId) {
        User user = userDao.findById(userId);
        try {
            FlashcardSet flashcardSet = new FlashcardSet(name, description, topic, user);
            flashcardSetDao.persist(flashcardSet);
            return flashcardSet.getSetId(); //return the ID of created flashcardSet
        } catch (Exception e) {
            System.err.println("Error in adding flashcard set: " + e.getMessage());
            e.printStackTrace();
            return -1; //return -1 to show error
        }
    }
}