package com.flash_card.model.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "classrooms")
public class Classroom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "classroom_id")
    private int classroomId;

    @Column(name = "classroom_name")
    private String classroomName;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "teacher_id", referencedColumnName = "user_id")
    private User teacher;

    @OneToMany(mappedBy = "classroom")
    private List<ClassMember> classMembers;

    @OneToMany(mappedBy = "classroom")
    private List<AssignedSet> assignedSets;

    public Classroom(String classroomName, String description, User teacher) {
        this.classroomName = classroomName;
        this.description = description;
        this.teacher = teacher;
    }

    public Classroom() {
    }

    public void setClassroomId(int classroomId) {
        this.classroomId = classroomId;
    }

    public int getClassroomId() {
        return classroomId;
    }

    public void setClassroomName(String classroomName) {
        this.classroomName = classroomName;
    }

    public String getClassroomName() {
        return classroomName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setTeacher(User teacher) {
        this.teacher = teacher;
    }

    public User getTeacher() {
        return teacher;
    }
}
