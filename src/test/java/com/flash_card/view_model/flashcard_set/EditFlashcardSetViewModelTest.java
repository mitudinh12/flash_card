package com.flash_card.view_model.flashcard_set;

import static org.junit.jupiter.api.Assertions.*;

import com.flash_card.model.entity.FlashcardSet;
import com.flash_card.model.entity.TestSetupAbstract;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class EditFlashcardSetViewModelTest extends TestSetupAbstract {
    private EditFlashcardSetViewModel editFlashcardSetViewModel;

    @BeforeEach
    public void setUp() {
        editFlashcardSetViewModel = new EditFlashcardSetViewModel(entityManager);
    }

    @Test
    public void testEditFlashcardSetViewModel() {
        editFlashcardSetViewModel = new EditFlashcardSetViewModel(entityManager);
        assertNotNull(editFlashcardSetViewModel);
    }

    @Test
    public void testSaveFlashcardSet() {
/*        String newName = "Updated Set Name";
        String newDescription = "Updated Description";
        String newTopic = "Updated Topic";

        editFlashcardSetViewModel.saveFlashcardSet(testFlashcardSet1.getSetId(), newName, newDescription, newTopic);

        FlashcardSet updatedSet = flashcardSetDao.findById(testFlashcardSet1.getSetId());
        assertNotNull(updatedSet);
        assertEquals(newName, updatedSet.getSetName());
        assertEquals(newDescription, updatedSet.getSetDescription());
        assertEquals(newTopic, updatedSet.getSetTopic());*/
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