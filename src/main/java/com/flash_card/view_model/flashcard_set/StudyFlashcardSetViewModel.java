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
import com.flash_card.model.dao.CardDifficultLevelDao;
import com.flash_card.model.entity.CardDifficultLevel;
import jakarta.persistence.EntityManager;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleStringProperty;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

public class StudyFlashcardSetViewModel {
    private final StudyDao studyDao;
    private Study currentStudy;
    private final UserDao userDao;
    private final FlashcardDao flashcardDao;
    private final FlashcardSetDao flashcardSetDao;
    private final CardDifficultLevelDao cardDifficultLevelDao;
    private List<Flashcard> flashcards;
    private int setId;
    private final IntegerProperty currentIndex = new SimpleIntegerProperty(0);
    private final StringProperty setName = new SimpleStringProperty();
    private final StringProperty total = new SimpleStringProperty();
    private final StringProperty studyTime = new SimpleStringProperty();
    private final StringProperty studiedNum = new SimpleStringProperty();

    public StudyFlashcardSetViewModel(EntityManager entityManager) {
        flashcardDao = FlashcardDao.getInstance(entityManager);
        studyDao = StudyDao.getInstance(entityManager);
        flashcardSetDao = FlashcardSetDao.getInstance(entityManager);
        userDao = UserDao.getInstance(entityManager);
        cardDifficultLevelDao = CardDifficultLevelDao.getInstance(entityManager);
    }


    /* DB INTERACTION METHODS */

    public void loadFlashcards(int setId, String setName) {
        this.setId = setId;
        this.setName.set(setName);
        flashcards = flashcardDao.getHardFlashcards(setId, currentStudy.getStudyId());
        this.total.set(String.valueOf(flashcards.size()));
    }

    public void startStudy(String userId, int setId) {
        User user = userDao.findById(userId);
        FlashcardSet flashcardSet = flashcardSetDao.findById(setId);
        currentStudy = studyDao.findByUserIdAndSetId(userId, setId);

        //if there is no study record, create a new one
        if (currentStudy == null) {
            currentStudy = new Study(user, flashcardSet, LocalDateTime.now(), null, 0);
            studyDao.persist(currentStudy);

            //match all flashcards of this set with this study to have a default difficulty level hard
            List<Flashcard> flashcards = flashcardDao.findBySetId(setId);
            for (Flashcard flashcard : flashcards) {
                CardDifficultLevel cardDifficultLevel = new CardDifficultLevel(flashcard, currentStudy, DifficultyLevel.hard);
                cardDifficultLevelDao.persistCardDifficultLevel(cardDifficultLevel);
            }
        } else {
            currentStudy.setStartTime(LocalDateTime.now()); //else update the start time
            currentStudy.setEndTime(null);
            studyDao.update(currentStudy);
        }
    }

    public void endStudy() {
        if (currentStudy != null) {
            currentStudy.setEndTime(LocalDateTime.now());
            //then update the number of studied cards to study table
            currentStudy.setNumberStudiedWords(getStudiedFlashcards());
            studyDao.update(currentStudy);
        }
    }

    public void updateFlashcardLevel(DifficultyLevel difficulty) {
        Flashcard currentFlashcard = getCurrentFlashcard();
        CardDifficultLevel cardDifficultLevel = cardDifficultLevelDao.findCardDifficultLevelByCardIdAndStudyId(currentFlashcard.getCardId(), currentStudy.getStudyId());
        cardDifficultLevel.setDifficultLevel(difficulty);
        cardDifficultLevelDao.persistCardDifficultLevel(cardDifficultLevel);
    }

    public void resetAllFlashcardLevel() {
        List<Flashcard> flashcards = flashcardDao.findBySetId(setId);
        for (Flashcard flashcard : flashcards) {
            CardDifficultLevel cardDifficultLevel = cardDifficultLevelDao.findCardDifficultLevelByCardIdAndStudyId(flashcard.getCardId(), currentStudy.getStudyId());
            if (cardDifficultLevel != null) {
                cardDifficultLevel.setDifficultLevel(DifficultyLevel.hard);
                cardDifficultLevelDao.persistCardDifficultLevel(cardDifficultLevel);
            }
        }
    }

    public void updateStudyDetails(String userId, int setId) {
        Study study = studyDao.findByUserIdAndSetId(userId, setId);
        LocalDateTime startTime = study.getStartTime();
        LocalDateTime endTime = study.getEndTime() != null ? study.getEndTime() : LocalDateTime.now();
        //calculate the study time in minutes and seconds
        if (startTime != null && endTime != null && !endTime.isBefore(startTime)) {
            Duration duration = Duration.between(startTime, endTime);
            long seconds = duration.getSeconds();
            long minutes = seconds / 60;
            seconds = seconds % 60;
            studyTime.set("Study duration: " + minutes + " minutes " + seconds + " seconds");
            studiedNum.set("Flashcard studied: " + study.getNumberStudiedWords() + "/" + flashcardDao.findBySetId(setId).size());
        } else {
            studyTime.set("Study duration: 0 seconds");
            studiedNum.set("Flashcard studied: 0/" + flashcardDao.findBySetId(setId).size());
        }
    }

    public int getTotalFlashcards() {
        return flashcardDao.findBySetId(setId).size();
    }

    public int getStudiedFlashcards() {
        return (int) flashcardDao.findBySetId(setId).stream()
                .filter(flashcard -> {
                    CardDifficultLevel cardDifficultLevel = cardDifficultLevelDao.findCardDifficultLevelByCardIdAndStudyId(flashcard.getCardId(), currentStudy.getStudyId());
                    return cardDifficultLevel != null && cardDifficultLevel.getDifficultLevel() == DifficultyLevel.easy;
                })
                .count();
    }

    /* HELPER METHODS */

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

    public void shuffleFlashcards() {
        Collections.shuffle(flashcards);
        currentIndex.set(0); //reset the index to show the first card
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

    public StringProperty studyTimeProperty() {
        return studyTime;
    }

    public StringProperty studiedNumProperty() {
        return studiedNum;
    }

    public List<Flashcard> getFlashcards() {
        return flashcards;
    }

    public DifficultyLevel getCurrentFlashcardDifficultLevel() {
        Flashcard currentFlashcard = getCurrentFlashcard();
        CardDifficultLevel cardDifficultLevel = cardDifficultLevelDao.findCardDifficultLevelByCardIdAndStudyId(currentFlashcard.getCardId(), currentStudy.getStudyId());
        return cardDifficultLevel != null ? cardDifficultLevel.getDifficultLevel() : DifficultyLevel.hard;
    }
}