package com.flash_card.view_model.flashcard_set;

import com.flash_card.model.dao.*;
import com.flash_card.model.entity.*;
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
    private final CardDifficultLevelDao cardDifficultLevelDao = CardDifficultLevelDao.getInstance(entityManager);
    private final User testUser = new User("87524526877785", "John", "Doe", "testMail45678@example.com", "sample-id-token");
    private final FlashcardSet testFlashcardSet = new FlashcardSet("Test Set", "Test Description", "Test Topic", testUser);
    private final Flashcard testFlashcard = new Flashcard("Test term", "Test definition", testFlashcardSet, testUser);
    private final FlashcardSet testFlashcardSet2 = new FlashcardSet("Test Set2", "Test Description", "Test Topic", testUser);
    private final Flashcard testFlashcard2 = new Flashcard("Test term2", "Test definition2", testFlashcardSet2, testUser);
    private final Study testStudy = new Study(testUser, testFlashcardSet, LocalDateTime.now(), null, 0);
    private final CardDifficultLevel testCardDifficultLevel = new CardDifficultLevel(testFlashcard2, testStudy, DifficultyLevel.hard);

    @BeforeEach
    void setUp() {
        userDao.persist(testUser);
        flashcardSetDao.persist(testFlashcardSet);
        flashcardDao.persist(testFlashcard);
        flashcardSetDao.persist(testFlashcardSet2);
        flashcardDao.persist(testFlashcard2);
        studyDao.persist(testStudy);
        cardDifficultLevelDao.persistCardDifficultLevel(testCardDifficultLevel);
    }

    @AfterEach
    void tearDown() {
        Study study = studyDao.findByUserIdAndSetId(testUser.getUserId(), testFlashcardSet.getSetId());
        if (study != null) {
            studyDao.delete(study);
        }
        cardDifficultLevelDao.deleteCardDifficultLevel(testCardDifficultLevel);
        flashcardDao.delete(testFlashcard2);
        flashcardSetDao.delete(testFlashcardSet2);
        flashcardDao.delete(testFlashcard);
        flashcardSetDao.delete(testFlashcardSet);
        userDao.delete(testUser);
    }

    @Test
    @Order(1)
    void testLoadFlashcards() {
        studyFlashcardSetViewModel.setCurrentStudy(testStudy);
        studyFlashcardSetViewModel.loadFlashcards(testFlashcardSet2.getSetId(), testFlashcardSet2.getSetName());
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
        studyFlashcardSetViewModel.setCurrentStudy(testStudy);
        studyFlashcardSetViewModel.loadFlashcards(testFlashcardSet2.getSetId(), testFlashcardSet2.getSetName());
        studyFlashcardSetViewModel.updateFlashcardLevel(DifficultyLevel.easy);
        Flashcard currentFlashcard = studyFlashcardSetViewModel.getCurrentFlashcard();
        CardDifficultLevel cardDifficultLevel = cardDifficultLevelDao.findCardDifficultLevelByCardIdAndStudyId(currentFlashcard.getCardId(), studyFlashcardSetViewModel.getCurrentStudy().getStudyId());
        assertEquals(DifficultyLevel.easy, cardDifficultLevel.getDifficultLevel());
    }

    @Test
    @Order(5)
    void testResetAllFlashcardLevel() {
        studyFlashcardSetViewModel.setCurrentStudy(testStudy);
        studyFlashcardSetViewModel.loadFlashcards(testFlashcardSet2.getSetId(), testFlashcardSet2.getSetName());
        studyFlashcardSetViewModel.resetAllFlashcardLevel();
        List<Flashcard> flashcards = studyFlashcardSetViewModel.getFlashcards();
        for (Flashcard flashcard : flashcards) {
            CardDifficultLevel cardDifficultLevel = cardDifficultLevelDao.findCardDifficultLevelByCardIdAndStudyId(flashcard.getCardId(), studyFlashcardSetViewModel.getCurrentStudy().getStudyId());
            assertEquals(DifficultyLevel.hard, cardDifficultLevel.getDifficultLevel());
        }
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
        studyFlashcardSetViewModel.setCurrentStudy(testStudy);
        studyFlashcardSetViewModel.loadFlashcards(testFlashcardSet2.getSetId(), testFlashcardSet2.getSetName());
        studyFlashcardSetViewModel.updateFlashcardLevel(DifficultyLevel.easy);
        int studiedFlashcards = studyFlashcardSetViewModel.getStudiedFlashcards();
        assertEquals(1, studiedFlashcards);
    }

    @Test
    @Order(9)
    void testGetCurrentFlashcard() {
        studyFlashcardSetViewModel.setCurrentStudy(testStudy);
        studyFlashcardSetViewModel.loadFlashcards(testFlashcardSet2.getSetId(), testFlashcardSet2.getSetName());
        Flashcard currentFlashcard = studyFlashcardSetViewModel.getCurrentFlashcard();
        assertNotNull(currentFlashcard);
        assertEquals(testFlashcard2.getTerm(), currentFlashcard.getTerm());
    }

    @Test
    @Order(10)
    void testNextFlashcard() {
        studyFlashcardSetViewModel.setCurrentStudy(testStudy);
        studyFlashcardSetViewModel.loadFlashcards(testFlashcardSet2.getSetId(), testFlashcardSet2.getSetName());
        studyFlashcardSetViewModel.nextFlashcard();
        assertEquals(0, studyFlashcardSetViewModel.currentIndexProperty().get());
    }

    @Test
    @Order(11)
    void testPreviousFlashcard() {
        studyFlashcardSetViewModel.setCurrentStudy(testStudy);
        studyFlashcardSetViewModel.loadFlashcards(testFlashcardSet2.getSetId(), testFlashcardSet2.getSetName());
        studyFlashcardSetViewModel.previousFlashcard();
        assertEquals(0, studyFlashcardSetViewModel.currentIndexProperty().get());
    }

    @Test
    @Order(12)
    void testShuffleFlashcards() {
        studyFlashcardSetViewModel.setCurrentStudy(testStudy);
        studyFlashcardSetViewModel.loadFlashcards(testFlashcardSet2.getSetId(), testFlashcardSet2.getSetName());
        studyFlashcardSetViewModel.shuffleFlashcards();
        assertEquals(0, studyFlashcardSetViewModel.currentIndexProperty().get());
    }

    @Test
    @Order(13)
    void testSetNameProperty() {
        studyFlashcardSetViewModel.setCurrentStudy(testStudy);
        studyFlashcardSetViewModel.loadFlashcards(testFlashcardSet2.getSetId(), testFlashcardSet2.getSetName());
        assertEquals(testFlashcardSet2.getSetName(), studyFlashcardSetViewModel.setNameProperty().get());
    }

    @Test
    @Order(14)
    void testTotalProperty() {
        studyFlashcardSetViewModel.setCurrentStudy(testStudy);
        studyFlashcardSetViewModel.loadFlashcards(testFlashcardSet2.getSetId(), testFlashcardSet2.getSetName());
        assertEquals(String.valueOf(1), studyFlashcardSetViewModel.totalProperty().get());
    }

    @Test
    @Order(15)
    void testGetCurrentFlashcardDifficultLevel() {
        studyFlashcardSetViewModel.setCurrentStudy(testStudy);
        studyFlashcardSetViewModel.loadFlashcards(testFlashcardSet2.getSetId(), testFlashcardSet2.getSetName());
        DifficultyLevel difficultyLevel = studyFlashcardSetViewModel.getCurrentFlashcardDifficultLevel();
        assertNotNull(difficultyLevel);
    }
}