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

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class EditFlashcardViewModelTest {

    private static final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("FlashcardMariaDbUnitTest");
    private static final EntityManager entityManager = entityManagerFactory.createEntityManager();
    private final EditFlashcardViewModel editFlashcardViewModel = new EditFlashcardViewModel(entityManager);
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
    }

    @AfterEach
    void tearDown() {
        if (flashcardDao.findById(testFlashcard.getCardId()) != null) {
            flashcardDao.delete(testFlashcard);
        }
        flashcardSetDao.delete(testFlashcardSet);
        userDao.delete(testUser);
    }

    @Test
    public void testEditFlashcardViewModel() {
        assertNotNull(editFlashcardViewModel);
    }

    @Test
    public void testGetSetName() {
        String setName = editFlashcardViewModel.getSetName(testFlashcardSet.getSetId());
        assertEquals(testFlashcardSet.getSetName(), setName);
        assertNull(editFlashcardViewModel.getSetName(0));
    }

    @Test
    public void testGetFlashcardIdsBySetId() {
        List<Integer> flashcardIds = editFlashcardViewModel.getFlashcardIdsBySetId(testFlashcardSet.getSetId());
        assertFalse(flashcardIds.isEmpty());
        assertEquals(testFlashcard.getCardId(), flashcardIds.get(0));
    }

    @Test
    public void testTerm() {
        String term = editFlashcardViewModel.term(testFlashcard.getCardId());
        assertEquals(testFlashcard.getTerm(), term);
    }

    @Test
    public void testDefinition() {
        String definition = editFlashcardViewModel.definition(testFlashcard.getCardId());
        assertEquals(testFlashcard.getDefinition(), definition);
    }

    @Test
    public void testUpdateFlashcard() {
        String newTerm = "Updated Term";
        String newDefinition = "Updated Definition";
        editFlashcardViewModel.updateFlashcard(testFlashcard.getCardId(), newTerm, newDefinition);
        Flashcard updatedFlashcard = flashcardDao.findById(testFlashcard.getCardId());
        assertEquals(newTerm, updatedFlashcard.getTerm());
        assertEquals(newDefinition, updatedFlashcard.getDefinition());
    }

    @Test
    public void testIsLastFlashcard() {
        boolean isLast = editFlashcardViewModel.isLastFlashcard(testFlashcardSet.getSetId());
        assertTrue(isLast);
    }

    @Test
    public void testDeleteFlashcard() {
        editFlashcardViewModel.deleteFlashcard(testFlashcard.getCardId(), testFlashcardSet.getSetId());
        Flashcard deletedFlashcard = flashcardDao.findById(testFlashcard.getCardId());
        assertNull(deletedFlashcard);
    }
}