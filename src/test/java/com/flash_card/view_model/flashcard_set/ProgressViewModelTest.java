package com.flash_card.view_model.flashcard_set;

import com.flash_card.framework.DifficultyLevel;
import com.flash_card.model.dao.*;
import com.flash_card.model.entity.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProgressViewModelTest {
    private static final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("FlashcardMariaDbUnitTest");
    private static final EntityManager entityManager = entityManagerFactory.createEntityManager();
    private final ProgressViewModel progressViewModel = new ProgressViewModel(entityManager);
    private final FlashcardDao flashcardDao = FlashcardDao.getInstance(entityManager);
    private final UserDao userDao = UserDao.getInstance(entityManager);
    private final StudyDao studyDao = StudyDao.getInstance(entityManager);
    private final QuizDao quizDao = QuizDao.getInstance(entityManager);
    private final ClassroomDao classroomDao = ClassroomDao.getInstance(entityManager);
    private final ClassMemberDao classMemberDao = ClassMemberDao.getInstance(entityManager);
    private final FlashcardSetDao flashcardSetDao = FlashcardSetDao.getInstance(entityManager);


    private final User testStudent = new User("random-id123", "Anna", "Smith", "randommail1@gmail.com", "654321");
    private final User testTeacher = new User("id-random123", "Jo", "Hands", "randommail2@gmail.com", "123456");
    private final Classroom testClassroom = new Classroom("Math", "Mathematics", testTeacher);
    private final FlashcardSet testFlashcardSet = new FlashcardSet("Math", "Mathematics", "Math", testTeacher);
    private final Flashcard testFlashcard = new Flashcard("Term", "Definition", DifficultyLevel.hard, testFlashcardSet, testTeacher);
    private final ClassMember classMember = new ClassMember(testStudent, testClassroom);
    @BeforeEach
    void setUp() {
        userDao.persist(testTeacher);
        userDao.persist(testStudent);
        classroomDao.persistClass(testClassroom);
        flashcardSetDao.persist(testFlashcardSet);
        flashcardDao.persist(testFlashcard);
        classMemberDao.persistClassMember(classMember);
    }

    @AfterEach
    void tearDown() {
        Study study = studyDao.findByUserIdAndSetId(testStudent.getUserId(), testFlashcardSet.getSetId());
        if (study != null) {
            studyDao.delete(study);
        }

        List<Quiz> quizzes = quizDao.findByUserIdAndSetId(testStudent.getUserId(), testFlashcardSet.getSetId());
        for (Quiz quiz : quizzes) {
            quizDao.delete(quiz);
        }
        classMemberDao.deleteClassMember(classMember);
        flashcardDao.delete(testFlashcard);
        flashcardSetDao.delete(testFlashcardSet);
        classroomDao.deleteClass(testClassroom);

        if (entityManager.contains(testTeacher)) {
            userDao.delete(testTeacher);
        } else {
            userDao.delete(entityManager.merge(testTeacher));
        }

        if (entityManager.contains(testStudent)) {
            userDao.delete(testStudent);
        } else {
            userDao.delete(entityManager.merge(testStudent));
        }
    }

    @Test
    @Order(1)
    void testGetTotalFlashcards() {
        int totalFlashcards = progressViewModel.getTotalFlashcards(testFlashcardSet.getSetId());
        assertEquals(1, totalFlashcards);
    }

    @Test
    @Order(2)
    void testGetStudiedFlashcards() {
        Study study = new Study(testStudent, testFlashcardSet, LocalDateTime.now(), null, 1);
        studyDao.persist(study);
        int studiedFlashcards = progressViewModel.getStudiedFlashcards(testStudent.getUserId(), testFlashcardSet.getSetId());
        assertEquals(1, studiedFlashcards);
    }

    @Test
    @Order(3)
    void testGetAllQuizzesForUserAndSet() {
        Quiz quiz1 = new Quiz(testStudent, testFlashcardSet, LocalDateTime.now().minusDays(1), LocalDateTime.now(), 3, 2);
        Quiz quiz2 = new Quiz(testStudent, testFlashcardSet, LocalDateTime.now().minusDays(2), LocalDateTime.now(), 4, 1);
        quizDao.persist(quiz1);
        quizDao.persist(quiz2);

        List<Quiz> quizzes = progressViewModel.getAllQuizzesForUserAndSet(testStudent.getUserId(), testFlashcardSet.getSetId());
        assertNotNull(quizzes);
        assertEquals(2, quizzes.size());
        assertTrue(quizzes.contains(quiz1));
        assertTrue(quizzes.contains(quiz2));
    }

    @Test
    @Order(4)
    void testCalculateHighestQuizPercentage() {
        Quiz quiz = new Quiz(testStudent, testFlashcardSet, LocalDateTime.now().minusDays(1), LocalDateTime.now(), 3, 0);
        quizDao.persist(quiz);
        double highestQuizPercentage = progressViewModel.calculateHighestQuizPercentage(testStudent.getUserId(), testFlashcardSet.getSetId());
        assertEquals(100.0, highestQuizPercentage);
    }

    @Test
    @Order(5)
    void testGetStudentProgressList() {
        Study study = new Study(testStudent, testFlashcardSet, LocalDateTime.now(), null, 1);
        studyDao.persist(study);
        Quiz quiz = new Quiz(testStudent, testFlashcardSet, LocalDateTime.now().minusDays(1), LocalDateTime.now(), 3, 0);
        quizDao.persist(quiz);
        List<Map<String, Object>> studentProgressList = progressViewModel.getStudentProgressList(testClassroom.getClassroomId(), testFlashcardSet.getSetId());
        assertFalse(studentProgressList.isEmpty());
        Map<String, Object> studentProgress = studentProgressList.getFirst();
        assertEquals("Anna", studentProgress.get("studentName"));
        assertEquals("1/1", studentProgress.get("flashcardsProgress"));
        assertEquals(100.0, studentProgress.get("highestQuizPercentage"));
    }
}