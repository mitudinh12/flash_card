package com.flash_card.framework;

import com.flash_card.model.entity.FlashcardSet;
import javafx.beans.property.StringProperty;

public abstract class SetViewModel {
    /**
     * Returns the property for the set name.
     *
     * @return the set name property
     */
    public abstract StringProperty setNameProperty();
    /**
     * Returns the property for the set language.
     *
     * @return the set language property
     */
    public abstract StringProperty setLanguageProperty();
    /**
     * Returns the property for the set topic.
     *
     * @return the set topic property
     */
    public abstract StringProperty setTopicProperty();
    /**
     * Returns the property for the number of flashcards.
     *
     * @return the number of flashcards property
     */
    public abstract StringProperty setNumberFlashcard();
    /**
     * Returns the type of the set.
     *
     * @return the type of the set
     */
    public abstract String getType();
    /**
     * Returns the flashcard set.
     *
     * @return the flashcard set
     */
    //    public abstract void updateEntity();
    public abstract FlashcardSet getSet();


}
