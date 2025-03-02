package com.flash_card.model.dao;

import com.flash_card.model.entity.ClassMember;
import com.flash_card.model.entity.Classroom;
import com.flash_card.model.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ClassMemberDaoTest {
    private ClassMember testClassMember;
    private User testTeacher = new User("789", "Jo", "Hands", "jo@gmail.com", "123456");
    private Classroom testClassroom = new Classroom("Java 101", "abc123", testTeacher);
    private User testStudent = new User("987", "Anna", "Smith", "anna@gmail.com", "654321");
    private static EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("FlashcardMariaDbUnitTest");
    private static EntityManager entityManager = entityManagerFactory.createEntityManager();
    private ClassroomDao classroomDao = ClassroomDao.getInstance(entityManager);
    private ClassMemberDao classMemberDao = ClassMemberDao.getInstance(entityManager);
    private UserDao userDao = UserDao.getInstance(entityManager);

    @BeforeEach
    void setUp() {
        userDao.persist(testTeacher);
        userDao.persist(testStudent);
        classroomDao.persistClass(testClassroom);
    }

    @AfterEach
    void tearDown() {
        ClassMember foundClassMember = classMemberDao.findByStudentIdAndClassId(testClassroom.getClassroomId(), testStudent.getUserId());
        if (foundClassMember == null) {
            return;
        }
        classMemberDao.deleteClassMember(foundClassMember);
        testClassMember = null;
        classroomDao.deleteClass(testClassroom);
        userDao.delete(testTeacher);
        userDao.delete(testStudent);
    }

    @Test
    @Order(1)
    void testPersistClassMember() {
        testClassMember = new ClassMember(testStudent, testClassroom);
        classMemberDao.persistClassMember(testClassMember);
        ClassMember foundClassMember = classMemberDao.findByStudentIdAndClassId(testClassroom.getClassroomId(), testStudent.getUserId());
        assertNotNull(foundClassMember, "Persisted class member should be found");
        assertEquals(testClassroom.getClassroomId(), foundClassMember.getClassroom().getClassroomId(), "Classroom id should match");
        ClassMember invalidClassMember = null;
        assertFalse(classMemberDao.persistClassMember(invalidClassMember), "Persisting class member should return false when an exception occurs");
    }

    @Test
    @Order(2)
    void testDeleteClassMember() {
        testClassMember = new ClassMember(testStudent, testClassroom);
        classMemberDao.persistClassMember(testClassMember);
        ClassMember foundClassMember = classMemberDao.findByStudentIdAndClassId(testClassroom.getClassroomId(), testStudent.getUserId());
        assertNotNull(foundClassMember, "Persisted class member should be found");
        classMemberDao.deleteClassMember(foundClassMember);
        assertNull(classMemberDao.findByStudentIdAndClassId(testClassroom.getClassroomId(), testStudent.getUserId()), "Deleted class member should not be found");
        assertFalse(classMemberDao.deleteClassMember(null), "Deleting class member should return false when an exception occurs");
    }

    @Test
    @Order(3)
    void testFindAllStudentByClassId() {
        testClassMember = new ClassMember(testStudent, testClassroom);
        classMemberDao.persistClassMember(testClassMember);
        ClassMember foundClassMember = classMemberDao.findByStudentIdAndClassId(testClassroom.getClassroomId(), testStudent.getUserId());
        assertNotNull(foundClassMember, "Persisted class member should be found");
        assertEquals(testClassroom.getClassroomId(), foundClassMember.getClassroom().getClassroomId(), "Classroom id should match");
        assertEquals(1, classMemberDao.findAllStudentByClassId(testClassroom.getClassroomId()).size(), "Should return one student");
    }

    @Test
    @Order(4)
    void testFindByStudentIdAndClassId() {
        testClassMember = new ClassMember(testStudent, testClassroom);
        classMemberDao.persistClassMember(testClassMember);
        ClassMember foundClassMember = classMemberDao.findByStudentIdAndClassId(testClassroom.getClassroomId(), testStudent.getUserId());
        assertNotNull(foundClassMember, "Persisted class member should be found");
        assertNull(classMemberDao.findByStudentIdAndClassId(0, null), "Should return null when student id and class id are null");
    }

    @Test
    @Order(5)
    void testFindAllClassesByStudentId() {
        testClassMember = new ClassMember(testStudent, testClassroom);
        classMemberDao.persistClassMember(testClassMember);
        List<Classroom> classMembers = classMemberDao.findAllClassesByStudentId(testStudent.getUserId());
        assertNotNull(classMembers, "Should return a list of classes");
    }

}