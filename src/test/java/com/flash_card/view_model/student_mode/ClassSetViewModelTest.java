//package com.flash_card.view_model.student_mode;
//
//import javafx.beans.property.SimpleStringProperty;
//import org.junit.jupiter.api.Test;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class ClassSetViewModelTest extends TestSetupAbstract {
//
//    @Test
//    void getType() {
//        assertEquals("assigned", assignedFlashcardSetViewModel.getType());
//    }
//
//    @Test
//    void testSetNameProperty() {
//        SimpleStringProperty setName = new SimpleStringProperty(testFlashcardSet3.getSetName());
//        assertTrue(setName.getName().equals(assignedFlashcardSetViewModel.setNameProperty().getName()));
//    }
//
//    @Test
//    void testSetTopicProperty() {
//        SimpleStringProperty setTopic = new SimpleStringProperty(testFlashcardSet3.getSetTopic());
//        assertTrue(setTopic.getName().equals(assignedFlashcardSetViewModel.setTopicProperty().getName()));
//    }
//
//    @Test
//    void testSetNumberFlashcard() {
//        SimpleStringProperty numberFlashcard = new SimpleStringProperty(String.valueOf(testFlashcardSet3.getNumberFlashcards()));
//        assertTrue(numberFlashcard.getName().equals(assignedFlashcardSetViewModel.setNumberFlashcard().getName()));
//    }
//
//    @Test
//    void testUpdateEntity() {
//        assignedFlashcardSetViewModel.updateEntity();
//        assertEquals(testFlashcardSet3.getSetName(), assignedFlashcardSetViewModel.getSet().getSetName());
//        assertEquals(testFlashcardSet3.getSetTopic(), assignedFlashcardSetViewModel.getSet().getSetTopic());
//    }
//
//    @Test
//    void testGetSet() {
//        assertEquals(testFlashcardSet3, assignedFlashcardSetViewModel.getSet());
//    }
//
//}