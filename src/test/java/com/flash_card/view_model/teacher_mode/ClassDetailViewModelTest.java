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

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ClassDetailViewModelTest {
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

    @BeforeEach
    void setUp() {
        userDao.persist(testTeacher);
        userDao.persist(testStudent);
        teacherViewModel = new TeacherViewModel("789", entityManager);
        classDetailViewModel = new ClassDetailViewModel("789", entityManager);
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

    // Test the loadStudents method
    @Test
    @Order(1)
    void testLoadStudents() {
        teacherViewModel.addClass("Math", "Mathematics");
        Classroom classroom = classroomDao.findAllClassByTeacherId("789").getFirst();
        User student = userDao.findById("987");
        int result = teacherViewModel.addStudent(classroom.getClassroomId(), student.getEmail());
        assertEquals(1, result);
        classDetailViewModel.loadStudents(classroom.getClassroomId());
        assertFalse(classDetailViewModel.getStudentList().isEmpty());
    }

    // Test the loadSets method
    @Test
    @Order(2)
    void testLoadSets() {
        teacherViewModel.addClass("Math", "Mathematics");
        Classroom classroom = classroomDao.findAllClassByTeacherId("789").getFirst();
        flashcardSetDao.persist(testFlashcardSet);

        int result = teacherViewModel.assignFlashcardSets(List.of(testFlashcardSet.getSetId()), classroom.getClassroomId());
        assertEquals(1, result);

        classDetailViewModel.loadSets(classroom.getClassroomId());
        assertFalse(classDetailViewModel.getSetList().isEmpty());
    }

    @Test
    @Order(3)
    void testAssignSets() {
        teacherViewModel.addClass("Math", "Mathematics");
        Classroom classroom = classroomDao.findAllClassByTeacherId("789").getFirst();
        flashcardSetDao.persist(testFlashcardSet);

        int result = classDetailViewModel.assignSets(List.of(testFlashcardSet.getSetId()), classroom.getClassroomId());
        assertEquals(1, result);

        int result2 = classDetailViewModel.assignSets(List.of(testFlashcardSet.getSetId()), 0);
        assertEquals(-1, result2);

    }

    @Test
    @Order(4)
    void testDeleteSet() {
        teacherViewModel.addClass("Math", "Mathematics");
        Classroom classroom = classroomDao.findAllClassByTeacherId("789").getFirst();
        flashcardSetDao.persist(testFlashcardSet);

        int result = teacherViewModel.assignFlashcardSets(List.of(testFlashcardSet.getSetId()), classroom.getClassroomId());
        assertEquals(1, result);

        classDetailViewModel.loadSets(classroom.getClassroomId());

        int result2 = classDetailViewModel.deleteSet(classroom.getClassroomId(), classDetailViewModel.getSetList().getFirst());
        assertEquals(1, result2);

        int result3 = classDetailViewModel.assignSets(List.of(testFlashcardSet.getSetId()), classroom.getClassroomId());
        assertEquals(1, result3);

        classDetailViewModel.loadSets(classroom.getClassroomId());

        int result4 = classDetailViewModel.deleteSet(0, classDetailViewModel.getSetList().getFirst());
        assertEquals(0, result4);
    }

    @Test
    @Order(5)
    void testDeleteStudent() {
        teacherViewModel.addClass("Math", "Mathematics");
        Classroom classroom = classroomDao.findAllClassByTeacherId("789").getFirst();
        User student = userDao.findById("987");

        int result = teacherViewModel.addStudent(classroom.getClassroomId(), student.getEmail());
        assertEquals(1, result);

        classDetailViewModel.loadStudents(classroom.getClassroomId());

        String result2 = classDetailViewModel.deleteStudent(classroom.getClassroomId(), classDetailViewModel.getStudentList().getFirst());
        assertNull(result2);

        int result3 = teacherViewModel.addStudent(classroom.getClassroomId(), student.getEmail());
        assertEquals(1, result3);

        classDetailViewModel.loadStudents(classroom.getClassroomId());
        String result4 = classDetailViewModel.deleteStudent(0, classDetailViewModel.getStudentList().getFirst());
        assertEquals("Error in deleting student", result4);

        String result5 = classDetailViewModel.deleteStudent(classroom.getClassroomId(), null);
        assertNull(result5);
    }

}