package com.flash_card.view_model.user;

import static org.junit.jupiter.api.Assertions.*;

import com.flash_card.model.dao.FlashcardSetDao;
import com.flash_card.model.dao.SharedSetsDao;
import com.flash_card.model.entity.FlashcardSet;
import com.flash_card.model.entity.SharedSet;
import com.flash_card.view_model.flashcard_set.OwnFlashcardSetViewModel;
import com.flash_card.view_model.flashcard_set.SharedFlashcardSetViewModel;
import com.flash_card.framework.SetViewModel;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class HomepageViewModelTest {
    private HomepageViewModel homepageViewModel;
    private FlashcardSetDao flashcardSetDaoMock;
    private SharedSetsDao sharedSetsDaoMock;
    private final String userId = "user123";

    @BeforeEach
    void setUp() {
        flashcardSetDaoMock = Mockito.mock(FlashcardSetDao.class);
        sharedSetsDaoMock = mock(SharedSetsDao.class);

        // Stub DAO responses
        List<FlashcardSet> ownFlashcardSets = new ArrayList<>();
        ownFlashcardSets.add(new FlashcardSet("set1", "My Flashcard Set"));

        List<SharedSet> sharedSets = new ArrayList<>();
        SharedSet sharedSet = new SharedSet();
        sharedSet.setFlashcardSet(new FlashcardSet("set2", "Shared Flashcard Set"));
        sharedSets.add(sharedSet);

        when(flashcardSetDaoMock.findAllSetsByCreatorId(userId)).thenReturn(ownFlashcardSets);
        when(sharedSetsDaoMock.findByUserId(userId)).thenReturn(sharedSets);

        // Initialize ViewModel with mock DAOs
        homepageViewModel = new HomepageViewModel(userId) {
            @Override
            protected FlashcardSetDao getFlashcardSetDao() {
                return flashcardSetDaoMock;
            }

            @Override
            protected SharedSetsDao getSharedSetsDao() {
                return sharedSetsDaoMock;
            }
        };
    }

    @Test
    void testLoadFlashcards() {
        ObservableList<SetViewModel> flashcardList = homepageViewModel.getFlashcardList();

        assertNotNull(flashcardList, "Flashcard list should not be null");
        assertEquals(2, flashcardList.size(), "Flashcard list should contain 2 items");
        assertTrue(flashcardList.get(0) instanceof OwnFlashcardSetViewModel, "First item should be an own flashcard set");
        assertTrue(flashcardList.get(1) instanceof SharedFlashcardSetViewModel, "Second item should be a shared flashcard set");
    }

    @Test
    void testDeleteOwnFlashcardSet() {
        SetViewModel ownFlashcardSet = new OwnFlashcardSetViewModel(new FlashcardSet("set1", "My Flashcard Set"));

        homepageViewModel.deleteFlashcardSet(ownFlashcardSet);

        verify(flashcardSetDaoMock, times(1)).delete(ownFlashcardSet.getSet());
        assertFalse(homepageViewModel.getFlashcardList().contains(ownFlashcardSet), "Flashcard list should not contain deleted own flashcard set");
    }

    @Test
    void testDeleteSharedFlashcardSet() {
        SharedSet sharedSet = new SharedSet();
        sharedSet.setFlashcardSet(new FlashcardSet("set2", "Shared Flashcard Set"));
        SetViewModel sharedFlashcardSet = new SharedFlashcardSetViewModel(sharedSet.getFlashcardSet());

        when(sharedSetsDaoMock.findBySetIdAndUserId("set2", userId)).thenReturn(sharedSet);

        homepageViewModel.deleteFlashcardSet(sharedFlashcardSet);

        verify(sharedSetsDaoMock, times(1)).delete(sharedSet);
        assertFalse(homepageViewModel.getFlashcardList().contains(sharedFlashcardSet), "Flashcard list should not contain deleted shared flashcard set");
    }

    @Test
    void testDeleteNullFlashcardSet() {
        homepageViewModel.deleteFlashcardSet(null);
        verifyNoInteractions(flashcardSetDaoMock, sharedSetsDaoMock);
    }
}
