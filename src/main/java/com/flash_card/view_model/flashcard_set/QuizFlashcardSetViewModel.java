package com.flash_card.view_model.flashcard_set;

import com.flash_card.model.dao.FlashcardDao;
import com.flash_card.model.dao.FlashcardSetDao;
import com.flash_card.model.dao.QuizDao;
import com.flash_card.model.dao.UserDao;
import com.flash_card.model.entity.Flashcard;
import com.flash_card.model.entity.FlashcardSet;
import com.flash_card.model.entity.Quiz;
import com.flash_card.model.entity.User;
import jakarta.persistence.EntityManager;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class QuizFlashcardSetViewModel {
    private UserDao userDao;
    private QuizDao quizDao;
    private FlashcardSetDao flashcardSetDao;
    private FlashcardDao flashcardDao;
    private int setId;
    private List<Flashcard> flashcards;
    private Quiz currentQuiz;
    private boolean stopQuiz = true;
    private boolean correctAnswer;

    private final IntegerProperty currentIndex = new SimpleIntegerProperty(0);
    private final StringProperty setName = new SimpleStringProperty();
    private final StringProperty total = new SimpleStringProperty();
    private final StringProperty quizTime = new SimpleStringProperty();

    public QuizFlashcardSetViewModel(EntityManager entityManager) {
        userDao = UserDao.getInstance(entityManager);
        quizDao = QuizDao.getInstance(entityManager);
        flashcardSetDao = FlashcardSetDao.getInstance(entityManager);
        flashcardDao = FlashcardDao.getInstance(entityManager);
    }

    public void loadFlashcards(int setId, String setName) {
        this.setId = setId;
        this.setName.set(setName);
        flashcards = flashcardDao.findBySetId(setId);
        this.total.set(String.valueOf(flashcards.size()));
    }

    public void startQuiz(String userId, int setId) {
        User user = userDao.findById(userId);
        FlashcardSet flashcardSet = flashcardSetDao.findById(setId);
        currentQuiz = new Quiz(user, flashcardSet, LocalDateTime.now(), null, 0,0);
        quizDao.persist(currentQuiz);
        stopQuiz = false;
    }

    public void endQuiz(int correctAnswers, int wrongAnswers) {
        currentQuiz.setEndTime(LocalDateTime.now());
        currentQuiz.setCorrectTimes(correctAnswers);
        currentQuiz.setWrongTimes(wrongAnswers);
        quizDao.update(currentQuiz);
        stopQuiz = true;
    }

    public void stopQuiz() {
        stopQuiz = true;
        quizDao.delete(currentQuiz);
    }

    public void addCorrectTimes() {
        currentQuiz.setCorrectTimes(currentQuiz.getCorrectTimes() + 1);
    }

    public void addWrongTimes() {
        currentQuiz.setWrongTimes(currentQuiz.getWrongTimes() + 1);
    }

    public void stopQuizTimer() {
        LocalDateTime endTime = LocalDateTime.now();
        Duration duration = Duration.between(currentQuiz.getStartTime(), endTime);
        long seconds = duration.getSeconds();
        long minutes = seconds / 60;
        seconds = seconds % 60;
        quizTime.set("Quiz time: " + minutes + "m " + seconds + "s");
    }

    public boolean isAnswerCorrect(String answer) {
        correctAnswer = answer.equals(flashcards.get(currentIndex.get()).getDefinition());
        return correctAnswer;
    }

    //TODO: These methods can be implemented in a parent class
    public int getTotalFlashcards() { return flashcardDao.findBySetId(setId).size(); }

    public Flashcard getCurrentFlashcard() {
        return flashcards.get(currentIndex.get());
    }

    public void nextFlashcard() {
        if (currentIndex.get() < flashcards.size() - 1) {
            currentIndex.set(currentIndex.get() + 1);
        }
    }
    public void previousFlashcard() {
        if (currentIndex.get() > 0) {
            currentIndex.set(currentIndex.get() - 1);
        }
    }

    public IntegerProperty currentIndexProperty() {
        return currentIndex;
    }

    public StringProperty setNameProperty() {
        return setName;
    }

    public StringProperty totalProperty() {
        return total;
    }

    public List<Flashcard> getFlashcards() {
        return flashcards;
    }

}
