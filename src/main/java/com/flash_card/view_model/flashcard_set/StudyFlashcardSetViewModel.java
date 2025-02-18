package com.flash_card.view_model.flashcard_set;

import com.flash_card.framework.DifficultyLevel;
import com.flash_card.model.dao.FlashcardDao;
import com.flash_card.model.dao.FlashcardSetDao;
import com.flash_card.model.dao.StudyDao;
import com.flash_card.model.dao.UserDao;
import com.flash_card.model.entity.Flashcard;
import com.flash_card.model.entity.FlashcardSet;
import com.flash_card.model.entity.Study;
import com.flash_card.model.entity.User;
import jakarta.persistence.EntityManager;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleStringProperty;

import java.time.LocalDateTime;
import java.util.List;

public class StudyFlashcardSetViewModel {
    private final StudyDao studyDao;
    private Study currentStudy;
    private UserDao userDao;
    private final FlashcardDao flashcardDao;
    private FlashcardSetDao flashcardSetDao;
    private List<Flashcard> flashcards;
    private int setId;
    private final IntegerProperty currentIndex = new SimpleIntegerProperty(0);
    private final StringProperty setName = new SimpleStringProperty();
    private final StringProperty total = new SimpleStringProperty();

    public StudyFlashcardSetViewModel(EntityManager entityManager) {
        flashcardDao = FlashcardDao.getInstance(entityManager);
        studyDao = StudyDao.getInstance(entityManager);
        flashcardSetDao = FlashcardSetDao.getInstance(entityManager);
        userDao = UserDao.getInstance(entityManager);
    }

    public void loadFlashcards(int setId, String setName) {
        this.setId = setId;
        flashcards = flashcardDao.getHardFlashcards(setId);
        this.setName.set(setName);
        this.total.set(String.valueOf(flashcards.size()));
    }

    public void startStudy(String userId, int setId) {
        User user = userDao.findById(userId);
        FlashcardSet flashcardSet = flashcardSetDao.findById(setId);
        currentStudy = studyDao.findByUserIdAndSetId(userId, setId);

        if (currentStudy == null) {
            currentStudy = new Study(user, flashcardSet, LocalDateTime.now(), null, 0);
            studyDao.persist(currentStudy);
        } else {
            currentStudy.setStartTime(LocalDateTime.now());
            studyDao.update(currentStudy);
        }
    }

    public void endStudy() {
        if (currentStudy != null) {
            currentStudy.setEndTime(LocalDateTime.now());

            //count the number of "easy" flashcards
            long easyFlashcardsCount = flashcardDao.findBySetId(setId).stream()
                    .filter(flashcard -> flashcard.getDifficultLevel() == DifficultyLevel.easy)
                    .count();

            //update the number of studied cards
            currentStudy.setNumberStudiedWords((int) easyFlashcardsCount);
            studyDao.update(currentStudy);
        }
    }

    public Flashcard getCurrentFlashcard() {
        return flashcards.get(currentIndex.get());
    }

    public void nextFlashcard() {
        if (currentIndex.get() < flashcards.size() - 1) {
            currentIndex.set(currentIndex.get() + 1);
        }
    }

    public void previousFlashcard() {
        if (currentIndex.get() > 0) {
            currentIndex.set(currentIndex.get() - 1);
        }
    }

    public IntegerProperty currentIndexProperty() {
        return currentIndex;
    }

    public StringProperty setNameProperty() {
        return setName;
    }

    public StringProperty totalProperty() {
        return total;
    }

    public void updateFlashcardLevel(DifficultyLevel difficulty) {
        Flashcard currentFlashcard = getCurrentFlashcard();
        currentFlashcard.setDifficultLevel(difficulty);
        flashcardDao.update(currentFlashcard);
    }
}