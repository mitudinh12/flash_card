package com.flash_card.view_model.teacher_mode;

import com.flash_card.model.dao.UserDao;
import com.flash_card.model.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class StudentViewModelTest {
    User testStudent = new User("987", "Anna", "Smith", "anna@gmail.com", "654321");
    private TeacherViewModel teacherViewModel = null;
    private StudentViewModel studentViewModel = null;
    private static EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("FlashcardMariaDbUnitTest");
    private static EntityManager entityManager = entityManagerFactory.createEntityManager();
    private UserDao userDao = UserDao.getInstance(entityManager);

    @BeforeEach
    void setUp() {
        userDao.persist(testStudent);
        teacherViewModel = new TeacherViewModel("789", entityManager);
        studentViewModel = new StudentViewModel(testStudent, teacherViewModel);
    }

    @AfterEach
    void tearDown() {
        User student = userDao.findById(testStudent.getUserId());
        if (student != null) {
            userDao.delete(student);
        }
    }

    @Test
    @Order(1)
    void testGetStudentName() {
        assertEquals("Anna Smith", studentViewModel.getStudentName().getValue());
    }

    @Test
    @Order(2)
    void testGetStudentEmail() {
        assertEquals("anna@gmail.com", studentViewModel.getStudentEmail().getValue());
    }

    @Test
    @Order(3)
    void testGetStudentId() {
        assertEquals("987", studentViewModel.getStudentId());
    }
}