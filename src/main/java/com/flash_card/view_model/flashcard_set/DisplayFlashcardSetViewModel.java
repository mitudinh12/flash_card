package com.flash_card.view_model.flashcard_set;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import com.flash_card.model.entity.FlashcardSet;

public class DisplayFlashcardSetViewModel {
    private final FlashcardSet flashcardSet;

    private final StringProperty setName;
    private final StringProperty setTopic;
    private final StringProperty numberFLashcard;

    public DisplayFlashcardSetViewModel(FlashcardSet flashcardSet) {
        this.flashcardSet = flashcardSet;

        // binding to entity fields
        this.setName = new SimpleStringProperty(flashcardSet.getSetName());
        this.setTopic = new SimpleStringProperty(flashcardSet.getSetTopic());
        this.numberFLashcard = new SimpleStringProperty(String.valueOf(flashcardSet.getNumberFlashcards()));
    }

    // return properties for UI binding
    public StringProperty setNameProperty() { return setName; }
    public StringProperty setTopicProperty() { return setTopic; }
    public StringProperty setNumberFLashcard() { return numberFLashcard;}

    // update entity when properties change
    public void updateEntity() {
        flashcardSet.setSetName(setName.get());
        flashcardSet.setSetTopic(setTopic.get());
    }

    public FlashcardSet getSet() {return flashcardSet;}
}