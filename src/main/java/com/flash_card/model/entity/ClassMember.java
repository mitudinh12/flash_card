package com.flash_card.model.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "classroom_members")
public class ClassMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "classroom_members_id")
    private int classMemberId;

    @ManyToOne
    @JoinColumn(name = "student_id", referencedColumnName = "user_id", nullable = false)
    private User student;

    @ManyToOne
    @JoinColumn(name = "classroom_id", referencedColumnName = "classroom_id", nullable = false)
    private Classroom classroom;

    public ClassMember(User student, Classroom classroom) {
        super();
        this.student = student;
        this.classroom = classroom;
    }

    public ClassMember() {}

    public int getId() {
        return classMemberId;
    }

    public User getStudent() {
        return student;
    }

    public Classroom getClassroom() {
        return classroom;
    }
}
