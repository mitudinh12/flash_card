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

public class ProgressViewModel {
    private final QuizDao quizDao;
    private final FlashcardDao flashcardDao;
    private final ClassMemberDao classMemberDao;
    private final StudyDao studyDao;

    public ProgressViewModel(EntityManager entityManager) {
        quizDao = QuizDao.getInstance(entityManager);
        flashcardDao = FlashcardDao.getInstance(entityManager);
        classMemberDao = ClassMemberDao.getInstance(entityManager);
        studyDao = StudyDao.getInstance(entityManager);
    }

    public int getTotalFlashcards(int setId) {
        return flashcardDao.findBySetId(setId).size();
    }

    public int getStudiedFlashcards(String userId, int setId) {
        Study study = studyDao.findByUserIdAndSetId(userId, setId);
        return study != null ? study.getNumberStudiedWords() : 0;
    }

    public List<Quiz> getAllQuizzesForUserAndSet(String userId, int setId) {
        return quizDao.findByUserIdAndSetId(userId, setId);
    }

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
