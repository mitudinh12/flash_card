package com.flash_card.view_model.user;

import com.flash_card.model.entity.FlashcardSet;
import com.flash_card.view_model.flashcard_set.FlashcardSetViewModel;
import com.flash_card.view_model.flashcard_set.OwnFlashcardSetViewModel;
import com.flash_card.framework.SetViewModel;
import com.flash_card.view_model.flashcard_set.SharedFlashcardSetViewModel;
import jakarta.persistence.EntityManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

/**
 * ViewModel for managing flashcard sets on the user's homepage.
 * Combines both owned and shared flashcard sets for display and operations.
 */
public class HomepageViewModel {

    /**
     * List of flashcard sets owned by the user.
     */
    private final ObservableList<OwnFlashcardSetViewModel> ownFlashcardList = FXCollections.observableArrayList();

    /**
     * List of flashcard sets shared with the user.
     */
    private final ObservableList<SharedFlashcardSetViewModel> sharedFlashcardList = FXCollections.observableArrayList();

    /**
     * Combined list of all flashcard sets for display (both own and shared).
     */
    private final ObservableList<SetViewModel> flashcardList = FXCollections.observableArrayList();

    /**
     * ViewModel handling logic for individual flashcard set operations.
     */
    private FlashcardSetViewModel flashcardSetViewModel;

    /**
     * The ID of the currently logged-in user.
     */
    private String userId;

    /**
     * Constructs a new {@code HomepageViewModel} with the given user ID and entity manager.
     *
     * @param currentUserId         the ID of the logged-in user
     * @param entityManager  the {@link EntityManager} used for data access
     */
    public HomepageViewModel(final String currentUserId, final EntityManager entityManager) {
        this.userId = currentUserId;
        flashcardSetViewModel = new FlashcardSetViewModel(userId, entityManager);
    }

    /**
     * Loads flashcard sets owned by or shared with the current user and
     * populates the corresponding observable lists.
     *
     * @param currentUserId the user ID used to filter flashcard sets
     */
    public void loadFlashcards(final String currentUserId) {
        List<FlashcardSet> ownFlashcardSets = flashcardSetViewModel.findOwnSets();
        List<FlashcardSet> sharedFlashcardSets = flashcardSetViewModel.findSharedSets();

        sharedFlashcardList.clear();
        ownFlashcardList.clear();

        ownFlashcardSets.stream()
                .map(OwnFlashcardSetViewModel::new)
                .forEach(ownFlashcardList::add);

        sharedFlashcardSets.stream()
                .map(SharedFlashcardSetViewModel::new)
                .forEach(sharedFlashcardList::add);

        flashcardList.addAll(ownFlashcardList);
        flashcardList.addAll(sharedFlashcardList);
    }

    /**
     * Returns the combined list of all flashcard sets.
     *
     * @return the observable list of all flashcard sets
     */
    public ObservableList<SetViewModel> getFlashcardList() {
        return flashcardList;
    }

    /**
     * Returns the list of flashcard sets owned by the user.
     *
     * @return the observable list of owned flashcard sets
     */
    public ObservableList<OwnFlashcardSetViewModel> getOwnFlashcardList() {
        return ownFlashcardList;
    }

    /**
     * Returns the list of flashcard sets shared with the user.
     *
     * @return the observable list of shared flashcard sets
     */
    public ObservableList<SharedFlashcardSetViewModel> getSharedFlashcardList() {
        return sharedFlashcardList;
    }

    /**
     * Deletes the given flashcard set based on its type (own or shared)
     * and removes it from the corresponding observable lists.
     *
     * @param viewModel the view model representing the flashcard set to delete
     */
    public void deleteFlashcardSet(final SetViewModel viewModel) {
        if (viewModel == null) {
            return;
        }

        if ("own".equals(viewModel.getType())) {
            flashcardSetViewModel.deleteOwnFlashcardSet(viewModel.getSet());
            ownFlashcardList.remove(viewModel);
        } else {
            flashcardSetViewModel.deleteSharedFlashcardSet(userId, viewModel.getSet().getSetId());
            sharedFlashcardList.remove(viewModel);
        }

        flashcardList.remove(viewModel);
    }
}
