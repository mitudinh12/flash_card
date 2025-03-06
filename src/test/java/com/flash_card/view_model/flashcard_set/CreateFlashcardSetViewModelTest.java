//package com.flash_card.view_model.flashcard_set;
//
//import com.flash_card.model.dao.FlashcardSetDao;
//import com.flash_card.model.dao.UserDao;
//import com.flash_card.model.entity.FlashcardSet;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//public class CreateFlashcardSetViewModelTest extends TestSetupAbstract {
//    private CreateFlashcardSetViewModel createFlashcardSetViewModel;
//    private FlashcardSetDao flashcardSetDao;
//    private UserDao userDao;
//
//    @BeforeEach
//    public void setUp() {
//        flashcardSetDao = FlashcardSetDao.getInstance(entityManager);
//        userDao = UserDao.getInstance(entityManager);
//        userDao.persist(testUser1); // Save test user
//        createFlashcardSetViewModel = new CreateFlashcardSetViewModel(entityManager);
//    }
//
//    @Test
//    public void testCreateFlashcardSetViewModel() {
//        assertNotNull(createFlashcardSetViewModel);
//    }
//
//    @Test
//    public void testAddSet() {
//        String name = "Test Set";
//        String description = "Test Description";
//        String topic = "Test Topic";
//        String userId = testUser1.getUserId();
//        System.out.println("userId: " + userId);
//
//        int setId = createFlashcardSetViewModel.addSet(name, description, topic, userId);
//
//        FlashcardSet retrievedSet = flashcardSetDao.findById(setId);
//        assertNotNull(retrievedSet);
//        assertEquals(name, retrievedSet.getSetName());
//        assertEquals(description, retrievedSet.getSetDescription());
//        assertEquals(topic, retrievedSet.getSetTopic());
//    }
//}
