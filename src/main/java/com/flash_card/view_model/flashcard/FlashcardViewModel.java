package com.flash_card.view_model.flashcard;

import com.flash_card.model.dao.FlashcardDao;
import com.flash_card.model.entity.Flashcard;
import com.flash_card.model.entity.FlashcardSet;
import com.flash_card.model.entity.DifficultyLevel;
import com.flash_card.model.entity.User;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class FlashcardViewModel {
    private final StringProperty term = new SimpleStringProperty();
    private final StringProperty definition = new SimpleStringProperty();
    private final FlashcardDao flashcardDao = FlashcardDao.getInstance();

    public StringProperty termProperty() {
        return term;
    }

    public StringProperty definitionProperty() {
        return definition;
    }

    public void saveFlashcard(FlashcardSet flashcardSet, User flashcardCreator) {
        Flashcard flashcard = new Flashcard(term.get(), definition.get(), DifficultyLevel.HARD, flashcardSet, flashcardCreator);
        flashcardDao.persist(flashcard);
    }
}