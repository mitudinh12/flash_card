package com.flash_card.view_model.flashcard_set;

import com.flash_card.localization.Localization;
import com.flash_card.model.dao.QuizDao;
import com.flash_card.model.entity.Quiz;
import jakarta.persistence.EntityManager;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.time.Duration;

/**
 * ViewModel for managing and displaying quiz results.
 * Provides methods to retrieve quiz details such as total correct answers, total wrong answers, and quiz duration.
 */
public class QuizResultViewModel {
    /**
     * DAO for managing quiz-related database operations.
     */
    private final QuizDao quizDao;

    /**
     * The ID of the quiz.
     */
    private final int quizId;

    /**
     * The quiz entity associated with this ViewModel.
     */
    private final Quiz quiz;

    /**
     * Property for the quiz duration as a formatted string.
     */
    private final StringProperty quizTime = new SimpleStringProperty();

    /**
     * Localization instance for retrieving localized messages.
     */
    private final Localization localization = Localization.getInstance();

    /**
     * Constant for the number of seconds in a minute.
     */
    private static final int SECONDS_IN_A_MINUTE = 60;

    /**
     * Constructs a QuizResultViewModel with the specified EntityManager and quiz ID.
     * Initializes the quiz and calculates the quiz duration.
     *
     * @param entityManager the EntityManager for database operations
     * @param quizIdParam        the ID of the quiz
     */
    public QuizResultViewModel(final EntityManager entityManager, final int quizIdParam) {
        quizDao = QuizDao.getInstance(entityManager);
        this.quizId = quizIdParam;
        this.quiz = quizDao.findById(quizId);
        calculateTime();
    }


    /**
     * Gets the total number of correct answers in the quiz.
     *
     * @return the total number of correct answers
     */
    public int getTotalCorrect() {
        return quiz.getCorrectTimes();
    }

    /**
     * Gets the total number of wrong answers in the quiz.
     *
     * @return the total number of wrong answers
     */
    public int getTotalWrong() {
        return quiz.getWrongTimes();
    }

    /**
     * Gets the name of the flashcard set associated with the quiz.
     *
     * @return the name of the flashcard set
     */
    public String getSetName() {
        return quiz.getFlashcardSet().getSetName();
    }

    /**
     * Gets the ID of the flashcard set associated with the quiz.
     *
     * @return the ID of the flashcard set
     */
    public int getFlashcardSetId() {
        return quiz.getFlashcardSet().getSetId();
    }

    /**
     * Calculates the duration of the quiz and formats it as a localized string.
     * Updates the `quizTime` property with the formatted duration.
     */
    public void calculateTime() {
        Duration duration = Duration.between(quiz.getStartTime(), quiz.getEndTime());
        long seconds = duration.getSeconds();
        long minutes = seconds / SECONDS_IN_A_MINUTE;
        seconds = seconds % SECONDS_IN_A_MINUTE;
        quizTime.set(localization.getMessage("flashcardSet.quizTime")
                + minutes + localization.getMessage("flashcardSet.minuteAnnotation")
                + seconds + localization.getMessage("flashcardSet.secondAnnotation"));
    }

    /**
     * Gets the property for the quiz duration as a formatted string.
     *
     * @return the quiz time property
     */
    public StringProperty quizTimeProperty() {
        return quizTime;
    }
}
