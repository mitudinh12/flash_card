package com.flash_card.model.entity;

import static org.junit.jupiter.api.Assertions.*;

import com.flash_card.model.dao.FlashcardSetDao;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FlashcardSetTest {
    private FlashcardSet flashcardSet;
    private User flashcardCreator;
    private FlashcardSet testFlashCardSet;

    @BeforeEach
    void setUp() {
        flashcardCreator = new User("123", "Mock", "User", "mock.user@gmail.com", "2f7b9c4e-5d12-4a8f-bf6e-9c3d2a6b8e5f");
        flashcardSet = new FlashcardSet("Java Basics", "A set for Java beginners", "Programming", flashcardCreator);
    }

    @Test
    void testConstructor() {
        FlashcardSet newFlashcardSet = new FlashcardSet("Advanced Java", "A set for advanced Java learners", "Programming", flashcardCreator);

        assertNotNull(newFlashcardSet, "FlashcardSet should not be null");
        assertEquals("Advanced Java", newFlashcardSet.getSetName());
        assertEquals("A set for advanced Java learners", newFlashcardSet.getSetDescription());
        assertEquals("Programming", newFlashcardSet.getSetTopic());
        assertEquals(flashcardCreator, newFlashcardSet.getSetCreator());
        assertEquals(0, newFlashcardSet.getNumberFlashcards());
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
    void testGetSetName() {
        assertEquals("Java Basics", flashcardSet.getSetName());
    }

    @Test
    void testGetSetDescription() {
        assertEquals("A set for Java beginners", flashcardSet.getSetDescription());
    }

    @Test
    void testGetSetTopic() {
        assertEquals("Programming", flashcardSet.getSetTopic());
    }

    @Test
    void testGetNumberFlashcards() {
        assertEquals(0, flashcardSet.getNumberFlashcards());
    }

    @Test
    void testGetSetCreator() {
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

    @Test
    void testSetSetNameWithNull() {
        flashcardSet.setSetName(null);
        assertNull(flashcardSet.getSetName(), "Name should be null after setting null value");
    }

    @Test
    void testSetSetDescriptionWithNull() {
        flashcardSet.setSetDescription(null);
        assertNull(flashcardSet.getSetDescription(), "Description should be null after setting null value");
    }

    @Test
    void testSetSetTopicWithNull() {
        flashcardSet.setSetTopic(null);
        assertNull(flashcardSet.getSetTopic(), "Topic should be null after setting null value");
    }
}
