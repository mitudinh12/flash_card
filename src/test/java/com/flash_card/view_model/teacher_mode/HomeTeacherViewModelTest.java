package com.flash_card.view_model.teacher_mode;

import com.flash_card.model.dao.AssignedSetDao;
import com.flash_card.model.dao.ClassroomDao;
import com.flash_card.model.dao.FlashcardSetDao;
import com.flash_card.model.dao.UserDao;
import com.flash_card.model.entity.Classroom;
import com.flash_card.model.entity.FlashcardSet;
import com.flash_card.model.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class HomeTeacherViewModelTest {
    private static EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("FlashcardMariaDbUnitTest");
    private static EntityManager entityManager = entityManagerFactory.createEntityManager();
    private User testTeacher = new User("789", "Jo", "Hands", "jo@gmail.com", "123456");
    User testStudent = new User("987", "Anna", "Smith", "anna@gmail.com", "654321");
    private FlashcardSet testFlashcardSet = new FlashcardSet("Java Basics", "Java 101", "Programing",testTeacher);
    private UserDao userDao = UserDao.getInstance(entityManager);
    private ClassroomDao classroomDao = ClassroomDao.getInstance(entityManager);
    private TeacherViewModel teacherViewModel = null;
    private FlashcardSetDao flashcardSetDao = FlashcardSetDao.getInstance(entityManager);
    private AssignedSetDao assignedSetDao = AssignedSetDao.getInstance(entityManager);
    private ClassDetailViewModel classDetailViewModel = null;
    private HomeTeacherViewModel homeTeacherViewModel = null;

    @BeforeEach
    void setUp() {
        userDao.persist(testTeacher);
        userDao.persist(testStudent);
        teacherViewModel = new TeacherViewModel("789", entityManager);
        homeTeacherViewModel = new HomeTeacherViewModel("789", entityManager);
    }

    @AfterEach
    void tearDown() {
        Classroom classroom = classroomDao.findClassById(1);
        if (classroom != null) {
            classroomDao.deleteClass(classroom);
        }

        User user = userDao.findById("789");
        if (user != null) {
            userDao.delete(user);
        }

        User student = userDao.findById("987");
        if (student != null) {
            userDao.delete(student);
        }
    }

    // Test the loadClassrooms method
    @Test
    @Order(1)
    void testLoadClassrooms() {
        teacherViewModel.addClass("Math", "Mathematics");
        Classroom classroom = classroomDao.findAllClassByTeacherId("789").getFirst();

        homeTeacherViewModel.loadClassrooms();
        assertFalse(homeTeacherViewModel.getClassroomList().isEmpty());
    }

    // Test the deleteClass method
    @Test
    @Order(2)
    void testDeleteClass() {
        teacherViewModel.addClass("Math", "Mathematics");
        homeTeacherViewModel.loadClassrooms();
        assertFalse(homeTeacherViewModel.getClassroomList().isEmpty());
        int result = homeTeacherViewModel.deleteClass(homeTeacherViewModel.getClassroomList().getFirst());
        assertEquals(1, result);

        int result2 = homeTeacherViewModel.deleteClass(null);
        assertEquals(-1, result2);
    }

}