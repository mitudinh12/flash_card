package com.flash_card.model.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "shared_sets")
public class SharedSet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sharing_id")
    private int sharingId;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    private User user;  //

    @ManyToOne
    @JoinColumn(name = "set_id", referencedColumnName = "set_id", nullable = false)
    private FlashcardSet flashcardSet;

    public SharedSet(User user, FlashcardSet flashcardSet) {
        this.user = user;
        this.flashcardSet = flashcardSet;
    }

    public SharedSet() {}

    public User getUser() {
        return user;
    }

    public FlashcardSet getFlashcardSet() {
        return flashcardSet;
    }

    public int getSharingId() {
        return sharingId;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setFlashcardSet(FlashcardSet flashcardSet) {
        this.flashcardSet = flashcardSet;
    }
}
