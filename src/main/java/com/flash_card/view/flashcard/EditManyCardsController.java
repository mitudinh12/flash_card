package com.flash_card.view.flashcard;

import com.flash_card.framework.ViewController;
import com.flash_card.model.dao.FlashcardDao;  //Dao need to be in ViewModel instead
import com.flash_card.model.dao.FlashcardSetDao; //Dao need to be in ViewModel instead
import com.flash_card.model.entity.Flashcard;
import com.flash_card.model.entity.FlashcardSet;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.List;
//this file for testing only not follow mvvm Dao need to be in ViewModel instead
public class EditManyCardsController extends ViewController {
    private final FlashcardDao flashcardDao = FlashcardDao.getInstance();
    private final FlashcardSetDao flashcardSetDao = FlashcardSetDao.getInstance();
    private int flashcardSetId;

    @FXML
    public Label setName;
    @FXML
    public VBox flashcardsContainer;

    public void setFlashcardSetId(int setId) {
        this.flashcardSetId = setId;
        loadFlashcardSetDetails();;
        loadFlashcards();
    }

    private void loadFlashcardSetDetails() { //should be in ViewModel
        FlashcardSet flashcardSet = flashcardSetDao.findById(flashcardSetId);
        if (flashcardSet != null) {
            setName.setText(flashcardSet.getSetName()); //change title to flashcard set name
        }
    }

    private void loadFlashcards() {
        List<Flashcard> flashcards = flashcardDao.findBySetId(flashcardSetId); //should be in ViewModel
        flashcardsContainer.getChildren().clear();
        for (Flashcard flashcard : flashcards) {
            HBox flashcardBox = new HBox();
            flashcardBox.getStyleClass().add("hbox-flashcard-set");
            Label termLabel = new Label(flashcard.getTerm());
            Label definitionLabel = new Label(flashcard.getDefinition());
            Button editButton = new Button("Edit");
            Button deleteButton = new Button("Delete");

            editButton.setOnAction(event -> handleEditFlashcard(flashcard));
            deleteButton.setOnAction(event -> handleDeleteFlashcard(flashcard));

            flashcardBox.getChildren().addAll(termLabel, definitionLabel, editButton, deleteButton);
            flashcardsContainer.getChildren().add(flashcardBox);
        }
    }

    @FXML
    private void handleAddCard() {
        goToAddFlashcardPage();
    }

    private void goToAddFlashcardPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/flash_card/fxml/add-flashcard.fxml"));
            Parent root = loader.load();
            AddFlashcardController controller = loader.getController();
            controller.setFlashcardSetId(flashcardSetId); // pass the flashcardSetId to the next flashcard
            Scene scene = setName.getScene();
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleEditFlashcard(Flashcard flashcard) {
    }

    private void handleDeleteFlashcard(Flashcard flashcard) {
        List<Flashcard> flashcards = flashcardDao.findBySetId(flashcardSetId); // should be in ViewModel
        if (flashcards.size() == 1) {
            showAlert("Warning", "The last card cannot be deleted. You need to delete the whole set.");
        } else {
            flashcardDao.delete(flashcard); // should be in ViewModel
            loadFlashcards();
        }
    }
}

