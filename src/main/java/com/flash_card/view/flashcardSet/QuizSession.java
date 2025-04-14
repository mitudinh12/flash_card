package com.flash_card.view.flashcardSet;

/**
 * Singleton class representing the session for a quiz.
 * Stores information about the current quiz, including the set ID, set name, and quiz ID.
 */
public final class QuizSession {

    /**
     * Singleton instance of the QuizSession.
     */
    private static QuizSession instance;

    /**
     * ID of the flashcard set being used in the quiz.
     */
    private int setId;

    /**
     * Name of the flashcard set being used in the quiz.
     */
    private String setName;

    /**
     * ID of the current quiz session.
     */
    private int quizId;

    /**
     * Private constructor to enforce the singleton pattern.
     */
    private QuizSession() { }

    /**
     * Returns the singleton instance of the QuizSession.
     * If the instance does not exist, it is created.
     *
     * @return the singleton instance of QuizSession
     */
    public static QuizSession getInstance() {
        if (instance == null) {
            instance = new QuizSession();
        }
        return instance;
    }

    /**
     * Gets the ID of the flashcard set.
     *
     * @return the flashcard set ID
     */
    public int getSetId() {
        return setId;
    }

    /**
     * Sets the ID of the flashcard set.
     *
     * @param setIdParam the flashcard set ID to set
     */
    public void setSetId(final int setIdParam) {
        this.setId = setIdParam;
    }

    /**
     * Gets the name of the flashcard set.
     *
     * @return the flashcard set name
     */
    public String getSetName() {
        return setName;
    }

    /**
     * Sets the name of the flashcard set.
     *
     * @param setNameParam the flashcard set name to set
     */
    public void setSetName(final String setNameParam) {
        this.setName = setNameParam;
    }

    /**
     * Gets the ID of the current quiz session.
     *
     * @return the quiz ID
     */
    public int getQuizId() {
        return quizId;
    }

    /**
     * Sets the ID of the current quiz session.
     *
     * @param quizIdParam the quiz ID to set
     */
    public void setQuizId(final int quizIdParam) {
        this.quizId = quizIdParam;
    }

    /**
     * Clears the session data by resetting the set ID, set name, and quiz ID.
     */
    public void clear() {
        setId = 0;
        setName = null;
        quizId = 0;
    }
}
