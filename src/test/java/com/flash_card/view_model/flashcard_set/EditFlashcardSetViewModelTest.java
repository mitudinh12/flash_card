package com.flash_card.view_model.flashcard_set;

import static org.junit.jupiter.api.Assertions.*;

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

public class EditFlashcardSetViewModelTest {
    private static final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("FlashcardMariaDbUnitTest");
    private static final EntityManager entityManager = entityManagerFactory.createEntityManager();
    private EditFlashcardSetViewModel editFlashcardSetViewModel = new EditFlashcardSetViewModel(entityManager);

    private final FlashcardDao flashcardDao = FlashcardDao.getInstance(entityManager);
    private final FlashcardSetDao flashcardSetDao = FlashcardSetDao.getInstance(entityManager);
    private final UserDao userDao = UserDao.getInstance(entityManager);
    private final User testUser = new User("f5hhgds41", "John", "Doe", "563424gfgbf@example.com", "sample-id-token");
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
    public void testEditFlashcardSetViewModel() {
        assertNotNull(editFlashcardSetViewModel);
    }

    @Test
    public void testSaveFlashcardSet() {
        String newName = "Updated Set Name";
        String newDescription = "Updated Description";
        String newTopic = "Updated Topic";

        editFlashcardSetViewModel.saveFlashcardSet(testFlashcardSet.getSetId(), newName, newDescription, newTopic);

        FlashcardSet updatedSet = flashcardSetDao.findById(testFlashcardSet.getSetId());
        assertNotNull(updatedSet);
        assertEquals(newName, updatedSet.getSetName());
        assertEquals(newDescription, updatedSet.getSetDescription());
        assertEquals(newTopic, updatedSet.getSetTopic());
    }

    @Test
    public void testSaveFlashcardSetWithInvalidId() {
        int invalidSetId = -1;
        String newName = "Updated Set Name";
        String newDescription = "Updated Description";
        String newTopic = "Updated Topic";

        editFlashcardSetViewModel.saveFlashcardSet(invalidSetId, newName, newDescription, newTopic);

        FlashcardSet updatedSet = flashcardSetDao.findById(invalidSetId);
        assertNull(updatedSet);
    }
}