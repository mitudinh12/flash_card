package com.flash_card.view_model.flashcard_set;

import com.flash_card.framework.SetViewModel;
import com.flash_card.localization.Localization;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import com.flash_card.model.entity.FlashcardSet;

/**
 * ViewModel for managing a shared flashcard set.
 * Provides properties for UI binding and access to flashcard set data.
 */
public class SharedFlashcardSetViewModel extends SetViewModel {
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
     * The type of the flashcard set, which is always "shared".
     */
    private final String type = "shared";

    /**
     * Constructs a SharedFlashcardSetViewModel for the given flashcard set.
     *
     * @param flashcardSet the flashcard set to be managed by this ViewModel
     */
    public SharedFlashcardSetViewModel(FlashcardSet flashcardSet) {
        this.flashcardSet = flashcardSet;

        // binding to entity fields
        this.setName = new SimpleStringProperty(flashcardSet.getSetName());
        this.setLanguage = new SimpleStringProperty(getLocalizedMessageOrDefault(flashcardSet.getSetLanguage(), flashcardSet.getSetLanguage()));
        this.setTopic = new SimpleStringProperty(flashcardSet.getSetTopic());
        this.numberFlashcard = new SimpleStringProperty(String.valueOf(flashcardSet.getNumberFlashcards()));
    }

    // return properties for UI binding
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
     * @return the number of flashcards property
     */
    public StringProperty setNumberFlashcard() {
        return numberFlashcard;
    }

    /**
     * Gets the type of the flashcard set.
     *
     * @return the type of the flashcard set, which is "shared"
     */
    public String getType() {
        return this.type;
    }

    /**
     * Updates the flashcard set entity with the current property values.
     */
    public void updateEntity() {
        flashcardSet.setSetName(setName.get());
        flashcardSet.setSetTopic(setTopic.get());
    }

    /**
     * Gets the flashcard set associated with this ViewModel.
     *
     * @return the flashcard set
     */
    public FlashcardSet getSet() {
        return flashcardSet;
    }

    /**
     * Retrieves a localized message for the given key, or returns a default value if localization fails.
     *
     * @param key          the key for the localized message
     * @param defaultValue the default value to return if localization fails
     * @return the localized message or the default value
     */
    private String getLocalizedMessageOrDefault(String key, String defaultValue) {
        try {
            return localization.getMessage(key);
        } catch (Exception e) {
            // Log the exception if needed
            return defaultValue; // Return the default value if localization fails
        }
    }
}
