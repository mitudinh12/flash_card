package com.flash_card.view.flashcardSet;

import com.flash_card.view_model.flashcard_set.StudyFlashcardSetViewModel;

/**
 * Singleton class representing a study session.
 * Manages the current flashcard set being studied and its associated data.
 */
public final class StudySession {

    /**
     * Singleton instance of the StudySession.
     */
    private static StudySession instance;

    /**
     * ID of the current flashcard set.
     */
    private int setId;

    /**
     * Name of the current flashcard set.
     */
    private String setName;

    /**
     * ViewModel for managing the current flashcard set's data and logic.
     */
    private StudyFlashcardSetViewModel viewModel;

    /**
     * Private constructor to enforce singleton pattern.
     */
    private StudySession() { }

    /**
     * Returns the singleton instance of the StudySession.
     * If the instance does not exist, it is created.
     *
     * @return the singleton instance of StudySession
     */
    public static StudySession getInstance() {
        if (instance == null) {
            instance = new StudySession();
        }
        return instance;
    }

    /**
     * Gets the ID of the current flashcard set.
     *
     * @return the ID of the flashcard set
     */
    public int getSetId() {
        return setId;
    }

    /**
     * Sets the ID of the current flashcard set.
     *
     * @param setIdParam the ID of the flashcard set
     */
    public void setSetId(final int setIdParam) {
        this.setId = setIdParam;
    }

    /**
     * Gets the name of the current flashcard set.
     *
     * @return the name of the flashcard set
     */
    public String getSetName() {
        return setName;
    }

    /**
     * Sets the name of the current flashcard set.
     *
     * @param setNameParam the name of the flashcard set
     */
    public void setSetName(final String setNameParam) {
        this.setName = setNameParam;
    }

    /**
     * Gets the ViewModel for managing the current flashcard set's data and logic.
     *
     * @return the ViewModel for the flashcard set
     */
    public StudyFlashcardSetViewModel getViewModel() {
        return viewModel;
    }

    /**
     * Sets the ViewModel for managing the current flashcard set's data and logic.
     *
     * @param viewModelParam the ViewModel for the flashcard set
     */
    public void setViewModel(final StudyFlashcardSetViewModel viewModelParam) {
        this.viewModel = viewModelParam;
    }

    /**
     * Clears the current study session data, resetting the set ID, name, and ViewModel.
     */
    public void clear() {
        setId = 0;
        setName = null;
        viewModel = null;
    }
}
