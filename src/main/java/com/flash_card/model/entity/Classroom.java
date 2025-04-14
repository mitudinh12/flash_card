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
 * Entity class representing a classroom in the application.
 * Maps to the "classrooms" table in the database.
 */
@Entity
@Table(name = "classrooms")
public class Classroom {
    /**
     * Unique identifier for the classroom.
     * Maps to the "classroom_id" column in the database.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "classroom_id")
    private int classroomId;
    /**
     * Name of the classroom.
     * Maps to the "classroom_name" column in the database.
     */
    @Column(name = "classroom_name")
    private String classroomName;
    /**
     * Description of the classroom.
     * Maps to the "description" column in the database.
     */
    @Column(name = "description")
    private String description;
    /**
     * The teacher associated with the classroom.
     * Maps to the "teacher_id" column in the database.
     */
    @ManyToOne
    @JoinColumn(name = "teacher_id", referencedColumnName = "user_id")
    private User teacher;
    /**
     * Constructs a Classroom with the specified name, description, and teacher.
     *
     * @param classroomNameParam the name of the classroom
     * @param descriptionParam   the description of the classroom
     * @param teacherParam       the teacher associated with the classroom
     */
    public Classroom(final String classroomNameParam, final String descriptionParam, final User teacherParam) {
        this.classroomName = classroomNameParam;
        this.description = descriptionParam;
        this.teacher = teacherParam;
    }
    /**
     * Default constructor for the Classroom entity.
     */
    public Classroom() {
    }
    /**
     * Returns the unique identifier of the classroom.
     *
     * @return the ID of the classroom
     */
    public int getClassroomId() {
        return classroomId;
    }
    /**
     * Returns the name of the classroom.
     *
     * @return the name of the classroom
     */
    public String getClassroomName() {
        return classroomName;
    }
    /**
     * Sets the name of the classroom.
     *
     * @param classroomNameParam the name to set
     */
    public void setClassroomName(final String classroomNameParam) {
        this.classroomName = classroomNameParam;
    }
    /**
     * Returns the description of the classroom.
     *
     * @return the description of the classroom
     */
    public String getDescription() {
        return description;
    }
    /**
     * Sets the description of the classroom.
     *
     * @param descriptionParam the description to set
     */
    public void setDescription(final String descriptionParam) {
        this.description = descriptionParam;
    }
    /**
     * Returns the teacher associated with the classroom.
     *
     * @return the teacher
     */
    public User getTeacher() {
        return teacher;
    }
}
