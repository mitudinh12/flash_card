package com.flash_card.model.dao;

import static org.junit.jupiter.api.Assertions.*;

import com.flash_card.model.entity.SharedSet;
import com.flash_card.model.entity.TestSetupAbstract;
import com.flash_card.model.entity.User;
import com.flash_card.model.entity.FlashcardSet;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.UUID;

class SharedSetsDaoTest extends TestSetupAbstract {
    private EntityManager entityManager;
    private SharedSetsDao sharedSetDao;
    private SharedSet sharedSet;
    private User user3;
    private FlashcardSet flashcardSet;

    @BeforeEach
    void setUp() {
        String uniqueUserId = UUID.randomUUID().toString();
        String uniqueEmail = "mock.user" + System.currentTimeMillis() + "@gmail.com";
        user3 = new User(uniqueUserId, "Mock3", "User3", uniqueEmail, "227b9c4e-5d12-4a8f-bf6e-9c3d2a6b8e5f");
        flashcardSet = new FlashcardSet("Java Basics", "A set for Java beginners", "Programming", user3);
        sharedSet = new SharedSet(user3, flashcardSet);

        entityManager = entityManagerFactory.createEntityManager();
        sharedSetDao = SharedSetsDao.getInstance(entityManager);

        entityManager.getTransaction().begin();
        entityManager.persist(user3);
        entityManager.persist(flashcardSet);
        entityManager.persist(sharedSet);
        entityManager.getTransaction().commit();
    }

    @Test
    void testGetInstance() {
        SharedSetsDao instance1 = SharedSetsDao.getInstance(entityManager);
        SharedSetsDao instance2 = SharedSetsDao.getInstance(entityManager);
        assertNotNull(instance1, "SharedSetsDao instance should not be null");
        assertSame(instance1, instance2, "SharedSetsDao should follow singleton pattern");
    }

    @Test
    void testPersist() {
        SharedSet retrievedSharedSet = sharedSetDao.findById(sharedSet.getSharingId());
        assertNotNull(retrievedSharedSet);
        assertEquals(user3.getUserId(), retrievedSharedSet.getUser().getUserId());
        assertEquals(flashcardSet.getSetId(), retrievedSharedSet.getFlashcardSet().getSetId());
    }


    @Test
    void testFindById() {
        SharedSet foundSharedSet = sharedSetDao.findById(sharedSet.getSharingId());
        assertNotNull(foundSharedSet);
        assertEquals(sharedSet.getSharingId(), foundSharedSet.getSharingId());
    }

    @Test
    void testFindByUserId() {
        List<SharedSet> sharedSets = sharedSetDao.findByUserId(user3.getUserId());
        assertFalse(sharedSets.isEmpty(), "SharedSets should not be empty for valid userId");
    }

    @Test
    void testFindByUserIdNoResults() {
        List<SharedSet> sharedSets = sharedSetDao.findByUserId("invalidUserId");
        assertTrue(sharedSets.isEmpty(), "SharedSets should be empty for invalid userId");
    }

    @Test
    void testDelete() {
        sharedSetDao.delete(sharedSetDao.findById(sharedSet.getSharingId()));
        SharedSet deletedSharedSet = sharedSetDao.findById(sharedSet.getSharingId());
        assertNull(deletedSharedSet, "Deleted shared set should return null");
    }

    @Test
    void testFindBySetIdAndUserId() {
        SharedSet retrievedSharedSet = sharedSetDao.findBySetIdAndUserId(flashcardSet.getSetId(), user3.getUserId());
        assertNotNull(retrievedSharedSet);
        assertEquals(flashcardSet.getSetId(), retrievedSharedSet.getFlashcardSet().getSetId());
        assertEquals(user3.getUserId(), retrievedSharedSet.getUser().getUserId());
    }

    @Test
    void testFindBySetIdAndUserIdNoResults() {
        SharedSet sharedSetResult = sharedSetDao.findBySetIdAndUserId(flashcardSet.getSetId(), "invalidUserId");
        assertNull(sharedSetResult, "No shared set should be found for an invalid userId");
    }


    @AfterEach
    void tearDown() {
        entityManager.close();
    }
}
