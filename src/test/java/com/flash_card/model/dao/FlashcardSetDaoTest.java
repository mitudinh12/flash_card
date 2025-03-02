package com.flash_card.model.dao;

import com.flash_card.model.entity.FlashcardSet;
import com.flash_card.model.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FlashcardSetDaoTest {
    private static EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("FlashcardMariaDbUnitTest");
    private static EntityManager entityManager = entityManagerFactory.createEntityManager();
    private FlashcardSetDao flashcardSetDao = FlashcardSetDao.getInstance(entityManager);
    private UserDao userDao = UserDao.getInstance(entityManager);
    private User testUser;
    private FlashcardSet testFlashcardSet;

    @BeforeEach
    void setUp() {
        testUser = new User("123", "Jane", "Doe", "jane.doe@gmail.com", "abcdef");
        userDao.persist(testUser);
        assertNotNull(userDao.findById("123"), "User should be found in the database");

        testFlashcardSet = new FlashcardSet("Python Basics", "A set for Python beginners", "Programming", testUser);
        flashcardSetDao.persist(testFlashcardSet);
//        assertNotNull(flashcardSetDao.findById(testFlashcardSet.getSetId()), "Flashcard set should be found in the database");
    }

    @AfterEach
    void tearDown() {
        FlashcardSet foundSet = flashcardSetDao.findById(testFlashcardSet.getSetId());
        if (foundSet != null) {
            flashcardSetDao.delete(foundSet);
        }
        User foundUser = userDao.findById(testUser.getUserId());
        if (foundUser != null) {
            userDao.delete(foundUser);
        }
    }

    @Test
    @Order(1)
    void testPersist() {
        int setId = testFlashcardSet.getSetId();
        assertNotEquals(0, setId, "FlashcardSet ID should be generated and not 0");
        FlashcardSet invalidSet = null;
        assertFalse(flashcardSetDao.persist(invalidSet), "Should return false when exception is thrown");
    }

    @Test
    @Order(2)
    void testFindById() {
        assertNotNull(flashcardSetDao.findById(testFlashcardSet.getSetId()), "Should return flashcard set when found");
        assertNull(flashcardSetDao.findById(0), "Should return null when flashcard set is not found");
    }

    @Test
    @Order(3)
    void testUpdate() {
        testFlashcardSet.setSetName("Advanced Python");
        assertTrue(flashcardSetDao.update(testFlashcardSet), "Should return true when flashcard set is updated");
        assertEquals("Advanced Python", flashcardSetDao.findById(testFlashcardSet.getSetId()).getSetName(), "Should reflect the updated name");
        assertFalse(flashcardSetDao.update(null), "Should return false when exception is thrown");
    }

    @Test
    @Order(4)
    void testDelete() {
        assertTrue(flashcardSetDao.delete(testFlashcardSet), "Should return true when flashcard set is deleted");
        assertFalse(flashcardSetDao.delete(null), "Should return false when exception is thrown");
    }

    @Test
    @Order(5)
    void testFindByUserId() {
        FlashcardSet testFlashcardSet00 = new FlashcardSet("Python Basics 00", "A set for Python beginners", "Programming", testUser);
        flashcardSetDao.persist(testFlashcardSet00);

        List<FlashcardSet> sets = flashcardSetDao.findByUserId(testUser.getUserId());
        assertNotNull(sets, "Should return a list, even if empty");
        assertFalse(sets.isEmpty(), "Should contain at least one flashcard set");
        assertEquals(testFlashcardSet00.getSetId(), sets.getLast().getSetId(), "Returned flashcard set ID should match the persisted set");

        List<FlashcardSet> emptySets = flashcardSetDao.findByUserId("nonexistent");
        assertNotNull(emptySets, "Should return a list, even if empty");
        assertTrue(emptySets.isEmpty(), "Should return an empty list when no flashcard sets match");
    }
}