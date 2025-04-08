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
 * Entity class representing a study session in the application.
 * Maps to the "studies" table in the database.
 */
@Entity
@Table(name = "studies")
public class Study {
    /**
     * Unique identifier for the study session.
     * Maps to the "study_id" column in the database.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "study_id")
    private int studyId;
    /**
     * The user associated with the study session.
     * Maps to the "user_id" column in the database.
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;
    /**
     * The flashcard set being studied.
     * Maps to the "set_id" column in the database.
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "set_id", referencedColumnName = "set_id")
    private FlashcardSet flashcardSet;
    /**
     * The start time of the study session.
     * Maps to the "start_time" column in the database.
     */
    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;
    /**
     * The end time of the study session.
     * Maps to the "end_time" column in the database.
     */
    @Column(name = "end_time")
    private LocalDateTime endTime;
    /**
     * The number of words studied during the session.
     * Maps to the "number_studied_words" column in the database.
     */
    @Column(name = "number_studied_words", nullable = false)
    private int numberStudiedWords;
    /**
     * Default constructor for the Study entity.
     */
    public Study() {
    }
    /**
     * Constructs a Study with the specified user, flashcard set, start time, end time, and number of studied words.
     *
     * @param userParam              the user associated with the study session
     * @param flashcardSetParam      the flashcard set being studied
     * @param startTimeParam         the start time of the study session
     * @param endTimeParam           the end time of the study session
     * @param numberStudiedWordsParam the number of words studied during the session
     */
    public Study(User userParam, FlashcardSet flashcardSetParam, LocalDateTime startTimeParam, LocalDateTime endTimeParam, int numberStudiedWordsParam) {
        this.user = userParam;
        this.flashcardSet = flashcardSetParam;
        this.startTime = startTimeParam;
        this.endTime = endTimeParam;
        this.numberStudiedWords = numberStudiedWordsParam;
    }
    /**
     * Returns the unique identifier of the study session.
     *
     * @return the ID of the study session
     */
    public int getStudyId() {
        return studyId;
    }
    /**
     * Returns the user associated with the study session.
     *
     * @return the user
     */
    public User getUser() {
        return user;
    }
    /**
     * Sets the user associated with the study session.
     *
     * @param userParam the user to set
     */
    public void setUser(User userParam) {
        this.user = userParam;
    }
    /**
     * Returns the flashcard set being studied.
     *
     * @return the flashcard set
     */
    public FlashcardSet getFlashcardSet() {
        return flashcardSet;
    }
    /**
     * Sets the flashcard set being studied.
     *
     * @param flashcardSetParam the flashcard set to set
     */
    public void setFlashcardSet(FlashcardSet flashcardSetParam) {
        this.flashcardSet = flashcardSetParam;
    }
    /**
     * Returns the start time of the study session.
     *
     * @return the start time
     */
    public LocalDateTime getStartTime() {
        return startTime;
    }

    /**
     * Sets the start time of the study session.
     *
     * @param startTimeParam the start time to set
     */
    public void setStartTime(LocalDateTime startTimeParam) {
        this.startTime = startTimeParam;
    }
    /**
     * Returns the end time of the study session.
     *
     * @return the end time
     */
    public LocalDateTime getEndTime() {
        return endTime;
    }
    /**
     * Sets the end time of the study session.
     *
     * @param endTimeParam the end time to set
     */
    public void setEndTime(LocalDateTime endTimeParam) {
        this.endTime = endTimeParam;
    }
    /**
     * Returns the number of words studied during the session.
     *
     * @return the number of studied words
     */
    public int getNumberStudiedWords() {
        return numberStudiedWords;
    }
    /**
     * Sets the number of words studied during the session.
     *
     * @param numberStudiedWordsParam the number of studied words to set
     */
    public void setNumberStudiedWords(int numberStudiedWordsParam) {
        this.numberStudiedWords = numberStudiedWordsParam;
    }
}