package com.flash_card.view_model.flashcard;

import com.flash_card.model.dao.FlashcardDao;
import com.flash_card.model.dao.FlashcardSetDao;
import com.flash_card.model.entity.Flashcard;
import com.flash_card.model.entity.FlashcardSet;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.stream.Collectors;
/**
 * ViewModel class for managing the editing of flashcards.
 * Provides methods to retrieve, update, and delete flashcards and their details.
 */
public class EditFlashcardViewModel {
    /**
     * DAO for managing {@link Flashcard} entities.
     */
    private final FlashcardDao flashcardDao;
    /**
     * DAO for managing {@link FlashcardSet} entities.
     */
    private final FlashcardSetDao flashcardSetDao;
    /**
     * Constructor to initialize the ViewModel with the DAOs.
     *
     * @param entityManager the {@link EntityManager} instance for database operations
     */
    public EditFlashcardViewModel(final EntityManager entityManager) {
        this.flashcardDao = FlashcardDao.getInstance(entityManager);
        this.flashcardSetDao = FlashcardSetDao.getInstance(entityManager);
    }

    /**
     * Retrieves the name of a flashcard set by its ID.
     *
     * @param flashcardSetId the ID of the flashcard set
     * @return the name of the flashcard set, or null if not found
     */
    public String getSetName(final int flashcardSetId) {
        FlashcardSet flashcardSet = flashcardSetDao.findById(flashcardSetId);
        if (flashcardSet != null) {
            return flashcardSet.getSetName();
        } else {
            return null;
        }
    }

    /**
     * Retrieves a list of flashcard IDs for a given flashcard set ID.
     * The IDs are sorted in descending order.
     *
     * @param flashcardSetId the ID of the flashcard set
     * @return a list of flashcard IDs
     */
    public List<Integer> getFlashcardIdsBySetId(final int flashcardSetId) {
        List<Flashcard> flashcards = flashcardDao.findBySetId(flashcardSetId);
        return flashcards.stream()
                .sorted((f1, f2) -> f2.getCardId() - f1.getCardId())
                .map(Flashcard::getCardId)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a flashcard by its ID.
     *
     * @param flashcardId the ID of the flashcard
     * @return the {@link Flashcard} entity
     */
    private Flashcard getFlashcardById(final int flashcardId) {
        return flashcardDao.findById(flashcardId);
    }
    /**
     * Retrieves the term of a flashcard by its ID.
     *
     * @param flashcardId the ID of the flashcard
     * @return the term of the flashcard
     */
    public String term(final int flashcardId) {
        return getFlashcardById(flashcardId).getTerm();
    }
    /**
     * Retrieves the definition of a flashcard by its ID.
     *
     * @param flashcardId the ID of the flashcard
     * @return the definition of the flashcard
     */
    public String definition(final int flashcardId) {
        return getFlashcardById(flashcardId).getDefinition();
    }

    /**
     * Updates a flashcard's term and definition in the database.
     *
     * @param flashcardId the ID of the flashcard to update
     * @param term        the new term for the flashcard
     * @param definition  the new definition for the flashcard
     */
    public void updateFlashcard(final int flashcardId, final String term, final String definition) {
        Flashcard flashcard = getFlashcardById(flashcardId);
        if (flashcard != null) {
            flashcard.setTerm(term);
            flashcard.setDefinition(definition);
            flashcardDao.update(flashcard);
        }
    }

    /**
     * Checks if a flashcard set contains only one flashcard.
     *
     * @param flashcardSetId the ID of the flashcard set
     * @return true if the flashcard set contains only one flashcard, false otherwise
     */
    public boolean isLastFlashcard(final int flashcardSetId) {
        List<Flashcard> flashcards = flashcardDao.findBySetId(flashcardSetId);
        return flashcards.size() == 1;
    }
    /**
     * Deletes a flashcard by its ID and updates the flashcard set's count.
     *
     * @param flashcardId    the ID of the flashcard to delete
     * @param flashcardSetId the ID of the flashcard set containing the flashcard
     */
    public void deleteFlashcard(final int flashcardId, final int flashcardSetId) {
        flashcardDao.delete(flashcardDao.findById(flashcardId));

        //decrease number of flashcards in the set by 1
        FlashcardSet flashcardSet = flashcardSetDao.findById(flashcardSetId);
        flashcardSet.subtractNumberFlashcard();
        flashcardSetDao.update(flashcardSet);
    }
}
