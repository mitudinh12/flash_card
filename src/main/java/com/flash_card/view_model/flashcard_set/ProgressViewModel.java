package com.flash_card.view_model.flashcard_set;

import com.flash_card.framework.DifficultyLevel;
import com.flash_card.model.dao.FlashcardDao;
import com.flash_card.model.dao.QuizDao;
import com.flash_card.model.entity.Quiz;
import jakarta.persistence.EntityManager;

import java.util.List;

public class ProgressViewModel {
    private final QuizDao quizDao;
    private final FlashcardDao flashcardDao;

    public ProgressViewModel(EntityManager entityManager) {
        quizDao = QuizDao.getInstance(entityManager);
        flashcardDao = FlashcardDao.getInstance(entityManager);
    }

    public int getTotalFlashcards(int setId) {
        return flashcardDao.findBySetId(setId).size();
    }

    public int getStudiedFlashcards(int setId) {
        return (int) flashcardDao.findBySetId(setId).stream()
                .filter(flashcard -> flashcard.getDifficultLevel() == DifficultyLevel.easy)
                .count();
    }

    public List<Quiz> getAllQuizzesForUserAndSet(String userId,int setId) {
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
}
