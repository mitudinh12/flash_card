package com.flash_card.view_model.flashcard_set;

import com.flash_card.model.dao.FlashcardSetDao;
import com.flash_card.model.entity.FlashcardSet;

public class EditFlashcardSetViewModel {
    private FlashcardSetDao flashcardSetDao;

    public EditFlashcardSetViewModel() {
        flashcardSetDao = FlashcardSetDao.getInstance();
    }

    public void saveFlashcardSet(int setId, String setName, String setDescription, String setTopic) {
        FlashcardSet flashcardSet = flashcardSetDao.findById(setId);
        if (flashcardSet != null) {
            flashcardSet.setSetName(setName);
            flashcardSet.setSetDescription(setDescription);
            flashcardSet.setSetTopic(setTopic);
            flashcardSetDao.update(flashcardSet);
        }
    }

    public void deleteFlashcardSet(int setId) {
        FlashcardSet flashcardSet = flashcardSetDao.findById(setId);
        if (flashcardSet != null) {
            flashcardSetDao.delete(flashcardSet);
        }
    }
}