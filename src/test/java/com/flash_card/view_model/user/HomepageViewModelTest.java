package com.flash_card.view_model.user;

import static org.junit.jupiter.api.Assertions.*;

import com.flash_card.model.dao.FlashcardSetDao;
import com.flash_card.model.dao.SharedSetsDao;
import com.flash_card.model.entity.FlashcardSet;
import com.flash_card.model.entity.SharedSet;
import com.flash_card.view_model.flashcard_set.OwnFlashcardSetViewModel;
import com.flash_card.view_model.flashcard_set.SharedFlashcardSetViewModel;
import com.flash_card.framework.SetViewModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.flash_card.view_model.user.HomepageViewModel;
import com.flash_card.model.entity.TestSetupAbstract;

import java.util.ArrayList;
import java.util.List;

public class HomepageViewModelTest extends TestSetupAbstract {
    private HomepageViewModel homepageViewModel;
    private ObservableList<SharedFlashcardSetViewModel> sharedFlashcardList = FXCollections.observableArrayList();

    @BeforeEach
    public void setUp() {
        homepageViewModel = new HomepageViewModel(testUser1.getUserId(),entityManager);

    }

    @Test
    public void testHomepageViewModel() {
        homepageViewModel = new HomepageViewModel(testUser1.getUserId(), entityManager);
        assertNotNull(homepageViewModel);
    }

    @Test
    public void testLoadFlashcards() {
        assertNotNull(homepageViewModel);
        homepageViewModel.loadFlashcards(testUser1.getUserId());
        ObservableList<SetViewModel> flashcardList = homepageViewModel.getFlashcardList();
        ObservableList<OwnFlashcardSetViewModel> ownFlashcardList = homepageViewModel.getOwnFlashcardList();
        ObservableList<SharedFlashcardSetViewModel> sharedFlashcardList = homepageViewModel.getSharedFlashcardList();
        assertNotNull(flashcardList);
        assertNotNull(ownFlashcardList);
        assertNotNull(sharedFlashcardList);
    }

    @Test
    public void testDeleteFlashcardSet() {
        assertNotNull(homepageViewModel);
        homepageViewModel.loadFlashcards(testUser1.getUserId());
        ObservableList<SetViewModel> flashcardList = homepageViewModel.getFlashcardList();
        assertNotNull(flashcardList);

        homepageViewModel.deleteFlashcardSet(ownFlashcardSetViewModel);
        assertNull (homepageViewModel.getOwnFlashcardList());
        assertNull(flashcardSetDao.findByUserId(testUser1.getUserId()));

        homepageViewModel.deleteFlashcardSet(sharedFlashcardSetViewModel);
        assertNull(homepageViewModel.getSharedFlashcardList().getFirst());
        assertNull(sharedSetsDao.findBySetIdAndUserId(testFlashcardSet2.getSetId(), testUser1.getUserId()));

        assertNull(homepageViewModel.getFlashcardList().getFirst());

 }

    @Test
    public void testGetFlashcardList() {
        assertNotNull(homepageViewModel);
        homepageViewModel.loadFlashcards(testUser1.getUserId());
        ObservableList<SetViewModel> flashcardList = homepageViewModel.getFlashcardList();
        assertNotNull(flashcardList);
    }
}
