package com.flash_card.view_model.flashcard_set;

import com.flash_card.model.dao.FlashcardDao;
import com.flash_card.model.entity.Flashcard;
import jakarta.persistence.EntityManager;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleStringProperty;

import java.util.List;

public class StudyFlashcardSetViewModel {
    private final FlashcardDao flashcardDao;
    private List<Flashcard> flashcards;
    private final IntegerProperty currentIndex = new SimpleIntegerProperty(0);
    private final StringProperty setName = new SimpleStringProperty();
    private final StringProperty total = new SimpleStringProperty();

    public StudyFlashcardSetViewModel(EntityManager entityManager) {
        flashcardDao = FlashcardDao.getInstance(entityManager);
    }

    public void loadFlashcards(int setId, String setName) {
        flashcards = flashcardDao.getHardFlashcards(setId);
        this.setName.set(setName);
        this.total.set(String.valueOf(flashcards.size()));
    }

    public Flashcard getCurrentFlashcard() {
        return flashcards.get(currentIndex.get());
    }

    public void nextFlashcard() {
        if (currentIndex.get() < flashcards.size() - 1) {
            currentIndex.set(currentIndex.get() + 1);
        }
    }

    public void previousFlashcard() {
        if (currentIndex.get() > 0) {
            currentIndex.set(currentIndex.get() - 1);
        }
    }

    public IntegerProperty currentIndexProperty() {
        return currentIndex;
    }

    public StringProperty setNameProperty() {
        return setName;
    }

    public StringProperty totalProperty() {
        return total;
    }
}