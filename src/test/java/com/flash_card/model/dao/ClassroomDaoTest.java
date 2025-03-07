package com.flash_card.model.dao;

import com.flash_card.model.entity.Classroom;
import com.flash_card.model.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

class ClassroomDaoTest {
    private Classroom testClassroom;
    private User testTeacher;
    User teacher = new User("classroomdao", "Jo", "Hands", "classroomdao@gmail.com", "123456");
    private final String className = "Java 101";
    private final String description = "abc123";
    Classroom classroom = new Classroom(className, description, teacher);
    protected static EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("FlashcardMariaDbUnitTest");
    protected static EntityManager entityManager = entityManagerFactory.createEntityManager();
    private ClassroomDao classroomDao = ClassroomDao.getInstance(entityManager);
    private UserDao userDao = UserDao.getInstance(entityManager);

    @BeforeEach
    void setUp() {
        userDao.persist(teacher);
        testTeacher = userDao.findById("classroomdao");
    }

    @AfterEach
    void tearDown() {
        if (testClassroom == null) {
            return;
        }
        classroomDao.deleteClass(testClassroom);
        testClassroom = null;
        userDao.delete(testTeacher);

    }

    @Test
    @Order(1)
    void testPersistClass() {
        classroomDao.persistClass(classroom);
        testClassroom = classroomDao.findClassById(classroom.getClassroomId());
        assertNotNull(testClassroom, "Persisted classroom should be found");
        assertEquals(className, testClassroom.getClassroomName(), "Class name should match");
        Classroom invalidClassroom = null;
        assertFalse(classroomDao.persistClass(invalidClassroom), "Persisting classroom should return false when an exception occurs");
    }

    @Test
    @Order(2)
    void testDeleteClass() {
        classroomDao.persistClass(classroom);
        testClassroom = classroomDao.findClassById(classroom.getClassroomId());
        assertNotNull(testClassroom, "Persisted classroom should be found");
        classroomDao.deleteClass(testClassroom);
        testClassroom = classroomDao.findClassById(classroom.getClassroomId());
        assertNull(testClassroom, "Deleted classroom should not be found");
        assertFalse(classroomDao.deleteClass(testClassroom), "Deleting the non exist classroom should return false");
    }

    @Test
    @Order(3)
    void testUpdateClass() {
        classroomDao.persistClass(classroom);
        testClassroom = classroomDao.findClassById(classroom.getClassroomId());
        assertNotNull(testClassroom, "Persisted classroom should be found");
        String updatedClassName = "Java 102";
        testClassroom.setClassroomName(updatedClassName);
        classroomDao.updateClass(testClassroom);
        testClassroom = classroomDao.findClassById(classroom.getClassroomId());
        assertEquals(updatedClassName, testClassroom.getClassroomName(), "Class name should be updated");
        Classroom invalidClassroom = null;
        assertFalse(classroomDao.updateClass(invalidClassroom), "Updating the non exist classroom should return false");
    }

    @Test
    @Order(4)
    void testFindClassById() {
        classroomDao.persistClass(classroom);
        testClassroom = classroomDao.findClassById(classroom.getClassroomId());
        assertNotNull(testClassroom, "Persisted classroom should be found");
        assertEquals(className, testClassroom.getClassroomName(), "Class name should match");
        assertNull(classroomDao.findClassById(0), "Finding the non exist classroom should return null");
    }

    @Test
    @Order(5)
    void testFindAllClassByTeacherId() {
        classroomDao.persistClass(classroom);
        List<Classroom> classrooms = classroomDao.findAllClassByTeacherId(testTeacher.getUserId());
        testClassroom = classrooms.getFirst();
        assertTrue(classrooms.contains(classroom), "Persisted classroom should be found");
        assertEquals(className, testClassroom.getClassroomName(), "Class name should match");
    }
}