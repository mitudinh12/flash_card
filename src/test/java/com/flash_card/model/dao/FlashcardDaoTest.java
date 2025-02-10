package com.flash_card.model.dao;

import static org.junit.jupiter.api.Assertions.*;

import com.flash_card.model.entity.Flashcard;
import com.flash_card.model.entity.FlashcardSet;
import com.flash_card.model.entity.TestSetupAbstract;
import com.flash_card.model.entity.User;
import com.flash_card.framework.DifficultyLevel;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;

class FlashcardDaoTest extends TestSetupAbstract {
    private EntityManager entityManager;
    private FlashcardDao flashcardDao;
    private Flashcard flashcard;
    private User user;
    private FlashcardSet flashcardSet;

    @BeforeEach
    void setUp() {
        user = new User("123", "Mock", "User", "mock.user@gmail.com", "2f7b9c4e-5d12-4a8f-bf6e-9c3d2a6b8e5f");
        flashcardSet = new FlashcardSet("Java Basics", "A set for Java beginners", "Programming", user);
        flashcard = new Flashcard("Java", "A programming language", DifficultyLevel.hard, flashcardSet, user);

        entityManager = entityManagerFactory.createEntityManager();
        flashcardDao = FlashcardDao.getInstance(entityManager);
        entityManager.getTransaction().begin();
        entityManager.persist(flashcardSet);
        entityManager.persist(flashcard);
        entityManager.getTransaction().commit();
    }

    @Test
    void testGetInstance() {
        FlashcardDao instance1 = FlashcardDao.getInstance(entityManager);
        FlashcardDao instance2 = FlashcardDao.getInstance(entityManager);
        assertNotNull(instance1, "FlashcardDao instance should not be null");
        assertSame(instance1, instance2, "FlashcardDao should follow singleton pattern");
    }

    @Test
    void testPersist() {
        Flashcard retrievedFlashcard = flashcardDao.findById(flashcard.getCardId());
        assertNotNull(retrievedFlashcard);
        assertEquals("Java", retrievedFlashcard.getTerm());
    }

    @Test
    void testFindById() {
        Flashcard foundFlashcard = flashcardDao.findById(flashcard.getCardId());
        assertNotNull(foundFlashcard);
        assertEquals(flashcard.getCardId(), foundFlashcard.getCardId());
    }

    @Test
    void testUpdate() {
        flashcard.setDefinition("A widely-used programming language");
        flashcardDao.update(flashcard);
        Flashcard updatedFlashcard = flashcardDao.findById(flashcard.getCardId());
        assertEquals("A widely-used programming language", updatedFlashcard.getDefinition());
    }

    @Test
    void testFindBySetId() {
        List<Flashcard> flashcards = flashcardDao.findBySetId(flashcardSet.getSetId());
        assertFalse(flashcards.isEmpty());
    }

    @Test
    void testDelete() {
        flashcardDao.delete(flashcardDao.findById(flashcard.getCardId()));
        Flashcard deletedFlashcard = flashcardDao.findById(flashcard.getCardId());
        assertNull(deletedFlashcard);
    }
}