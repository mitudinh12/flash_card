package com.flash_card.model.dao;

import static org.junit.jupiter.api.Assertions.*;

import com.flash_card.model.entity.Flashcard;
import com.flash_card.model.entity.FlashcardSet;
import com.flash_card.model.entity.User;
import com.flash_card.framework.DifficultyLevel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;

class FlashcardDaoTest {

    private FlashcardDao flashcardDao;
    private Flashcard flashcard;
    private User user;
    private FlashcardSet flashcardSet;

    @BeforeEach
    void setUp() {
        flashcardDao = FlashcardDao.getInstance();
        user = new User("111082144844659073288", "Mock", "User", "mock.user@gmail.com", "2f7b9c4e-5d12-4a8f-bf6e-9c3d2a6b8e5f");
        flashcardSet = new FlashcardSet("Java Basics", "A set for Java beginners", "Programming", user);
        flashcard = new Flashcard("Java", "A programming language", DifficultyLevel.hard, flashcardSet, user);
    }

    @Test
    void testPersist() {
        flashcardDao.persist(flashcard);
        Flashcard retrievedFlashcard = flashcardDao.findById(flashcard.getCardId());
        assertNotNull(retrievedFlashcard);
        assertEquals("Java", retrievedFlashcard.getTerm());
    }

    @Test
    void testFindById() {
        flashcardDao.persist(flashcard);
        Flashcard foundFlashcard = flashcardDao.findById(flashcard.getCardId());
        assertNotNull(foundFlashcard);
        assertEquals(flashcard.getCardId(), foundFlashcard.getCardId());
    }

    @Test
    void testUpdate() {
        flashcardDao.persist(flashcard);
        flashcard.setDefinition("A widely-used programming language");
        flashcardDao.update(flashcard);
        Flashcard updatedFlashcard = flashcardDao.findById(flashcard.getCardId());
        assertEquals("A widely-used programming language", updatedFlashcard.getDefinition());
    }

//    @Test
//    void testFindByUserId() {
//        flashcardDao.persist(flashcard);
//        List<Flashcard> flashcards = flashcardDao.findByUserId((user.getUserId()));
//        assertFalse(flashcards.isEmpty());
//    }

    @Test
    void testFindBySetId() {
        flashcardDao.persist(flashcard);
        List<Flashcard> flashcards = flashcardDao.findBySetId(flashcardSet.getSetId());
        assertFalse(flashcards.isEmpty());
    }

    @Test
    void testDelete() {
        flashcardDao.persist(flashcard);
        flashcardDao.delete(flashcard);
        Flashcard deletedFlashcard = flashcardDao.findById(flashcard.getCardId());
        assertNull(deletedFlashcard);
    }
}