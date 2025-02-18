package com.flash_card.view_model.teacher_mode;

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

    public ClassDetailViewModel(String teacherId, EntityManager em) {
        this.teacherViewModel = new TeacherViewModel(teacherId, em);
    }

    public void loadStudents(int classId) {
        List<User> students = teacherViewModel.getAllStudentsByClassId(classId);
        studentList.clear();
        students.stream()
                .map(student -> new StudentViewModel(student, teacherViewModel))
                .forEach(studentList::add);
    }

    public void loadSets(int classId) {
        List<FlashcardSet> flashcardSets = teacherViewModel.getAllSetsByClassId(classId);
        setList.clear();
        flashcardSets.stream()
                .map(AssignedFlashcardSetViewModel::new)
                .forEach(setList::add);
    }

    public int assignSets(List<Integer> setIds, int classId) {
        int result = teacherViewModel.assignFlashcardSets(setIds, classId);
        if (result == 1) {
            loadSets(classId);
            return 1;
        } else {
            return -1;
        }
    }

    public int deleteSet(int classId, AssignedFlashcardSetViewModel viewModel) {
        int setId = viewModel.getSet().getSetId();
        int result = teacherViewModel.deleteAssignedSet(setId, classId);
        if (result == 1) {
            setList.remove(viewModel);
            return 1;
        } else if (result == 0) {
            return 0;
        } else {
            return -1;
        }
    }

    public String deleteStudent(int classId ,StudentViewModel viewModel) {
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
