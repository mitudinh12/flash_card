package com.flash_card.view_model.student_mode;

import com.flash_card.framework.SetViewModel;
import com.flash_card.model.dao.AssignedSetDao;
import com.flash_card.model.dao.ClassroomDao;
import com.flash_card.model.dao.FlashcardSetDao;
import com.flash_card.model.entity.Classroom;
import com.flash_card.model.entity.FlashcardSet;
import jakarta.persistence.EntityManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class ClassDetailsViewModel {
    private AssignedSetDao assignedSetDao;
    private ClassroomDao classroomDao;
    private FlashcardSetDao flashcardSetDao;
    private int classId;
    private final ObservableList<SetViewModel> setList;
    private final ObservableList<ClassSetViewModel> flashcardSetList = FXCollections.observableArrayList();


    public ClassDetailsViewModel(EntityManager entityManager) {
        assignedSetDao = AssignedSetDao.getInstance(entityManager);
        flashcardSetDao = FlashcardSetDao.getInstance(entityManager);
        classroomDao = ClassroomDao.getInstance(entityManager);
        setList = FXCollections.observableArrayList();
    }

    public void loadClass(int classId) {
        this.classId = classId;
    }

    public ObservableList<SetViewModel> loadSets() {
        List<FlashcardSet> set = assignedSetDao.findAllSetsByClassId(classId);
        flashcardSetList.clear();
        set.stream().map(ClassSetViewModel::new).forEach(flashcardSetList::add);
        setList.addAll(flashcardSetList);
        return setList;
    }
}
