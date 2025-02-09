package com.flash_card.model.entity;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UserTest extends TestSetupAbstract {
    private EntityManager entityManager;
    private User testUser;
    private String userId = "123";
    private String firstName = "John";
    private String lastName = "Doe";
    private String email = "john.doe@example.com";
    private String idToken = "sample-id-token";


    @BeforeAll
    void setUpEntityManager() {
        entityManager = entityManagerFactory.createEntityManager();

        entityManager.getTransaction().begin();
        User user = new User(userId, firstName, lastName, email, idToken);
        entityManager.persist(user);
        entityManager.getTransaction().commit();

        this.testUser = entityManager.find(User.class, "123");
        assertNotNull(testUser, "User should be found in the database");
        assertEquals("John", testUser.getFirstName());
        assertEquals("Doe", testUser.getLastName());
        assertEquals("john.doe@example.com", testUser.getEmail());
    }

    @Test
    void testConnection() {
        assertNotNull(entityManager, "EntityManager should not be null");
    }

    @Test
    void testEmptyConstructor() {
        User emptyUser = new User(); // Using the no-arg constructor

        assertNotNull(emptyUser, "User object should not be null");
        assertNull(emptyUser.getUserId(), "UserId should be null for empty constructor");
        assertNull(emptyUser.getFirstName(), "FirstName should be null for empty constructor");
        assertNull(emptyUser.getLastName(), "LastName should be null for empty constructor");
        assertNull(emptyUser.getEmail(), "Email should be null for empty constructor");
    }

    @Test
    void testGetUserId() {
        testUser = entityManager.find(User.class, userId);
        assertEquals(userId, testUser.getUserId(), "Fail to get UserId");
    }

    @Test
    void testGetFirstName() {
        testUser = entityManager.find(User.class, userId);
        assertEquals(firstName, testUser.getFirstName(), "Fail to get FirstName");
    }

    @Test
    void testGetLastName() {
        testUser = entityManager.find(User.class, userId);
        assertEquals(lastName, testUser.getLastName(), "Fail to get LastName");
    }

    @Test
    void testGetEmail() {
        testUser = entityManager.find(User.class, userId);
        assertEquals(email, testUser.getEmail(), "Fail to get Email");
    }

}
