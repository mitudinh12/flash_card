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
 * Entity class representing a flashcard in the application.
 * Maps to the "flashcards" table in the database.
 */
@Entity
@Table(name = "flashcards")
public class Flashcard {
    /**
     * Unique identifier for the flashcard.
     * Maps to the "card_id" column in the database.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "card_id")
    private int cardId;
    /**
     * The term of the flashcard.
     * Maps to the "term" column in the database.
     */
    @Column(name = "term")
    private String term;
    /**
     * The definition of the flashcard.
     * Maps to the "definition" column in the database.
     */
    @Column(name = "definition")
    private String definition;
    /**
     * The flashcard set associated with this flashcard.
     * Maps to the "set_id" column in the database.
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "set_id", referencedColumnName = "set_id")
    private FlashcardSet flashcardSet;
    /**
     * The creator of the flashcard.
     * Maps to the "creator_id" column in the database.
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "creator_id", referencedColumnName = "user_id")
    private User flashcardCreator;

    /**
     * Constructs a Flashcard with the specified term, definition, flashcard set, and creator.
     *
     * @param termParam          the term of the flashcard
     * @param definitionParam    the definition of the flashcard
     * @param flashcardSetParam  the flashcard set associated with this flashcard
     * @param flashcardCreatorParam the creator of the flashcard
     */
    public Flashcard(String termParam, String definitionParam, final FlashcardSet flashcardSetParam, final User flashcardCreatorParam) {
        super();
        this.term = termParam;
        this.definition = definitionParam;
        this.flashcardSet = flashcardSetParam;
        this.flashcardCreator = flashcardCreatorParam;
    }
    /**
     * Default constructor for the Flashcard entity.
     */
    public Flashcard() {
    }
    /**
     * Returns the unique identifier of the flashcard.
     *
     * @return the ID of the flashcard
     */
    public int getCardId() {
        return cardId;
    }
    /**
     * Returns the term of the flashcard.
     *
     * @return the term of the flashcard
     */
    public String getTerm() {
        return term;
    }
    /**
     * Sets the term of the flashcard.
     *
     * @param termParam the term to set
     */
    public void setTerm(String termParam) {
        this.term = termParam;
    }
    /**
     * Returns the definition of the flashcard.
     *
     * @return the definition of the flashcard
     */
    public String getDefinition() {
        return definition;
    }
    /**
     * Sets the definition of the flashcard.
     *
     * @param definitionParam the definition to set
     */
    public void setDefinition(String definitionParam) {
        this.definition = definitionParam;
    }
    /**
     * Returns the flashcard set associated with this flashcard.
     *
     * @return the flashcard set
     */
    public FlashcardSet getFlashcardSet() {
        return flashcardSet;
    }
    /**
     * Sets the flashcard set associated with this flashcard.
     *
     * @param flashcardSetParam the flashcard set to set
     */
    public void setFlashcardSet(FlashcardSet flashcardSetParam) {
        this.flashcardSet = flashcardSetParam;
    }
    /**
     * Returns the creator of the flashcard.
     *
     * @return the creator of the flashcard
     */
    public User getFlashcardCreator() {
        return flashcardCreator;
    }
    /**
     * Sets the creator of the flashcard.
     *
     * @param flashcardCreatorParam the creator to set
     */
    public void setFlashcardCreator(User flashcardCreatorParam) {
        this.flashcardCreator = flashcardCreatorParam;
    }
}
