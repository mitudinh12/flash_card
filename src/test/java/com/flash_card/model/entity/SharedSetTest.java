package com.flash_card.model.entity;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SharedSetTest {

    private SharedSet sharedSet;
    private User user;
    private FlashcardSet flashcardSet;

    @BeforeEach
    void setUp() {
        user = new User("111082144844659073288", "Mock", "User", "mock.user@gmail.com", "2f7b9c4e-5d12-4a8f-bf6e-9c3d2a6b8e5f");
        flashcardSet = new FlashcardSet("Java Basics", "A set for Java beginners", "Programming", user);
        sharedSet = new SharedSet(user, flashcardSet);
    }

    @Test
    void testGetters() {
        assertEquals(user, sharedSet.getUser());
        assertEquals(flashcardSet, sharedSet.getFlashcardSet());
    }

    @Test
    void testSetUser() {
        User newUser = new User();
        sharedSet.setUser(newUser);
        assertEquals(newUser, sharedSet.getUser());
    }

    @Test
    void testSetFlashcardSet() {
        FlashcardSet newSet = new FlashcardSet();
        sharedSet.setFlashcardSet(newSet);
        assertEquals(newSet, sharedSet.getFlashcardSet());
    }
}
