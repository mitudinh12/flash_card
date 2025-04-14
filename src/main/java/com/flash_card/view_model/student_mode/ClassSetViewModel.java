package com.flash_card.view_model.student_mode;

import com.flash_card.framework.SetViewModel;
import com.flash_card.model.entity.FlashcardSet;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
/**
 * ViewModel class for managing the properties of a flashcard set assigned to a class.
 * Provides methods for UI binding and updating the underlying entity.
 */
public class ClassSetViewModel extends SetViewModel {
    /**
     * The {@link FlashcardSet} entity associated with this ViewModel.
     */
    private final FlashcardSet flashcardSet;
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
    private final StringProperty numberFLashcard;
    /**
     * The type of the flashcard set, which is always "assigned".
     */
    private final String type = "assigned";
    /**
     * Constructor to initialize the ViewModel with the given {@link FlashcardSet}.
     * Binds the properties to the fields of the entity.
     *
     * @param flashcardSetParam the {@link FlashcardSet} entity to bind
     */
    public ClassSetViewModel(final FlashcardSet flashcardSetParam) {
        this.flashcardSet = flashcardSetParam;

        // binding to entity fields
        this.setName = new SimpleStringProperty(flashcardSet.getSetName());
        this.setLanguage = new SimpleStringProperty(flashcardSet.getSetLanguage());
        this.setTopic = new SimpleStringProperty(flashcardSet.getSetTopic());
        this.numberFLashcard = new SimpleStringProperty(String.valueOf(flashcardSet.getNumberFlashcards()));
    }

    /**
     * Returns the property for the name of the flashcard set.
     *
     * @return the name property
     */
    public StringProperty setNameProperty() {
        return setName;
    }
    /**
     * Returns the property for the language of the flashcard set.
     *
     * @return the language property
     */
    public StringProperty setLanguageProperty() {
        return setLanguage;
    }
    /**
     * Returns the property for the topic of the flashcard set.
     *
     * @return the topic property
     */
    public StringProperty setTopicProperty() {
        return setTopic;
    }
    /**
     * Returns the property for the number of flashcards in the set.
     *
     * @return the number of flashcards property
     */
    public StringProperty setNumberFlashcard() {
        return numberFLashcard;
    }
    /**
     * Returns the type of the flashcard set.
     *
     * @return the type of the flashcard set
     */
    public String getType() {
        return this.type;
    }

    /**
     * Updates the underlying {@link FlashcardSet} entity with the current property values.
     */
    public void updateEntity() {
        flashcardSet.setSetName(setName.get());
        flashcardSet.setSetTopic(setTopic.get());
    }
    /**
     * Returns the underlying {@link FlashcardSet} entity.
     *
     * @return the {@link FlashcardSet} entity
     */
    public FlashcardSet getSet() {
        return flashcardSet;
    }
}
