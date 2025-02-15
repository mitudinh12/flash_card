package com.flash_card.view_model.teacher_mode;

import com.flash_card.model.entity.Classroom;
import jakarta.persistence.EntityManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

import static org.hibernate.internal.util.collections.ArrayHelper.forEach;

public class HomeTeacherViewModel {
    private final ObservableList<ClassRoomViewModel> classroomList = FXCollections.observableArrayList();
    private TeacherViewModel teacherViewModel;
    private String userId;

    public HomeTeacherViewModel(String userId, EntityManager em) {
        this.userId = userId;
        teacherViewModel = new TeacherViewModel(userId, em);
    }

    public void loadClassrooms() {
        List<Classroom> classrooms = teacherViewModel.getAllClassByTeacherId();
        classroomList.clear();

        classrooms.stream()
                .map(classroom -> new ClassRoomViewModel(classroom, teacherViewModel))
                .forEach(classroomList::add);
    }

    public void deleteClass(ClassRoomViewModel viewModel) {
        if (viewModel == null) return;
        teacherViewModel.deleteClass(viewModel.getClassroom());
        classroomList.remove(viewModel);

    }

    public ObservableList<ClassRoomViewModel> getClassroomList() {
        return classroomList;
    }
}
