package com.flash_card.model.entity;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SharedSetTest extends TestSetupAbstract {
    private SharedSet sharedSet;
    private User mockUser;
    private FlashcardSet mockFlashcardSet;

    @BeforeEach
    void setUp() {
        mockUser = new User("123", "Mock", "User", "mock.user@gmail.com", "2f7b9c4e-5d12-4a8f-bf6e-9c3d2a6b8e5f");
        mockFlashcardSet = new FlashcardSet("Java Basics", "A set for Java beginners", "Programming", mockUser);
        sharedSet = new SharedSet(mockUser, mockFlashcardSet);
    }

    @Test
    void testEmptyConstructor() {
        SharedSet emptySharedSet = new SharedSet();
        assertNotNull(emptySharedSet, "SharedSet object should not be null");
        assertEquals(0, emptySharedSet.getSharingId(), "Sharing ID should be 0 for empty constructor");
        assertNull(emptySharedSet.getUser(), "User should be null for empty constructor");
        assertNull(emptySharedSet.getFlashcardSet(), "FlashcardSet should be null for empty constructor");
    }

    @Test
    void testGetters() {
        assertEquals(mockUser, sharedSet.getUser());
        assertEquals(mockFlashcardSet, sharedSet.getFlashcardSet());
    }

    @Test
    void testSetUser() {
        User newUser = new User("456", "New", "User", "new.user@gmail.com", "3f7c9d5e-5d12-4a8f-bf6e-9c3d2a6b8e5f");
        sharedSet.setUser(newUser);
        assertEquals(newUser, sharedSet.getUser());
    }

    @Test
    void testSetFlashcardSet() {
        FlashcardSet newSet = new FlashcardSet("Advanced Java", "For experienced developers", "Programming", mockUser);
        sharedSet.setFlashcardSet(newSet);
        assertEquals(newSet, sharedSet.getFlashcardSet());
    }
}
