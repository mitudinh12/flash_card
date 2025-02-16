package com.flash_card.view_model.flashcard_set;

import com.flash_card.model.entity.FlashcardSet;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class AssignedFlashcardSetViewModel {
    private final FlashcardSet flashcardSet;
    private final StringProperty setName;
    private final StringProperty setTopic;
    private final StringProperty numberFlashcard;
    private final String type = "assigned";

    public AssignedFlashcardSetViewModel(FlashcardSet flashcardSet) {
        this.flashcardSet = flashcardSet;
        this.setName = new SimpleStringProperty(flashcardSet.getSetName());
        this.setTopic = new SimpleStringProperty(flashcardSet.getSetTopic());
        this.numberFlashcard = new SimpleStringProperty(String.valueOf(flashcardSet.getNumberFlashcards()));
    }

    public String getType() {
        return type;
    }

    // return properties for UI binding
    public StringProperty setNameProperty() { return setName; }
    public StringProperty setTopicProperty() { return setTopic; }
    public StringProperty setNumberFlashcard() { return numberFlashcard;}

    public FlashcardSet getSet() {return flashcardSet;}
}
