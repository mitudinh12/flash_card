package com.flash_card.model.entity;

import com.flash_card.model.dao.ClassMemberDao;
import com.flash_card.model.dao.ClassroomDao;
import com.flash_card.model.dao.UserDao;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClassMemberTest {
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
        ClassMember classMember = new ClassMember(testStudent, testClassroom);
        classMemberDao.persistClassMember(classMember);
        testClassMember = classMemberDao.findByStudentIdAndClassId(testClassroom.getClassroomId(), testStudent.getUserId());
        assertNotNull(testClassMember, "ClassMember should be found in the database");
    }

    @AfterEach
    void tearDown() {
        if (testClassMember == null) {
            return;
        }
        classMemberDao.deleteClassMember(testClassMember);
        testClassMember = null;
        classroomDao.deleteClass(testClassroom);
        userDao.delete(testTeacher);
        userDao.delete(testStudent);
    }

    @Test
    void testEmptyConstructor() {
        ClassMember emptyClassMember = new ClassMember();
        assertNotNull(emptyClassMember, "ClassMember object should not be null");
    }

    @Test
    void testGetId() {
        assertNotEquals(0, testClassMember.getId(), "Fail to get ClassMemberId");
    }

    @Test
    void testGetClassroom() {
        assertEquals(testClassroom, testClassMember.getClassroom(), "Fail to get Classroom");
    }
}