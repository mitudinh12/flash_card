package com.flash_card.framework;

import com.flash_card.model.entity.FlashcardSet;
import javafx.beans.property.StringProperty;

public abstract class SetViewModel {
    public abstract StringProperty setNameProperty();
    public abstract StringProperty setTopicProperty();
    public abstract StringProperty setNumberFlashcard();
    public abstract String getType();

    public abstract void updateEntity();
    public abstract FlashcardSet getSet();

}
