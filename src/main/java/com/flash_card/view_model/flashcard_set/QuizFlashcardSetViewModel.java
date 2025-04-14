package com.flash_card.view_model.flashcard_set;

import com.flash_card.localization.Localization;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * ViewModel for managing quizzes for a flashcard set.
 * Handles the logic for starting, stopping, and progressing through a quiz.
 */
public class QuizFlashcardSetViewModel {
    /**
     * DAO for managing user-related database operations.
     */
    private final UserDao userDao;

    /**
     * DAO for managing quiz-related database operations.
     */
    private final QuizDao quizDao;

    /**
     * DAO for managing flashcard set-related database operations.
     */
    private final FlashcardSetDao flashcardSetDao;

    /**
     * DAO for managing flashcard-related database operations.
     */
    private final FlashcardDao flashcardDao;

    /**
     * The ID of the flashcard set being quizzed.
     */
    private int setId;

    /**
     * The list of flashcards in the current flashcard set.
     */
    private List<Flashcard> flashcards;

    /**
     * The current quiz instance.
     */
    private Quiz currentQuiz;

    /**
     * Indicates whether the last answer was correct.
     */
    private boolean correctAnswer;

    /**
     * The number of correct answers in the quiz.
     */
    private int correctTimes;

    /**
     * The number of wrong answers in the quiz.
     */
    private int wrongTimes;

    /**
     * The ID of the current quiz.
     */
    private int quizId;

    /**
     * Property for the current index of the flashcard in the quiz.
     */
    private final IntegerProperty currentIndex = new SimpleIntegerProperty(0);

    /**
     * Property for the name of the flashcard set.
     */
    private final StringProperty setName = new SimpleStringProperty();

    /**
     * Property for the total number of flashcards in the set.
     */
    private final StringProperty total = new SimpleStringProperty();

    /**
     * Property for the quiz time.
     */
    private final StringProperty quizTime = new SimpleStringProperty();

    /**
     * Property for the current term being quizzed.
     */
    private final StringProperty currentTerm = new SimpleStringProperty();

    /**
     * Property for the instruction text displayed during the quiz.
     */
    private final StringProperty instructionText = new SimpleStringProperty();

    /**
     * Property for the first answer option.
     */
    private final StringProperty answer1 = new SimpleStringProperty();

    /**
     * Property for the second answer option.
     */
    private final StringProperty answer2 = new SimpleStringProperty();

    /**
     * Property for the third answer option.
     */
    private final StringProperty answer3 = new SimpleStringProperty();

    /**
     * Property for the fourth answer option.
     */
    private final StringProperty answer4 = new SimpleStringProperty();

    /**
     * Localization instance for retrieving localized messages.
     */
    private final Localization localization = Localization.getInstance();

    /**
     * The number of wrong answers allowed before the quiz ends.
     */
    private static final int NUMBER_OF_WRONG_ANSWERS = 3;
    /**
     * Constructs a QuizFlashcardSetViewModel with the specified EntityManager.
     *
     * @param entityManager the EntityManager for database operations
     */
    public QuizFlashcardSetViewModel(final EntityManager entityManager) {
        userDao = UserDao.getInstance(entityManager);
        quizDao = QuizDao.getInstance(entityManager);
        flashcardSetDao = FlashcardSetDao.getInstance(entityManager);
        flashcardDao = FlashcardDao.getInstance(entityManager);
    }

    /**
     * Loads the flashcards for the specified set ID and name.
     *
     * @param setIdParam   the ID of the flashcard set
     * @param setNameParam the name of the flashcard set
     */
    public void loadFlashcards(final int setIdParam, final String setNameParam) {
        this.setId = setIdParam;
        this.setName.set(setNameParam);
        this.flashcards = flashcardDao.findBySetId(setId);
        this.total.set(String.valueOf(flashcards.size()));
    }

    /**
     * Starts a quiz for the specified user and flashcard set.
     *
     * @param userId the ID of the user taking the quiz
     * @param setIdParam  the ID of the flashcard set
     */
    public void startQuiz(final String userId, final int setIdParam) {
        User user = userDao.findById(userId);
        FlashcardSet flashcardSet = flashcardSetDao.findById(setIdParam);
        currentQuiz = new Quiz(user, flashcardSet, LocalDateTime.now(), null, 0, 0);
        quizDao.persist(currentQuiz);
    }

    /**
     * Completes the quiz and updates the end time.
     */
    public void finishQuiz() {
        currentQuiz.setEndTime(LocalDateTime.now());
        quizDao.update(currentQuiz);
        correctTimes = currentQuiz.getCorrectTimes();
        wrongTimes = currentQuiz.getWrongTimes();
        quizId = currentQuiz.getQuizId();
    }

