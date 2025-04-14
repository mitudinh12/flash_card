package com.flash_card.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
/**
 * Entity class representing an assigned flashcard set in the application.
 * Maps to the "assigned_sets" table in the database.
 */
@Entity
@Table(name = "assigned_sets")
public class AssignedSet {
    /**
     * Unique identifier for the assigned set.
     * Maps to the "assigning_id" column in the database.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "assigning_id")
    private int id;
    /**
     * The flashcard set associated with this assignment.
     * Maps to the "set_id" column in the database.
     */
    @ManyToOne
    @JoinColumn(name = "set_id", referencedColumnName = "set_id", nullable = false)
    private FlashcardSet flashcardSet;
    /**
     * The classroom to which the flashcard set is assigned.
     * Maps to the "classroom_id" column in the database.
     */
    @ManyToOne
    @JoinColumn(name = "classroom_id", referencedColumnName = "classroom_id", nullable = false)
    private Classroom classroom;

    /**
     * Constructs an AssignedSet with the specified flashcard set and classroom.
     *
     * @param flashcardSetParam the flashcard set to assign
     * @param classroomParam the classroom to which the set is assigned
     */
    public AssignedSet(final FlashcardSet flashcardSetParam, final Classroom classroomParam) {
        super();
        this.flashcardSet = flashcardSetParam;
        this.classroom = classroomParam;
    }

    /**
     * Default constructor for the AssignedSet entity.
     */
    public AssignedSet() {
    }
    /**
     * Returns the unique identifier of the assigned set.
     *
     * @return the ID of the assigned set
     */
    public int getId() {
        return id;
    }
    /**
     * Returns the flashcard set associated with this assignment.
     *
     * @return the flashcard set
     */
    public FlashcardSet getFlashcardSet() {
        return flashcardSet;
    }
    /**
     * Returns the classroom to which the flashcard set is assigned.
     *
     * @return the classroom
     */
    public Classroom getClassroom() {
        return classroom;
    }

}
