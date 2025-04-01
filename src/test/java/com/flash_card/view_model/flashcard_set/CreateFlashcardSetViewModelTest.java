package com.flash_card.view_model.flashcard_set;

import com.flash_card.model.dao.FlashcardDao;
import com.flash_card.model.dao.FlashcardSetDao;
import com.flash_card.model.dao.UserDao;
import com.flash_card.model.entity.Flashcard;
import com.flash_card.model.entity.FlashcardSet;
import com.flash_card.model.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CreateFlashcardSetViewModelTest {

    private static final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("FlashcardMariaDbUnitTest");
    private static final EntityManager entityManager = entityManagerFactory.createEntityManager();
    private CreateFlashcardSetViewModel createFlashcardSetViewModel = new CreateFlashcardSetViewModel( entityManager);;
    private final FlashcardDao flashcardDao = FlashcardDao.getInstance(entityManager);
    private final FlashcardSetDao flashcardSetDao = FlashcardSetDao.getInstance(entityManager);
    private final UserDao userDao = UserDao.getInstance(entityManager);
    private final User testUser = new User("g5d5dgd", "John", "Doe", "testMaildfsd@example.com", "sample-id-token");
    private final FlashcardSet testFlashcardSet = new FlashcardSet("Test Set", "Test Description", "Test Topic", testUser);
    private final Flashcard testFlashcard = new Flashcard("Test term", "Test definition", testFlashcardSet, testUser);

    @BeforeEach
    void setUp() {
        userDao.persist(testUser);
        flashcardSetDao.persist(testFlashcardSet);
        flashcardDao.persist(testFlashcard);
    }

    @AfterEach
    void tearDown() {
        flashcardDao.delete(testFlashcard);
        flashcardSetDao.delete(testFlashcardSet);
        userDao.delete(testUser);
    }

    @Test
    public void testCreateFlashcardSetViewModel() {
        assertNotNull(createFlashcardSetViewModel);
    }

    @Test
    public void testAddSet() {
        String name = "Test Set";
        String language = "thai";
        String description = "Test Description";
        String topic = "Test Topic";
        String userId = testUser.getUserId();
        System.out.println("userId: " + userId);

        int setId = createFlashcardSetViewModel.addSet(name, language, description, topic, userId);

        FlashcardSet retrievedSet = flashcardSetDao.findById(setId);
        assertNotNull(retrievedSet);
        assertEquals(name, retrievedSet.getSetName());
        assertEquals(description, retrievedSet.getSetDescription());
        assertEquals(topic, retrievedSet.getSetTopic());
    }
}