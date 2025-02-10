package com.flash_card.model.dao;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import com.flash_card.model.entity.FlashcardSet;
import com.flash_card.model.entity.TestSetupAbstract;
import com.flash_card.model.entity.User;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.UUID;

class FlashcardSetDaoTest extends TestSetupAbstract {
    private EntityManager entityManager;
    private FlashcardSetDao flashcardSetDao;
    private FlashcardSet flashcardSet;
    private User user2;

    @BeforeEach
    void setUp() {
        String uniqueUserId = UUID.randomUUID().toString();
        String uniqueEmail = "mock.user" + System.currentTimeMillis() + "@gmail.com";
        user2 = new User(uniqueUserId, "Mock2", "User2", uniqueEmail, "227b9c4e-5d12-4a8f-bf6e-9c3d2a6b8e5f");
        flashcardSet = new FlashcardSet("Java Basics2", "A set for Java beginners2", "Programming2", user2);

        entityManager = entityManagerFactory.createEntityManager();
        flashcardSetDao = FlashcardSetDao.getInstance(entityManager);

        entityManager.getTransaction().begin();
        entityManager.persist(user2);
        entityManager.persist(flashcardSet);
        entityManager.getTransaction().commit();
    }


    @Test
    void testGetInstance() {
        FlashcardSetDao instance1 = FlashcardSetDao.getInstance(entityManager);
        FlashcardSetDao instance2 = FlashcardSetDao.getInstance(entityManager);
        assertNotNull(instance1, "FlashcardSetDao instance should not be null");
        assertSame(instance1, instance2, "FlashcardSetDao should follow singleton pattern");
    }

    @Test
    void testPersist() {
        FlashcardSet retrievedSet = flashcardSetDao.findById(flashcardSet.getSetId());
        assertNotNull(retrievedSet);
        assertEquals("Java Basics2", retrievedSet.getSetName());
    }

    @Test
    void testFindById() {
        FlashcardSet foundSet = flashcardSetDao.findById(flashcardSet.getSetId());
        assertNotNull(foundSet);
        assertEquals(flashcardSet.getSetId(), foundSet.getSetId());
    }

    @Test
    void testUpdate() {
        flashcardSet.setSetName("Advanced Java");
        flashcardSetDao.update(flashcardSet);
        FlashcardSet updatedSet = flashcardSetDao.findById(flashcardSet.getSetId());
        assertEquals("Advanced Java", updatedSet.getSetName());
    }

    @Test
    void testFindByUserId() {
        List<FlashcardSet> flashcardSets = flashcardSetDao.findByUserId(user2.getUserId());
        assertFalse(flashcardSets.isEmpty());
    }

    @Test
    void testDelete() {
        flashcardSetDao.delete(flashcardSetDao.findById(flashcardSet.getSetId()));
        FlashcardSet deletedSet = flashcardSetDao.findById(flashcardSet.getSetId());
        assertNull(deletedSet);
    }


}