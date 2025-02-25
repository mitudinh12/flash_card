package com.flash_card.model.dao;

import com.flash_card.model.entity.AssignedSet;
import com.flash_card.model.entity.Classroom;
import com.flash_card.model.entity.FlashcardSet;
import com.flash_card.model.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AssignedSetDaoTest {
    private User testTeacher = new User("789", "Jo", "Hands", "jo@gmail.com", "123456");
    private Classroom testClassroom = new Classroom("Java 101", "abc123", testTeacher);
    private FlashcardSet testFlashcardSet = new FlashcardSet("Java Basics", "Java 101", "Programing",testTeacher);
    private static EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("FlashcardMariaDbUnitTest");
    private static EntityManager entityManager = entityManagerFactory.createEntityManager();
    private ClassroomDao classroomDao = ClassroomDao.getInstance(entityManager);
    private UserDao userDao = UserDao.getInstance(entityManager);
    private FlashcardSetDao flashcardSetDao = FlashcardSetDao.getInstance(entityManager);
    private AssignedSetDao assignedSetDao = AssignedSetDao.getInstance(entityManager);

    @BeforeEach
    void setUp() {
        userDao.persist(testTeacher);
        classroomDao.persistClass(testClassroom);
        flashcardSetDao.persist(testFlashcardSet);
    }

    @AfterEach
    void tearDown() {
        AssignedSet foundAssignedSet = assignedSetDao.findBySetIdAndClassId(testFlashcardSet.getSetId(), testClassroom.getClassroomId());
        if (foundAssignedSet == null) {
            return;
        }
        assignedSetDao.deleteAssignedSet(foundAssignedSet);
        flashcardSetDao.delete(testFlashcardSet);
        classroomDao.deleteClass(testClassroom);
        userDao.delete(testTeacher);
    }

    @Test
    @Order(1)
    void testPersistAssignedSet() {
        AssignedSet assignedSet = new AssignedSet(testFlashcardSet, testClassroom);
        assertTrue(assignedSetDao.persistAssignedSet(assignedSet), "Should return true when assignedSet is persisted");
        AssignedSet invalidAssignedSet = null;
        assertFalse(assignedSetDao.persistAssignedSet(invalidAssignedSet), "Should return false when exception is thrown");
    }

    @Test
    @Order(2)
    void testDeleteAssignedSet() {
        AssignedSet assignedSet = new AssignedSet(testFlashcardSet, testClassroom);
        assignedSetDao.persistAssignedSet(assignedSet);
        assertTrue(assignedSetDao.deleteAssignedSet(assignedSet), "Should return true when assignedSet is deleted");
        AssignedSet invalidAssignedSet = null;
        assertFalse(assignedSetDao.deleteAssignedSet(invalidAssignedSet), "Should return false when exception is thrown");
    }

    @Test
    @Order(3)
    void testFindAllSetsByClassId() {
        AssignedSet assignedSet = new AssignedSet(testFlashcardSet, testClassroom);
        assignedSetDao.persistAssignedSet(assignedSet);
        assertFalse(assignedSetDao.findAllSetsByClassId(testClassroom.getClassroomId()).isEmpty(), "Should return 1 when assignedSet is found");
    }

    @Test
    @Order(4)
    void testFindBySetIdAndClassId() {
        AssignedSet assignedSet = new AssignedSet(testFlashcardSet, testClassroom);
        assignedSetDao.persistAssignedSet(assignedSet);
        assertNotNull(assignedSetDao.findBySetIdAndClassId(testFlashcardSet.getSetId(), testClassroom.getClassroomId()), "Should return assignedSet when found");
        assertNull(assignedSetDao.findBySetIdAndClassId(0, 0), "Should return null when assignedSet is not found");
    }


}