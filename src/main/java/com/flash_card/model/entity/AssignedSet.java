package com.flash_card.model.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "assigned_sets")
public class AssignedSet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "assigning_id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "set_id", referencedColumnName = "set_id", nullable = false)
    private FlashcardSet flashcardSet;

    @ManyToOne
    @JoinColumn(name = "classroom_id", referencedColumnName = "classroom_id", nullable = false)
    private Classroom classroom;

    public AssignedSet(FlashcardSet flashcardSet, Classroom classroom) {
        super();
        this.flashcardSet = flashcardSet;
        this.classroom = classroom;
    }

    public AssignedSet() {}

    public int getId() {
        return id;
    }

    public FlashcardSet getFlashcardSet() {
        return flashcardSet;
    }

    public Classroom getClassroom() {
        return classroom;
    }

}
