package com.flash_card.view_model.teacher_mode;

import com.flash_card.model.entity.User;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class StudentViewModel {
    private User student;
    private final StringProperty studentName;
    private final StringProperty studentEmail;

    public StudentViewModel(User student, TeacherViewModel teacherViewModel) {
        this.student = student;
        String name = student.getFirstName() + " " + student.getLastName();
        this.studentName = new SimpleStringProperty(name) ;
        this.studentEmail = new SimpleStringProperty(student.getEmail());
    }

    public StringProperty getStudentName() {return studentName;}

    public StringProperty getStudentEmail() {return studentEmail;}

    public String getStudentId() {return this.student.getUserId();}
}
