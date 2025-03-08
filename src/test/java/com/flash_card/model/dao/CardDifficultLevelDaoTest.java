package com.flash_card.model.dao;

import com.flash_card.framework.DifficultyLevel;
import com.flash_card.model.entity.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class CardDifficultLevelDaoTest {
    private static EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("FlashcardMariaDbUnitTest");
    private static EntityManager entityManager = entityManagerFactory.createEntityManager();
    private UserDao userDao = UserDao.getInstance(entityManager);
    private FlashcardSetDao flashcardSetDao = FlashcardSetDao.getInstance(entityManager);
    private FlashcardDao flashcardDao = FlashcardDao.getInstance(entityManager);
    private StudyDao studyDao = StudyDao.getInstance(entityManager);
    private CardDifficultLevelDao cardDifficultLevelDao = CardDifficultLevelDao.getInstance(entityManager);
    private final LocalDateTime startTime = LocalDateTime.now();
    private final LocalDateTime endTime = LocalDateTime.now().plusHours(1);
    private User creator = new User("creator1", "first_name", "last_name", "email", "id_token1");
    private FlashcardSet flashcardSet = new FlashcardSet("Database1","description1", "database1", creator);
    private Flashcard flashcard = new Flashcard("term1", "definition1", flashcardSet, creator);
    private Study study = new Study(creator, flashcardSet, startTime, endTime, 20);
    private CardDifficultLevel cardDifficultLevel = new CardDifficultLevel(flashcard, study, DifficultyLevel.easy);

    @BeforeEach
    void setUp() {
        userDao.persist(creator);
        flashcardSetDao.persist(flashcardSet);
        flashcardDao.persist(flashcard);
        studyDao.persist(study);
    }

    @AfterEach
    void tearDown() {
        if (cardDifficultLevel != null) {
            User user = userDao.findByEmail("email");
            FlashcardSet set = flashcardSetDao.findByUserId(user.getUserId()).getFirst();
            Flashcard flashcard = flashcardDao.findBySetId(set.getSetId()).getFirst();
            Study study = studyDao.findByUserIdAndSetId(user.getUserId(), set.getSetId());

            studyDao.delete(study);
            flashcardDao.delete(flashcard);
            flashcardSetDao.delete(set);
            userDao.delete(user);
            cardDifficultLevel = null;
        }
    }

    @Test
    void testPersistCardDifficultLevel() {
        assertTrue(cardDifficultLevelDao.persistCardDifficultLevel(cardDifficultLevel));
        assertFalse(cardDifficultLevelDao.persistCardDifficultLevel(null));
    }

    @Test
    void testDeleteCardDifficultLevel() {
        cardDifficultLevelDao.persistCardDifficultLevel(cardDifficultLevel);
        assertTrue(cardDifficultLevelDao.deleteCardDifficultLevel(cardDifficultLevel));
        assertFalse(cardDifficultLevelDao.deleteCardDifficultLevel(null));
    }

    @Test
    void testFindCardDifficultLevelByCardIdAndStudyId() {
        cardDifficultLevelDao.persistCardDifficultLevel(cardDifficultLevel);
        assertNotNull(cardDifficultLevelDao.findCardDifficultLevelByCardIdAndStudyId(flashcard.getCardId(), study.getStudyId()));
        assertNull(cardDifficultLevelDao.findCardDifficultLevelByCardIdAndStudyId(0, 0));
    }
}