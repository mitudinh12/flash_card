package com.flash_card.model.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "studies")
public class Study {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "study_id")
    private int studyId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "set_id", referencedColumnName = "set_id")
    private FlashcardSet flashcardSet;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Column(name = "number_studied_words", nullable = false)
    private int numberStudiedWords;

    public Study() {}

    public Study(User user, FlashcardSet flashcardSet, LocalDateTime startTime, LocalDateTime endTime, int numberStudiedWords) {
        this.user = user;
        this.flashcardSet = flashcardSet;
        this.startTime = startTime;
        this.endTime = endTime;
        this.numberStudiedWords = numberStudiedWords;
    }

    public int getStudyId() {
        return studyId;
    }

    public User getUser() {
        return user;
    }

    public FlashcardSet getFlashcardSet() {
        return flashcardSet;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public int getNumberStudiedWords() {
        return numberStudiedWords;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setFlashcardSet(FlashcardSet flashcardSet) {
        this.flashcardSet = flashcardSet;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public void setNumberStudiedWords(int numberStudiedWords) {
        this.numberStudiedWords = numberStudiedWords;
    }
}