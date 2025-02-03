package com.flash_card.view_model;
import com.flash_card.model.entity.FlashcardSet;
import com.flash_card.model.dao.FlashcardSetDao;
import com.flash_card.view.CreateFlashcardSetView;

public class CreateFlashcardSetViewModel {
    private final FlashcardSetDao flashcardSetDao;
    private final CreateFlashcardSetView createFlashcardSetView;

    public CreateFlashcardSetViewModel(CreateFlashcardSetView view) {
        this.createFlashcardSetView = view;
        this.flashcardSetDao = new FlashcardSetDao();
    }

    public void addSet(String name, String description, String topic) {
            try {
                FlashcardSet flashcardSet = new FlashcardSet(name, description, topic);
                flashcardSetDao.persist(flashcardSet);
            } catch (Exception e) {
                System.err.println("Error in adding flashcard set: " + e.getMessage());
                e.printStackTrace();
            }
    }
}
