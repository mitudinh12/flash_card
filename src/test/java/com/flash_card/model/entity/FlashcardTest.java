package com.flash_card.model.entity;

import static org.junit.jupiter.api.Assertions.*;

import com.flash_card.framework.DifficultyLevel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FlashcardTest extends TestSetupAbstract {
    private Flashcard flashcard;
    private FlashcardSet mockFlashcardSet;
    private User mockUser;

    @BeforeEach
    void setUp() {
        mockUser = new User("123", "Mock", "User", "mock.user@gmail.com", "2f7b9c4e-5d12-4a8f-bf6e-9c3d2a6b8e5f");
        mockFlashcardSet = new FlashcardSet("Java", "A programming language", "Programming", mockUser);
        flashcard = new Flashcard("Java", "A programming language", DifficultyLevel.hard, mockFlashcardSet, mockUser);
    }

    @Test
    void testEmptyConstructor() {
        Flashcard emptyFlashcard = new Flashcard();
        assertNotNull(emptyFlashcard, "Flashcard object should not be null");
        assertEquals(0, emptyFlashcard.getCardId(), "FlashcardId should be 0 for empty constructor");
        assertNull(emptyFlashcard.getDefinition(), "Definition should be null for empty constructor");
        assertNull(emptyFlashcard.getTerm(), "Term should be null for empty constructor");
        assertNull(emptyFlashcard.getDifficultLevel(), "Difficulty level should be null for empty constructor");
        assertNull(emptyFlashcard.getFlashcardSet(), "FS should be null for empty constructor");
        assertNull(emptyFlashcard.getFlashcardCreator(), "Creator should be null for empty constructor");
    }

    @Test
    void testGetCardId() {
        assertEquals(0, flashcard.getCardId(), "Card ID should be 0 for a new flashcard");
    }

    @Test
    void testGetTerm() {
        assertEquals("Java", flashcard.getTerm(), "Term should be Java");
    }

    @Test
    void testGetDefinition() {
        assertEquals("A programming language", flashcard.getDefinition(), "Definition should be A programming language");
    }

    @Test
    void testGetDifficultLevel() {
        assertEquals(DifficultyLevel.hard, flashcard.getDifficultLevel(), "Difficulty level should be hard");
    }

    @Test
    void testGetFlashcardSet() {
        assertEquals(mockFlashcardSet, flashcard.getFlashcardSet(), "Flashcard set should be Java");
    }

    @Test
    void testGetFlashcardCreator() {
        assertEquals(mockUser, flashcard.getFlashcardCreator(), "Flashcard creator should be Mock User");
    }

    @Test
    void testSetTerm() {
        flashcard.setTerm("Python");
        assertEquals("Python", flashcard.getTerm(), "Term should be Python");
    }

    @Test
    void testSetDefinition() {
        flashcard.setDefinition("Another programming language");
        assertEquals("Another programming language", flashcard.getDefinition(), "Definition should be Another programming language");
    }

    @Test
    void testSetDifficultLevel() {
        flashcard.setDifficultLevel(DifficultyLevel.easy);
        assertEquals(DifficultyLevel.easy, flashcard.getDifficultLevel(), "Difficulty level should be easy");
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
