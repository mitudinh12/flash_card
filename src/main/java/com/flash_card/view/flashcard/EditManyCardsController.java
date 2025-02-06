package com.flash_card.view.flashcard;

import com.flash_card.framework.ViewController;
import com.flash_card.view_model.flashcard.EditFlashcardViewModel;
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

public class EditManyCardsController extends ViewController {
    private int flashcardSetId;
    private EditFlashcardViewModel viewModel = new EditFlashcardViewModel();

    @FXML
    public Label setName;
    @FXML
    public VBox flashcardsContainer;

    //INITIALIZE PAGE
    public void setFlashcardSetId(int setId) {
        this.flashcardSetId = setId; //retrieve the setId from the previous page
        loadFlashcardSetName();
        loadFlashcards();
    }

    //set name as title
    private void loadFlashcardSetName() {
        String name = viewModel.getSetName(flashcardSetId);
        if (name != null) {
            setName.setText(name);
        }
    }

    //display all flashcards in the set
    private void loadFlashcards() {
        List<Integer> flashcardIds = viewModel.getFlashcardIdsBySetId(flashcardSetId);
        flashcardsContainer.getChildren().clear();
        for (int flashcardId : flashcardIds) {
            HBox flashcardBox = new HBox();
            flashcardBox.getStyleClass().add("hbox-flashcard-set");
            Label termLabel = new Label(viewModel.term(flashcardId));
            Label definitionLabel = new Label(viewModel.definition(flashcardId));
            Button editButton = new Button("Edit");
            Button deleteButton = new Button("Delete");

            editButton.setOnAction(event -> handleEditFlashcard(flashcardId));
            deleteButton.setOnAction(event -> handleDeleteFlashcard(flashcardId));

            flashcardBox.getChildren().addAll(termLabel, definitionLabel, editButton, deleteButton);
            flashcardsContainer.getChildren().add(flashcardBox);
        }
    }

    @FXML
    private void handleAddCard() {
        goToAddFlashcardPage();
    }

    @FXML
    private void handleEditFlashcard(int flashcardId) {
        goToEditFlashcardPage(flashcardId);
    }

    //safe
    @FXML
    private void handleDeleteFlashcard(int flashcardId) {
        if (viewModel.isLastFlashcard(flashcardSetId)) {
            showAlert("Warning", "The last card cannot be deleted. You need to delete the whole set.");
        } else {
            viewModel.deleteFlashcard(flashcardId);
        }
        loadFlashcards(); //reload the flashcards
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

    private void goToEditFlashcardPage(int flashcardId) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/flash_card/fxml/edit-flashcard.fxml"));
            Parent root = loader.load();
            EditFlashcardController controller = loader.getController();
            controller.setFlashcardSetId(flashcardSetId);
            controller.setFlashcardId(flashcardId); //pass the cardId and setId to the controller
            Scene scene = setName.getScene();
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

