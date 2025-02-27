package com.flash_card.view_model.student_mode;
import com.flash_card.model.dao.ClassMemberDao;
import com.flash_card.model.dao.ClassroomDao;
import com.flash_card.model.dao.UserDao;
import com.flash_card.model.entity.ClassMember;
import com.flash_card.model.entity.Classroom;
import com.flash_card.model.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class HomeStudentViewModelTest {
    private static EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("FlashcardMariaDbUnitTest");
    private static EntityManager entityManager = entityManagerFactory.createEntityManager();
    private User testStudent = new User("987", "Anna", "Smith", "anna@gmail.com", "654321");
    private User testTeacher = new User("789", "Jo", "Hands", "jo@gmail.com", "123456");
    private Classroom testClassroom = new Classroom("Math", "Mathematics", testTeacher);
    private UserDao userDao = UserDao.getInstance(entityManager);
    private ClassroomDao classroomDao = ClassroomDao.getInstance(entityManager);
    private ClassMemberDao classMemberDao = ClassMemberDao.getInstance(entityManager);
    private HomeStudentViewModel homeStudentViewModel = null;

    @BeforeEach
    void setUp() {
        userDao.persist(testTeacher);
        userDao.persist(testStudent);
        classroomDao.persistClass(testClassroom);
        ClassMember classMember = new ClassMember(testStudent, testClassroom);
        classMemberDao.persistClassMember(classMember);
        homeStudentViewModel = new HomeStudentViewModel("987", entityManager);
    }

    @AfterEach
    void tearDown() {
        ClassMember classMember = classMemberDao.findByStudentIdAndClassId(testClassroom.getClassroomId(), testStudent.getUserId());
        if (classMember != null) {
            classMemberDao.deleteClassMember(classMember);
        }
        classroomDao.deleteClass(testClassroom);
        userDao.delete(testTeacher);
        userDao.delete(testStudent);
    }

    @Test
    void testGetClassInfo() {
        homeStudentViewModel.loadClasses();
        List<Map<String, Object>> classInfoList = homeStudentViewModel.getClassInfo();
        assertFalse(classInfoList.isEmpty());
        Map<String, Object> classInfo = classInfoList.get(0);
        assertEquals("Math", classInfo.get("className"));
        assertEquals("Jo Hands", classInfo.get("teacherName"));
    }
}