package com.flash_card.view_model.flashcard_set;

import com.flash_card.framework.DifficultyLevel;
import com.flash_card.localization.Localization;
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

/**
 * ViewModel for managing the study process of a flashcard set.
 * Handles logic for starting, ending, and progressing through a study session.
 */
public class StudyFlashcardSetViewModel {

    /** DAO for managing study-related database operations. */
    private final StudyDao studyDao;

    /** The current study session. */
    private Study currentStudy;

    /** DAO for managing user-related database operations. */
    private final UserDao userDao;

    /** DAO for managing flashcard-related database operations. */
    private final FlashcardDao flashcardDao;

    /** DAO for managing flashcard set-related database operations. */
    private final FlashcardSetDao flashcardSetDao;

    /** DAO for managing card difficulty level-related database operations. */
    private final CardDifficultLevelDao cardDifficultLevelDao;

    /** List of flashcards in the current flashcard set. */
    private List<Flashcard> flashcards;

    /** The ID of the flashcard set being studied. */
    private int setId;

    /** Property for the current index of the flashcard in the study session. */
    private final IntegerProperty currentIndex = new SimpleIntegerProperty(0);

    /** Property for the name of the flashcard set. */
    private final StringProperty setName = new SimpleStringProperty();

    /** Property for the total number of flashcards in the set. */
    private final StringProperty total = new SimpleStringProperty();

    /** Property for the study time. */
    private final StringProperty studyTime = new SimpleStringProperty();

    /** Property for the number of studied flashcards. */
    private final StringProperty studiedNum = new SimpleStringProperty();

    /** Localization instance for retrieving localized messages. */
    private final Localization localization = Localization.getInstance();

    /** Constant for the number of seconds in a minute. */
    private static final int SECONDS_IN_A_MINUTE = 60;

    /**
     * Constructs a StudyFlashcardSetViewModel with the specified EntityManager.
     *
     * @param entityManager the EntityManager for database operations
     */
    public StudyFlashcardSetViewModel(final EntityManager entityManager) {
        flashcardDao = FlashcardDao.getInstance(entityManager);
        studyDao = StudyDao.getInstance(entityManager);
        flashcardSetDao = FlashcardSetDao.getInstance(entityManager);
        userDao = UserDao.getInstance(entityManager);
        cardDifficultLevelDao = CardDifficultLevelDao.getInstance(entityManager);
    }


    /* DB INTERACTION METHODS */
    /**
     * Loads the flashcards for the specified flashcard set and ensures all cards have a difficulty level.
     *
     * @param setIdParam   the ID of the flashcard set
     * @param setNameParam the name of the flashcard set
     */
    public void loadFlashcards(final int setIdParam, final String setNameParam) {
        this.setId = setIdParam;
        this.setName.set(setNameParam);
        List<Flashcard> allFlashcards = flashcardDao.findBySetId(setId);
        //Make sure all flashcards of the current study have difficulty level
        for (Flashcard flashcard : allFlashcards) {
            CardDifficultLevel cardDifficultLevel = cardDifficultLevelDao
                    .findCardDifficultLevelByCardIdAndStudyId(
                            flashcard.getCardId(),
                            currentStudy.getStudyId()
                    );
            if (cardDifficultLevel == null) {
                //Add difficulty level hard for new card added to the set
                cardDifficultLevel = new CardDifficultLevel(flashcard, currentStudy, DifficultyLevel.hard);
                cardDifficultLevelDao.persistCardDifficultLevel(cardDifficultLevel);
            }
        }
        flashcards = flashcardDao.getHardFlashcards(setId, currentStudy.getStudyId());
        this.total.set(String.valueOf(flashcards.size()));
    }

