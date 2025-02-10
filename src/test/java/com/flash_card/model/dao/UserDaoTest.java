package com.flash_card.model.dao;

import static org.junit.jupiter.api.Assertions.*;

import com.flash_card.model.entity.TestSetupAbstract;
import com.flash_card.model.entity.User;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserDaoTest extends TestSetupAbstract {
    private final String userId = "300";
    private final String firstName = "Alice";
    private final String lastName = "Wonderland";
    private final String email = "alice@example.com";
    private final String idToken = "alice-token";

    @BeforeAll
    void setUp() {
        User testUser = new User(userId, firstName, lastName, email, idToken);
        userDao.persist(testUser);
    }

    @Test
    void testGetInstance() {
        UserDao instance1 = UserDao.getInstance(entityManager);
        UserDao instance2 = UserDao.getInstance(entityManager);
        assertNotNull(instance1, "UserDao instance should not be null");
        assertSame(instance1, instance2, "UserDao should follow singleton pattern");
    }

    @Test
    void testPersist() {
        User newUser = new User("301", "Bob", "Builder", "bob@example.com", "bob-token");
        userDao.persist(newUser);

        User foundUser = userDao.findById("301");
        assertNotNull(foundUser, "Persisted user should be found");
        assertEquals("Bob", foundUser.getFirstName(), "First name should match");
    }

    @Test
    void testUpdate() {
        User existingUser = userDao.findById(userId);
        assertNotNull(existingUser, "User should exist before update");

        String newFirstName = "UpdatedAlice";
        User updatingUser = new User(userId, newFirstName, lastName, email, idToken);
        userDao.update(updatingUser);

        User updatedUser = userDao.findById(userId);
        assertEquals("UpdatedAlice", updatedUser.getFirstName(), "First name should be updated");
    }

    @Test
    void testFindById() {
        User foundUser = userDao.findById(testUser1.getUserId());
        assertNotNull(foundUser, "User should be found");
        assertEquals(testUser1.getFirstName(), foundUser.getFirstName(), "First name should match");
    }

    @Test
    void testFindByEmail() {
        User foundUser = userDao.findByEmail(email);
        assertNotNull(foundUser, "User should be found by email");
        assertEquals(userId, foundUser.getUserId(), "UserId should match");
    }

    @Test
    void testFindByEmailNotFound() {
        User foundUser = userDao.findByEmail("notfound@example.com");
        assertNull(foundUser, "Non-existing email should return null");
    }
}
