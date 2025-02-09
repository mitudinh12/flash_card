package com.flash_card.view_model.flashcard;

import com.flash_card.model.entity.Flashcard;
import com.flash_card.model.entity.FlashcardSet;
import com.flash_card.model.entity.User;
import com.flash_card.view_model.user.HomepageViewModel;
import com.flash_card.view_model.user_auth.AuthSessionViewModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.flash_card.model.entity.TestSetupAbstract;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CreateFlashcardViewModelTest extends TestSetupAbstract {
    private CreateFlashcardViewModel createFlashcardViewModel;

    @BeforeEach
    public void setUp() {
        createFlashcardViewModel = new CreateFlashcardViewModel(entityManager);
    }


    @Test
    public void testCreateFlashcardViewModel() {
        createFlashcardViewModel = new CreateFlashcardViewModel(entityManager);
        assertNotNull(createFlashcardViewModel);
    }
/*
    @Test
    public void testSaveFlashcard() {
        int flashcardSetId = testFlashcardSet1.getSetId();
        String term = "Polymorphism";
        String definition = "The ability of an object to take on many forms.";

        createFlashcardViewModel.saveFlashcard(term, definition, flashcardSetId);

        List<Flashcard> flashcards = flashcardDao.findBySetId(flashcardSetId);
        boolean flashcardFound = flashcards.stream()
                .anyMatch(flashcard -> term.equals(flashcard.getTerm()) && definition.equals(flashcard.getDefinition()));

        assertTrue(flashcardFound, "Saved flashcard should be found in the flashcard set");
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

 */
}