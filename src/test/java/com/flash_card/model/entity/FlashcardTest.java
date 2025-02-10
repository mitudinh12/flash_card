package com.flash_card.model.entity;

import static org.junit.jupiter.api.Assertions.*;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.flash_card.framework.DifficultyLevel;

import java.math.BigInteger;

class FlashcardTest extends TestSetupAbstract {
    private EntityManager entityManager;
    private Flashcard flashcard;
    private FlashcardSet mockFlashcardSet;
    private User mockUser;
    private Flashcard testFlashcard;

    @BeforeEach
    void setUp() {
        mockFlashcardSet = new FlashcardSet("Java", "A programming language", "Programming", mockUser);
        mockUser = new User("123", "Mock", "User", "mock.user@gmail.com", "2f7b9c4e-5d12-4a8f-bf6e-9c3d2a6b8e5f");
        flashcard = new Flashcard("Java", "A programming language", DifficultyLevel.hard, mockFlashcardSet, mockUser);
        this.testFlashcard = flashcardDao.findById(Integer.parseInt("1"));
        assertNotNull(testFlashcard, "Flashcard should be found in the database");
    }

    @Test
    void testEmptyConstructor() {
        Flashcard emptyFlashcard = new Flashcard(); // Using the no-arg constructor

        assertNotNull(emptyFlashcard, "Flashcard object should not be null");
        assertEquals(0, emptyFlashcard.getCardId(), "FlashcardId should be 0 for empty constructor");
        assertNull(emptyFlashcard.getDefinition(), "Definition should be null for empty constructor");
        assertNull(emptyFlashcard.getTerm(), "Term should be null for empty constructor");
        assertNull(emptyFlashcard.getDifficultLevel(), "Difficulty level should be null for empty constructor");
        assertNull(emptyFlashcard.getFlashcardSet(), "FS should be null for empty constructor");
        assertNull(emptyFlashcard.getFlashcardCreator(), "Creator should be null for empty constructor");
    }

    @Test
    void testGetters() {
        assertEquals("Java", flashcard.getTerm());
        assertEquals("A programming language", flashcard.getDefinition());
        assertEquals(DifficultyLevel.hard, flashcard.getDifficultLevel());
        assertEquals(mockFlashcardSet, flashcard.getFlashcardSet());
        assertEquals(mockUser, flashcard.getFlashcardCreator());
    }

    @Test
    void testSetTerm() {
        flashcard.setTerm("Python");
        assertEquals("Python", flashcard.getTerm());
    }

    @Test
    void testSetDefinition() {
        flashcard.setDefinition("Another programming language");
        assertEquals("Another programming language", flashcard.getDefinition());
    }

    @Test
    void testSetDifficultLevel() {
        flashcard.setDifficultLevel(DifficultyLevel.hard);
        assertEquals(DifficultyLevel.hard, flashcard.getDifficultLevel());
    }

    @Test
    void testSetFlashcardSet() {
        FlashcardSet newSet = new FlashcardSet("Java2", "A programming language2", "Programming2", mockUser);
        flashcard.setFlashcardSet(newSet);
        assertEquals(newSet, flashcard.getFlashcardSet());
    }

    @Test
    void testSetFlashcardCreator() {
        User newUser = new User("111082144844119073288", "Mock2", "User2", "mock.user2@gmail.com", "2f7b9c5e-5d12-4a8f-bf6e-9c3d2a6b8e5f");
        flashcard.setFlashcardCreator(newUser);
        assertEquals(newUser, flashcard.getFlashcardCreator());
    }
}



