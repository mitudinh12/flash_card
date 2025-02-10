package com.flash_card.model.entity;

import static org.junit.jupiter.api.Assertions.*;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FlashcardSetTest extends TestSetupAbstract {
    private FlashcardSet flashcardSet;
    private User flashcardCreator;
    private FlashcardSet testFlashCardSet;

    @BeforeEach
    void setUp() {
        flashcardCreator = new User("123", "Mock", "User", "mock.user@gmail.com", "2f7b9c4e-5d12-4a8f-bf6e-9c3d2a6b8e5f");
        flashcardSet = new FlashcardSet("Java Basics", "A set for Java beginners", "Programming", flashcardCreator);

        this.testFlashCardSet = flashcardSetDao.findById(Integer.parseInt("1"));
        assertNotNull(testFlashCardSet, "FlashcardSet should be found in the database");
    }
    @Test
    void testEmptyConstructor() {
        FlashcardSet emptyFlashcardSet = new FlashcardSet(); // Using the no-arg constructor

        assertNotNull(emptyFlashcardSet, "Flashcard set object should not be null");
        assertEquals(0, emptyFlashcardSet.getSetId(), "FlashcardSet Id should be 0 for empty constructor");
        assertNull(emptyFlashcardSet.getSetName(), "Name should be null for empty constructor");
        assertNull(emptyFlashcardSet.getSetDescription(), "Description should be null for empty constructor");
        assertNull(emptyFlashcardSet.getSetTopic(), "Topic should be null for empty constructor");
        assertEquals(0, emptyFlashcardSet.getNumberFlashcards(), "Flashcard numbers should be null for empty constructor");
        assertNull(emptyFlashcardSet.getSetCreator(), "Creator should be null for empty constructor");
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