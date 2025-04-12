package com.flash_card.view_model.teacher_mode;

import com.flash_card.model.entity.User;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * ViewModel representing a student in teacher mode.
 * Provides observable properties for displaying the student's name and email in the UI.
 */
public class StudentViewModel {

    /**
     * The {@link User} entity representing the student.
     */
    private User student;

    /**
     * The full name of the student, exposed as a JavaFX property.
     */
    private final StringProperty studentName;

    /**
     * The email address of the student, exposed as a JavaFX property.
     */
    private final StringProperty studentEmail;

    /**
     * Constructs a new {@code StudentViewModel} for the given student.
     *
     * @param currentStudent the {@link User} entity representing the student
     * @param teacherViewModel the associated teacher view model (unused but may be useful in future logic)
     */
    public StudentViewModel(final User currentStudent, final TeacherViewModel teacherViewModel) {
        this.student = currentStudent;
        String name = student.getFirstName() + " " + student.getLastName();
        this.studentName = new SimpleStringProperty(name);
        this.studentEmail = new SimpleStringProperty(student.getEmail());
    }

    /**
     * Returns the student's full name as a JavaFX {@code StringProperty}.
     *
     * @return the full name property of the student
     */
    public StringProperty getStudentName() {
        return studentName;
    }

    /**
     * Returns the student's email as a JavaFX {@code StringProperty}.
     *
     * @return the email property of the student
     */
    public StringProperty getStudentEmail() {
        return studentEmail;
    }

    /**
     * Returns the unique identifier of the student.
     *
     * @return the student ID
     */
    public String getStudentId() {
        return this.student.getUserId();
    }
}
