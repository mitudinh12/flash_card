package com.flash_card.view_model.user;

import static org.junit.jupiter.api.Assertions.*;

import com.flash_card.model.dao.FlashcardSetDao;
import com.flash_card.model.dao.SharedSetsDao;
import com.flash_card.model.dao.UserDao;
import com.flash_card.model.entity.FlashcardSet;
import com.flash_card.model.entity.SharedSet;
import com.flash_card.model.entity.User;
import com.flash_card.view_model.flashcard_set.OwnFlashcardSetViewModel;
import com.flash_card.view_model.flashcard_set.SharedFlashcardSetViewModel;
import com.flash_card.framework.SetViewModel;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class HomepageViewModelTest {
    private static EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("FlashcardMariaDbUnitTest");
    private static EntityManager entityManager = entityManagerFactory.createEntityManager();
    private User testCreator = new User("homepageid", "home", "page", "homepage@gmail.com", "homepagetoken");
    private User testUser = new User("userhome", "user", "one", "userhome@gmail.com", "usertoken");
    private FlashcardSet set1 = new FlashcardSet("Java Basics", "Java 101", "Programming", testCreator);
    private FlashcardSet set2 = new FlashcardSet("Java Basics", "Java 101", "Programing",testUser);
    private SharedSet sharedSet1 = new SharedSet(testCreator, set2);
    private UserDao userDao = UserDao.getInstance(entityManager);
    private FlashcardSetDao flashcardSetDao = FlashcardSetDao.getInstance(entityManager);
    private SharedSetsDao sharedSetsDao = SharedSetsDao.getInstance(entityManager);
    private HomepageViewModel homepageViewModel;
    private OwnFlashcardSetViewModel ownFlashcardSetViewModel = new OwnFlashcardSetViewModel(set1);
    private SharedFlashcardSetViewModel sharedFlashcardSetViewModel = new SharedFlashcardSetViewModel(set2);
    private ObservableList<SharedFlashcardSetViewModel> sharedFlashcardList = FXCollections.observableArrayList();

    @BeforeEach
    public void setUp() {
        userDao.persist(testCreator);
        userDao.persist(testUser);
        flashcardSetDao.persist(set1);
        flashcardSetDao.persist(set2);
        sharedSetsDao.persist(sharedSet1);
        homepageViewModel = new HomepageViewModel(testCreator.getUserId(),entityManager);

    }

    @Test
    public void testHomepageViewModel() {
        homepageViewModel = new HomepageViewModel(testCreator.getUserId(), entityManager);
        assertNotNull(homepageViewModel);
    }

    @Test
    public void testLoadFlashcards() {
        assertNotNull(homepageViewModel);
        homepageViewModel.loadFlashcards(testCreator.getUserId());
        ObservableList<SetViewModel> flashcardList = homepageViewModel.getFlashcardList();
        ObservableList<OwnFlashcardSetViewModel> ownFlashcardList = homepageViewModel.getOwnFlashcardList();
        ObservableList<SharedFlashcardSetViewModel> sharedFlashcardList = homepageViewModel.getSharedFlashcardList();
        assertNotNull(flashcardList);
        assertNotNull(ownFlashcardList);
        assertNotNull(sharedFlashcardList);
    }

    @Test
    public void testDeleteFlashcardSet() {

        homepageViewModel.deleteFlashcardSet(null);
        assertTrue(homepageViewModel.getOwnFlashcardList().isEmpty());

        assertNotNull(homepageViewModel);
        homepageViewModel.loadFlashcards(testCreator.getUserId());
        ObservableList<SetViewModel> flashcardList = homepageViewModel.getFlashcardList();
        assertNotNull(flashcardList);

        homepageViewModel.deleteFlashcardSet(ownFlashcardSetViewModel);
        assertFalse(homepageViewModel.getOwnFlashcardList().contains(ownFlashcardSetViewModel));

        homepageViewModel.deleteFlashcardSet(sharedFlashcardSetViewModel);
        assertFalse(homepageViewModel.getSharedFlashcardList().contains(sharedFlashcardSetViewModel));

    }

    @Test
    public void testGetFlashcardList() {
        assertNotNull(homepageViewModel);
        homepageViewModel.loadFlashcards(testCreator.getUserId());
        ObservableList<SetViewModel> flashcardList = homepageViewModel.getFlashcardList();
        assertNotNull(flashcardList);
    }
}
