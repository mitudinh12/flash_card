package com.flash_card.view_model.teacher_mode;

import com.flash_card.model.entity.Flashcard;
import com.flash_card.model.entity.FlashcardSet;
import com.flash_card.model.entity.User;
import com.flash_card.view_model.flashcard_set.AssignedFlashcardSetViewModel;
import jakarta.persistence.EntityManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class ClassDetailViewModel {
    private final ObservableList<StudentViewModel> studentList = FXCollections.observableArrayList();
    private final ObservableList<AssignedFlashcardSetViewModel> setList = FXCollections.observableArrayList();
    private TeacherViewModel teacherViewModel;
    private int classId;

    public ClassDetailViewModel(int classId, String teacherId, EntityManager em) {
        this.classId = classId;
        this.teacherViewModel = new TeacherViewModel(teacherId, em);
    }

    public void loadStudents() {
        List<User> students = teacherViewModel.getAllStudentsByClassId(classId);
        studentList.clear();
        students.stream()
                .map(student -> new StudentViewModel(student, teacherViewModel))
                .forEach(studentList::add);
    }

    public void loadSets() {
        List<FlashcardSet> flashcardSets = teacherViewModel.getAllSetsByClassId(classId);
        setList.clear();
        flashcardSets.stream()
                .map(AssignedFlashcardSetViewModel::new)
                .forEach(setList::add);
    }

    public String deleteStudent(StudentViewModel viewModel) {
        if (viewModel == null) return null;
        int result = teacherViewModel.deleteStudent(classId, viewModel.getStudentId());
        if (result != -1) {
            studentList.remove(viewModel);
            return null;
        } else {
            return "Error in deleting student";
        }
    }

    public ObservableList<StudentViewModel> getStudentList() {
        return studentList;
    }

    public ObservableList<AssignedFlashcardSetViewModel> getSetList() {
        return setList;
    }

}
