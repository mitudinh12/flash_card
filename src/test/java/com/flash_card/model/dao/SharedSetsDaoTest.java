package com.flash_card.model.dao;

import static org.junit.jupiter.api.Assertions.*;

import com.flash_card.model.entity.SharedSet;
import com.flash_card.model.entity.User;
import com.flash_card.model.entity.FlashcardSet;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;
import java.util.List;
import java.util.UUID;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SharedSetsDaoTest {
    private static EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("FlashcardMariaDbUnitTest");
    private static EntityManager entityManager = entityManagerFactory.createEntityManager();
    private SharedSetsDao sharedSetDao = SharedSetsDao.getInstance(entityManager);
    private UserDao userDao = UserDao.getInstance(entityManager);
    private FlashcardSetDao flashcardSetDao = FlashcardSetDao.getInstance(entityManager);
    private User testUser;
    private FlashcardSet testFlashcardSet;
    private SharedSet testSharedSet;

    @BeforeEach
    void setUp() {
        testUser = new User(UUID.randomUUID().toString(), "Mock3", "User3", "mock.user" + System.currentTimeMillis() + "@gmail.com", "227b9c4e-5d12-4a8f-bf6e-9c3d2a6b8e5f");
        userDao.persist(testUser);
        assertNotNull(userDao.findById(testUser.getUserId()), "User should be found in the database");

        testFlashcardSet = new FlashcardSet("Java Basics", "A set for Java beginners", "Programming", testUser);
        flashcardSetDao.persist(testFlashcardSet);
        assertNotNull(flashcardSetDao.findById(testFlashcardSet.getSetId()), "Flashcard set should be found in the database");

        testSharedSet = new SharedSet(testUser, testFlashcardSet);
        sharedSetDao.persist(testSharedSet);
    }

    @AfterEach
    void tearDown() {
        SharedSet foundSet = sharedSetDao.findById(testSharedSet.getSharingId());
        if (foundSet != null) {
            sharedSetDao.delete(foundSet);
        }
        FlashcardSet foundFlashcardSet = flashcardSetDao.findById(testFlashcardSet.getSetId());
        if (foundFlashcardSet != null) {
            flashcardSetDao.delete(foundFlashcardSet);
        }
        User foundUser = userDao.findById(testUser.getUserId());
        if (foundUser != null) {
            userDao.delete(foundUser);
        }
    }

    @Test
    @Order(1)
    void testPersist() {
        int sharingId = testSharedSet.getSharingId();
        assertNotEquals(0, sharingId, "SharedSet ID should be generated and not 0");
        SharedSet invalidSharedSet = new SharedSet(null, null);
        assertFalse(sharedSetDao.persist(invalidSharedSet), "Should return false when exception is thrown");
    }

    @Test
    @Order(2)
    void testFindById() {
        assertNotNull(sharedSetDao.findById(testSharedSet.getSharingId()), "Should return shared set when found");
        assertNull(sharedSetDao.findById(0), "Should return null when shared set is not found");
    }

    @Test
    @Order(3)
    void testDelete() {
        assertTrue(sharedSetDao.delete(testSharedSet), "Should return true when shared set is deleted");
        assertFalse(sharedSetDao.delete(null), "Should return false when exception is thrown");
    }

    @Test
    @Order(4)
    void testFindByUserId() {
        List<SharedSet> sharedSets = sharedSetDao.findByUserId(testUser.getUserId());
        assertNotNull(sharedSets, "Should return a list, even if empty");
        assertFalse(sharedSets.isEmpty(), "Should contain at least one shared set");
        assertEquals(testSharedSet.getSharingId(), sharedSets.get(0).getSharingId(), "Returned shared set ID should match the persisted set");

        List<SharedSet> emptySets = sharedSetDao.findByUserId("nonexistent");
        assertNotNull(emptySets, "Should return a list, even if empty");
        assertTrue(emptySets.isEmpty(), "Should return an empty list when no shared sets match");
    }

    @Test
    @Order(5)
    void testFindBySetIdAndUserId() {
        SharedSet retrievedSharedSet = sharedSetDao.findBySetIdAndUserId(testFlashcardSet.getSetId(), testUser.getUserId());
        assertNotNull(retrievedSharedSet, "Should return a shared set when found");
        assertEquals(testFlashcardSet.getSetId(), retrievedSharedSet.getFlashcardSet().getSetId(), "Returned flashcard set ID should match");
        assertEquals(testUser.getUserId(), retrievedSharedSet.getUser().getUserId(), "Returned user ID should match");
    }

    @Test
    @Order(6)
    void testFindBySetIdAndUserIdNoResults() {
        SharedSet sharedSetResult = sharedSetDao.findBySetIdAndUserId(testFlashcardSet.getSetId(), "invalidUserId");
        assertNull(sharedSetResult, "Should return null when no shared set matches the criteria");
    }
}