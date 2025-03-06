package com.flash_card.view_model.flashcard;

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

public class CreateFlashcardViewModelTest {
    private CreateFlashcardViewModel createFlashcardViewModel;
    private static final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("FlashcardMariaDbUnitTest");
    private static final EntityManager entityManager = entityManagerFactory.createEntityManager();
    private final FlashcardDao flashcardDao = FlashcardDao.getInstance(entityManager);
    private final FlashcardSetDao flashcardSetDao = FlashcardSetDao.getInstance(entityManager);
    private final UserDao userDao = UserDao.getInstance(entityManager);
    private final User testUser = new User("12345678910", "John", "Doe", "testMail@example.com", "sample-id-token");
    private final FlashcardSet testFlashcardSet = new FlashcardSet("Test Set", "Test Description", "Test Topic", testUser);
    private final Flashcard testFlashcard = new Flashcard("Test term", "Test definition", testFlashcardSet, testUser);

    @BeforeEach
    void setUp() {
        userDao.persist(testUser);
        flashcardSetDao.persist(testFlashcardSet);
        flashcardDao.persist(testFlashcard);
        createFlashcardViewModel = new CreateFlashcardViewModel(testUser.getUserId(), entityManager);
    }

    @AfterEach
    void tearDown() {
        flashcardDao.delete(testFlashcard);
        flashcardSetDao.delete(testFlashcardSet);
        userDao.delete(testUser);
    }

    @Test
    public void testCreateFlashcardViewModel() {
        assertNotNull(createFlashcardViewModel);
    }

    @Test
    public void testGetCurrentFlashcardSet() {
        FlashcardSet flashcardSet = createFlashcardViewModel.getCurrentFlashcardSet(testFlashcardSet.getSetId());
        assertNotNull(flashcardSet);
    }

    @Test
    public void testGetCurrentUser() {
        User currentUser = createFlashcardViewModel.getCurrentUser();
        assertNotNull(currentUser);
    }

    @Test
    public void testSaveFlashcard() {
        createFlashcardViewModel.saveFlashcard("testTerm", "testDefinition", testFlashcardSet.getSetId());
        assertEquals(2, flashcardDao.findBySetId(testFlashcardSet.getSetId()).size());
    }

    @Test
    public void testIsFlashcardSetEmpty() {
        int flashcardSetId = testFlashcardSet.getSetId();
        boolean isEmpty = createFlashcardViewModel.isFlashcardSetEmpty(flashcardSetId);
        assertFalse(isEmpty, "Flashcard set should not be empty");
    }

    @Test
    public void testDeleteFlashcardSetIfEmpty() {
        int flashcardSetId = testFlashcardSet.getSetId();
        createFlashcardViewModel.deleteFlashcardSetIfEmpty(flashcardSetId);
        FlashcardSet deletedFlashcardSet = flashcardSetDao.findById(flashcardSetId);
        assertNull(deletedFlashcardSet);
    }
}