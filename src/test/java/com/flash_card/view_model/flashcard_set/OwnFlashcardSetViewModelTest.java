package com.flash_card.view_model.flashcard_set;

import com.flash_card.model.entity.FlashcardSet;
import com.flash_card.model.entity.User;
import javafx.beans.property.SimpleStringProperty;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OwnFlashcardSetViewModelTest {
    private User testCreator1 = new User("ownedVm", "own", "vm", "own@gmail.com", "ownvntoken");
    private FlashcardSet setOwned = new FlashcardSet("Java Basics", "Java 101", "Programing",testCreator1);
    private OwnFlashcardSetViewModel ownedFlashcardSetViewModel = new OwnFlashcardSetViewModel(setOwned);

    @Test
    void getType() {
        assertEquals("own", ownedFlashcardSetViewModel.getType());
    }

    @Test
    void testSetNameProperty() {
        SimpleStringProperty setName = new SimpleStringProperty(setOwned.getSetName());
        assertEquals(setName.getName(), ownedFlashcardSetViewModel.setNameProperty().getName());
    }

    @Test
    void testSetLanguageProperty() {
        setOwned.setSetLanguage("english");
        SimpleStringProperty setLanguage = new SimpleStringProperty(setOwned.getSetLanguage());
        assertEquals(setLanguage.getName(), ownedFlashcardSetViewModel.setLanguageProperty().getName());
    }

    @Test
    void testSetTopicProperty() {
        SimpleStringProperty setTopic = new SimpleStringProperty(setOwned.getSetTopic());
        assertEquals(setTopic.getName(), ownedFlashcardSetViewModel.setTopicProperty().getName());
    }

    @Test
    void testSetNumberFlashcard() {
        SimpleStringProperty numberFlashcard = new SimpleStringProperty(String.valueOf(setOwned.getNumberFlashcards()));
        assertEquals(numberFlashcard.getName(), ownedFlashcardSetViewModel.setNumberFlashcard().getName());
    }

    @Test
    void testGetSet() {
        assertEquals(setOwned, ownedFlashcardSetViewModel.getSet());
    }
}