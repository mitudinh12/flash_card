package com.flash_card.model.dao;

import com.flash_card.model.entity.Study;
import com.flash_card.model.entity.User;
import com.flash_card.model.entity.FlashcardSet;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class StudyDaoTest {
    private User testUser = new User("123456", "John", "Doe", "abcdef@example.com", "password");
    private FlashcardSet testFlashcardSet = new FlashcardSet("Sample Set", "Description", "Sample Topic", testUser);
    protected static EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("FlashcardMariaDbUnitTest");
    protected static EntityManager entityManager = entityManagerFactory.createEntityManager();
    private StudyDao studyDao = StudyDao.getInstance(entityManager);
    private UserDao userDao = UserDao.getInstance(entityManager);
    private FlashcardSetDao flashcardSetDao = FlashcardSetDao.getInstance(entityManager);
    private Study study;
    private boolean result;

    @BeforeEach
    void setUp() {
        Study study1 = new Study(testUser, testFlashcardSet, LocalDateTime.now(), LocalDateTime.now().plusHours(1), 20);
        userDao.persist(testUser);
        flashcardSetDao.persist(testFlashcardSet);
        result= studyDao.persist(study1);
        study = studyDao.findByUserIdAndSetId(testUser.getUserId(), testFlashcardSet.getSetId());
    }

    @AfterEach
    void tearDown() {

        if (study == null) {
            return;
        }
        studyDao.delete(study);
        flashcardSetDao.delete(testFlashcardSet);
        userDao.delete(testUser);
    }

    @Test
    @Order(1)
    void testPersistStudy() {
        assertTrue(result, "Should return true when study is persisted");
        assertFalse(studyDao.persist(null), "Should return false when exception is thrown");
    }

    @Test
    @Order(2)
    void testDeleteStudy() {
        assertTrue(studyDao.delete(study), "Should return true when study is deleted");
        assertFalse(studyDao.delete(null), "Should return false when exception is thrown");
    }

    @Test
    @Order(3)
    void testUpdateStudy() {
        study.setNumberStudiedWords(30);
        assertTrue(studyDao.update(study), "Should return true when study is updated");
        assertFalse(studyDao.update(null), "Should return false when exception is thrown");
    }

    @Test
    @Order(4)
    void testFindById() {
        Study foundStudy = studyDao.findById(study.getStudyId());
        assertNotNull(foundStudy, "Persisted study should be found");
    }

    @Test
    @Order(5)
    void testFindByUserIdAndSetId() {
        Study foundStudy = studyDao.findByUserIdAndSetId(testUser.getUserId(), testFlashcardSet.getSetId());
        assertNotNull(foundStudy, "Persisted study should be found by user ID and set ID");
    }
}