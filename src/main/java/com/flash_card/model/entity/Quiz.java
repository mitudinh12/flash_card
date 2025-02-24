package com.flash_card.model.entity;

import jakarta.persistence.*;
import org.checkerframework.checker.units.qual.C;

import java.time.LocalDateTime;

@Entity
@Table(name = "quizzes")
public class Quiz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "quiz_id")
    private int quizId;

    @OneToOne(optional = false)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

    @OneToOne(optional = false)
    @JoinColumn(name = "set_id", referencedColumnName = "set_id")
    private FlashcardSet flashcardSet;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Column(name = "correct_times")
    private int correctTimes;

    @Column(name = "wrong_times")
    private int wrongTimes;

    public Quiz() {}

    public Quiz(User user, FlashcardSet set, LocalDateTime startTime, LocalDateTime endTime, int correctTimes, int wrongTimes) {
        this.user = user;
        this.flashcardSet = set;
        this.startTime = startTime;
        this.endTime = endTime;
        this.correctTimes = correctTimes;
        this.wrongTimes = wrongTimes;
    }

    public int getQuizId() {
        return quizId;
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

    public int getCorrectTimes() {
        return correctTimes;
    }

    public int getWrongTimes() {
        return wrongTimes;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setFlashcardSet(FlashcardSet set) {
        this.flashcardSet = set;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public void setCorrectTimes(int correctTimes) {
        this.correctTimes = correctTimes;
    }

    public void setWrongTimes(int wrongTimes) {
        this.wrongTimes = wrongTimes;
    }

}
