package com.flash_card.view_model.teacher_mode;

import com.flash_card.model.entity.Classroom;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * ViewModel representing a classroom in teacher mode. Wraps a {@link Classroom} entity
 * and provides observable properties for use in the JavaFX UI, such as class name,
 * number of students, and number of assigned sets.
 */
public class ClassRoomViewModel {

    /**
     * The underlying {@link Classroom} entity.
     */
    private Classroom classroom;

    /**
     * The name of the classroom, exposed as a JavaFX property.
     */
    private final StringProperty className;

    /**
     * The number of students in the classroom, exposed as a JavaFX property.
     */
    private final StringProperty numberStudents;

    /**
     * The number of flashcard sets assigned to the classroom, exposed as a JavaFX property.
     */
    private final StringProperty numberSets;

    /**
     * The unique identifier of the classroom.
     */
    private final int classId;

    /**
     * The description of the classroom.
     */
    private String classDescription;

    /**
     * Constructs a new {@code ClassRoomViewModel} using the given {@link Classroom} entity
     * and teacher data provider.
     *
     * @param currentClassroom        the classroom entity to represent
     * @param teacherViewModel the view model used to fetch student and set information
     */
    public ClassRoomViewModel(final Classroom currentClassroom, final TeacherViewModel teacherViewModel) {
        this.classroom = currentClassroom;
        this.classId = classroom.getClassroomId();
        this.classDescription = classroom.getDescription();
        this.className = new SimpleStringProperty(this.classroom.getClassroomName());
        this.numberStudents = new SimpleStringProperty(String.valueOf(
                teacherViewModel.getAllStudentsByClassId(this.classroom.getClassroomId()).size()));
        this.numberSets = new SimpleStringProperty(String.valueOf(
                teacherViewModel.getAllSetsByClassId(this.classroom.getClassroomId()).size()));
    }

    /**
     * Returns the class name as a JavaFX {@code StringProperty}.
     *
     * @return the class name property
     */
    public StringProperty getClassName() {
        return className;
    }

    /**
     * Returns the number of students as a JavaFX {@code StringProperty}.
     *
     * @return the number of students property
     */
    public StringProperty getNumberStudents() {
        return numberStudents;
    }

    /**
     * Returns the number of assigned sets as a JavaFX {@code StringProperty}.
     *
     * @return the number of sets property
     */
    public StringProperty getNumberSets() {
        return numberSets;
    }

    /**
     * Returns the underlying {@link Classroom} entity.
     *
     * @return the classroom entity
     */
    public Classroom getClassroom() {
        return classroom;
    }

    /**
     * Returns the unique identifier of the classroom.
     *
     * @return the classroom ID
     */
    public int getClassId() {
        return classId;
    }

    /**
     * Returns the description of the classroom.
     *
     * @return the class description
     */
    public String getClassDescription() {
        return classDescription;
    }
}
