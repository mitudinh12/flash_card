//package com.flash_card.view_model.flashcard_set;
//
//import javafx.beans.property.SimpleStringProperty;
//import org.junit.jupiter.api.Test;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class SharedFlashcardSetViewModelTest extends TestSetupAbstract {
//
//    @Test
//    void getType() {
//        assertEquals("shared", sharedFlashcardSetViewModel.getType());
//    }
//
//    @Test
//    void testSetNameProperty() {
//        SimpleStringProperty setName = new SimpleStringProperty(testFlashcardSet2.getSetName());
//        assertTrue(setName.getName().equals(sharedFlashcardSetViewModel.setNameProperty().getName()));
//    }
//
//    @Test
//    void testSetTopicProperty() {
//        SimpleStringProperty setTopic = new SimpleStringProperty(testFlashcardSet2.getSetTopic());
//        assertTrue(setTopic.getName().equals(sharedFlashcardSetViewModel.setTopicProperty().getName()));
//    }
//
//    @Test
//    void testSetNumberFlashcard() {
//        SimpleStringProperty numberFlashcard = new SimpleStringProperty(String.valueOf(testFlashcardSet2.getNumberFlashcards()));
//        assertTrue(numberFlashcard.getName().equals(sharedFlashcardSetViewModel.setNumberFlashcard().getName()));
//    }
//
//    @Test
//    void testUpdateEntity() {
//        sharedFlashcardSetViewModel.updateEntity();
//        assertEquals(testFlashcardSet2.getSetName(), sharedFlashcardSetViewModel.getSet().getSetName());
//        assertEquals(testFlashcardSet2.getSetTopic(), sharedFlashcardSetViewModel.getSet().getSetTopic());
//    }
//
//    @Test
//    void testGetSet() {
//        assertEquals(testFlashcardSet2, sharedFlashcardSetViewModel.getSet());
//    }
//
//}