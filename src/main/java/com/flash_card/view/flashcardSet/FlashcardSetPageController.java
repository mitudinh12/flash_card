package com.flash_card.view.flashcardSet;

import com.flash_card.framework.ViewController;
import com.flash_card.model.dao.FlashcardSetDao; //Dao need to be in ViewModel instead
import com.flash_card.model.entity.FlashcardSet;
import com.flash_card.model.entity.SharedSet;
import com.flash_card.view_model.flashcard_set.SharedSetViewModel;
import com.flash_card.view_model.user_auth.AuthSessionViewModel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
//this file for testing only not follow mvvm Dao need to be in ViewModel instead
public class FlashcardSetPageController extends ViewController {
    private final AuthSessionViewModel authSessionViewModel = AuthSessionViewModel.getInstance();
    private final FlashcardSetDao flashcardSetDao = FlashcardSetDao.getInstance(); //should be in ViewModel
    private SharedSetViewModel sharedSetViewModel = new SharedSetViewModel();

    @FXML
    private Label userName;

    @FXML
    private VBox flashcardSetsContainer;

    @FXML
    private void initialize() {
        setUserName(authSessionViewModel.getVerifiedUserInfo().get("firstName"));
        loadThreeNewestFlashcardSets();
    }

    private void loadThreeNewestFlashcardSets() {
        List<FlashcardSet> ownSets = flashcardSetDao.findByUserId(authSessionViewModel.getVerifiedUserInfo().get("userId")); //should be in ViewModel
        flashcardSetsContainer.getChildren().clear();
        for (FlashcardSet set : ownSets) {
            HBox hbox = new HBox(10);
            hbox.getStyleClass().add("hbox-flashcard-set");
            Label setName = new Label(set.getSetName());
            Button learnButton = new Button("Learn");
            Button quizButton = new Button("Quiz");
            Button editButton = new Button("Edit");
            Button shareButton = new Button("Share");

            learnButton.setOnAction(event -> handleLearn(set));
            quizButton.setOnAction(event -> handleQuiz(set));
            editButton.setOnAction(event -> handleEdit(set.getSetId(), set.getSetName(), set.getSetDescription(), set.getSetTopic())); //pass set info
            shareButton.setOnAction(event -> handleShare(set.getSetId()));

            hbox.getChildren().addAll(setName, learnButton, quizButton, editButton, shareButton);
            flashcardSetsContainer.getChildren().add(hbox);
        }

        List<FlashcardSet> sharedSets = sharedSetViewModel.getSharedFlashcardSets(authSessionViewModel.getVerifiedUserInfo().get("userId"));
        for (FlashcardSet set : sharedSets) {
            HBox hbox = new HBox(10);
            hbox.getStyleClass().add("hbox-flashcard-set");
            Label setName = new Label(set.getSetName());
            Button learnButton = new Button("Learn");
            Button quizButton = new Button("Quiz");
            Button deleteButton = new Button("Delete");

            learnButton.setOnAction(event -> handleLearn(set));
            quizButton.setOnAction(event -> handleQuiz(set));
            deleteButton.setOnAction(event -> handleDelete(set.getSetId()));

            hbox.getChildren().addAll(setName, learnButton, quizButton, deleteButton);
            flashcardSetsContainer.getChildren().add(hbox);

        }
    }

    private void handleLearn(FlashcardSet set) {
    }

    private void handleQuiz(FlashcardSet set)  {
    }

    private void handleEdit(int setId, String setName, String setDescription, String setTopic) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/flash_card/fxml/edit-set.fxml"));
            Parent root = loader.load();

            //pass the FlashcardSet data to the EditFlashcardSetController
            EditFlashcardSetController controller = loader.getController();
            controller.setFlashcardSet(setId, setName, setDescription, setTopic);

            Scene scene = flashcardSetsContainer.getScene();
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleShare(int setId ) {
        Stage newStage = new Stage();
        newStage.setTitle("FLASHCARDS SHARING");
        HBox hBox = new HBox();
        hBox.setPadding(new Insets(10, 10, 10, 10));

        Label emailLabel = new Label("Enter email: ");
        TextField emailField = new TextField();
        Button shareButton = new Button("Share");

        hBox.getChildren().addAll(emailLabel, emailField, shareButton);
        Scene scene = new Scene(hBox);
        newStage.setScene(scene);
        newStage.initModality(Modality.APPLICATION_MODAL);
        newStage.show();

        shareButton.setOnAction(event -> {
            if (!sharedSetViewModel.isUserValid(emailField.getText())) {
                showAlert("Invalid email", "The email you entered is not valid. Please try again.");
                return;
            } else {
                sharedSetViewModel.saveSharedFlashcardSet(emailField.getText(), setId);
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("The flashcard set has been shared successfully!");
                alert.showAndWait();
            }
            newStage.close();
        });

    }

    private void handleDelete(int flashcardSetId) {
        sharedSetViewModel.deleteSharedFlashcardSet(flashcardSetId);
        showAlert("Success", "The flashcard set has been deleted successfully!");
    }

    public void setUserName(String name) {
        userName.setText("Hi, " + name);
    }
}
