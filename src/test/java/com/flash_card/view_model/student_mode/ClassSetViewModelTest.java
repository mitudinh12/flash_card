package com.flash_card.view_model.student_mode;

import com.flash_card.model.dao.FlashcardDao;
import com.flash_card.model.dao.FlashcardSetDao;
import com.flash_card.model.dao.UserDao;
import com.flash_card.model.entity.Flashcard;
import com.flash_card.model.entity.FlashcardSet;
import com.flash_card.model.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import javafx.beans.property.SimpleStringProperty;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClassSetViewModelTest {
    private static final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("FlashcardMariaDbUnitTest");
    private static final EntityManager entityManager = entityManagerFactory.createEntityManager();
    private final FlashcardDao flashcardDao = FlashcardDao.getInstance(entityManager);
    private final FlashcardSetDao flashcardSetDao = FlashcardSetDao.getInstance(entityManager);
    private final UserDao userDao = UserDao.getInstance(entityManager);
    private final User testUser = new User("8541287", "John", "Doe", "testMail542445@example.com", "sample-id-token");
    private final FlashcardSet testFlashcardSet = new FlashcardSet("Test Set", "Test Description", "Test Topic", testUser);
    private final Flashcard testFlashcard = new Flashcard("Test term", "Test definition", testFlashcardSet, testUser);
    private ClassSetViewModel assignedFlashcardSetViewModel = new ClassSetViewModel(testFlashcardSet);

    @BeforeEach
    void setUp() {
        userDao.persist(testUser);
        flashcardSetDao.persist(testFlashcardSet);
        flashcardDao.persist(testFlashcard);
    }

    @AfterEach
    void tearDown() {
        flashcardDao.delete(testFlashcard);
        flashcardSetDao.delete(testFlashcardSet);
        userDao.delete(testUser);
    }

    @Test
    void getType() {
        assertEquals("assigned", assignedFlashcardSetViewModel.getType());
    }

    @Test
    void testSetNameProperty() {
        SimpleStringProperty setName = new SimpleStringProperty(testFlashcardSet.getSetName());
        assertTrue(setName.getName().equals(assignedFlashcardSetViewModel.setNameProperty().getName()));
    }

    @Test
    void testSetTopicProperty() {
        SimpleStringProperty setTopic = new SimpleStringProperty(testFlashcardSet.getSetTopic());
        assertTrue(setTopic.getName().equals(assignedFlashcardSetViewModel.setTopicProperty().getName()));
    }

    @Test
    void testSetLanguageProperty() {
        testFlashcardSet.setSetLanguage("english");
        SimpleStringProperty setLanguage = new SimpleStringProperty(testFlashcardSet.getSetLanguage());
        assertEquals(setLanguage.getName(), assignedFlashcardSetViewModel.setLanguageProperty().getName());
    }

    @Test
    void testSetNumberFlashcard() {
        SimpleStringProperty numberFlashcard = new SimpleStringProperty(String.valueOf(testFlashcardSet.getNumberFlashcards()));
        assertTrue(numberFlashcard.getName().equals(assignedFlashcardSetViewModel.setNumberFlashcard().getName()));
    }

    @Test
    void testUpdateEntity() {
        assignedFlashcardSetViewModel.updateEntity();
        assertEquals(testFlashcardSet.getSetName(), assignedFlashcardSetViewModel.getSet().getSetName());
        assertEquals(testFlashcardSet.getSetTopic(), assignedFlashcardSetViewModel.getSet().getSetTopic());
    }

    @Test
    void testGetSet() {
        assertEquals(testFlashcardSet, assignedFlashcardSetViewModel.getSet());
    }

}