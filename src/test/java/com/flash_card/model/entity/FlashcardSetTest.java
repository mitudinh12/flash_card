package com.flash_card.model.entity;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FlashcardSetTest {

    private FlashcardSet flashcardSet;
    private User flashcardCreator;

    @BeforeEach
    void setUp() {
        flashcardCreator = new User("111082144844659073288", "Mock", "User", "mock.user@gmail.com", "2f7b9c4e-5d12-4a8f-bf6e-9c3d2a6b8e5f");
        flashcardSet = new FlashcardSet("Java Basics", "A set for Java beginners", "Programming", flashcardCreator);
    }

    @Test
    void testGetters() {
        assertEquals("Java Basics", flashcardSet.getSetName());
        assertEquals("A set for Java beginners", flashcardSet.getSetDescription());
        assertEquals("Programming", flashcardSet.getSetTopic());
        assertEquals(0, flashcardSet.getNumberFlashcards());
        assertEquals(flashcardCreator, flashcardSet.getSetCreator());
    }

    @Test
    void testSetSetName() {
        flashcardSet.setSetName("Advanced Java");
        assertEquals("Advanced Java", flashcardSet.getSetName());
    }

    @Test
    void testSetSetDescription() {
        flashcardSet.setSetDescription("A set for advanced Java concepts");
        assertEquals("A set for advanced Java concepts", flashcardSet.getSetDescription());
    }

    @Test
    void testSetSetTopic() {
        flashcardSet.setSetTopic("Computer Science");
        assertEquals("Computer Science", flashcardSet.getSetTopic());
    }

    @Test
    void testAddNumberFlashcards() {
        flashcardSet.addNumberFlashcards();
        assertEquals(1, flashcardSet.getNumberFlashcards());
    }

    @Test
    void testSubtractNumberFlashcard() {
        flashcardSet.addNumberFlashcards();
        flashcardSet.subtractNumberFlashcard();
        assertEquals(0, flashcardSet.getNumberFlashcards());
    }
}