    /**
     * Stops or cancels the quiz in progress.
     */
    public void stopQuiz() {
        quizDao.delete(currentQuiz);
    }

    /**
     * Increments the count of correct answers in the quiz.
     */
    public void addCorrectTimes() {
        currentQuiz.setCorrectTimes(currentQuiz.getCorrectTimes() + 1);
        quizDao.update(currentQuiz);
    }

    /**
     * Increments the count of wrong answers in the quiz.
     */
    public void addWrongTimes() {
        currentQuiz.setWrongTimes(currentQuiz.getWrongTimes() + 1);
        quizDao.update(currentQuiz);
    }

    /**
     * Checks if the provided answer is correct for the current flashcard.
     *
     * @param answer the answer to check
     * @return true if the answer is correct, false otherwise
     */
    public boolean isAnswerCorrect(final String answer) {
        correctAnswer = answer.equals(getCurrentFlashcard().getDefinition());
        if (correctAnswer) {
            addCorrectTimes();
            instructionText.set(localization.getMessage("flashcardSet.correct") + "!");
        } else {
            addWrongTimes();
            instructionText.set(localization.getMessage("flashcardSet.wrong") + "!");
        }
        return correctAnswer;
    }

    /**
     * Loads the question and answer options for the current flashcard.
     */
    public void loadQuestion() {
        currentTerm.set(getCurrentFlashcard().getTerm());
        instructionText.set(localization.getMessage("flashcardSet.quizInstruction"));

        List<String> answers = new ArrayList<>();
        answers.add(getCurrentFlashcard().getDefinition());

        List<Flashcard> newFlashcards = flashcards.stream()
                .filter(flashcard -> !flashcard.getTerm().equals(getCurrentFlashcard().getTerm()))
                .collect(Collectors.toList());

        Collections.shuffle(newFlashcards);

        newFlashcards.stream()
                .limit(NUMBER_OF_WRONG_ANSWERS)
                .map(Flashcard::getDefinition)
                .forEach(answers::add);

        // Shuffle answers and assign them
        Collections.shuffle(answers);
        answer1.set(answers.get(0));
        answer2.set(answers.get(1));
        answer3.set(answers.get(2));
        answer4.set(answers.get(NUMBER_OF_WRONG_ANSWERS));
    }

    /**
     * Checks if the current flashcard is the last one in the set.
     *
     * @return true if it is the last flashcard, false otherwise
     */
    public boolean isLastFlashcard() {
        return currentIndex.get() == flashcards.size() - 1;
    }

    /**
     * Gets the current flashcard being quizzed.
     *
     * @return the current flashcard
     */
    public Flashcard getCurrentFlashcard() {
        return flashcards.get(currentIndex.get());
    }

    /**
     * Moves to the next flashcard in the quiz.
     */
    public void nextFlashcard() {
        if (currentIndex.get() < flashcards.size() - 1) {
            currentIndex.set(currentIndex.get() + 1);
            loadQuestion();
        }
    }

    /**
     * Gets the ID of the current quiz.
     *
     * @return the quiz ID
     */
    public int getQuizId() {
        return quizId;
    }

    /**
     * Gets the list of flashcards in the current flashcard set.
     *
     * @return the list of flashcards
     */
    public List<Flashcard> getFlashcards() {
        return flashcards;
    }

    //Bind properties
    /**
     * Gets the current index property.
     *
     * @return the current index property
     */
    public IntegerProperty currentIndexProperty() {
        return currentIndex;
    }

    /**
     * Gets the set name property.
     *
     * @return the set name property
     */
    public StringProperty setNameProperty() {
        return setName;
    }

    /**
     * Gets the total property, representing the total number of flashcards in the set.
     *
     * @return the total property
     */
    public StringProperty totalProperty() {
        return total;
    }

    /**
     * Gets the current term property, representing the term currently being quizzed.
     *
     * @return the current term property
     */
    public StringProperty currentTermProperty() {
        return currentTerm;
    }

    /**
     * Gets the instruction text property, representing the instructions displayed during the quiz.
     *
     * @return the instruction text property
     */
    public StringProperty instructionTextProperty() {
        return instructionText;
    }

    /**
     * Gets the first answer option property.
     *
     * @return the first answer option property
     */
    public StringProperty answer1Property() {
        return answer1;
    }

    /**
     * Gets the second answer option property.
     *
     * @return the second answer option property
     */
    public StringProperty answer2Property() {
        return answer2;
    }

    /**
     * Gets the third answer option property.
     *
     * @return the third answer option property
     */
    public StringProperty answer3Property() {
        return answer3;
    }

    /**
     * Gets the fourth answer option property.
     *
     * @return the fourth answer option property
     */
    public StringProperty answer4Property() {
        return answer4;
    }
}
