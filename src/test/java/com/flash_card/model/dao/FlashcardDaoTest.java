package com.flash_card.model.dao;

import static org.junit.jupiter.api.Assertions.*;

import com.flash_card.model.entity.Flashcard;
import com.flash_card.model.entity.FlashcardSet;
import com.flash_card.model.entity.TestSetupAbstract;
import com.flash_card.model.entity.User;
import com.flash_card.framework.DifficultyLevel;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.UUID;

class FlashcardDaoTest extends TestSetupAbstract {
    private EntityManager entityManager;
    private FlashcardDao flashcardDao;
    private Flashcard flashcard;
    private Flashcard flashcard2;
    private User user;
    private FlashcardSet flashcardSet;

    @BeforeEach
    void setUp() {
        String uniqueUserId = UUID.randomUUID().toString();
        String uniqueEmail = "mock.user" + System.currentTimeMillis() + "@gmail.com";
        user = new User(uniqueUserId, "Mock", "User", uniqueEmail, UUID.randomUUID().toString());
        flashcardSet = new FlashcardSet("Java Basics", "A set for Java beginners", "Programming", user);
        flashcard = new Flashcard("Java", "A programming language", flashcardSet, user);
        flashcard2 = new Flashcard("Java2", "A programming language2", flashcardSet, user);

        entityManager = entityManagerFactory.createEntityManager();
        flashcardDao = FlashcardDao.getInstance(entityManager);

        entityManager.getTransaction().begin();
        entityManager.persist(user);
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
        assertNotNull(retrievedFlashcard, "Persisted flashcard should not be null");
        assertEquals("Java", retrievedFlashcard.getTerm(), "Term should match");
        assertEquals(flashcardSet.getSetId(), retrievedFlashcard.getFlashcardSet().getSetId(), "Flashcard should belong to the correct set");
    }

    @Test
    void testFindById() {
        Flashcard foundFlashcard = flashcardDao.findById(flashcard.getCardId());
        assertNotNull(foundFlashcard, "Flashcard should not be null");
        assertEquals(flashcard.getCardId(), foundFlashcard.getCardId(), "Flashcard ID should match");
        assertNull(flashcardDao.findById(99999), "Flashcard should be null for invalid ID");
    }

    @Test
    void testFindByIdInvalidId() {
        Flashcard foundFlashcard = flashcardDao.findById(-1);
        assertNull(foundFlashcard, "Flashcard should be null for invalid ID");
    }

    @Test
    void testUpdate() {
        flashcard.setDefinition("A widely-used programming language");
        assertTrue(flashcardDao.update(flashcard), "Flashcard update should succeed");
        Flashcard updatedFlashcard = flashcardDao.findById(flashcard.getCardId());
        assertEquals("A widely-used programming language", updatedFlashcard.getDefinition(), "Updated definition should match");
        Flashcard nullFlashcard = null;
        assertFalse(flashcardDao.update(nullFlashcard), "Flashcard update should fail for null flashcard");
    }

    @Test
    void testDelete() {
        assertTrue(flashcardDao.delete(flashcard2), "Flashcard deletion should succeed");
        assertNull(flashcardDao.findById(flashcard2.getCardId()), "Deleted flashcard should be null");
        Flashcard nullFlashcard = null;
        assertFalse(flashcardDao.delete(nullFlashcard), "Flashcard deletion should fail for null flashcard");
    }

    @Test
    void testFindBySetId() {
        List<Flashcard> flashcards = flashcardDao.findBySetId(flashcardSet.getSetId());
        assertFalse(flashcards.isEmpty(), "Flashcards should not be empty for valid set ID");
        assertEquals(flashcardSet.getSetId(), flashcards.get(0).getFlashcardSet().getSetId(), "Returned flashcards should belong to the specified set");
    }

    @Test
    void testFindBySetIdInvalidSetId() {
        List<Flashcard> flashcards = flashcardDao.findBySetId(-1);
        assertTrue(flashcards.isEmpty(), "Flashcards should be empty for invalid set ID");
    }

/*
    @Test
    void testGetHardFlashcards() {
        List<Flashcard> hardFlashcards = flashcardDao.getHardFlashcards(flashcardSet.getSetId());
        assertFalse(hardFlashcards.isEmpty(), "There should be hard flashcards in the set");
        assertEquals(DifficultyLevel.hard, hardFlashcards.get(0).getDifficultLevel(), "Returned flashcards should have difficulty level 'hard'");
    }

    @Test
    void testGetHardFlashcardsNoResults() {
        Flashcard easyFlashcard = new Flashcard("Python", "Another language", DifficultyLevel.easy, flashcardSet, user);
        entityManager.getTransaction().begin();
        entityManager.persist(easyFlashcard);
        entityManager.getTransaction().commit();

        List<Flashcard> hardFlashcards = flashcardDao.getHardFlashcards(flashcardSet.getSetId());
        assertEquals(1, hardFlashcards.size(), "There should be only one hard flashcard");
    }
*/

    @AfterEach
    void tearDown() {
        if (entityManager.isOpen()) {
            entityManager.close();
        }
    }
}