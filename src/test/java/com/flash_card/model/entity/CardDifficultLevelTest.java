package com.flash_card.model.entity;

import com.flash_card.model.dao.FlashcardDao;
import com.flash_card.model.dao.FlashcardSetDao;
import com.flash_card.model.dao.StudyDao;
import com.flash_card.model.dao.UserDao;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import com.flash_card.framework.DifficultyLevel;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class CardDifficultLevelTest {
    private static EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("FlashcardMariaDbUnitTest");
    private static EntityManager entityManager = entityManagerFactory.createEntityManager();
    UserDao userDao = UserDao.getInstance(entityManager);
    FlashcardSetDao flashcardSetDao = FlashcardSetDao.getInstance(entityManager);
    FlashcardDao flashcardDao = FlashcardDao.getInstance(entityManager);
    StudyDao studyDao = StudyDao.getInstance(entityManager);
    private final LocalDateTime startTime = LocalDateTime.now();
    private final LocalDateTime endTime = LocalDateTime.now().plusHours(1);
    CardDifficultLevel cardDifficultLevel;

    @BeforeEach
    void setUp() {
        User creator = new User("creator1", "first_name", "last_name", "email", "id_token1");
        userDao.persist(creator);
        FlashcardSet flashcardSet = new FlashcardSet("Database1","description1", "database1", creator);
        flashcardSetDao.persist(flashcardSet);
        Flashcard flashcard = new Flashcard("term1", "definition1", flashcardSet, creator);
        flashcardDao.persist(flashcard);
        Study study = new Study(creator, flashcardSet, startTime, endTime, 20);
        studyDao.persist(study);
        cardDifficultLevel = new CardDifficultLevel(flashcard, study, DifficultyLevel.easy);
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
    void testEmptyConstructor() {
        CardDifficultLevel cardDifficultLevel1 =  new CardDifficultLevel();
        assertNotNull(cardDifficultLevel1, "CardDifficultLevel object should not be null");
    }

    @Test
    void testGetId() {
        assertEquals(1, cardDifficultLevel.getId(), "Fail to get CardDifficultLevelId");
    }

    @Test
    void testGetFlashcard() {
        assertNotNull(cardDifficultLevel.getFlashcard(), "Fail to get Flashcard");
    }

    @Test
    void testGetStudy() {
        assertNotNull(cardDifficultLevel.getStudy(), "Fail to get Study");
    }

    @Test
    void testGetDifficultLevel() {
        assertEquals(DifficultyLevel.easy, cardDifficultLevel.getDifficultLevel(), "Fail to get DifficultyLevel");
    }

    @Test
    void testSetDifficultLevel() {
        cardDifficultLevel.setDifficultLevel(DifficultyLevel.hard);
        assertEquals(DifficultyLevel.hard, cardDifficultLevel.getDifficultLevel(), "Fail to set DifficultyLevel");
    }

}