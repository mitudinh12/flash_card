package com.flash_card.model.entity;

import com.flash_card.framework.DifficultyLevel;
import jakarta.persistence.*;

@Entity
@Table(name = "card_difficult_levels")
public class CardDifficultLevel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "level_id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "card_id", referencedColumnName = "card_id", nullable = false)
    private Flashcard flashcard;

    @ManyToOne
    @JoinColumn(name = "study_id", referencedColumnName = "study_id", nullable = false)
    private Study study;

    @Enumerated(EnumType.STRING)
    @Column(name = "difficult_level")
    private DifficultyLevel difficultLevel;

    public CardDifficultLevel() {}

    public CardDifficultLevel(Flashcard flashcard, Study study, DifficultyLevel difficultLevel) {
        super();
        this.flashcard = flashcard;
        this.study = study;
        this.difficultLevel = difficultLevel;
    }

    public int getId() {
        return id;
    }

    public Flashcard getFlashcard() {
        return flashcard;
    }

    public Study getStudy() {
        return study;
    }

    public DifficultyLevel getDifficultLevel() {
        return difficultLevel;
    }

    public void setDifficultLevel(DifficultyLevel difficultLevel) {
        this.difficultLevel = difficultLevel;
    }

}
