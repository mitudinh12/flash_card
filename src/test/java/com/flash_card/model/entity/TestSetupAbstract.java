package com.flash_card.model.entity;

import com.flash_card.framework.DifficultyLevel;
import com.flash_card.model.dao.FlashcardSetDao;
import com.flash_card.model.dao.UserDao;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS) // Ensures @BeforeAll runs only once
public abstract class TestSetupAbstract {
    protected static EntityManagerFactory entityManagerFactory;
    protected static EntityManager entityManager;

    private User testUser1;
    private String userId = "123";
    private String firstName = "John";
    private String lastName = "Doe";
    private String email = "john.doe@example.com";
    private String idToken = "sample-id-token";

    private FlashcardSet testFlashcardSet1;
    private String setName = "Java Basics";
    private String setDescription = "Basic concepts of Java programming";
    private String setTopic = "Java";
    private User flashcardCreator = testUser1;

    private Flashcard testFlashcard1;
    private String term = "Java";
    private String definition = "A programming language";
    private DifficultyLevel difficultyLevel = DifficultyLevel.easy;
    private FlashcardSet flashcardSet = testFlashcardSet1;

    private User testUser2;
    private String userId2 = "456";
    private String firstName2 = "Jane";
    private String lastName2 = "Doe";
    private String email2 = "jane.doe@example.com";
    private String idToken2 = "sample-id-token-2";

    private FlashcardSet testFlashcardSet2;
    private String setName2 = "Java Advanced";
    private String setDescription2 = "Advanced concepts of Java programming";
    private String setTopic2 = "Java";
    private User flashcardCreator2 = testUser2;

    private Flashcard testFlashcard2;
    private String term2 = "Java";
    private String definition2 = "A programming language";
    private DifficultyLevel difficultyLevel2 = DifficultyLevel.easy;
    private FlashcardSet flashcardSet2 = testFlashcardSet2;

    private SharedSet testSharedSet1;
    private SharedSet testSharedSet2;

    @BeforeAll
    public void setUpDatabase() {
        System.out.println("Initializing EntityManagerFactory...");
        entityManagerFactory = Persistence.createEntityManagerFactory("test-persistence-unit");
        entityManager = entityManagerFactory.createEntityManager();

        // set up test data (entity instances)
        testUser1 = new User(userId, firstName, lastName, email, idToken);
        testFlashcardSet1 = new FlashcardSet(setName, setDescription, setTopic, flashcardCreator);
        testFlashcard1 = new Flashcard(term, definition, difficultyLevel, flashcardSet, testUser1);
        testFlashcardSet2 = new FlashcardSet(setName2, setDescription2, setTopic2, flashcardCreator2);
        testFlashcard2 = new Flashcard(term2, definition2, difficultyLevel2, flashcardSet2, testUser2);
        testSharedSet1 = new SharedSet(testUser2, testFlashcardSet1);
        testSharedSet2 = new SharedSet(testUser1, testFlashcardSet2);

        // set up Dao instances
        UserDao userDao = UserDao.getInstance(entityManager);
        FlashcardSetDao flashcardSetDao = FlashcardSetDao.getInstance(entityManager);

    }

    @AfterAll
    public void tearDownDatabase() {
        System.out.println("Closing EntityManagerFactory...");
        if (entityManagerFactory != null) {
            entityManagerFactory.close();
        }
    }
}
