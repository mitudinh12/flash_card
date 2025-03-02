package com.flash_card.model.dao;

import com.flash_card.model.dao.QuizDao;
import com.flash_card.model.entity.Quiz;
import com.flash_card.model.entity.User;
import com.flash_card.model.entity.FlashcardSet;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class QuizDaoTest {
    private static EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("FlashcardMariaDbUnitTest");
    private static EntityManager entityManager = entityManagerFactory.createEntityManager();
    private QuizDao quizDao = QuizDao.getInstance(entityManager);
    private UserDao userDao = UserDao.getInstance(entityManager);
    private FlashcardSetDao flashcardSetDao = FlashcardSetDao.getInstance(entityManager);
    private Quiz testQuiz;

    @BeforeEach
    void setUp() {
        User user = new User("789", "Jo", "Hands", "jo@gmail.com", "123456");
        userDao.persist(user);
        assertNotNull(userDao.findById("789"), "User should be found in the database");
        FlashcardSet flashcardSet = new FlashcardSet("Java Basics", "A set for Java beginners", "Programming", user);
        flashcardSetDao.persist(flashcardSet);
        assertNotNull(flashcardSetDao.findById(flashcardSet.getSetId()), "Flashcard set should be found in the database");
        this.testQuiz = new Quiz(user, flashcardSet, LocalDateTime.now(), LocalDateTime.now().plusMinutes(10), 5, 2);
        quizDao.persist(testQuiz);
    }

    @AfterEach
    void tearDown() {
        Quiz foundQuiz = quizDao.findById(testQuiz.getQuizId());
        if (foundQuiz != null) {
            quizDao.delete(foundQuiz);
        }
    }

    @Test
    @Order(1)
    void testPersist() {
        int quizId = testQuiz.getQuizId();
        assertNotEquals(0, quizId, "Quiz ID should be generated and not 0");
        Quiz invalidQuiz = null;
        assertFalse(quizDao.persist(invalidQuiz), "Should return false when exception is thrown");
    }


    @Test
    @Order(2)
    void testFindById() {
        assertNotNull(quizDao.findById(testQuiz.getQuizId()), "Should return quiz when found");
        assertNull(quizDao.findById(0), "Should return null when quiz is not found");
    }

    @Test
    @Order(3)
    void testUpdate() {
        testQuiz.setWrongTimes(7);
        testQuiz.setCorrectTimes(0);
        assertTrue(quizDao.update(testQuiz), "Should return true when quiz is updated");
        assertEquals(7, quizDao.findById(testQuiz.getQuizId()).getWrongTimes(), "Should reflect the updated correct times");
        assertFalse(quizDao.update(null), "Should return false when exception is thrown");
    }

    @Test
    @Order(4)
    void testDelete() {
        assertTrue(quizDao.delete(testQuiz), "Should return true when quiz is deleted");
        assertFalse(quizDao.delete(null), "Should return false when exception is thrown");
    }

    @Test
    @Order(5)
    void testFindByUserIdAndSetId() {
        List<Quiz> quizzes = quizDao.findByUserIdAndSetId(testQuiz.getUser().getUserId(), testQuiz.getFlashcardSet().getSetId());
        assertNotNull(quizzes, "Should return a list, even if empty");
        assertFalse(quizzes.isEmpty(), "Should contain at least one quiz");
        assertEquals(1, quizzes.size(), "Should contain exactly one quiz");
        assertEquals(testQuiz.getQuizId(), quizzes.get(0).getQuizId(), "Returned quiz ID should match the persisted quiz");

        List<Quiz> emptyQuizzes = quizDao.findByUserIdAndSetId("nonexistent", 999);
        assertNotNull(emptyQuizzes, "Should return a list, even if empty");
        assertTrue(emptyQuizzes.isEmpty(), "Should return an empty list when no quizzes match");
    }
}
