package com.flash_card.view.flashcardSet;

import com.flash_card.view_model.flashcard_set.StudyFlashcardSetViewModel;

/**
 * Singleton class representing a study session.
 * Manages the current flashcard set being studied and its associated data.
 */
public class StudySession {

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
     * @param setId the ID of the flashcard set
     */
    public void setSetId(int setId) {
        this.setId = setId;
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
     * @param setName the name of the flashcard set
     */
    public void setSetName(String setName) {
        this.setName = setName;
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
     * @param viewModel the ViewModel for the flashcard set
     */
    public void setViewModel(StudyFlashcardSetViewModel viewModel) {
        this.viewModel = viewModel;
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
