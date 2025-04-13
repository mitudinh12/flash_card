package com.flash_card.model.entity;

import com.flash_card.framework.DifficultyLevel;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
/**
 * Entity class representing the difficulty level of a flashcard in a study session.
 * Maps to the "card_difficult_levels" table in the database.
 */
@Entity
@Table(name = "card_difficult_levels")
public class CardDifficultLevel {
    /**
     * Unique identifier for the difficulty level entry.
     * Maps to the "level_id" column in the database.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "level_id")
    private int id;
    /**
     * The flashcard associated with this difficulty level.
     * Maps to the "card_id" column in the database.
     */
    @ManyToOne
    @JoinColumn(name = "card_id", referencedColumnName = "card_id", nullable = false)
    private Flashcard flashcard;

    /**
     * The study session associated with this difficulty level.
     * Maps to the "study_id" column in the database.
     */
    @ManyToOne
    @JoinColumn(name = "study_id", referencedColumnName = "study_id", nullable = false)
    private Study study;
    /**
     * The difficulty level of the flashcard.
     * Maps to the "difficult_level" column in the database.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "difficult_level")
    private DifficultyLevel difficultLevel;
    /**
     * Default constructor for the CardDifficultLevel entity.
     */
    public CardDifficultLevel() {
    }
    /**
     * Constructs a CardDifficultLevel with the specified flashcard, study session, and difficulty level.
     *
     * @param flashcardParam      the flashcard associated with this difficulty level
     * @param studyParam          the study session associated with this difficulty level
     * @param difficultLevelParam the difficulty level of the flashcard
     */
    public CardDifficultLevel(final Flashcard flashcardParam,
                              final Study studyParam,
                              final DifficultyLevel difficultLevelParam) {
        super();
        this.flashcard = flashcardParam;
        this.study = studyParam;
        this.difficultLevel = difficultLevelParam;
    }
    /**
     * Returns the unique identifier of the difficulty level entry.
     *
     * @return the ID of the difficulty level entry
     */
    public int getId() {
        return id;
    }
    /**
     * Returns the flashcard associated with this difficulty level.
     *
     * @return the flashcard
     */
    public Flashcard getFlashcard() {
        return flashcard;
    }
    /**
     * Returns the study session associated with this difficulty level.
     *
     * @return the study session
     */
    public Study getStudy() {
        return study;
    }
    /**
     * Returns the difficulty level of the flashcard.
     *
     * @return the difficulty level
     */
    public DifficultyLevel getDifficultLevel() {
        return difficultLevel;
    }
    /**
     * Sets the difficulty level of the flashcard.
     *
     * @param difficultLevelParam the difficulty level to set
     */
    public void setDifficultLevel(final  DifficultyLevel difficultLevelParam) {
        this.difficultLevel = difficultLevelParam;
    }

}
