package com.flash_card.view.createFlashcardPage;

import com.flash_card.model.dao.FlashcardSetDao;
import com.flash_card.model.dao.UserDao;
import com.flash_card.model.entity.FlashcardSet;
import com.flash_card.model.entity.User;
import com.flash_card.view.MenuController;
import com.flash_card.view_model.flashcard.FlashcardViewModel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class CreateFlashcardController extends MenuController {
    @FXML
    private TextField termField;
    @FXML
    private TextField definitionField;

    private final FlashcardViewModel viewModel = new FlashcardViewModel();
    private final UserDao userDao = UserDao.getInstance();
    private final FlashcardSetDao flashcardSetDao = FlashcardSetDao.getInstance();

    @FXML
    public void initialize() {
        termField.textProperty().bindBidirectional(viewModel.termProperty());
        definitionField.textProperty().bindBidirectional(viewModel.definitionProperty());
    }

    @FXML
    public void handleCreateFlashcard() {
        FlashcardSet flashcardSet = getCurrentFlashcardSet();
        User flashcardCreator = getCurrentUser();
        viewModel.saveFlashcard(flashcardSet, flashcardCreator);
        System.out.println("Flashcard saved. Create new one");
        goToCreateFlashcardPage();
    }

    @FXML
    public void handleSave() {
        FlashcardSet flashcardSet = getCurrentFlashcardSet();
        User flashcardCreator = getCurrentUser();
        viewModel.saveFlashcard(flashcardSet, flashcardCreator);
        System.out.println("Flashcard saved. Back to Flashcard Page");
        goToFlashcardPage();
    }

    @FXML
    public void handleCancel() {
        System.out.println("Cancel");
        goToFlashcardPage();
    }

    //will try to get the current flashcard set info from previous page
    private FlashcardSet getCurrentFlashcardSet() {
        return flashcardSetDao.findById(1);
    }

    //will get User through AuthSessionViewModel
    private User getCurrentUser() {
        return  userDao.findById("1"); //actually should be AuthSessionViewModel.getCurrentUser().getId()
    }

    private void goToFlashcardPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/flash_card/fxml/flashcard.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) termField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void goToCreateFlashcardPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/flash_card/fxml/create_flashcard.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) termField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}