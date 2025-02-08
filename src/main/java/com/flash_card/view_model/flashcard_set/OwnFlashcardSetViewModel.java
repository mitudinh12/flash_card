package com.flash_card.view_model.flashcard_set;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import com.flash_card.model.entity.FlashcardSet;

public class OwnFlashcardSetViewModel extends SetViewModel{

    private final FlashcardSet flashcardSet;
    private final StringProperty setName;
    private final StringProperty setTopic;
    private final StringProperty numberFlashcard;
    private final String type = "own";

    public OwnFlashcardSetViewModel(FlashcardSet flashcardSet) {
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


    // update entity when properties change
    public void updateEntity() {
        flashcardSet.setSetName(setName.get());
        flashcardSet.setSetTopic(setTopic.get());
    }

    public FlashcardSet getSet() {return flashcardSet;}

}