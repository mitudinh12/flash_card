package com.flash_card.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
/**
 * Entity class representing a shared flashcard set in the application.
 * Maps to the "shared_sets" table in the database.
 */
@Entity
@Table(name = "shared_sets")
public class SharedSet {
    /**
     * Unique identifier for the shared set.
     * Maps to the "sharing_id" column in the database.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sharing_id")
    private int sharingId;
    /**
     * The user associated with the shared set.
     * Maps to the "user_id" column in the database.
     */
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    private User user;  //
    /**
     * The flashcard set being shared.
     * Maps to the "set_id" column in the database.
     */
    @ManyToOne
    @JoinColumn(name = "set_id", referencedColumnName = "set_id", nullable = false)
    private FlashcardSet flashcardSet;
    /**
     * Constructs a SharedSet with the specified user and flashcard set.
     *
     * @param userParam         the user associated with the shared set
     * @param flashcardSetParam the flashcard set being shared
     */
    public SharedSet(final User userParam, FlashcardSet flashcardSetParam) {
        this.user = userParam;
        this.flashcardSet = flashcardSetParam;
    }
    /**
     * Default constructor for the SharedSet entity.
     */
    public SharedSet() {
    }
    /**
     * Returns the unique identifier of the shared set.
     *
     * @return the ID of the shared set
     */
    public User getUser() {
        return user;
    }
    /**
     * Sets the user associated with the shared set.
     *
     * @param userParam the user to set
     */
    public void setUser(final User userParam) {
        this.user = userParam;
    }
    /**
     * Returns the flashcard set being shared.
     *
     * @return the flashcard set
     */
    public FlashcardSet getFlashcardSet() {
        return flashcardSet;
    }
    /**
     * Sets the flashcard set being shared.
     *
     * @param flashcardSetParam the flashcard set to set
     */
    public void setFlashcardSet(final FlashcardSet flashcardSetParam) {
        this.flashcardSet = flashcardSetParam;
    }
    /**
     * Returns the sharingId when a user shares a flashcard set.
     *
     * @return the sharing ID
     */
    public int getSharingId() {
        return sharingId;
    }
}
