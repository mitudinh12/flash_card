package com.flash_card.view_model.flashcard;

import com.flash_card.model.dao.FlashcardDao;
import com.flash_card.model.dao.FlashcardSetDao;
import com.flash_card.model.entity.Flashcard;
import com.flash_card.model.entity.FlashcardSet;
import java.util.List;
import java.util.stream.Collectors;


public class EditFlashcardViewModel {
    private final FlashcardDao flashcardDao = FlashcardDao.getInstance();
    private final FlashcardSetDao flashcardSetDao = FlashcardSetDao.getInstance();

    private Flashcard getFlashcardById(int flashcardId) {
        return flashcardDao.findById(flashcardId);
    }

    public String term(int flashcardId) {
        return getFlashcardById(flashcardId).getTerm();
    }

    public String definition(int flashcardId) {
        return getFlashcardById(flashcardId).getDefinition();
    }

    public void updateFlashcard(int flashcardId, String term, String definition) {
        Flashcard flashcard = getFlashcardById(flashcardId);
        flashcard.setTerm(term);
        flashcard.setDefinition(definition);
        flashcardDao.update(flashcard);
    }

    public boolean isLastFlashcard(int flashcardSetId) {
        List<Flashcard> flashcards = flashcardDao.findBySetId(flashcardSetId);
        return flashcards.size() == 1;
    }

    public void deleteFlashcard(int flashcardId) {
        flashcardDao.delete(flashcardDao.findById(flashcardId));
    }

    public List<Integer> getFlashcardIdsBySetId(int flashcardSetId) {
        List<Flashcard> flashcards = flashcardDao.findBySetId(flashcardSetId);
        return flashcards.stream()
                .map(Flashcard::getCardId)
                .collect(Collectors.toList());
    }

    public String getSetName(int flashcardSetId) {
        FlashcardSet flashcardSet = flashcardSetDao.findById(flashcardSetId);
        return flashcardSet.getSetName();
    }
}