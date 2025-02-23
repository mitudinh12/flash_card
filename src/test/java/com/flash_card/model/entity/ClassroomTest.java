package com.flash_card.model.entity;

import com.flash_card.model.dao.ClassroomDao;
import com.flash_card.model.dao.UserDao;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ClassroomTest {
    private Classroom testClassroom;
    private User testTeacher;
    private final String className = "Java 101";
    private final String description = "abc123";
    protected static EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("FlashcardMariaDbUnitTest");
    protected static EntityManager entityManager = entityManagerFactory.createEntityManager();
    private ClassroomDao classroomDao = ClassroomDao.getInstance(entityManager);
    private UserDao userDao = UserDao.getInstance(entityManager);

    @BeforeEach
    void setUp() {
        User teacher = new User("789", "Jo", "Hands", "jo@gmail.com", "123456");
        userDao.persist(teacher);
        testTeacher = userDao.findById("789");
        assertNotNull(testTeacher, "Teacher should be found in the database");
        Classroom classroom = new Classroom(className, description, teacher);
        classroomDao.persistClass(classroom);
        this.testClassroom = classroomDao.findClassById(classroom.getClassroomId());
        assertNotNull(testClassroom, "Classroom should be found in the database");
    }

    @AfterEach
    void tearDown() {
        if (testTeacher == null) {
            return;
        }
        classroomDao.deleteClass(testClassroom);
        testClassroom = null;
        userDao.delete(testTeacher);
    }

    @Test
    void testEmptyConstructor() {
        Classroom emptyClassroom = new Classroom(); // Using the no-arg constructor

        assertNotNull(emptyClassroom, "Classroom object should not be null");
    }

    @Test
    void testGetClassroomId() {
        assertNotEquals(0, testClassroom.getClassroomId(), "Fail to get ClassroomId");
    }

    @Test
    void testGetClassName() {
        assertEquals(className, testClassroom.getClassroomName(), "Fail to get ClassName");
    }

    @Test
    void testSetClassName() {
        String newClassName = "Java 102";
        testClassroom.setClassroomName(newClassName);
        assertEquals(newClassName, testClassroom.getClassroomName(), "Fail to set ClassName");
    }

    @Test
    void testGetDescription() {
        assertEquals(description, testClassroom.getDescription(), "Fail to get ClassCode");
    }

    @Test
    void testSetDescription() {
        String newDescription = "def456";
        testClassroom.setDescription(newDescription);
        assertEquals(newDescription, testClassroom.getDescription(), "Fail to set Description");
    }

    @Test
    void testGetTeacher() {
        assertEquals(testTeacher, testClassroom.getTeacher(), "Fail to get TeacherId");
    }
}