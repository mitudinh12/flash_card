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

    @Column(name = "student_id")
    private String studentId;

    @Column(name = "classroom_id")
    private int classroomId;

    @OneToOne(mappedBy = "classMember")
    private Classroom classroom;

    @OneToOne(mappedBy = "classMember")
    private User student;

    public ClassMember(String studentId, int classroomId) {
        super();
        this.studentId = studentId;
        this.classroomId = classroomId;
    }

    public ClassMember() {}

    public int getId() {
        return classMemberId;
    }

    public String getStudentId() {
        return studentId;
    }

    public int getClassroomId() {
        return classroomId;
    }

    public void setClassroomId(int classroomId) {
        this.classroomId = classroomId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }
}
