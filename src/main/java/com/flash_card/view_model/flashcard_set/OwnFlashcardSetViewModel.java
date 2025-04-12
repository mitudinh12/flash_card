package com.flash_card.view_model.flashcard_set;

import com.flash_card.framework.SetViewModel;
import com.flash_card.localization.Localization;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import com.flash_card.model.entity.FlashcardSet;

/**
 * ViewModel for managing an owned flashcard set.
 * Provides properties for UI binding and access to flashcard set data.
 */
public class OwnFlashcardSetViewModel extends SetViewModel {

    /**
     * The flashcard set associated with this ViewModel.
     */
    private final FlashcardSet flashcardSet;

    /**
     * Localization instance for retrieving localized messages.
     */
    private final Localization localization = Localization.getInstance();

    /**
     * Property for the name of the flashcard set.
     */
    private final StringProperty setName;

    /**
     * Property for the language of the flashcard set.
     */
    private final StringProperty setLanguage;

    /**
     * Property for the topic of the flashcard set.
     */
    private final StringProperty setTopic;

    /**
     * Property for the number of flashcards in the set.
     */
    private final StringProperty numberFlashcard;

    /**
     * The type of the flashcard set, which is always "own".
     */
    private final String type = "own";

    /**
     * Constructs an OwnFlashcardSetViewModel for the given flashcard set.
     *
     * @param flashcardSet the flashcard set to be managed by this ViewModel
     */
    public OwnFlashcardSetViewModel(FlashcardSet flashcardSet) {
        this.flashcardSet = flashcardSet;
        this.setName = new SimpleStringProperty(flashcardSet.getSetName());
        this.setLanguage = new SimpleStringProperty(localization.getMessage(flashcardSet.getSetLanguage()));
        this.setTopic = new SimpleStringProperty(flashcardSet.getSetTopic());
        this.numberFlashcard = new SimpleStringProperty(String.valueOf(flashcardSet.getNumberFlashcards()));
    }

    /**
     * Gets the type of the flashcard set.
     *
     * @return the type of the flashcard set, which is "own"
     */
    public String getType() {
        return type;
    }

    /**
     * Gets the property for the name of the flashcard set.
     *
     * @return the name property of the flashcard set
     */
    public StringProperty setNameProperty() {
        return setName;
    }

    /**
     * Gets the property for the language of the flashcard set.
     *
     * @return the language property of the flashcard set
     */
    public StringProperty setLanguageProperty() {
        return setLanguage;
    }

    /**
     * Gets the property for the topic of the flashcard set.
     *
     * @return the topic property of the flashcard set
     */
    public StringProperty setTopicProperty() {
        return setTopic;
    }

    /**
     * Gets the property for the number of flashcards in the set.
     *
     * @return the number of flashcards property of the flashcard set
     */
    public StringProperty setNumberFlashcard() {
        return numberFlashcard;
    }

    /**
     * Gets the flashcard set associated with this ViewModel.
     *
     * @return the flashcard set managed by this ViewModel
     */
    public FlashcardSet getSet() {
        return flashcardSet;
    }
}
