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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.flash_card.framework.TriviaQuestionGenerator;

public class QuizFlashcardSetViewModel {
    private UserDao userDao;
    private QuizDao quizDao;
    private FlashcardSetDao flashcardSetDao;
    private FlashcardDao flashcardDao;
    private int setId;
    private List<Flashcard> flashcards;
    private Quiz currentQuiz;
    private boolean correctAnswer;
    private TriviaQuestionGenerator triviaQuestionGenerator;
    private int correctTimes;
    private int wrongTimes;
    private int quizId;

    private final IntegerProperty currentIndex = new SimpleIntegerProperty(0);
    private final StringProperty setName = new SimpleStringProperty();
    private final StringProperty total = new SimpleStringProperty();
    private final StringProperty quizTime = new SimpleStringProperty();
    private final StringProperty currentTerm = new SimpleStringProperty();
    private final StringProperty instructionText = new SimpleStringProperty();
    private StringProperty answer1 = new SimpleStringProperty();
    private StringProperty answer2 = new SimpleStringProperty();
    private StringProperty answer3 = new SimpleStringProperty();
    private StringProperty answer4 = new SimpleStringProperty();

    public QuizFlashcardSetViewModel(EntityManager entityManager) {
        userDao = UserDao.getInstance(entityManager);
        quizDao = QuizDao.getInstance(entityManager);
        flashcardSetDao = FlashcardSetDao.getInstance(entityManager);
        flashcardDao = FlashcardDao.getInstance(entityManager);
        triviaQuestionGenerator = TriviaQuestionGenerator.getInstance();
    }
    //Determine the Flashcard Set, load its name and flashcards
    public void loadFlashcards(int setId, String setName) {
        this.setId = setId;
        this.setName.set(setName);
        flashcards = flashcardDao.findBySetId(setId);
        this.total.set(String.valueOf(flashcards.size()));
    }
    //Start the quiz and the clock
    public void startQuiz(String userId, int setId) {
        User user = userDao.findById(userId);
        FlashcardSet flashcardSet = flashcardSetDao.findById(setId);
        currentQuiz = new Quiz(user, flashcardSet, LocalDateTime.now(), null, 0,0);
        quizDao.persist(currentQuiz);

    }
    //Complete the quiz and update the clock
    public void finishQuiz() {
        currentQuiz.setEndTime(LocalDateTime.now());
        quizDao.update(currentQuiz);
        correctTimes = currentQuiz.getCorrectTimes();
        wrongTimes = currentQuiz.getWrongTimes();
        quizId = currentQuiz.getQuizId();
    }
    //Stop or cancel the quiz in the middle of progress
    public void stopQuiz() {
        quizDao.delete(currentQuiz);
    }

    public void addCorrectTimes() {
        currentQuiz.setCorrectTimes(currentQuiz.getCorrectTimes() + 1);
    }

    public void addWrongTimes() {
        currentQuiz.setWrongTimes(currentQuiz.getWrongTimes() + 1);
    }

    //check correct answer
    public boolean isAnswerCorrect(String answer) {
        correctAnswer = answer.equals(getCurrentFlashcard().getDefinition());
        if (correctAnswer) {
            addCorrectTimes();
            instructionText.set("Correct!");
        } else {
            addWrongTimes();
            instructionText.set("Wrong!");
        }
        return correctAnswer;
    }
    //Load the term, definition, and fake answers of the current flashcard(index)
    public void loadQuestion() {
        currentTerm.set(getCurrentFlashcard().getTerm());
        instructionText.set("Choose the correct definition");

        List<String> answers = new ArrayList<>();
        answers.add(getCurrentFlashcard().getDefinition());
        answers.addAll(triviaQuestionGenerator.getFakeAnswers());

        Collections.shuffle(answers);
        answer1.set(answers.get(0));
        answer2.set(answers.get(1));
        answer3.set(answers.get(2));
        answer4.set(answers.get(3));
        triviaQuestionGenerator.reloadAnswer(flashcardSetDao.findById(setId).getSetTopic());
    }


    //check last flashcard
    public boolean isLastFlashcard() {
        return currentIndex.get() == flashcards.size() - 1;
    }

    public Flashcard getCurrentFlashcard() {
        return flashcards.get(currentIndex.get());
    }

    //Move to the next flashcard in the set
    public void nextFlashcard() {
        if (currentIndex.get() < flashcards.size() - 1) {
            currentIndex.set(currentIndex.get() + 1);
            loadQuestion();
        }
    }
    public String getQuizTopic(int setId) {
        FlashcardSet set = flashcardSetDao.findById(setId);
        return set.getSetTopic();
    }

    public int getQuizId() {
        return quizId;
    }

    //Bind properties

    public IntegerProperty currentIndexProperty() {
        return currentIndex;
    }

    public StringProperty setNameProperty() {
        return setName;
    }

    public StringProperty totalProperty() {
        return total;
    }

    public StringProperty currentTermProperty() {
        return currentTerm;
    }

    public StringProperty instructionTextProperty() {
        return instructionText;
    }

    public StringProperty answer1Property() {
        return answer1;
    }
    public StringProperty answer2Property() {
        return answer2;
    }
    public StringProperty answer3Property() {
        return answer3;
    }
    public StringProperty answer4Property() {
        return answer4;
    }

}
