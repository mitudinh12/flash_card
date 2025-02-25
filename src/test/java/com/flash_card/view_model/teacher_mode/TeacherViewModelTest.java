package com.flash_card.view_model.teacher_mode;

import com.flash_card.model.dao.AssignedSetDao;
import com.flash_card.model.dao.ClassroomDao;
import com.flash_card.model.dao.FlashcardSetDao;
import com.flash_card.model.dao.UserDao;
import com.flash_card.model.entity.AssignedSet;
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
class TeacherViewModelTest {
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

    @BeforeEach
    void setUp() {
        userDao.persist(testTeacher);
        userDao.persist(testStudent);
        teacherViewModel = new TeacherViewModel("789", entityManager);
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

    @Test
    @Order(1)
    void testAddClass() {
        int result = teacherViewModel.addClass("Math", "Mathematics");
        assertEquals(1, result);

        int result2 = teacherViewModel.addClass(null, null);
        assertEquals(-1, result2);


    }

    @Test
    @Order(2)
    void testEditClass() {
        teacherViewModel.addClass("Math", "Mathematics");
        Classroom classroom = classroomDao.findAllClassByTeacherId("789").getFirst();

        int result1 = teacherViewModel.editClass(classroom.getClassroomId(), "Math", "Mathematics");
        assertEquals(1, result1);

        int result2 = teacherViewModel.editClass(10, "Math", "Mathematics");
        assertEquals(0, result2);
    }

    @Test
    @Order(3)
    void testDeleteClass() {
        teacherViewModel.addClass("Math", "Mathematics");
        Classroom classroom = classroomDao.findAllClassByTeacherId("789").getFirst();

        int result1 = teacherViewModel.deleteClass(classroom);
        assertEquals(1, result1);

        int result2 = teacherViewModel.deleteClass(null);
        assertEquals(-1, result2);
    }

    @Test
    @Order(4)
    void testAddStudent() {
        User student = userDao.findById("987");
        teacherViewModel.addClass("Math", "Mathematics");
        Classroom classroom = classroomDao.findAllClassByTeacherId("789").getFirst();
        int result = teacherViewModel.addStudent(classroom.getClassroomId(), student.getEmail());
        assertEquals(1, result);

        int result2 = teacherViewModel.addStudent(0, student.getEmail());
        assertEquals(0, result2);

        int result3 = teacherViewModel.addStudent(classroom.getClassroomId(), "abc");
        assertEquals(0, result3);
    }

    @Test
    @Order(5)
    void testDeleteStudent() {
        User student = userDao.findById("987");
        teacherViewModel.addClass("Math", "Mathematics");
        Classroom classroom = classroomDao.findAllClassByTeacherId("789").getFirst();
        teacherViewModel.addStudent(classroom.getClassroomId(), student.getEmail());
        int result = teacherViewModel.deleteStudent(classroom.getClassroomId(), student.getUserId());
        assertEquals(1, result);

        int result2 = teacherViewModel.deleteStudent(classroom.getClassroomId(), "123");
        assertEquals(0, result2);
    }

    @Test
    @Order(6)
    void testIsUserValid() {
        boolean result = teacherViewModel.isUserValid("anna@gmail.com");
        assertTrue(result);

        boolean result2 = teacherViewModel.isUserValid("abc");
        assertFalse(result2);
    }

    @Test
    @Order(7)
    void testIsUserAdded() {
        teacherViewModel.addClass("Math", "Mathematics");
        Classroom classroom = classroomDao.findAllClassByTeacherId("789").getFirst();

        boolean result = teacherViewModel.isStudentAdded(classroom.getClassroomId(), "anna@gmail.com");
        assertFalse(result);

        teacherViewModel.addStudent(classroom.getClassroomId(), "anna@gmail.com");
        boolean result2 = teacherViewModel.isStudentAdded(classroom.getClassroomId(), "anna@gmail.com");
        assertTrue(result2);
    }

    @Test
    @Order(8)
    void testGetAllStudentsByClassId() {
        teacherViewModel.addClass("Math", "Mathematics");
        Classroom classroom = classroomDao.findAllClassByTeacherId("789").getFirst();
        teacherViewModel.addStudent(classroom.getClassroomId(), "anna@gmail.com");
        List<User> students = teacherViewModel.getAllStudentsByClassId(classroom.getClassroomId());
        assertFalse(students.isEmpty());
    }

    @Test
    @Order(9)
    void testGetAllSetsByClassId() {
        teacherViewModel.addClass("Math", "Mathematics");
        Classroom classroom = classroomDao.findAllClassByTeacherId("789").getFirst();
        flashcardSetDao.persist(testFlashcardSet);
        AssignedSet assignedSet = new AssignedSet(testFlashcardSet, classroom);
        assignedSetDao.persistAssignedSet(assignedSet);
        List<FlashcardSet> sets = teacherViewModel.getAllSetsByClassId(classroom.getClassroomId());
        assertFalse(sets.isEmpty());
        flashcardSetDao.delete(testFlashcardSet);
        assignedSetDao.deleteAssignedSet(assignedSet);
    }

    @Test
    @Order(10)
    void testGetAllUnassignedSetsByClassIdAndTeacherId() {
        teacherViewModel.addClass("Math", "Mathematics");
        Classroom classroom = classroomDao.findAllClassByTeacherId("789").getFirst();
        flashcardSetDao.persist(testFlashcardSet);
        List<FlashcardSet> sets = teacherViewModel.getAllUnassignedSetsByClassIdAndTeacherId(classroom.getClassroomId());
        assertFalse(sets.isEmpty());
        flashcardSetDao.delete(testFlashcardSet);
    }

    @Test
    @Order(11)
    void testAssignFlashcardSets() {
        teacherViewModel.addClass("Math", "Mathematics");
        Classroom classroom = classroomDao.findAllClassByTeacherId("789").getFirst();
        flashcardSetDao.persist(testFlashcardSet);

        List<Integer> listSetIds = List.of(testFlashcardSet.getSetId());
        int result = teacherViewModel.assignFlashcardSets(listSetIds, classroom.getClassroomId());
        assertEquals(1, result);

        List<Integer> listSetIds2 = List.of(0);
        int result2 = teacherViewModel.assignFlashcardSets(listSetIds2, classroom.getClassroomId());
        assertEquals(-1, result2);
    }

    @Test
    @Order(12)
    void testDeleteAssignedSet() {
        teacherViewModel.addClass("Math", "Mathematics");
        Classroom classroom = classroomDao.findAllClassByTeacherId("789").getFirst();
        flashcardSetDao.persist(testFlashcardSet);
        AssignedSet assignedSet = new AssignedSet(testFlashcardSet, classroom);
        assignedSetDao.persistAssignedSet(assignedSet);

        int result = teacherViewModel.deleteAssignedSet(testFlashcardSet.getSetId(), classroom.getClassroomId());
        assertEquals(1, result);

        int result2 = teacherViewModel.deleteAssignedSet(0, classroom.getClassroomId());
        assertEquals(-1, result2);
    }

    @Test
    @Order(13)
    void testGetAllClassByTeacherId() {
        teacherViewModel.addClass("Math", "Mathematics");
        List<Classroom> classrooms = teacherViewModel.getAllClassByTeacherId();
        assertFalse(classrooms.isEmpty());
    }

}