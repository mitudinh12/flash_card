package com.flash_card.view_model.flashcard_set;
import com.flash_card.model.dao.ClassMemberDao;
import com.flash_card.model.dao.FlashcardDao;
import com.flash_card.model.dao.QuizDao;
import com.flash_card.model.dao.StudyDao;
import com.flash_card.model.entity.Quiz;
import com.flash_card.model.entity.User;
import jakarta.persistence.EntityManager;
import com.flash_card.model.entity.Study;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ViewModel for tracking and managing progress related to flashcard sets.
 * Provides methods to retrieve progress data for users, quizzes, and flashcards.
 */
public class ProgressViewModel {

    /**
            * DAO for managing quiz-related database operations.
     */
    private final QuizDao quizDao;

    /**
     * DAO for managing flashcard-related database operations.
     */
    private final FlashcardDao flashcardDao;

    /**
     * DAO for managing class member-related database operations.
     */
    private final ClassMemberDao classMemberDao;

    /**
     * DAO for managing study-related database operations.
     */
    private final StudyDao studyDao;

    /**
     * Constructor for ProgressViewModel.
     * Initializes the DAO instances using the provided EntityManager.
     *
     * @param entityManager The EntityManager to be used for database operations.
     */
    public ProgressViewModel(EntityManager entityManager) {
        quizDao = QuizDao.getInstance(entityManager);
        flashcardDao = FlashcardDao.getInstance(entityManager);
        classMemberDao = ClassMemberDao.getInstance(entityManager);
        studyDao = StudyDao.getInstance(entityManager);
    }

    /**
     * Retrieves the total number of flashcards in a given set.
     *
     * @param setId The ID of the flashcard set.
     * @return The total number of flashcards in the specified set.
     */
    public int getTotalFlashcards(int setId) {
        return flashcardDao.findBySetId(setId).size();
    }

    /**
     * Retrieves the number of flashcards studied by a user in a given set.
     *
     * @param userId The ID of the user.
     * @param setId  The ID of the flashcard set.
     * @return The number of flashcards studied by the user in the specified set.
     */
    public int getStudiedFlashcards(String userId, int setId) {
        Study study = studyDao.findByUserIdAndSetId(userId, setId);
        return study != null ? study.getNumberStudiedWords() : 0;
    }

    /**
     * Retrieves a list of all quizzes taken by a user in a given set.
     *
     * @param userId The ID of the user.
     * @param setId  The ID of the flashcard set.
     * @return A list of quizzes taken by the user in the specified set.
     */
    public List<Quiz> getAllQuizzesForUserAndSet(String userId, int setId) {
        return quizDao.findByUserIdAndSetId(userId, setId);
    }

    /**
     * Calculates the highest quiz percentage for a user in a given set.
     *
     * @param userId The ID of the user.
     * @param setId  The ID of the flashcard set.
     * @return The highest quiz percentage achieved by the user in the specified set.
     */
    public double calculateHighestQuizPercentage(String userId, int setId) {
        List<Quiz> quizzes = getAllQuizzesForUserAndSet(userId, setId);
        double highestPercentage = 0;

        for (Quiz quiz : quizzes) {
            int totalQuestions = quiz.getCorrectTimes() + quiz.getWrongTimes();
            if (totalQuestions > 0) {
                double percentage = (double) quiz.getCorrectTimes() / totalQuestions * 100;
                if (percentage > highestPercentage) {
                    highestPercentage = percentage;
                }
            }
        }

        return highestPercentage;
    }

    /**
     * Retrieves a list of student progress in a given class and set.
     *
     * @param classId The ID of the class.
     * @param setId   The ID of the flashcard set.
     * @return A list of maps containing student progress data.
     */
    public List<Map<String, Object>> getStudentProgressList(int classId, int setId) {
        List<Map<String, Object>> studentProgressList = new ArrayList<>();
        List<User> students = classMemberDao.findAllStudentByClassId(classId);
        for (User student : students) {
            Map<String, Object> studentProgress = new HashMap<>();
            studentProgress.put("studentName", student.getFirstName());
            int studiedFlashcards = getStudiedFlashcards(student.getUserId(), setId);
            int totalFlashcards = getTotalFlashcards(setId);
            studentProgress.put("flashcardsProgress", studiedFlashcards + "/" + totalFlashcards);
            double highestQuizPercentage = calculateHighestQuizPercentage(student.getUserId(), setId);
            studentProgress.put("highestQuizPercentage", highestQuizPercentage);
            studentProgressList.add(studentProgress);
        }
        return studentProgressList;
    }
}
