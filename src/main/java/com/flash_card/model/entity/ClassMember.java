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
 * Entity class representing a member of a classroom.
 * Maps to the "classroom_members" table in the database.
 */
@Entity
@Table(name = "classroom_members")
public class ClassMember {
    /**
     * Unique identifier for the classroom member.
     * Maps to the "classroom_members_id" column in the database.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "classroom_members_id")
    private int classMemberId;
    /**
     * The student associated with this classroom membership.
     * Maps to the "student_id" column in the database.
     */
    @ManyToOne
    @JoinColumn(name = "student_id", referencedColumnName = "user_id", nullable = false)
    private User student;
    /**
     * The classroom associated with this membership.
     * Maps to the "classroom_id" column in the database.
     */
    @ManyToOne
    @JoinColumn(name = "classroom_id", referencedColumnName = "classroom_id", nullable = false)
    private Classroom classroom;
    /**
     * Constructs a ClassMember with the specified student and classroom.
     *
     * @param studentParam   the student associated with this membership
     * @param classroomParam the classroom associated with this membership
     */
    public ClassMember(final User studentParam, final Classroom classroomParam) {
        super();
        this.student = studentParam;
        this.classroom = classroomParam;
    }
    /**
     * Default constructor for the ClassMember entity.
     */
    public ClassMember() {
    }

    /**
     * Returns the unique identifier of the classroom member.
     *
     * @return the ID of the classroom member
     */
    public int getId() {
        return classMemberId;
    }
    /**
     * Returns the classroom associated with this membership.
     *
     * @return the classroom
     */
    public Classroom getClassroom() {
        return classroom;
    }
}
