package com.flash_card.view.flashcard;

import com.flash_card.framework.ViewController;
import com.flash_card.localization.Localization;
import com.flash_card.view_model.entity.EntityManagerViewModel;
import com.flash_card.view_model.flashcard.EditFlashcardViewModel;
import jakarta.persistence.EntityManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import java.io.IOException;
import java.util.List;

public class EditManyCardsController extends ViewController {
    private EntityManager entityManager = EntityManagerViewModel.getEntityManager();
    private static int flashcardSetId;
    private EditFlashcardViewModel viewModel = new EditFlashcardViewModel(entityManager);
    private int currentPage = 0;
    private final int pageSize = 8;

    @FXML
    public Label setName;
    @FXML
    public VBox flashcardsContainer;
    @FXML
    private ImageView backIcon;
    @FXML
    private ImageView nextIcon;
    @FXML
    private Button backToHomepage;


    //INITIALIZE PAGE
    @FXML
    private void initialize() {
        setReloadFxml("/com/flash_card/fxml/edit-many-cards.fxml");
        loadFlashcardSetName();
        loadFlashcards();
    }

    public void setFlashcardSetId(int setId) {
        EditManyCardsController.flashcardSetId = setId; //retrieve the setId from the previous page
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
        int start = currentPage * pageSize;
        int end = Math.min(start + pageSize, flashcardIds.size());
        for (int i = start; i < end; i++) {
            int flashcardId = flashcardIds.get(i);
            HBox flashcardBox = new HBox();
            flashcardBox.getStyleClass().add("hbox-flashcard");
            flashcardBox.setAlignment(Pos.CENTER_LEFT);

            Button editButton = new Button(localization.getMessage("flashcard.edit"));
            editButton.getStyleClass().add("edit-action-button");
            Button deleteButton = new Button(localization.getMessage("flashcard.delete"));
            deleteButton.getStyleClass().add("delete-action-button");

            editButton.setOnAction(event -> handleEditFlashcard(flashcardId));
            deleteButton.setOnAction(event -> handleDeleteFlashcard(flashcardId));

            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);

            Label termLabel = new Label(viewModel.term(flashcardId));
            termLabel.getStyleClass().add("term-label");
            termLabel.setMaxWidth(80);
            termLabel.setMinWidth(80);

            Label definitionLabel = new Label(viewModel.definition(flashcardId));
            definitionLabel.getStyleClass().add("definition-label");
            definitionLabel.setMaxWidth(200);
            definitionLabel.setMinWidth(200);

            flashcardBox.getChildren().addAll(termLabel, definitionLabel, spacer, editButton, deleteButton);
            flashcardsContainer.getChildren().add(flashcardBox);
        }
        updateNavigationIcons(flashcardIds.size());
    }

    private void updateNavigationIcons(int totalFlashcards) {
        backIcon.setVisible(currentPage > 0);
        nextIcon.setVisible((currentPage + 1) * pageSize < totalFlashcards);
    }

    @FXML
    private void goNext(ActionEvent event) {
        if ((currentPage + 1) * pageSize < viewModel.getFlashcardIdsBySetId(flashcardSetId).size()) {
            currentPage++;
            loadFlashcards();
        }
    }

    @FXML
    private void goBack(ActionEvent event) {
        if (currentPage > 0) {
            currentPage--;
            loadFlashcards();
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
            showAlert(localization.getMessage("flashcard.warning"),localization.getMessage("flashcard.lastCardDeleteWarning"));
        } else {
            viewModel.deleteFlashcard(flashcardId, flashcardSetId); //delete the flashcard and decrease the number of flashcards in the set
        }
        loadFlashcards(); //reload the flashcards
    }

    private void goToAddFlashcardPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/flash_card/fxml/add-flashcard.fxml"));
            loader.setResources(localization.getBundle());
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
            loader.setResources(localization.getBundle());
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

