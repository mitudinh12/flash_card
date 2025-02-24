package com.flash_card.view_model.teacher_mode;

import com.flash_card.model.dao.UserDao;
import com.flash_card.model.entity.Classroom;
import com.flash_card.model.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ClassRoomViewModelTest {
    private User testTeacher = new User("789", "Jo", "Hands", "jo@gmail.com", "123456");
    Classroom testClassRoom = new Classroom("Math", "Math 101", testTeacher);
    private TeacherViewModel teacherViewModel = null;
    private ClassRoomViewModel classRoomViewModel = null;
    private static EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("FlashcardMariaDbUnitTest");
    private static EntityManager entityManager = entityManagerFactory.createEntityManager();

    @BeforeEach
    void setUp() {
        teacherViewModel = new TeacherViewModel("789", entityManager);
        classRoomViewModel = new ClassRoomViewModel(testClassRoom,teacherViewModel);
    }

    @Test
    void testGetClassName() {
        assertEquals("Math", classRoomViewModel.getClassName().getValue());
    }

    @Test
    void testGetNumberStudents() {
        assertEquals("0", classRoomViewModel.getNumberStudents().getValue());
    }

    @Test
    void testGetNumberSets() {
        assertEquals("0", classRoomViewModel.getNumberSets().getValue());
    }

    @Test
    void testGetClassroom() {
        assertEquals(testClassRoom, classRoomViewModel.getClassroom());
    }

    @Test
    void testGetClassId() {
        assertEquals(0, classRoomViewModel.getClassId());
    }

    @Test
    void testGetClassDescription() {
        assertEquals("Math 101", classRoomViewModel.getClassDescription());
    }
}