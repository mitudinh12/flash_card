//package com.flash_card.view_model.flashcard_set;
//
//import com.flash_card.model.dao.QuizDao;
//import com.flash_card.model.dao.UserDao;
//import com.flash_card.model.dao.FlashcardSetDao;
//import com.flash_card.model.entity.Quiz;
//import com.flash_card.model.entity.TestSetupAbstract;
//import com.flash_card.model.entity.User;
//import com.flash_card.model.entity.FlashcardSet;
//import jakarta.persistence.EntityManager;
//import jakarta.persistence.EntityManagerFactory;
//import jakarta.persistence.Persistence;
//import org.junit.jupiter.api.*;
//
//import java.time.LocalDateTime;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//public class QuizResultViewModelTest {
//    private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("FlashcardMariaDbUnitTest");
//    private static EntityManager entityManager = emf.createEntityManager();
//    private QuizDao quizDao = QuizDao.getInstance(entityManager);
//    private UserDao userDao = UserDao.getInstance(entityManager);
//    private FlashcardSetDao flashcardSetDao = FlashcardSetDao.getInstance(entityManager);
//
//    private User testUser = new User("uniqueUserId", "John", "Doe", "john1111@example.com", "token123");
//    private FlashcardSet testFlashcardSet = new FlashcardSet("Test Set", "Test Description", "Test Topic", testUser);
//    // Create a quiz with a fixed time interval, 5 minutes and 30 seconds
//    LocalDateTime startTime = LocalDateTime.now();
//    LocalDateTime endTime = startTime.plusMinutes(5).plusSeconds(30);
//    private Quiz testQuiz = new Quiz(testUser, testFlashcardSet, startTime, endTime, 5, 2);
//
//    QuizResultViewModel viewModel;
//
//    @BeforeEach
//    void setUp() {
//        // Create and persist a test user
//        userDao.persist(testUser);
//
//        User testUser1 = userDao.findById(testUser.getUserId());
//        if (testUser1 == null) {
//            throw new IllegalStateException("User was not persisted correctly.");
//        }
//
//        // Create and persist a test flashcard set
//        flashcardSetDao.persist(testFlashcardSet);
//
//        FlashcardSet testFlashcardSet1 = flashcardSetDao.findById(testFlashcardSet.getSetId());
//        if (testFlashcardSet1 == null) {
//            throw new IllegalStateException("Flashcard Set was not persisted correctly.");
//        }
//
//
//        quizDao.persist(testQuiz);
//        // Log the quiz ID before retrieval
//        System.out.println("Persisted Quiz ID: " + testQuiz.getQuizId());
//
//        // Make sure the quiz is retrievable
//        Quiz testQuiz1 = quizDao.findById(testQuiz.getQuizId());
//        if (testQuiz1 == null) {
//            throw new IllegalStateException("Quiz was not persisted correctly.");
//        }
//        viewModel = new QuizResultViewModel(entityManager, testQuiz.getQuizId());
//    }
//
//    @AfterEach
//    void tearDown() {
//        // Clean up persisted entities
//        Quiz persistedQuiz = quizDao.findById(testQuiz.getQuizId());
//        if (persistedQuiz != null) {
//            quizDao.delete(persistedQuiz);
//        }
//        flashcardSetDao.delete(testFlashcardSet);
//        userDao.delete(testUser);
//    }
//
//    @Test
//    @Order(1)
//    void testGetTotalCorrect() {
//        assertEquals(5, viewModel.getTotalCorrect(), "Total correct should be 5");
//    }
//
//    @Test
//    @Order(2)
//    void testGetTotalWrong() {
//        assertEquals(2, viewModel.getTotalWrong(), "Total wrong should be 2");
//    }
//
//    @Test
//    @Order(3)
//    void testGetSetName() {
//        assertEquals(testFlashcardSet.getSetName(), viewModel.getSetName(), "Set name should match the flashcard set name");
//    }
//
//    @Test
//    @Order(4)
//    void testGetFlashcardSetId() {
//        assertEquals(testFlashcardSet.getSetId(), viewModel.getFlashcardSetId(), "Flashcard set ID should match");
//    }
//
//    @Test
//    @Order(5)
//    void testCalculateTime() {
//        // Expecting "Quiz time: 5m 30s" based on the fixed interval we set in setUp()
//        assertEquals("Quiz time: 5m 30s", viewModel.quizTimeProperty().get(), "Quiz time should be correctly calculated");
//    }
//}