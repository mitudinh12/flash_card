package com.flash_card.view_model.teacher_mode;

import com.flash_card.model.entity.Classroom;
import jakarta.persistence.EntityManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

/**
 * ViewModel for the teacher's home page, responsible for managing and displaying
 * the list of classrooms assigned to the logged-in teacher.
 */
public class HomeTeacherViewModel {

    /**
     * The observable list of classrooms managed by the teacher.
     */
    private final ObservableList<ClassRoomViewModel> classroomList = FXCollections.observableArrayList();

    /**
     * The view model providing data access methods for teacher operations.
     */
    private final TeacherViewModel teacherViewModel;

    /**
     * Constructs a new {@code HomeTeacherViewModel} for the specified user.
     *
     * @param userId the ID of the teacher
     * @param em     the {@link EntityManager} for database access
     */
    public HomeTeacherViewModel(final String userId, final EntityManager em) {
        teacherViewModel = new TeacherViewModel(userId, em);
    }

    /**
     * Loads the list of classrooms taught by the current teacher and updates the observable list.
     */
    public void loadClassrooms() {
        List<Classroom> classrooms = teacherViewModel.getAllClassByTeacherId();
        classroomList.clear();

        classrooms.stream()
                .map(classroom -> new ClassRoomViewModel(classroom, teacherViewModel))
                .forEach(classroomList::add);
    }

    /**
     * Deletes the given classroom from the database and removes it from the UI list.
     *
     * @param viewModel the view model representing the classroom to delete
     * @return {@code 1} if deletion was successful, {@code -1} if the input is null
     */
    public int deleteClass(final ClassRoomViewModel viewModel) {
        if (viewModel == null) {
            return -1;
        }
        teacherViewModel.deleteClass(viewModel.getClassroom());
        classroomList.remove(viewModel);
        return 1;
    }

    /**
     * Returns the observable list of classroom view models.
     *
     * @return the list of classrooms
     */
    public ObservableList<ClassRoomViewModel> getClassroomList() {
        return classroomList;
    }
}
