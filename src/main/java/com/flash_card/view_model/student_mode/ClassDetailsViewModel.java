package com.flash_card.view_model.student_mode;

import com.flash_card.framework.SetViewModel;
import com.flash_card.model.dao.AssignedSetDao;
import com.flash_card.model.dao.ClassroomDao;
import com.flash_card.model.dao.FlashcardSetDao;
import com.flash_card.model.entity.FlashcardSet;
import jakarta.persistence.EntityManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;
/**
 * ViewModel class for managing the details of a class.
 * Provides methods to load class information and retrieve flashcard sets assigned to the class.
 */
public class ClassDetailsViewModel {
    /**
     * Observable list of generic set view models.
     */
    private final ObservableList<SetViewModel> setList;
    /**
     * Observable list of flashcard set view models.
     */
    private final ObservableList<ClassSetViewModel> flashcardSetList = FXCollections.observableArrayList();
    /**
     * DAO for managing assigned sets.
     */
    private final AssignedSetDao assignedSetDao;
    /**
     * DAO for managing classrooms.
     */
    private final ClassroomDao classroomDao;
    /**
     * DAO for managing flashcard sets.
     */
    private final FlashcardSetDao flashcardSetDao;
    /**
     * The ID of the class being managed.
     */
    private int classId;

    /**
     * Constructor to initialize the ViewModel with the DAOs.
     *
     * @param entityManager the {@link EntityManager} instance for database operations
     */
    public ClassDetailsViewModel(final EntityManager entityManager) {
        assignedSetDao = AssignedSetDao.getInstance(entityManager);
        flashcardSetDao = FlashcardSetDao.getInstance(entityManager);
        classroomDao = ClassroomDao.getInstance(entityManager);
        setList = FXCollections.observableArrayList();
    }
    /**
     * Loads the class details for the given class ID.
     *
     * @param classIdParam the ID of the class to load
     */
    public void loadClass(int classIdParam) {
        this.classId = classIdParam;
    }
    /**
     * Loads the flashcard sets assigned to the class and returns them as an observable list.
     *
     * @return an observable list of {@link SetViewModel} instances
     */
    public ObservableList<SetViewModel> loadSets() {
        List<FlashcardSet> set = assignedSetDao.findAllSetsByClassId(classId);
        flashcardSetList.clear();
        set.stream().map(ClassSetViewModel::new).forEach(flashcardSetList::add);
        setList.addAll(flashcardSetList);
        return setList;
    }
}
