//package com.flash_card.view_model.user;
//
//import static org.junit.jupiter.api.Assertions.*;
//import com.flash_card.view_model.flashcard_set.OwnFlashcardSetViewModel;
//import com.flash_card.view_model.flashcard_set.SharedFlashcardSetViewModel;
//import com.flash_card.framework.SetViewModel;
//import javafx.collections.FXCollections;
//import javafx.collections.ObservableList;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//public class HomepageViewModelTest extends TestSetupAbstract {
//    private HomepageViewModel homepageViewModel;
//    private ObservableList<SharedFlashcardSetViewModel> sharedFlashcardList = FXCollections.observableArrayList();
//
//    @BeforeEach
//    public void setUp() {
//        homepageViewModel = new HomepageViewModel(testUser1.getUserId(),entityManager);
//
//    }
//
//    @Test
//    public void testHomepageViewModel() {
//        homepageViewModel = new HomepageViewModel(testUser1.getUserId(), entityManager);
//        assertNotNull(homepageViewModel);
//    }
//
//    @Test
//    public void testLoadFlashcards() {
//        assertNotNull(homepageViewModel);
//        homepageViewModel.loadFlashcards(testUser1.getUserId());
//        ObservableList<SetViewModel> flashcardList = homepageViewModel.getFlashcardList();
//        ObservableList<OwnFlashcardSetViewModel> ownFlashcardList = homepageViewModel.getOwnFlashcardList();
//        ObservableList<SharedFlashcardSetViewModel> sharedFlashcardList = homepageViewModel.getSharedFlashcardList();
//        assertNotNull(flashcardList);
//        assertNotNull(ownFlashcardList);
//        assertNotNull(sharedFlashcardList);
//    }
//
//    @Test
//    public void testDeleteFlashcardSet() {
//
//        homepageViewModel.deleteFlashcardSet(null);
//        assertTrue(homepageViewModel.getOwnFlashcardList().isEmpty());
//
//        assertNotNull(homepageViewModel);
//        homepageViewModel.loadFlashcards(testUser1.getUserId());
//        ObservableList<SetViewModel> flashcardList = homepageViewModel.getFlashcardList();
//        assertNotNull(flashcardList);
//
//        homepageViewModel.deleteFlashcardSet(ownFlashcardSetViewModel);
//        assertFalse(homepageViewModel.getOwnFlashcardList().contains(ownFlashcardSetViewModel));
//
//        homepageViewModel.deleteFlashcardSet(sharedFlashcardSetViewModel);
//        assertFalse(homepageViewModel.getSharedFlashcardList().contains(sharedFlashcardSetViewModel));
//
//    }
//
//    @Test
//    public void testGetFlashcardList() {
//        assertNotNull(homepageViewModel);
//        homepageViewModel.loadFlashcards(testUser1.getUserId());
//        ObservableList<SetViewModel> flashcardList = homepageViewModel.getFlashcardList();
//        assertNotNull(flashcardList);
//    }
//}
