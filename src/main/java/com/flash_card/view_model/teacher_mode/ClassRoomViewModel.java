package com.flash_card.view_model.teacher_mode;

import com.flash_card.model.entity.Classroom;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ClassRoomViewModel {
    private Classroom classroom;
    private final StringProperty className;
    private final StringProperty numberStudents;
    private final StringProperty numberSets;
    private final int classId;
    private String classDescription;
    private final TeacherViewModel teacherViewModel;

    public ClassRoomViewModel(Classroom classroom, TeacherViewModel teacherViewModel) {
        this.classroom = classroom;
        this.classId = classroom.getClassroomId();
        this.classDescription = classroom.getDescription();
        this.teacherViewModel = teacherViewModel;
        this.className = new SimpleStringProperty(this.classroom.getClassroomName());
        this.numberStudents = new SimpleStringProperty(String.valueOf(teacherViewModel.getAllStudentsByClassId(this.classroom.getClassroomId()).size()));
        this.numberSets = new SimpleStringProperty(String.valueOf(teacherViewModel.getAllSetsByClassId(this.classroom.getClassroomId()).size()));
    }

    public StringProperty getClassName() { return className; }

    public StringProperty getNumberStudents() { return numberStudents; }

    public StringProperty getNumberSets() { return numberSets; }

    public Classroom getClassroom() { return classroom; }

    public int getClassId() { return classId; }

    public String getClassDescription() { return classDescription; }
}
