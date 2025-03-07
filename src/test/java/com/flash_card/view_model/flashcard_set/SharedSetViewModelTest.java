package com.flash_card.view_model.flashcard_set;

import com.flash_card.model.dao.FlashcardSetDao;
import com.flash_card.model.dao.SharedSetsDao;
import com.flash_card.model.dao.UserDao;
import com.flash_card.model.entity.FlashcardSet;
import com.flash_card.model.entity.SharedSet;
import com.flash_card.model.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class SharedSetViewModelTest {
    private static EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("FlashcardMariaDbUnitTest");
    private static EntityManager entityManager = entityManagerFactory.createEntityManager();
    private SharedSetViewModel sharedSetViewModel;
    private UserDao userDao;
    private FlashcardSetDao flashcardSetDao;
    private SharedSetsDao sharedSetsDao;
    private User testUser100;
    private User testUser101 = new User("shareid", "share", "User", "share@mail.com", "227b9c4e-5d12-4a8f-bf6e-9c3d2a6b8e5f");
    private FlashcardSet testSet100;
    private String uniqueEmail;

    @BeforeEach
    public void setUp() {
        userDao = UserDao.getInstance(entityManager);
        flashcardSetDao = FlashcardSetDao.getInstance(entityManager);
        sharedSetsDao = SharedSetsDao.getInstance(entityManager);

        // Create test user
        String uniqueUserId = UUID.randomUUID().toString();
        uniqueEmail = "mock.user" + System.currentTimeMillis() + "@gmail.com";
        testUser100 = new User(uniqueUserId, "Mock2", "User2", uniqueEmail, "227b9c4e-5d12-4a8f-bf6e-9c3d2a6b8e5f");
        userDao.persist(testUser100);
        userDao.persist(testUser101);

        // Create test flashcard set
        testSet100 = new FlashcardSet("Java Basics2", "A set for Java beginners2", "Programming2", testUser100);
        flashcardSetDao.persist(testSet100);

        sharedSetViewModel = new SharedSetViewModel(testUser100.getUserId(), entityManager);
    }

    @Test
    public void testSaveSharedFlashcardSet() {
        sharedSetViewModel.saveSharedFlashcardSet(uniqueEmail, testSet100.getSetId());

        SharedSet sharedSet = sharedSetsDao.findBySetIdAndUserId(testSet100.getSetId(), testUser100.getUserId());
        assertNotNull(sharedSet);
        assertEquals(testSet100.getSetId(), sharedSet.getFlashcardSet().getSetId());
        assertEquals(testUser100.getUserId(), sharedSet.getUser().getUserId());
    }

    @Test
    public void testIsUserValid() {
        assertTrue(sharedSetViewModel.isUserValid(uniqueEmail));
        assertFalse(sharedSetViewModel.isUserValid("invalid@example.com"));
    }

    @Test
    public void testIsUserAndSetShared() {
        sharedSetViewModel.saveSharedFlashcardSet(uniqueEmail, testSet100.getSetId());
        assertTrue(sharedSetViewModel.isUserAndSetShared(uniqueEmail, testSet100.getSetId()));
        assertFalse(sharedSetViewModel.isUserAndSetShared(testUser101.getEmail(), testSet100.getSetId()));
    }
}
