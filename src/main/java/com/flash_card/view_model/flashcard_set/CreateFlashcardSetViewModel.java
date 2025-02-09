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
    private final FlashcardSetDao flashcardSetDao =  FlashcardSetDao.getInstance(MariaDbJpaConnection.getInstance());
    private final CreateFlashcardSetView createFlashcardSetView;
    private final AuthSessionViewModel authSessionViewModel = AuthSessionViewModel.getInstance();

    public CreateFlashcardSetViewModel(CreateFlashcardSetView view) {
        this.createFlashcardSetView = view;
    }

    public int addSet(String name, String description, String topic) {
        String userId = authSessionViewModel.getVerifiedUserInfo().get("userId");
        User user = UserDao.getInstance(MariaDbJpaConnection.getInstance()).findById(userId);
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
