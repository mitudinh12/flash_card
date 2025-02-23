package com.flash_card.view_model.flashcard_set;

import com.flash_card.model.dao.QuizDao;
import com.flash_card.model.entity.Quiz;
import jakarta.persistence.EntityManager;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.time.Duration;


public class QuizResultViewModel {
    private QuizDao quizDao;
    private int quizId;
    private Quiz quiz;
    private StringProperty quizTime = new SimpleStringProperty();

    public QuizResultViewModel(EntityManager entityManager, int quizId) {
        quizDao = QuizDao.getInstance(entityManager);
        this.quizId = quizId;
        this.quiz = quizDao.findById(quizId);
        calculateTime();
    }

    public int getTotalCorrect() {
        return quiz.getCorrectTimes();
    }

    public int getTotalWrong() {
        return quiz.getWrongTimes();
    }

    public String getSetName() {
        return quiz.getFlashcardSet().getSetName();
    }

    public int getFlashcardSetId() {
        return quiz.getFlashcardSet().getSetId();
    }

    public void calculateTime() {
        Duration duration = Duration.between(quiz.getStartTime(), quiz.getEndTime());
        long seconds = duration.getSeconds();
        long minutes = seconds / 60;
        seconds = seconds % 60;
        System.out.println("Quiz time: " + minutes + "m " + seconds + "s");
        quizTime.set("Quiz time: " + minutes + "m " + seconds + "s");
    }

    public StringProperty quizTimeProperty() {
        return quizTime;
    }
}
