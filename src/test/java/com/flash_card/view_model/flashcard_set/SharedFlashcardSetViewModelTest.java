package com.flash_card.view_model.flashcard_set;

import com.flash_card.model.entity.FlashcardSet;
import com.flash_card.model.entity.User;
import javafx.beans.property.SimpleStringProperty;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SharedFlashcardSetViewModelTest {
    private User testCreator = new User("sharedVm", "share", "vm", "share@gmail.com", "sharevntoken");
    private FlashcardSet setShared = new FlashcardSet("Java Basics", "Java 101", "Programing",testCreator);
    private SharedFlashcardSetViewModel sharedFlashcardSetViewModel = new SharedFlashcardSetViewModel(setShared);
    @Test
    void getType() {
        assertEquals("shared", sharedFlashcardSetViewModel.getType());
    }

    @Test
    void testSetNameProperty() {
        SimpleStringProperty setName = new SimpleStringProperty(setShared.getSetName());
        assertEquals(setName.getName(), sharedFlashcardSetViewModel.setNameProperty().getName());
    }

    @Test
    void testSetTopicProperty() {
        SimpleStringProperty setTopic = new SimpleStringProperty(setShared.getSetTopic());
        assertEquals(setTopic.getName(), sharedFlashcardSetViewModel.setTopicProperty().getName());
    }

    @Test
    void testSetNumberFlashcard() {
        SimpleStringProperty numberFlashcard = new SimpleStringProperty(String.valueOf(setShared.getNumberFlashcards()));
        assertEquals(numberFlashcard.getName(), sharedFlashcardSetViewModel.setNumberFlashcard().getName());
    }

    @Test
    void testUpdateEntity() {
        sharedFlashcardSetViewModel.updateEntity();
        assertEquals(setShared.getSetName(), sharedFlashcardSetViewModel.getSet().getSetName());
        assertEquals(setShared.getSetTopic(), sharedFlashcardSetViewModel.getSet().getSetTopic());
    }

    @Test
    void testGetSet() {
        assertEquals(setShared, sharedFlashcardSetViewModel.getSet());
    }

}