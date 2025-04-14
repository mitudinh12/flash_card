package com.flash_card.view.studentMode;

import com.flash_card.view.flashcardSet.QuizFlashcardSetController;

/**
 * Controller class for managing the quiz flashcard set in student mode.
 * Extends the functionality of `QuizFlashcardSetController` to customize behavior for students.
 */
public class StudentQuizFlashcardSetController extends QuizFlashcardSetController {
    /**
     * Initializes the controller by setting up the FXML file, binding properties,
     * configuring answer button actions, and loading the flashcard set.
     */
    @Override
    public void initialize() {
        setReloadFxml("/com/flash_card/fxml/student-quiz-flashcard.fxml");
        bindProperties();
        setAnswerButtonActions();
        setFlashcardSet(quizSession.getSetId(), quizSession.getSetName());
    }

    /**
     * Navigates to the quiz result page after the quiz is completed.
     * Sets the quiz ID in the session and loads the result page.
     */
    @Override
    protected void goToResultPage() {
        quizSession.setQuizId(quizId);
        goToPage("/com/flash_card/fxml/student-quiz-result.fxml", setName.getScene());
    }
}