    /**
     * Starts a new study session or resumes an existing one for the specified user and flashcard set.
     *
     * @param userId the ID of the user
     * @param setIdParam  the ID of the flashcard set
     */
    public void startStudy(final String userId, final int setIdParam) {
        User user = userDao.findById(userId);
        FlashcardSet flashcardSet = flashcardSetDao.findById(setIdParam);
        currentStudy = studyDao.findByUserIdAndSetId(userId, setIdParam);

        //if there is no study record, create a new one
        if (currentStudy == null) {
            currentStudy = new Study(user, flashcardSet, LocalDateTime.now(), null, 0);
            studyDao.persist(currentStudy);

            //match all flashcards of this set with this study to have a default difficulty level hard
            List<Flashcard> allFlashcards = flashcardDao.findBySetId(setIdParam); // Renamed local variable
            for (Flashcard flashcard : allFlashcards) {
                CardDifficultLevel cardDifficultLevel = new CardDifficultLevel(
                        flashcard,
                        currentStudy,
                        DifficultyLevel.hard
                );
                cardDifficultLevelDao.persistCardDifficultLevel(cardDifficultLevel);
            }
        } else {
            currentStudy.setStartTime(LocalDateTime.now()); //else update the start time
            currentStudy.setEndTime(null);
            studyDao.update(currentStudy);
        }
    }

    /**
     * Ends the current study session and updates the number of studied flashcards.
     */
    public void endStudy() {
        if (currentStudy != null) {
            currentStudy.setEndTime(LocalDateTime.now());
            //then update the number of studied cards to study table
            currentStudy.setNumberStudiedWords(getStudiedFlashcards());
            studyDao.update(currentStudy);
        }
    }

    /**
     * Updates the difficulty level of the current flashcard.
     *
     * @param difficulty the new difficulty level
     */
    public void updateFlashcardLevel(final DifficultyLevel difficulty) {
        Flashcard currentFlashcard = getCurrentFlashcard();
        CardDifficultLevel cardDifficultLevel = cardDifficultLevelDao
                .findCardDifficultLevelByCardIdAndStudyId(
                        currentFlashcard.getCardId(),
                        currentStudy.getStudyId()
                );
        cardDifficultLevel.setDifficultLevel(difficulty);
        cardDifficultLevelDao.persistCardDifficultLevel(cardDifficultLevel);
    }

    /**
     * Resets the difficulty level of all flashcards in the set to "hard".
     */
    public void resetAllFlashcardLevel() {
        List<Flashcard> allFlashcards = flashcardDao.findBySetId(setId);
        for (Flashcard flashcard : allFlashcards) {
            CardDifficultLevel cardDifficultLevel = cardDifficultLevelDao
                    .findCardDifficultLevelByCardIdAndStudyId(
                            flashcard.getCardId(),
                            currentStudy.getStudyId()
                    );
            if (cardDifficultLevel != null) {
                cardDifficultLevel.setDifficultLevel(DifficultyLevel.hard);
                cardDifficultLevelDao.persistCardDifficultLevel(cardDifficultLevel);
            }
        }
    }

    /**
     * Updates the study details, including study time and the number of studied flashcards.
     *
     * @param userId the ID of the user
     * @param setIdParam  the ID of the flashcard set
     */
    public void updateStudyDetails(final String userId, final int setIdParam) {
        Study study = studyDao.findByUserIdAndSetId(userId, setIdParam);
        LocalDateTime startTime = study.getStartTime();
        LocalDateTime endTime = study.getEndTime() != null ? study.getEndTime() : LocalDateTime.now();
        //calculate the study time in minutes and seconds
        if (startTime != null && endTime != null && !endTime.isBefore(startTime)) {
            Duration duration = Duration.between(startTime, endTime);
            long seconds = duration.getSeconds();
            long minutes = seconds / SECONDS_IN_A_MINUTE;
            seconds = seconds % SECONDS_IN_A_MINUTE;
            studyTime.set(
                    localization.getMessage("flashcardSet.studyDuration") + minutes + " "
                            + localization.getMessage("flashcardSet.minuteAnnotation") + seconds + " "
                            + localization.getMessage("flashcardSet.secondAnnotation")
            );
            studiedNum.set(
                    localization.getMessage("flashcardSet.flashcardStudied") + " "
                            + study.getNumberStudiedWords() + "/"
                            + flashcardDao.findBySetId(setIdParam).size()
            );
        } else {
            studyTime.set(
                    localization.getMessage("flashcardSet.studyDuration") + "0 "
                            + localization.getMessage("flashcardSet.secondAnnotation")
            );
            studiedNum.set(
                    localization.getMessage("flashcardSet.flashcardStudied") + "0/"
                            + flashcardDao.findBySetId(setIdParam).size()
            );
        }
    }

