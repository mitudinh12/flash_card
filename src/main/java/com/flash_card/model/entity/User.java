package com.flash_card.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
/**
 * Entity class representing a user in the application.
 * Maps to the "users" table in the database.
 */
@Entity
@Table(name = "users")
public final class User {
    /**
     * Unique identifier for the user.
     * Maps to the "user_id" column in the database.
     */
    @Id
    @Column(name = "user_id")
    private String userId;
    /**
     * First name of the user.
     * Maps to the "first_name" column in the database.
     */
    @Column(name = "first_name")
    private String firstName;
    /**
     * Last name of the user.
     * Maps to the "last_name" column in the database.
     */
    @Column(name = "last_name")
    private String lastName;
    /**
     * Email address of the user.
     * Maps to the "email" column in the database.
     */
    @Column(name = "email", unique = true)
    private String email;
    /**
     * ID token of the user.
     * Maps to the "id_token" column in the database.
     */
    @Column(name = "id_token")
    private String idToken;
    /**
     * Constructs a User with the specified user ID, first name, last name, email, and ID token.
     *
     * @param userIdParam    the unique identifier for the user
     * @param firstNameParam the first name of the user
     * @param lastNameParam  the last name of the user
     * @param emailParam     the email address of the user
     * @param idTokenParam   the ID token of the user
     */
    public User(final String userIdParam, final String firstNameParam, final String lastNameParam, final String emailParam, String idTokenParam) {
        super();
        this.userId = userIdParam;
        this.firstName = firstNameParam;
        this.lastName = lastNameParam;
        this.email = emailParam;
        this.idToken = idTokenParam;
    }
    /**
     * Default constructor for the User entity.
     */
    public User() {
    }
    /**
     * Returns the unique identifier of the user.
     *
     * @return the user ID
     */
    public String getUserId() {
        return userId;
    }
    /**
     * Returns the first name of the user.
     *
     * @return the first name
     */
    public String getFirstName() {
        return firstName;
    }
    /**
     * Returns the last name of the user.
     *
     * @return the last name
     */
    public String getLastName() {
        return lastName;
    }
    /**
     * Returns the email address of the user.
     *
     * @return the email address
     */
    public String getEmail() {
        return email;
    }
    /**
     * Returns the ID token of the user.
     *
     * @return the ID token
     */
    public String getIdToken() {
        return idToken;
    }

}

