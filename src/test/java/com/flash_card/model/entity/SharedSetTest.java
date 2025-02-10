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
    void testConstructorWithValidParams() {
        SharedSet validSharedSet = new SharedSet(mockUser, mockFlashcardSet);
        assertEquals(mockUser, validSharedSet.getUser(), "User should be correctly set in constructor");
        assertEquals(mockFlashcardSet, validSharedSet.getFlashcardSet(), "FlashcardSet should be correctly set in constructor");
    }

    @Test
    void testGetUser() {
        assertEquals(mockUser, sharedSet.getUser(), "User should match the one passed in constructor");
    }

    @Test
    void testGetFlashcardSet() {
        assertEquals(mockFlashcardSet, sharedSet.getFlashcardSet(), "FlashcardSet should match the one passed in constructor");
    }

    @Test
    void testSetUser() {
        User newUser = new User("456", "New", "User", "new.user@gmail.com", "3f7c9d5e-5d12-4a8f-bf6e-9c3d2a6b8e5f");
        sharedSet.setUser(newUser);
        assertEquals(newUser, sharedSet.getUser(), "User should be updated correctly");
    }

    @Test
    void testSetFlashcardSet() {
        FlashcardSet newSet = new FlashcardSet("Advanced Java", "For experienced developers", "Programming", mockUser);
        sharedSet.setFlashcardSet(newSet);
        assertEquals(newSet, sharedSet.getFlashcardSet(), "FlashcardSet should be updated correctly");
    }

    @Test
    void testSetUserWithNull() {
        sharedSet.setUser(null);
        assertNull(sharedSet.getUser(), "User should be null after setting it to null");
    }

    @Test
    void testSetFlashcardSetWithNull() {
        sharedSet.setFlashcardSet(null);
        assertNull(sharedSet.getFlashcardSet(), "FlashcardSet should be null after setting it to null");
    }

}
