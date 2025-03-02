package com.flash_card.view_model.flashcard_set;

import com.flash_card.framework.DifficultyLevel;
import com.flash_card.model.dao.FlashcardDao;
import com.flash_card.model.dao.FlashcardSetDao;
import com.flash_card.model.dao.QuizDao;
import com.flash_card.model.dao.UserDao;
import com.flash_card.model.entity.Flashcard;
import com.flash_card.model.entity.FlashcardSet;
import com.flash_card.model.entity.Quiz;
import com.flash_card.model.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class QuizFlashcardSetViewModelTest {
    private static final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("FlashcardMariaDbUnitTest");
    private static final EntityManager entityManager = entityManagerFactory.createEntityManager();

    private final QuizFlashcardSetViewModel quizFlashcardSetViewModel = new QuizFlashcardSetViewModel(entityManager);
    private final FlashcardDao flashcardDao = FlashcardDao.getInstance(entityManager);
    private final QuizDao quizDao = QuizDao.getInstance(entityManager);
    private final FlashcardSetDao flashcardSetDao = FlashcardSetDao.getInstance(entityManager);
    private final UserDao userDao = UserDao.getInstance(entityManager);

    private final User testUser = new User("12345678910", "John", "Doe", "testMail@example.com", "sample-id-token");
    private final FlashcardSet testFlashcardSet = new FlashcardSet("Test Set", "Test Description", "Test Topic", testUser);
    private final Flashcard testFlashcard = new Flashcard("Test term", "Test definition", DifficultyLevel.hard, testFlashcardSet, testUser);
    private final Flashcard testFlashcard2 = new Flashcard("Test term 2", "Test definition 2", DifficultyLevel.hard, testFlashcardSet, testUser);
    private final Flashcard testFlashcard3 = new Flashcard("Test term 3", "Test definition 3", DifficultyLevel.hard, testFlashcardSet, testUser);
    private final Flashcard testFlashcard4 = new Flashcard("Test term 4", "Test definition 4", DifficultyLevel.hard, testFlashcardSet, testUser);

    @BeforeEach
    void setUp() {
        userDao.persist(testUser);
        flashcardSetDao.persist(testFlashcardSet);
        flashcardDao.persist(testFlashcard);
        flashcardDao.persist(testFlashcard2);
        flashcardDao.persist(testFlashcard3);
        flashcardDao.persist(testFlashcard4);
    }

    @AfterEach
    void tearDown() {
        // Clean up quiz if one was created during the test
        if (quizFlashcardSetViewModel.getQuizId() != 0) {
            Quiz quiz = quizDao.findById(quizFlashcardSetViewModel.getQuizId());
            if (quiz != null) {
                quizDao.delete(quiz);
            }
        }
        flashcardDao.delete(testFlashcard);
        flashcardSetDao.delete(testFlashcardSet);
        userDao.delete(testUser);
    }

    @Test
    @Order(1)
    void testLoadFlashcards() {
        quizFlashcardSetViewModel.loadFlashcards(testFlashcardSet.getSetId(), testFlashcardSet.getSetName());
        List<Flashcard> flashcards = quizFlashcardSetViewModel.getFlashcards();
        assertNotNull(flashcards, "Flashcards list should not be null");
        assertFalse(flashcards.isEmpty(), "Flashcards list should not be empty");
        assertEquals(testFlashcardSet.getSetName(), quizFlashcardSetViewModel.setNameProperty().get(), "Set name property should be set");
        assertEquals(String.valueOf(flashcards.size()), quizFlashcardSetViewModel.totalProperty().get(), "Total property should reflect flashcards count");
    }

    @Test
    @Order(2)
    void testStartAndFinishQuiz() {
        quizFlashcardSetViewModel.startQuiz(testUser.getUserId(), testFlashcardSet.getSetId());
        quizFlashcardSetViewModel.finishQuiz();
        int quizId = quizFlashcardSetViewModel.getQuizId();
        assertNotEquals(0, quizId, "Quiz ID should not be 0 after finishing quiz");
        Quiz persistedQuiz = quizDao.findById(quizId);
        assertNotNull(persistedQuiz, "Quiz should be persisted in the database");
        assertNotNull(persistedQuiz.getEndTime(), "Quiz end time should be set after finishing quiz");
    }

    @Test
    @Order(3)
    void testStopQuiz() {
        quizFlashcardSetViewModel.startQuiz(testUser.getUserId(), testFlashcardSet.getSetId());
        quizFlashcardSetViewModel.finishQuiz(); // to set the quizId properly
        int quizId = quizFlashcardSetViewModel.getQuizId();
        quizFlashcardSetViewModel.stopQuiz();
        Quiz deletedQuiz = quizDao.findById(quizId);
        assertNull(deletedQuiz, "Quiz should be deleted after stopping the quiz");
    }

    @Test
    @Order(4)
    void testIsAnswerCorrect() {
        // Start a quiz to initialize currentQuiz
        quizFlashcardSetViewModel.startQuiz(testUser.getUserId(), testFlashcardSet.getSetId());

        quizFlashcardSetViewModel.loadFlashcards(testFlashcardSet.getSetId(), testFlashcardSet.getSetName());
        quizFlashcardSetViewModel.loadQuestion();
        String correctDefinition = quizFlashcardSetViewModel.getCurrentFlashcard().getDefinition();
//
//        // Test correct answer scenario
        boolean resultCorrect = quizFlashcardSetViewModel.isAnswerCorrect(correctDefinition);
        assertTrue(resultCorrect, "The answer should be correct");
        assertEquals("Correct!", quizFlashcardSetViewModel.instructionTextProperty().get(), "Instruction text should indicate a correct answer");

//         Test wrong answer scenario
        boolean resultWrong = quizFlashcardSetViewModel.isAnswerCorrect("Incorrect answer");
        assertFalse(resultWrong, "The answer should be incorrect");
        assertEquals("Wrong!", quizFlashcardSetViewModel.instructionTextProperty().get(), "Instruction text should indicate a wrong answer");
    }


    @Test
    @Order(5)
    void testLoadQuestion() {
        quizFlashcardSetViewModel.loadFlashcards(testFlashcardSet.getSetId(), testFlashcardSet.getSetName());
        quizFlashcardSetViewModel.loadQuestion();
        assertEquals(quizFlashcardSetViewModel.getCurrentFlashcard().getTerm(),
                quizFlashcardSetViewModel.currentTermProperty().get(),
                "Current term should match flashcard's term");
        assertEquals("Choose the correct definition",
                quizFlashcardSetViewModel.instructionTextProperty().get(),
                "Instruction text should be set correctly");
        assertNotNull(quizFlashcardSetViewModel.answer1Property().get(), "Answer1 should not be null");
        assertNotNull(quizFlashcardSetViewModel.answer2Property().get(), "Answer2 should not be null");
        assertNotNull(quizFlashcardSetViewModel.answer3Property().get(), "Answer3 should not be null");
        assertNotNull(quizFlashcardSetViewModel.answer4Property().get(), "Answer4 should not be null");
    }

    @Test
    @Order(6)
    void testNextFlashcard() {
        // Add a second flashcard to allow for moving to the next one
        quizFlashcardSetViewModel.loadFlashcards(testFlashcardSet.getSetId(), testFlashcardSet.getSetName());
        quizFlashcardSetViewModel.loadQuestion();
        int initialIndex = quizFlashcardSetViewModel.currentIndexProperty().get();
        quizFlashcardSetViewModel.nextFlashcard();
        assertEquals(initialIndex + 1, quizFlashcardSetViewModel.currentIndexProperty().get(), "Current index should increment after calling nextFlashcard");
        // Verify that the current term is updated (it should differ from the first flashcard's term)
        assertNotEquals("Test term", quizFlashcardSetViewModel.currentTermProperty().get(), "Current term should update to the next flashcard");
    }

    @Test
    @Order(7)
    void testIsLastFlashcard() {
        quizFlashcardSetViewModel.loadFlashcards(testFlashcardSet.getSetId(), testFlashcardSet.getSetName());
        // With a single flashcard, it should be recognized as the last one
        int finalIndex = quizFlashcardSetViewModel.getFlashcards().size() - 1;
        quizFlashcardSetViewModel.currentIndexProperty().set(finalIndex);
        assertTrue(quizFlashcardSetViewModel.isLastFlashcard(), "With one flashcard, it should be marked as the last flashcard");
    }

    @Test
    @Order(8)
    void testProperties() {
        quizFlashcardSetViewModel.loadFlashcards(testFlashcardSet.getSetId(), testFlashcardSet.getSetName());
        assertEquals(testFlashcardSet.getSetName(), quizFlashcardSetViewModel.setNameProperty().get(), "Set name property should match the flashcard set name");
        assertEquals(String.valueOf(quizFlashcardSetViewModel.getFlashcards().size()),
                quizFlashcardSetViewModel.totalProperty().get(),
                "Total property should reflect the number of flashcards loaded");
    }
}