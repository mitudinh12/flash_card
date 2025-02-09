package com.flash_card.view_model.flashcard;

import com.flash_card.model.entity.FlashcardSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.flash_card.model.entity.TestSetupAbstract;

import static org.junit.jupiter.api.Assertions.*;

public class CreateFlashcardViewModelTest extends TestSetupAbstract {
    private CreateFlashcardViewModel createFlashcardViewModel;

    @BeforeEach
    public void setUp() {
        createFlashcardViewModel = new CreateFlashcardViewModel(testUser1.getUserId(),entityManager);
    }


    @Test
    public void testCreateFlashcardViewModel() {
        assertNotNull(createFlashcardViewModel);
    }

    @Test
    public void testGetCurrentFlashcardSet() {

    }

    @Test
    public void testGetCurrentUser() {

    }

    @Test
    public void testSaveFlashcard() {
    }


    @Test
    public void testIsFlashcardSetEmpty() {
        int flashcardSetId = testFlashcardSet2.getSetId();
        boolean isEmpty = createFlashcardViewModel.isFlashcardSetEmpty(flashcardSetId);
        assertTrue(isEmpty, "Flashcard set should be empty");
    }

    @Test
    public void testDeleteFlashcardSetIfEmpty() {
        int flashcardSetId = testFlashcardSet2.getSetId();
        createFlashcardViewModel.deleteFlashcardSetIfEmpty(flashcardSetId);
        FlashcardSet deletedFlashcardSet = flashcardSetDao.findById(flashcardSetId);
        assertNull(deletedFlashcardSet);
    }
}