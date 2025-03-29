package com.flash_card.view.flashcardSet;

import com.flash_card.view_model.flashcard_set.QuizResultViewModel;

public class QuizSession {
    private static QuizSession instance;
    private int setId;
    private String setName;
    private int quizId;

    private QuizSession() {}

    public static QuizSession getInstance() {
        if (instance == null) {
            instance = new QuizSession();
        }
        return instance;
    }

    public int getSetId() {
        return setId;
    }

    public void setSetId(int setId) {
        this.setId = setId;
    }

    public String getSetName() {
        return setName;
    }

    public void setSetName(String setName) {
        this.setName = setName;
    }

    public int getQuizId() {
        return quizId;
    }

    public void setQuizId(int quizId) {
        this.quizId = quizId;
    }

    public void clear() {
        setId = 0;
        setName = null;
        quizId = 0;
    }
}
