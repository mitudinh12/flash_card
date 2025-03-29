package com.flash_card.view.flashcardSet;

import com.flash_card.view_model.flashcard_set.StudyFlashcardSetViewModel;

public class StudySession {
    private static StudySession instance;
    private int setId;
    private String setName;
    private StudyFlashcardSetViewModel viewModel;

    private StudySession() {}

    public static StudySession getInstance() {
        if (instance == null) {
            instance = new StudySession();
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

    public StudyFlashcardSetViewModel getViewModel() {
        return viewModel;
    }

    public void setViewModel(StudyFlashcardSetViewModel viewModel) {
        this.viewModel = viewModel;
    }

    public void clear() {
        setId = 0;
        setName = null;
        viewModel = null;
    }
}