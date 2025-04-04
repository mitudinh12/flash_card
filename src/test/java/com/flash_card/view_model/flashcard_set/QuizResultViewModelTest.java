package com.flash_card.view_model.flashcard_set;

import com.flash_card.model.dao.QuizDao;
import com.flash_card.model.dao.UserDao;
import com.flash_card.model.dao.FlashcardSetDao;
import com.flash_card.model.entity.Quiz;
import com.flash_card.model.entity.User;
import com.flash_card.model.entity.FlashcardSet;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class QuizResultViewModelTest {
    private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("FlashcardMariaDbUnitTest");
    private static EntityManager entityManager = emf.createEntityManager();
    private QuizDao quizDao = QuizDao.getInstance(entityManager);
    private UserDao userDao = UserDao.getInstance(entityManager);
    private FlashcardSetDao flashcardSetDao = FlashcardSetDao.getInstance(entityManager);

    private User testUser = new User("uniqueUserId", "John", "Doe", "john1111@example.com", "token123");
    private FlashcardSet testFlashcardSet;
    // Create a quiz with a fixed time interval, 5 minutes and 30 seconds
    LocalDateTime startTime = LocalDateTime.now();
    LocalDateTime endTime = startTime.plusMinutes(5).plusSeconds(30);
    private Quiz testQuiz;

    QuizResultViewModel viewModel;

    @BeforeEach
    void setUp() {
        userDao.persist(testUser);
        assertNotNull(userDao.findById(testUser.getUserId()), "User should be persisted");

        flashcardSetDao.persist(new FlashcardSet("Test Set", "Test Description", "Test Topic", testUser));
        testFlashcardSet = flashcardSetDao.findByUserId(testUser.getUserId()).getFirst();
        assertNotNull(testFlashcardSet, "Flashcard set should be persisted");
    }

    @AfterEach
    void tearDown() {
//         Clean up persisted entities
        if (testFlashcardSet!=null) {
            Quiz persistedQuiz = quizDao.findByUserIdAndSetId(testUser.getUserId(), testFlashcardSet.getSetId()).getFirst();
            if (persistedQuiz != null) {
                quizDao.delete(persistedQuiz);
            }
            flashcardSetDao.delete(testFlashcardSet);
            userDao.delete(testUser);
        }
    }

    @Test
    @Order(1)
    void testGetTotalCorrect() {
        Quiz quiz = new Quiz(testUser, testFlashcardSet, startTime, endTime, 5, 0);
        boolean result = quizDao.persist(quiz);
        assertNotNull(startTime);
        assertEquals("Test Set", testFlashcardSet.getSetName(), "Flashcard set name should match");
        assertTrue(result, "Persisting quiz should return true");

        testQuiz = quizDao.findByUserIdAndSetId(testUser.getUserId(), testFlashcardSet.getSetId()).getFirst();
        assertNotNull(testQuiz, "Quiz should be persisted");

        viewModel = new QuizResultViewModel(entityManager, testQuiz.getQuizId());
        assertNotNull(viewModel, "ViewModel should be created");

        assertEquals(5, viewModel.getTotalCorrect(), "Total correct should be 5");
    }

    @Test
    @Order(2)
    void testGetTotalWrong() {
        quizDao.persist(new Quiz(testUser, testFlashcardSet, startTime, endTime, 5, 2));
        testQuiz = quizDao.findByUserIdAndSetId(testUser.getUserId(), testFlashcardSet.getSetId()).getFirst();
        assertNotNull(testQuiz, "Quiz should be persisted");

        viewModel = new QuizResultViewModel(entityManager, testQuiz.getQuizId());
        assertNotNull(viewModel, "ViewModel should be created");

        assertEquals(2, viewModel.getTotalWrong(), "Total wrong should be 2");
    }

    @Test
    @Order(3)
    void testGetSetName() {
        quizDao.persist(new Quiz(testUser, testFlashcardSet, startTime, endTime, 5, 2));
        testQuiz = quizDao.findByUserIdAndSetId(testUser.getUserId(), testFlashcardSet.getSetId()).getFirst();
        assertNotNull(testQuiz, "Quiz should be persisted");

        viewModel = new QuizResultViewModel(entityManager, testQuiz.getQuizId());
        assertNotNull(viewModel, "ViewModel should be created");

        assertEquals(testFlashcardSet.getSetName(), viewModel.getSetName(), "Set name should match the flashcard set name");
    }

    @Test
    @Order(4)
    void testGetFlashcardSetId() {
        quizDao.persist(new Quiz(testUser, testFlashcardSet, startTime, endTime, 5, 2));
        testQuiz = quizDao.findByUserIdAndSetId(testUser.getUserId(), testFlashcardSet.getSetId()).getFirst();
        assertNotNull(testQuiz, "Quiz should be persisted");

        viewModel = new QuizResultViewModel(entityManager, testQuiz.getQuizId());
        assertNotNull(viewModel, "ViewModel should be created");

        assertEquals(testFlashcardSet.getSetId(), viewModel.getFlashcardSetId(), "Flashcard set ID should match");
    }

    @Test
    @Order(5)
    void testCalculateTime() {
        quizDao.persist(new Quiz(testUser, testFlashcardSet, startTime, endTime, 5, 2));
        testQuiz = quizDao.findByUserIdAndSetId(testUser.getUserId(), testFlashcardSet.getSetId()).getFirst();
        assertNotNull(testQuiz, "Quiz should be persisted");

        viewModel = new QuizResultViewModel(entityManager, testQuiz.getQuizId());
        assertNotNull(viewModel, "ViewModel should be created");

        // Expecting "Quiz time: 5m 30s" based on the fixed interval we set in setUp()
        assertEquals("Quiz time: 5m30s", viewModel.quizTimeProperty().get(), "Quiz time should be correctly calculated");
    }
}