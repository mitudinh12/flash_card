package com.flash_card.model.entity;

import com.flash_card.model.dao.AssignedSetDao;
import com.flash_card.model.dao.ClassroomDao;
import com.flash_card.model.dao.FlashcardSetDao;
import com.flash_card.model.dao.UserDao;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AssignedSetTest {
    AssignedSet testAssignedSet;
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
        AssignedSet assignedSet = new AssignedSet(testFlashcardSet, testClassroom);
        assignedSetDao.persistAssignedSet(assignedSet);
        testAssignedSet = assignedSetDao.findBySetIdAndClassId(testFlashcardSet.getSetId(), testClassroom.getClassroomId());
        assertNotNull(testAssignedSet, "AssignedSet should not be null");
    }

    @AfterEach
    void tearDown() {
        if (testAssignedSet == null) {
            return;
        }
        assignedSetDao.deleteAssignedSet(testAssignedSet);
        testAssignedSet = null;
        classroomDao.deleteClass(testClassroom);
        flashcardSetDao.delete(testFlashcardSet);
        userDao.delete(testTeacher);
    }

    @Test
    void testEmptyConstructor() {
        AssignedSet emptyAssignedSet = new AssignedSet();
        assertNotNull(emptyAssignedSet, "AssignedSet object should not be null");
    }

    @Test
    void testGetId() {
        assertNotEquals(0, testAssignedSet.getId(), "Fail to get AssignedSetId");
    }

    @Test
    void testGetFlashcardSet() {
        assertEquals(testFlashcardSet, testAssignedSet.getFlashcardSet(), "Fail to get FlashcardSet");
    }

    @Test
    void testGetClassroom() {
        assertEquals(testClassroom, testAssignedSet.getClassroom(), "Fail to get Classroom");
    }
}