package com.flash_card.model.entity;

import jakarta.persistence.*;

enum DifficultyLevel {
    EASY,
    HARD
}

public class Flashcard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "card_id")
    private int cardId;

    @Column(name = "term")
    private String term;

    @Column(name = "definition")
    private String definition;

    @Enumerated(EnumType.STRING)
    @Column(name = "difficult_level")
    private DifficultyLevel difficultLevel;

    @ManyToOne(optional = false)
    @JoinColumn(name = "set_id", referencedColumnName = "set_id")
    private FlashcardSet flashcardSet;

    @ManyToOne(optional = false)
    @JoinColumn(name = "creator_id", referencedColumnName = "user_id")
    private User flashcardCreator;


    public Flashcard(String term, String definition, DifficultyLevel difficultLevel, FlashcardSet flashcardSet, User flashcardCreator) {
        super();
        this.term = term;
        this.definition = definition;
        this.difficultLevel = difficultLevel;
        this.flashcardSet = flashcardSet;
        this.flashcardCreator = flashcardCreator;
    }

    public Flashcard() {}

    public int getCardId() {
        return cardId;
    }

    public String getTerm() {
        return term;
    }

    public String getDefinition() {
        return definition;
    }

    public DifficultyLevel getDifficultLevel() {
        return difficultLevel;
    }

    public FlashcardSet getFlashcardSet() {
        return flashcardSet;
    }

    public User getFlashcardCreator() {
        return flashcardCreator;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public void setDifficultLevel(DifficultyLevel difficultLevel) {
        this.difficultLevel = difficultLevel;
    }

    public void setFlashcardSet(FlashcardSet flashcardSet) {
        this.flashcardSet = flashcardSet;
    }

    public void setFlashcardCreator(User flashcardCreator) {
        this.flashcardCreator = flashcardCreator;
    }
}
