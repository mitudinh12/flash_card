package com.flash_card.view_model.flashcard;

import com.flash_card.model.entity.Flashcard;
import com.flash_card.model.entity.FlashcardSet;
import com.flash_card.model.entity.TestSetupAbstract;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class EditFlashcardViewModelTest extends TestSetupAbstract {
    private EditFlashcardViewModel editFlashcardViewModel;

    @BeforeEach
    public void setUp() {
        editFlashcardViewModel = new EditFlashcardViewModel(entityManager);
    }

    @Test
    public void testEditFlashcardViewModel() {
        assertNotNull(editFlashcardViewModel);
    }

    @Test
    public void testGetSetName() {
        String setName = editFlashcardViewModel.getSetName(testFlashcardSet1.getSetId());
        assertEquals(testFlashcardSet1.getSetName(), setName);
    }

    @Test
    public void testGetFlashcardIdsBySetId() {
        List<Integer> flashcardIds = editFlashcardViewModel.getFlashcardIdsBySetId(testFlashcardSet1.getSetId());
        assertFalse(flashcardIds.isEmpty());
        assertEquals(testFlashcard1.getCardId(), flashcardIds.getFirst());
    }

    @Test
    public void testTerm() {
        String term = editFlashcardViewModel.term(testFlashcard1.getCardId());
        assertEquals(testFlashcard1.getTerm(), term);
    }

    @Test
    public void testDefinition() {
        String definition = editFlashcardViewModel.definition(testFlashcard1.getCardId());
        assertEquals(testFlashcard1.getDefinition(), definition);
    }

    @Test
    public void testUpdateFlashcard() {
        /*String newTerm = "Updated Term";
        String newDefinition = "Updated Definition";
        editFlashcardViewModel.updateFlashcard(testFlashcard1.getCardId(), newTerm, newDefinition);

        Flashcard updatedFlashcard = flashcardDao.findById(testFlashcard1.getCardId());
        assertEquals(newTerm, updatedFlashcard.getTerm());
        assertEquals(newDefinition, updatedFlashcard.getDefinition());*/
    }

    @Test
    public void testIsLastFlashcard() {
        boolean isLast = editFlashcardViewModel.isLastFlashcard(testFlashcardSet1.getSetId());
        assertTrue(isLast);
    }

    @Test
    public void testDeleteFlashcard() {
        editFlashcardViewModel.deleteFlashcard(testFlashcard1.getCardId(), testFlashcardSet1.getSetId());
        Flashcard deletedFlashcard = flashcardDao.findById(testFlashcard1.getCardId());
        assertNull(deletedFlashcard);
    }
}