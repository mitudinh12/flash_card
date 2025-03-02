package com.flash_card.model.entity;

import com.flash_card.model.dao.StudyDao;
import com.flash_card.model.dao.UserDao;
import com.flash_card.model.dao.FlashcardSetDao;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class StudyTest {
    private Study testStudy;
    private User testUser;
    private FlashcardSet testFlashcardSet;
    private final LocalDateTime startTime = LocalDateTime.now();
    private final LocalDateTime endTime = LocalDateTime.now().plusHours(1);
    private final int numberStudiedWords = 20;
    protected static EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("FlashcardMariaDbUnitTest");
    protected static EntityManager entityManager = entityManagerFactory.createEntityManager();
    private StudyDao studyDao = StudyDao.getInstance(entityManager);
    private UserDao userDao = UserDao.getInstance(entityManager);
    private FlashcardSetDao flashcardSetDao = FlashcardSetDao.getInstance(entityManager);

    @BeforeEach
    void setUp() {
        User user = new User("222", "John", "Doe", "blabla@example.com", "password");
        userDao.persist(user);
        testUser = userDao.findById("222");
        assertNotNull(testUser, "User should be found in the database");

        FlashcardSet flashcardSet = new FlashcardSet("Sample Set", "Description", "Sample Topic", testUser);
        flashcardSetDao.persist(flashcardSet);
        testFlashcardSet = flashcardSetDao.findById(flashcardSet.getSetId());
        assertNotNull(testFlashcardSet, "FlashcardSet should be found in the database");

        Study study = new Study(testUser, testFlashcardSet, startTime, endTime, numberStudiedWords);
        studyDao.persist(study);
        this.testStudy = studyDao.findById(study.getStudyId());
        assertNotNull(testStudy, "Study should be found in the database");
    }

    @AfterEach
    void tearDown() {
        if (testStudy == null) {
            return;
        }
        studyDao.delete(testStudy);
        testStudy = null;
        flashcardSetDao.delete(testFlashcardSet);
        testFlashcardSet = null;
        userDao.delete(testUser);
    }

    @Test
    @Order(1)
    void testEmptyConstructor() {
        Study emptyStudy = new Study();
        assertNotNull(emptyStudy, "Study object should not be null");
    }

    @Test
    @Order(2)
    void testGetStudyId() {
        assertNotEquals(0, testStudy.getStudyId(), "Fail to get StudyId");
    }

    @Test
    @Order(3)
    void testGetUser() {
        assertEquals(testUser, testStudy.getUser(), "Fail to get User");
    }

    @Test
    @Order(4)
    void testGetFlashcardSet() {
        assertEquals(testFlashcardSet, testStudy.getFlashcardSet(), "Fail to get FlashcardSet");
    }

    @Test
    @Order(5)
    void testGetStartTime() {
        assertEquals(startTime, testStudy.getStartTime(), "Fail to get StartTime");
    }

    @Test
    @Order(6)
    void testSetStartTime() {
        LocalDateTime newStartTime = LocalDateTime.now().minusHours(1);
        testStudy.setStartTime(newStartTime);
        assertEquals(newStartTime, testStudy.getStartTime(), "Fail to set StartTime");
    }

    @Test
    @Order(7)
    void testGetEndTime() {
        assertEquals(endTime, testStudy.getEndTime(), "Fail to get EndTime");
    }

    @Test
    @Order(8)
    void testSetEndTime() {
        LocalDateTime newEndTime = LocalDateTime.now().plusHours(2);
        testStudy.setEndTime(newEndTime);
        assertEquals(newEndTime, testStudy.getEndTime(), "Fail to set EndTime");
    }

    @Test
    @Order(9)
    void testGetNumberStudiedWords() {
        assertEquals(numberStudiedWords, testStudy.getNumberStudiedWords(), "Fail to get NumberStudiedWords");
    }

    @Test
    @Order(10)
    void testSetNumberStudiedWords() {
        int newNumberStudiedWords = 30;
        testStudy.setNumberStudiedWords(newNumberStudiedWords);
        assertEquals(newNumberStudiedWords, testStudy.getNumberStudiedWords(), "Fail to set NumberStudiedWords");
    }

    @Test
    @Order(11)
    void testSetUser() {
        testStudy.setUser(testUser);
        assertEquals(testUser, testStudy.getUser(), "Fail to set User");
    }

    @Test
    @Order(12)
    void testSetFlashcardSet() {
        testStudy.setFlashcardSet(testFlashcardSet);
        assertEquals(testFlashcardSet, testStudy.getFlashcardSet(), "Fail to set FlashcardSet");
    }
}