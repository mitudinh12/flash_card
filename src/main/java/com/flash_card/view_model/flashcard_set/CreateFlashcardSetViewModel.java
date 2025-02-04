package com.flash_card.view_model.flashcard_set;
import com.flash_card.model.dao.UserDao;
import com.flash_card.model.entity.FlashcardSet;
import com.flash_card.model.dao.FlashcardSetDao;
import com.flash_card.model.entity.User;
import com.flash_card.view.create_flashcard_set.CreateFlashcardSetView;
import com.flash_card.view_model.user_auth.AuthSessionViewModel;

public class CreateFlashcardSetViewModel {
    private final FlashcardSetDao flashcardSetDao;
    private final CreateFlashcardSetView createFlashcardSetView;
    private final AuthSessionViewModel authSessionViewModel = AuthSessionViewModel.getInstance();

    public CreateFlashcardSetViewModel(CreateFlashcardSetView view) {
        this.createFlashcardSetView = view;
        this.flashcardSetDao = new FlashcardSetDao();
    }

    public void addSet(String name, String description, String topic) {
        String userId = authSessionViewModel.getVerifiedUserInfo().get("userId");
        User user = UserDao.getInstance().findById(userId);
        System.out.println("User ID: " + userId);
        try {
            FlashcardSet flashcardSet = new FlashcardSet(name, description, topic, user);
            flashcardSetDao.persist(flashcardSet);
        } catch (Exception e) {
            System.err.println("Error in adding flashcard set: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
