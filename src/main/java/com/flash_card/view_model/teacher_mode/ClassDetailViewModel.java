package com.flash_card.view_model.teacher_mode;

import com.flash_card.localization.Localization;
import com.flash_card.model.entity.FlashcardSet;
import com.flash_card.model.entity.User;
import com.flash_card.view_model.flashcard_set.AssignedFlashcardSetViewModel;
import jakarta.persistence.EntityManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

/**
 * ViewModel for managing class details in teacher mode, including student lists
 * and assigned flashcard sets. Provides functionality to load, assign, and delete sets or students.
 */
public class ClassDetailViewModel {

    /**
     * Observable list of students in the selected class.
     */
    private final ObservableList<StudentViewModel> studentList = FXCollections.observableArrayList();

    /**
     * Observable list of flashcard sets assigned to the selected class.
     */
    private final ObservableList<AssignedFlashcardSetViewModel> setList = FXCollections.observableArrayList();

    /**
     * ViewModel that provides data access methods for the teacher's operations.
     */
    private TeacherViewModel teacherViewModel;

    /**
     * Localization instance for displaying localized messages.
     */
    private final Localization localization = Localization.getInstance();

    /**
     * Constructs a new {@code ClassDetailViewModel} with the given teacher ID and entity manager.
     *
     * @param teacherId the ID of the teacher managing the class
     * @param em        the {@link EntityManager} for database access
     */
    public ClassDetailViewModel(final String teacherId, final EntityManager em) {
        this.teacherViewModel = new TeacherViewModel(teacherId, em);
    }

    /**
     * Loads all students associated with the given class ID into the {@code studentList}.
     *
     * @param classId the ID of the class
     */
    public void loadStudents(final int classId) {
        List<User> students = teacherViewModel.getAllStudentsByClassId(classId);
        studentList.clear();
        students.stream()
                .map(student -> new StudentViewModel(student, teacherViewModel))
                .forEach(studentList::add);
    }

    /**
     * Loads all flashcard sets assigned to the given class ID into the {@code setList}.
     *
     * @param classId the ID of the class
     */
    public void loadSets(final int classId) {
        List<FlashcardSet> flashcardSets = teacherViewModel.getAllSetsByClassId(classId);
        setList.clear();
        flashcardSets.stream()
                .map(AssignedFlashcardSetViewModel::new)
                .forEach(setList::add);
    }

    /**
     * Assigns flashcard sets to a class based on the provided set IDs.
     *
     * @param setIds  a list of set IDs to assign
     * @param classId the ID of the class
     * @return {@code 1} if the operation is successful; {@code -1} otherwise
     */
    public int assignSets(final List<Integer> setIds, final int classId) {
        int result = teacherViewModel.assignFlashcardSets(setIds, classId);
        if (result == 1) {
            loadSets(classId);
            return 1;
        } else {
            return -1;
        }
    }

    /**
     * Deletes an assigned flashcard set from a class.
     *
     * @param classId   the ID of the class
     * @param viewModel the view model representing the assigned flashcard set
     * @return {@code 1} if the set was successfully deleted; {@code 0} otherwise
     */
    public int deleteSet(final int classId, final AssignedFlashcardSetViewModel viewModel) {
        int setId = viewModel.getSet().getSetId();
        int result = teacherViewModel.deleteAssignedSet(setId, classId);
        if (result == 1) {
            setList.remove(viewModel);
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * Deletes a student from a class and removes them from the UI if successful.
     *
     * @param classId   the ID of the class
     * @param viewModel the view model of the student to be deleted
     * @return {@code null} if deletion was successful; an error message otherwise
     */
    public String deleteStudent(final int classId, final StudentViewModel viewModel) {
        if (viewModel == null) {
            return null;
        }
        int result = teacherViewModel.deleteStudent(classId, viewModel.getStudentId());
        if (result == 1) {
            studentList.remove(viewModel);
            return null;
        } else {
            return localization.getMessage("teacher.errorDeleteStudent.message");
        }
    }

    /**
     * Returns the observable list of students in the class.
     *
     * @return an {@link ObservableList} of {@link StudentViewModel}
     */
    public ObservableList<StudentViewModel> getStudentList() {
        return studentList;
    }

    /**
     * Returns the observable list of flashcard sets assigned to the class.
     *
     * @return an {@link ObservableList} of {@link AssignedFlashcardSetViewModel}
     */
    public ObservableList<AssignedFlashcardSetViewModel> getSetList() {
        return setList;
    }
}