    /**
     * Gets the total number of flashcards in the set.
     *
     * @return the total number of flashcards
     */
    public int getTotalFlashcards() {
        return flashcardDao.findBySetId(setId).size();
    }

    /**
     * Gets the number of studied flashcards in the set.
     *
     * @return the number of studied flashcards
     */
    public int getStudiedFlashcards() {
        return (int) flashcardDao.findBySetId(setId).stream()
                .filter(flashcard -> {
                    CardDifficultLevel cardDifficultLevel = cardDifficultLevelDao
                            .findCardDifficultLevelByCardIdAndStudyId(
                                    flashcard.getCardId(),
                                    currentStudy.getStudyId()
                            );
                    return cardDifficultLevel != null && cardDifficultLevel.getDifficultLevel() == DifficultyLevel.easy;
                })
                .count();
    }

    /* HELPER METHODS */

    /**
     * Gets the current flashcard in the study session.
     *
     * @return the current flashcard
     */
    public Flashcard getCurrentFlashcard() {
        return flashcards.get(currentIndex.get());
    }

    /**
     * Moves to the next flashcard in the study session.
     */
    public void nextFlashcard() {
        if (currentIndex.get() < flashcards.size() - 1) {
            currentIndex.set(currentIndex.get() + 1);
        }
    }

    /**
     * Moves to the previous flashcard in the study session.
     */
    public void previousFlashcard() {
        if (currentIndex.get() > 0) {
            currentIndex.set(currentIndex.get() - 1);
        }
    }

    /**
     * Shuffles the flashcards in the set and resets the current index.
     */
    public void shuffleFlashcards() {
        Collections.shuffle(flashcards);
        currentIndex.set(0); //reset the index to show the first card
    }

    /**
     * Gets the property for the current index of the flashcard in the study session.
     *
     * @return the current index property
     */
    public IntegerProperty currentIndexProperty() {
        return currentIndex;
    }

    /**
     * Gets the property for the name of the flashcard set.
     *
     * @return the name property of the flashcard set
     */
    public StringProperty setNameProperty() {
        return setName;
    }

    /**
     * Gets the property for the total number of flashcards in the set.
     *
     * @return the total property of the flashcard set
     */
    public StringProperty totalProperty() {
        return total;
    }

    /**
     * Gets the property for the study time.
     *
     * @return the study time property
     */
    public StringProperty studyTimeProperty() {
        return studyTime;
    }

    /**
     * Gets the property for the number of studied flashcards.
     *
     * @return the studied number property
     */
    public StringProperty studiedNumProperty() {
        return studiedNum;
    }

    /**
     * Gets the current study session.
     *
     * @return the current study session
     */
    public List<Flashcard> getFlashcards() {
        return flashcards;
    }

    /**
     * Gets the current study session.
     *
     * @return the current study session
     */
    public Study getCurrentStudy() {
        return currentStudy;
    }

    /**
     * Sets the current study session.
     *
     * @param study the current study session
     */
    public void setCurrentStudy(final Study study) {
        this.currentStudy = study;
    }

    /**
     * Gets the current flashcard's difficulty level.
     *
     * @return the current flashcard's difficulty level
     */
    public DifficultyLevel getCurrentFlashcardDifficultLevel() {
        Flashcard currentFlashcard = getCurrentFlashcard();
        CardDifficultLevel cardDifficultLevel = cardDifficultLevelDao
                .findCardDifficultLevelByCardIdAndStudyId(
                        currentFlashcard.getCardId(),
                        currentStudy.getStudyId()
                );
        return cardDifficultLevel != null ? cardDifficultLevel.getDifficultLevel() : DifficultyLevel.hard;
    }
}
