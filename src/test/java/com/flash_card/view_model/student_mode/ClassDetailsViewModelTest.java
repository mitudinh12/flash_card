package com.flash_card.view_model.student_mode;

import com.flash_card.framework.SetViewModel;
import com.flash_card.model.dao.AssignedSetDao;
import com.flash_card.model.dao.ClassroomDao;
import com.flash_card.model.dao.FlashcardSetDao;
import com.flash_card.model.dao.UserDao;
import com.flash_card.model.entity.AssignedSet;
import com.flash_card.model.entity.Classroom;
import com.flash_card.model.entity.FlashcardSet;
import com.flash_card.model.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ClassDetailsViewModelTest {
    private static final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("FlashcardMariaDbUnitTest");
    private static final EntityManager entityManager = entityManagerFactory.createEntityManager();

    private AssignedSetDao assignedSetDao = AssignedSetDao.getInstance(entityManager);
    private ClassroomDao classroomDao = ClassroomDao.getInstance(entityManager);
    private FlashcardSetDao flashcardSetDao  = FlashcardSetDao.getInstance(entityManager);
    private UserDao userDao = UserDao.getInstance(entityManager);

    private User testTeacher = new User("987", "Jo987", "Hands", "jo987@gmail.com", "123456");
    private Classroom testClassroom = new Classroom("Math 987", "Mathematics", testTeacher);
    private FlashcardSet flashcardSet1 = new FlashcardSet("Test Set", "Test Description", "Test Topic", testTeacher);
    private FlashcardSet flashcardSet2 = new FlashcardSet("Test Set 2", "Test Description 2", "Test Topic 2", testTeacher);

    private AssignedSet assignedSet1 = new AssignedSet(flashcardSet1, testClassroom);
    private AssignedSet assignedSet2 = new AssignedSet(flashcardSet2, testClassroom);

    private ClassDetailsViewModel classDetailsViewModel;
    private ObservableList<SetViewModel> setList = FXCollections.observableArrayList();
    private ObservableList<ClassSetViewModel> flashcardSetList = FXCollections.observableArrayList();

    @BeforeEach
    public void setUp() {
        classDetailsViewModel = new ClassDetailsViewModel(entityManager);
        userDao.persist(testTeacher);
        classroomDao.persistClass(testClassroom);
        flashcardSetDao.persist(flashcardSet1);
        flashcardSetDao.persist(flashcardSet2);
        assignedSetDao.persistAssignedSet(assignedSet1);
        assignedSetDao.persistAssignedSet(assignedSet2);
    }

    @Test
    @Order(1)
    public void testClassDetailsViewModelInitialization() {
        assertNotNull(classDetailsViewModel);
    }

    @Test
    @Order(2)
    public void testLoadClass() {
        classDetailsViewModel.loadClass(testClassroom.getClassroomId());

        // Verify classId is set using reflection
        assertDoesNotThrow(() -> {
            var field = ClassDetailsViewModel.class.getDeclaredField("classId");
            field.setAccessible(true);
            assertEquals(testClassroom.getClassroomId(), field.get(classDetailsViewModel));
        });
    }

    @Test
    @Order(3)
    public void testLoadSets() {
        // Load sets in ViewModel
        classDetailsViewModel.loadClass(testClassroom.getClassroomId());
        ObservableList<SetViewModel> result = classDetailsViewModel.loadSets();

        // Validate results
        assertNotNull(result);
        assertEquals(2, result.size());
    }
}