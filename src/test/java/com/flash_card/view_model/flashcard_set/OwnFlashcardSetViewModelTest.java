package com.flash_card.view_model.flashcard_set;

import com.flash_card.model.entity.TestSetupAbstract;
import javafx.beans.property.SimpleStringProperty;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OwnFlashcardSetViewModelTest extends TestSetupAbstract {

    @Test
    void getType() {
        assertEquals("own", ownFlashcardSetViewModel.getType());
    }

    @Test
    void testSetNameProperty() {
        SimpleStringProperty setName = new SimpleStringProperty(testFlashcardSet1.getSetName());
        assertTrue(setName.getName().equals(ownFlashcardSetViewModel.setNameProperty().getName()));
    }

    @Test
    void testSetTopicProperty() {
        SimpleStringProperty setTopic = new SimpleStringProperty(testFlashcardSet1.getSetTopic());
        assertTrue(setTopic.getName().equals(ownFlashcardSetViewModel.setTopicProperty().getName()));
    }

    @Test
    void testSetNumberFlashcard() {
        SimpleStringProperty numberFlashcard = new SimpleStringProperty(String.valueOf(testFlashcardSet1.getNumberFlashcards()));
        assertTrue(numberFlashcard.getName().equals(ownFlashcardSetViewModel.setNumberFlashcard().getName()));
    }

//    @Test
//    void testUpdateEntity() {
//        ownFlashcardSetViewModel.updateEntity();
//        assertEquals(testFlashcardSet1.getSetName(), ownFlashcardSetViewModel.getSet().getSetName());
//        assertEquals(testFlashcardSet1.getSetTopic(), ownFlashcardSetViewModel.getSet().getSetTopic());
//    }

    @Test
    void testGetSet() {
        assertEquals(testFlashcardSet1, ownFlashcardSetViewModel.getSet());
    }
}