package com.flash_card.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;

import java.time.LocalDateTime;
/**
 * Entity class representing a quiz in the application.
 * Maps to the "quizzes" table in the database.
 */
@Entity
@Table(name = "quizzes")
public class Quiz {
    /**
     * The unique identifier for the quiz.
     * Maps to the "quiz_id" column in the database.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "quiz_id")
    private int quizId;
    /**
     * The user who took the quiz.
     * Maps to the "user_id" column in the database.
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;
    /**
     * The flashcard set associated with the quiz.
     * Maps to the "set_id" column in the database.
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "set_id", referencedColumnName = "set_id")
    private FlashcardSet flashcardSet;
    /**
     * The start time of the quiz.
     * Maps to the "start_time" column in the database.
     */
    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;
    /**
     * The end time of the quiz.
     * Maps to the "end_time" column in the database.
     */
    @Column(name = "end_time")
    private LocalDateTime endTime;
    /**
     * The number of correct answers in the quiz.
     * Maps to the "correct_times" column in the database.
     */
    @Column(name = "correct_times")
    private int correctTimes;
    /**
     * The number of wrong answers in the quiz.
     * Maps to the "wrong_times" column in the database.
     */
    @Column(name = "wrong_times")
    private int wrongTimes;
    /**
     * Default constructor for the Quiz entity.
     */
    public Quiz() {
    }
    /**
     * Constructs a Quiz with the specified user, flashcard set, start time, end time, correct times, and wrong times.
     *
     * @param userParam          the user who took the quiz
     * @param flashcardSetParam  the flashcard set associated with the quiz
     * @param startTimeParam     the start time of the quiz
     * @param endTimeParam       the end time of the quiz
     * @param correctTimesParam  the number of correct answers
     * @param wrongTimesParam    the number of wrong answers
     */
    public Quiz(final User userParam,
                final FlashcardSet flashcardSetParam,
                final LocalDateTime startTimeParam,
                final LocalDateTime endTimeParam,
                final int correctTimesParam,
                final int wrongTimesParam) {
        this.user = userParam;
        this.flashcardSet = flashcardSetParam;
        this.startTime = startTimeParam;
        this.endTime = endTimeParam;
        this.correctTimes = correctTimesParam;
        this.wrongTimes = wrongTimesParam;
    }
    /**
     * Returns the unique identifier of the quiz.
     *
     * @return the ID of the quiz
     */
    public int getQuizId() {
        return quizId;
    }
    /**
     * Returns the user who took the quiz.
     *
     * @return the user
     */
    public User getUser() {
        return user;
    }

    /**
     * Sets the user who took the quiz.
     *
     * @param userParam the user to set
     */
    public void setUser(final User userParam) {
        this.user = userParam;
    }
    /**
     * Returns the flashcard set associated with the quiz.
     *
     * @return the flashcard set
     */
    public FlashcardSet getFlashcardSet() {
        return flashcardSet;
    }

    /**
     * Sets the flashcard set associated with the quiz.
     *
     * @param flashcardSetParam the flashcard set to set
     */
    public void setFlashcardSet(final FlashcardSet flashcardSetParam) {
        this.flashcardSet = flashcardSetParam;
    }
    /**
     * Returns the start time of the quiz.
     *
     * @return the start time
     */
    public LocalDateTime getStartTime() {
        return startTime;
    }
    /**
     * Sets the start time of the quiz.
     *
     * @param startTimeParam the start time to set
     */
    public void setStartTime(final LocalDateTime startTimeParam) {
        this.startTime = startTimeParam;
    }
    /**
     * Returns the end time of the quiz.
     *
     * @return the end time
     */
    public LocalDateTime getEndTime() {
        return endTime;
    }
    /**
     * Sets the end time of the quiz.
     *
     * @param endTimeParam the end time to set
     */
    public void setEndTime(final LocalDateTime endTimeParam) {
        this.endTime = endTimeParam;
    }
    /**
     * Returns the number of correct answers in the quiz.
     *
     * @return the number of correct answers
     */
    public int getCorrectTimes() {
        return correctTimes;
    }
    /**
     * Sets the number of correct answers in the quiz.
     *
     * @param correctTimesParam the number of correct answers to set
     */
    public void setCorrectTimes(final int correctTimesParam) {
        this.correctTimes = correctTimesParam;
    }
    /**
     * Returns the number of wrong answers in the quiz.
     *
     * @return the number of wrong answers
     */
    public int getWrongTimes() {
        return wrongTimes;
    }
    /**
     * Sets the number of wrong answers in the quiz.
     *
     * @param wrongTimesParam the number of wrong answers to set
     */
    public void setWrongTimes(final int wrongTimesParam) {
        this.wrongTimes = wrongTimesParam;
    }

}
