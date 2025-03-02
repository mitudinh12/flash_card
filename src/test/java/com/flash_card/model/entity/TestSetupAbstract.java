package com.flash_card.model.entity;

import com.flash_card.framework.DifficultyLevel;
import com.flash_card.framework.SetViewModel;
import com.flash_card.model.dao.*;
import com.flash_card.view_model.flashcard_set.AssignedFlashcardSetViewModel;
import com.flash_card.view_model.flashcard_set.OwnFlashcardSetViewModel;
import com.flash_card.view_model.flashcard_set.SharedFlashcardSetViewModel;
import com.flash_card.view_model.flashcard_set.SharedSetViewModel;
import com.flash_card.view_model.student_mode.ClassSetViewModel;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS) // Ensures @BeforeAll runs only once
public abstract class TestSetupAbstract {
    protected static EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("FlashcardMariaDbUnitTest");
    protected static EntityManager entityManager = entityManagerFactory.createEntityManager();

    protected User testUser1;
    protected String userId = "123";
    protected String firstName = "John";
    protected String lastName = "Doe";
    protected String email = "john.doe@example.com";
    protected String idToken = "sample-id-token";

    protected FlashcardSet testFlashcardSet1;
    protected String setName = "Java Basics";
    protected String setDescription = "Basic concepts of Java programming";
    protected String setTopic = "Java";
    protected User flashcardCreator = testUser1;

    protected Flashcard testFlashcard1;
    protected String term = "Java";
    protected String definition = "A programming language";
    protected DifficultyLevel difficultyLevel = DifficultyLevel.easy;
    protected FlashcardSet flashcardSet = testFlashcardSet1;

    protected User testUser2;
    protected String userId2 = "456";
    protected String firstName2 = "Jane";
    protected String lastName2 = "Doe";
    protected String email2 = "jane.doe@example.com";
    protected String idToken2 = "sample-id-token-2";

    protected FlashcardSet testFlashcardSet2;
    protected String setName2 = "Java Advanced";
    protected String setDescription2 = "Advanced concepts of Java programming";
    protected String setTopic2 = "Java";
    protected User flashcardCreator2 = testUser2;

    protected Flashcard testFlashcard2;
    protected String term2 = "Java";
    protected String definition2 = "A programming language";
    protected DifficultyLevel difficultyLevel2 = DifficultyLevel.easy;
    protected FlashcardSet flashcardSet2 = testFlashcardSet2;

    protected User testUser3;
    protected String userId3 = "78912";
    protected String firstName3 = "Jane78912";
    protected String lastName3 = "Doe";
    protected String email3 = "jane.doe78912@example.com";
    protected String idToken3 = "sample-id-token-278912";

    protected FlashcardSet testFlashcardSet3;
    protected String setName3 = "Java Advanced 78912";
    protected String setDescription3 = "Advanced concepts of Java programming 78912";
    protected String setTopic3 = "Java 78912";
    protected User flashcardCreator3 = testUser3;

    protected Flashcard testFlashcard3;
    protected String term3 = "Java 78912";
    protected String definition3 = "A programming language 78912";
    protected DifficultyLevel difficultyLevel3 = DifficultyLevel.easy;
    protected FlashcardSet flashcardSet3 = testFlashcardSet3;

    protected SharedSet testSharedSet1;
    protected SharedSet testSharedSet2;

    protected FlashcardDao flashcardDao = FlashcardDao.getInstance(entityManager);
    protected FlashcardSetDao flashcardSetDao = FlashcardSetDao.getInstance(entityManager);
    protected SharedSetsDao sharedSetsDao = SharedSetsDao.getInstance(entityManager);
    protected UserDao userDao = UserDao.getInstance(entityManager);
    protected ClassroomDao classroomDao = ClassroomDao.getInstance(entityManager);

    protected OwnFlashcardSetViewModel ownFlashcardSetViewModel;
    protected SharedFlashcardSetViewModel sharedFlashcardSetViewModel;
    protected ClassSetViewModel assignedFlashcardSetViewModel;

    @BeforeEach
    public void setUpDatabase() {
        System.out.println("Initializing EntityManagerFactory...");

        // set up test data (entity instances)
        testUser1 = new User(userId, firstName, lastName, email, idToken);
        testUser2 = new User(userId2, firstName2, lastName2, email2, idToken2);
        testUser3 = new User(userId3, firstName3, lastName3, email3, idToken3);

        testFlashcardSet1 = new FlashcardSet(setName, setDescription, setTopic, testUser1);
        testFlashcard1 = new Flashcard(term, definition, difficultyLevel, testFlashcardSet1, testUser1);

        testFlashcardSet2 = new FlashcardSet(setName2, setDescription2, setTopic2, flashcardCreator2);
        testFlashcard2 = new Flashcard(term2, definition2, difficultyLevel2, flashcardSet2, testUser2);

        testSharedSet1 = new SharedSet(testUser2, testFlashcardSet1);
        testSharedSet2 = new SharedSet(testUser1, testFlashcardSet2);

        testFlashcardSet3 = new FlashcardSet(setName3, setDescription3, setTopic3, flashcardCreator3);
        testFlashcard3 = new Flashcard(term3, definition3, difficultyLevel3, flashcardSet3, testUser3);

        userDao.persist(testUser1);
        userDao.persist(testUser2);
        userDao.persist(testUser3);
        flashcardSetDao.persist(testFlashcardSet1);
        flashcardSetDao.persist(testFlashcardSet2);
        flashcardSetDao.persist(testFlashcardSet3);
        flashcardDao.persist(testFlashcard1);
        flashcardDao.persist(testFlashcard2);
        flashcardDao.persist(testFlashcard3);

        // set up ViewModel instances
        ownFlashcardSetViewModel = new OwnFlashcardSetViewModel(testFlashcardSet1);
        sharedFlashcardSetViewModel = new SharedFlashcardSetViewModel(testFlashcardSet2);
        assignedFlashcardSetViewModel = new ClassSetViewModel(testFlashcardSet3);
    }

    @AfterEach
    public void tearDownDatabase() {
        userDao.delete(testUser1);
        userDao.delete(testUser2);
//
//        System.out.println("Closing EntityManagerFactory...");
//        if (entityManagerFactory != null) {
//            entityManagerFactory.close();
//        }
    }
}