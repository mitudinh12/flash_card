package com.flash_card.view_model.teacher_mode;

import com.flash_card.model.entity.Classroom;
import jakarta.persistence.EntityManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class HomeTeacherViewModel {
    private final ObservableList<ClassRoomViewModel> classroomList = FXCollections.observableArrayList();
    private final TeacherViewModel teacherViewModel;

    public HomeTeacherViewModel(String userId, EntityManager em) {
        teacherViewModel = new TeacherViewModel(userId, em);
    }

    public void loadClassrooms() {
        List<Classroom> classrooms = teacherViewModel.getAllClassByTeacherId();
        classroomList.clear();

        classrooms.stream()
                .map(classroom -> new ClassRoomViewModel(classroom, teacherViewModel))
                .forEach(classroomList::add);
    }

    public int deleteClass(ClassRoomViewModel viewModel) {
        if (viewModel == null) return -1;
        teacherViewModel.deleteClass(viewModel.getClassroom());
        classroomList.remove(viewModel);
        return 1;
    }

    public ObservableList<ClassRoomViewModel> getClassroomList() {
        return classroomList;
    }
}
