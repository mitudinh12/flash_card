package com.flash_card.view_model.flashcard_set;

import com.flash_card.model.dao.FlashcardDao;
import com.flash_card.model.dao.FlashcardSetDao;
import com.flash_card.model.dao.StudyDao;
import com.flash_card.model.dao.UserDao;
import com.flash_card.model.entity.Flashcard;
import com.flash_card.model.entity.FlashcardSet;
import com.flash_card.model.entity.Study;
import com.flash_card.model.entity.User;
import com.flash_card.framework.DifficultyLevel;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class StudyFlashcardSetViewModelTest {
    private static final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("FlashcardMariaDbUnitTest");
    private static final EntityManager entityManager = entityManagerFactory.createEntityManager();
    private final StudyFlashcardSetViewModel studyFlashcardSetViewModel = new StudyFlashcardSetViewModel(entityManager);
    private final FlashcardDao flashcardDao = FlashcardDao.getInstance(entityManager);
    private final FlashcardSetDao flashcardSetDao = FlashcardSetDao.getInstance(entityManager);
    private final StudyDao studyDao = StudyDao.getInstance(entityManager);
    private final UserDao userDao = UserDao.getInstance(entityManager);
    private final User testUser = new User("12345678910", "John", "Doe", "testMail@example.com", "sample-id-token");
    private final FlashcardSet testFlashcardSet = new FlashcardSet("Test Set", "Test Description", "Test Topic", testUser);
    private final Flashcard testFlashcard = new Flashcard("Test term", "Test definition", DifficultyLevel.hard, testFlashcardSet, testUser);

    @BeforeEach
    void setUp() {
        userDao.persist(testUser);
        flashcardSetDao.persist(testFlashcardSet);
        flashcardDao.persist(testFlashcard);
    }

    @AfterEach
    void tearDown() {
        Study study = studyDao.findByUserIdAndSetId(testUser.getUserId(), testFlashcardSet.getSetId());
        if (study != null) {
            studyDao.delete(study);
        }
        flashcardDao.delete(testFlashcard);
        flashcardSetDao.delete(testFlashcardSet);
        userDao.delete(testUser);
    }

    @Test
    @Order(1)
    void testLoadFlashcards() {
        studyFlashcardSetViewModel.loadFlashcards(testFlashcardSet.getSetId(), testFlashcardSet.getSetName());
        List<Flashcard> flashcards = studyFlashcardSetViewModel.getFlashcards();
        assertNotNull(flashcards);
        assertFalse(flashcards.isEmpty());
    }

    @Test
    @Order(2)
    void testStartStudy() {
        studyFlashcardSetViewModel.startStudy(testUser.getUserId(), testFlashcardSet.getSetId());
        Study initialStudy = studyDao.findByUserIdAndSetId(testUser.getUserId(), testFlashcardSet.getSetId());
        assertNotNull(initialStudy);
        initialStudy.setStartTime(LocalDateTime.now().minusDays(1));
        studyDao.update(initialStudy);
        studyFlashcardSetViewModel.startStudy(testUser.getUserId(), testFlashcardSet.getSetId());
        Study updatedStudy = studyDao.findByUserIdAndSetId(testUser.getUserId(), testFlashcardSet.getSetId());
        assertNotNull(updatedStudy);
        assertNull(updatedStudy.getEndTime());
    }

    @Test
    @Order(3)
    void testEndStudy() {
        studyFlashcardSetViewModel.startStudy(testUser.getUserId(), testFlashcardSet.getSetId());
        studyFlashcardSetViewModel.endStudy();
        Study study = studyDao.findByUserIdAndSetId(testUser.getUserId(), testFlashcardSet.getSetId());
        assertNotNull(study.getEndTime());
    }

    @Test
    @Order(4)
    void testUpdateFlashcardLevel() {
        studyFlashcardSetViewModel.loadFlashcards(testFlashcardSet.getSetId(), testFlashcardSet.getSetName());
        studyFlashcardSetViewModel.updateFlashcardLevel(DifficultyLevel.easy);
        Flashcard flashcard = studyFlashcardSetViewModel.getCurrentFlashcard();
        assertEquals(DifficultyLevel.easy, flashcard.getDifficultLevel());
    }

    @Test
    @Order(5)
    void testResetAllFlashcardLevel() {
        studyFlashcardSetViewModel.loadFlashcards(testFlashcardSet.getSetId(), testFlashcardSet.getSetName());
        studyFlashcardSetViewModel.resetAllFlashcardLevel();
        List<Flashcard> flashcards = studyFlashcardSetViewModel.getFlashcards();
        flashcards.forEach(flashcard -> assertEquals(DifficultyLevel.hard, flashcard.getDifficultLevel()));
    }

    @Test
    @Order(6)
    void testUpdateStudyDetails() {
        studyFlashcardSetViewModel.startStudy(testUser.getUserId(), testFlashcardSet.getSetId());
        studyFlashcardSetViewModel.endStudy();
        studyFlashcardSetViewModel.updateStudyDetails(testUser.getUserId(), testFlashcardSet.getSetId());
        assertNotNull(studyFlashcardSetViewModel.studyTimeProperty().get());
        assertNotNull(studyFlashcardSetViewModel.studiedNumProperty().get());
    }

    @Test
    @Order(7)
    void testGetTotalFlashcards() {
        int totalFlashcards = studyFlashcardSetViewModel.getTotalFlashcards();
        assertNotNull(totalFlashcards);
    }

    @Test
    @Order(8)
    void testGetStudiedFlashcards() {
        studyFlashcardSetViewModel.loadFlashcards(testFlashcardSet.getSetId(), testFlashcardSet.getSetName());
        studyFlashcardSetViewModel.updateFlashcardLevel(DifficultyLevel.easy);
        int studiedFlashcards = studyFlashcardSetViewModel.getStudiedFlashcards();
        assertEquals(1, studiedFlashcards);
    }

    @Test
    @Order(9)
    void testGetCurrentFlashcard() {
        studyFlashcardSetViewModel.loadFlashcards(testFlashcardSet.getSetId(), testFlashcardSet.getSetName());
        Flashcard currentFlashcard = studyFlashcardSetViewModel.getCurrentFlashcard();
        assertNotNull(currentFlashcard);
        assertEquals(testFlashcard.getTerm(), currentFlashcard.getTerm());
    }

    @Test
    @Order(10)
    void testNextFlashcard() {
        studyFlashcardSetViewModel.loadFlashcards(testFlashcardSet.getSetId(), testFlashcardSet.getSetName());
        studyFlashcardSetViewModel.nextFlashcard();
        assertEquals(0, studyFlashcardSetViewModel.currentIndexProperty().get());
    }

    @Test
    @Order(11)
    void testPreviousFlashcard() {
        studyFlashcardSetViewModel.loadFlashcards(testFlashcardSet.getSetId(), testFlashcardSet.getSetName());
        studyFlashcardSetViewModel.previousFlashcard();
        assertEquals(0, studyFlashcardSetViewModel.currentIndexProperty().get());
    }

    @Test
    @Order(12)
    void testShuffleFlashcards() {
        studyFlashcardSetViewModel.loadFlashcards(testFlashcardSet.getSetId(), testFlashcardSet.getSetName());
        studyFlashcardSetViewModel.shuffleFlashcards();
        assertEquals(0, studyFlashcardSetViewModel.currentIndexProperty().get());
    }

    @Test
    @Order(13)
    void testSetNameProperty() {
        studyFlashcardSetViewModel.loadFlashcards(testFlashcardSet.getSetId(), testFlashcardSet.getSetName());
        assertEquals(testFlashcardSet.getSetName(), studyFlashcardSetViewModel.setNameProperty().get());
    }

    @Test
    @Order(14)
    void testTotalProperty() {
        studyFlashcardSetViewModel.loadFlashcards(testFlashcardSet.getSetId(), testFlashcardSet.getSetName());
        assertEquals(String.valueOf(1), studyFlashcardSetViewModel.totalProperty().get());
    }
}