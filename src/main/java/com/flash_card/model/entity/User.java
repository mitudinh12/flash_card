package com.flash_card.model.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class User {
    @Id
    @Column(name="user_id")
    private String userId;

    @Column(name="first_name")
    private String firstName;

    @Column(name="last_name")
    private String lastName;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name="id_token")
    private String idToken;

    // Relationships
    @OneToMany(mappedBy = "flashcardCreator")
    private List<Flashcard> flashcards;

    @OneToMany(mappedBy = "flashcardCreator")
    private List<FlashcardSet> flashcardSets;

    @OneToMany(mappedBy = "user")
    private List<SharedSet> sharedSets = new ArrayList<>();

    @OneToMany(mappedBy = "teacher")
    private List<Classroom> classrooms;

    @OneToOne(mappedBy = "student")
    private ClassMember classMember;

    public User(String userId, String firstName, String lastName, String email, String idToken) {
        super();
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.idToken = idToken;
    }

    public User() {}

    public String getUserId() {return userId;
    }

    public String getFirstName() {return firstName;}

    public String getLastName() {return lastName;}

    public String getEmail() {return email;}


}

