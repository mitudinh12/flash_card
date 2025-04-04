package com.flash_card.view_model.flashcard_set;


import com.flash_card.framework.SetViewModel;
import com.flash_card.localization.Localization;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import com.flash_card.model.entity.FlashcardSet;

public class SharedFlashcardSetViewModel extends SetViewModel {
    private final FlashcardSet flashcardSet;
    private final Localization localization = Localization.getInstance();
    private final StringProperty setName;
    private final StringProperty setLanguage;
    private final StringProperty setTopic;
    private final StringProperty numberFLashcard;
    private final String type = "shared";

    public SharedFlashcardSetViewModel(FlashcardSet flashcardSet) {
        this.flashcardSet = flashcardSet;

        // binding to entity fields
        this.setName = new SimpleStringProperty(flashcardSet.getSetName());
        this.setLanguage = new SimpleStringProperty(localization.getMessage(flashcardSet.getSetLanguage()));
        this.setTopic = new SimpleStringProperty(flashcardSet.getSetTopic());
        this.numberFLashcard = new SimpleStringProperty(String.valueOf(flashcardSet.getNumberFlashcards()));
    }

    // return properties for UI binding
    public StringProperty setNameProperty() { return setName; }
    public StringProperty setLanguageProperty() { return setLanguage; }
    public StringProperty setTopicProperty() { return setTopic; }
    public StringProperty setNumberFlashcard() { return numberFLashcard;}
    public String getType() {return this.type;}

    // update entity when properties change
    public void updateEntity() {
        flashcardSet.setSetName(setName.get());
        flashcardSet.setSetTopic(setTopic.get());
    }

    public FlashcardSet getSet() {return flashcardSet;}
}
