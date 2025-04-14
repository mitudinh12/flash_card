package com.flash_card.view_model.flashcard_set;

import com.flash_card.model.entity.FlashcardSet;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * ViewModel for managing an assigned flashcard set.
 * Provides properties for UI binding and access to flashcard set data.
 */
public class AssignedFlashcardSetViewModel {
    /**
     * The flashcard set associated with this ViewModel.
     */
    private final FlashcardSet flashcardSet;

    /**
     * Property for the name of the flashcard set.
     */
    private final StringProperty setName;

    /**
     * Property for the topic of the flashcard set.
     */
    private final StringProperty setTopic;

    /**
     * Property for the number of flashcards in the set.
     */
    private final StringProperty numberFlashcard;

    /**
     * The type of the flashcard set, which is always "assigned".
     */
    private final String type = "assigned";

    /**
     * Constructs an AssignedFlashcardSetViewModel for the given flashcard set.
     *
     * @param flashcardSetParam the flashcard set to be managed by this ViewModel
     */
    public AssignedFlashcardSetViewModel(final FlashcardSet flashcardSetParam) {
        this.flashcardSet = flashcardSetParam;
        this.setName = new SimpleStringProperty(flashcardSetParam.getSetName());
        this.setTopic = new SimpleStringProperty(flashcardSetParam.getSetTopic());
        this.numberFlashcard = new SimpleStringProperty(String.valueOf(flashcardSetParam.getNumberFlashcards()));
    }

    /**
     * Gets the type of the flashcard set.
     *
     * @return the type of the flashcard set, which is "assigned"
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
     * Gets the flashcard set associated with this ViewModel.
     *
     * @return the flashcard set
     */
    public FlashcardSet getSet() {
        return flashcardSet;
    }
}
