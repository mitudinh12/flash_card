package com.flash_card.model.entity;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.flash_card.framework.DifficultyLevel;

class FlashcardTest {

    private Flashcard flashcard;
    private FlashcardSet mockFlashcardSet;
    private User mockUser;

    @BeforeEach
    void setUp() {
        mockFlashcardSet = new FlashcardSet("Java", "A programming language", "Programming", mockUser);
        mockUser = new User("111082144844659073288", "Mock", "User", "mock.user@gmail.com", "2f7b9c4e-5d12-4a8f-bf6e-9c3d2a6b8e5f");
        flashcard = new Flashcard("Java", "A programming language", DifficultyLevel.hard, mockFlashcardSet, mockUser);
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



