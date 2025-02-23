package com.flash_card.view_model.flashcard_set;

import com.flash_card.model.dao.FlashcardSetDao;
import com.flash_card.model.datasource.MariaDbJpaConnection;
import com.flash_card.model.entity.FlashcardSet;
import jakarta.persistence.EntityManager;
import javafx.collections.ObservableList;

public class EditFlashcardSetViewModel {
    private FlashcardSetDao flashcardSetDao;
    private CreateFlashcardSetViewModel createFlashcardSetViewModel;

    public EditFlashcardSetViewModel(EntityManager entityManager) {
        flashcardSetDao = FlashcardSetDao.getInstance(entityManager);
        createFlashcardSetViewModel = new CreateFlashcardSetViewModel(entityManager);
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

    public ObservableList<String> reloadTopics() {
        createFlashcardSetViewModel.loadTopics();
        System.out.println(createFlashcardSetViewModel.getTopics());
        return createFlashcardSetViewModel.getTopics();
    }
}