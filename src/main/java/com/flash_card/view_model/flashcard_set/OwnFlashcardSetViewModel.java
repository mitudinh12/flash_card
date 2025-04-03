package com.flash_card.view_model.flashcard_set;

import com.flash_card.framework.SetViewModel;
import com.flash_card.localization.Localization;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import com.flash_card.model.entity.FlashcardSet;

public class OwnFlashcardSetViewModel extends SetViewModel {

    private final FlashcardSet flashcardSet;
    private final Localization localization = Localization.getInstance();
    private final StringProperty setName;
    private final StringProperty setLanguage;
    private final StringProperty setTopic;
    private final StringProperty numberFlashcard;
    private final String type = "own";

    public OwnFlashcardSetViewModel(FlashcardSet flashcardSet) {
        this.flashcardSet = flashcardSet;
        this.setName = new SimpleStringProperty(flashcardSet.getSetName());
        this.setLanguage = new SimpleStringProperty(localization.getMessage(flashcardSet.getSetLanguage()));
        this.setTopic = new SimpleStringProperty(flashcardSet.getSetTopic());
        this.numberFlashcard = new SimpleStringProperty(String.valueOf(flashcardSet.getNumberFlashcards()));
    }

    public String getType() {
        return type;
    }

    // return properties for UI binding
    public StringProperty setNameProperty() { return setName; }
    public StringProperty setLanguageProperty() { return setLanguage; }
    public StringProperty setTopicProperty() { return setTopic; }
    public StringProperty setNumberFlashcard() { return numberFlashcard;}

    public FlashcardSet getSet() {return flashcardSet;}

}