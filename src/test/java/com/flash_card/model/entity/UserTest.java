package com.flash_card.model.entity;

import com.flash_card.model.dao.UserDao;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UserTest {
    private String userId = "123";
    private String firstName = "John";
    private String lastName = "Doe";
    private String email = "john.doe@example.com";
    private String idToken = "sample-id-token";
    private User testUser = new User(userId, firstName, lastName, email, idToken);

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
        assertEquals(userId, testUser.getUserId(), "Fail to get UserId");
    }

    @Test
    void testGetFirstName() {
        assertEquals(firstName, testUser.getFirstName(), "Fail to get FirstName");
    }

    @Test
    void testGetLastName() {
        assertEquals(lastName, testUser.getLastName(), "Fail to get LastName");
    }

    @Test
    void testGetEmail() {
        assertEquals(email, testUser.getEmail(), "Fail to get Email");
    }

}
