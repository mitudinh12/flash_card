package com.flash_card.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public final class User {
    @Id
    @Column(name = "user_id")
    private String userId;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "id_token")
    private String idToken;

    public User(final String userIdParam, String firstNameParam, String lastNameParam, String emailParam, String idTokenParam) {
        super();
        this.userId = userIdParam;
        this.firstName = firstNameParam;
        this.lastName = lastNameParam;
        this.email = emailParam;
        this.idToken = idTokenParam;
    }

    public User() {
    }

    public String getUserId() {
        return userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }


}

