package com.flash_card.view.flashcardPage;

import com.flash_card.framework.ViewController;
import com.flash_card.model.dao.FlashcardSetDao;
import com.flash_card.model.entity.FlashcardSet;
import com.flash_card.view.homepage.HomePageController;
import com.flash_card.view_model.user_auth.AuthSessionViewModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

public class FlashcardController extends ViewController {
    private static final Logger log = LoggerFactory.getLogger(HomePageController.class);
    private final AuthSessionViewModel authSessionViewModel = AuthSessionViewModel.getInstance();
    private final FlashcardSetDao flashcardSetDao = FlashcardSetDao.getInstance();

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
        List<FlashcardSet> newestSets = flashcardSetDao.findThreeNewestSets();
        flashcardSetsContainer.getChildren().clear();
        for (FlashcardSet set : newestSets) {
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
            shareButton.setOnAction(event -> handleShare(set));

            hbox.getChildren().addAll(setName, learnButton, quizButton, editButton, shareButton);
            flashcardSetsContainer.getChildren().add(hbox);
        }
    }

    private void handleLearn(FlashcardSet set) {
    }

    private void handleQuiz(FlashcardSet set) {
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

    private void handleShare(FlashcardSet set) {
    }

    public void setUserName(String name) {
        userName.setText("Hi, " + name);
    }
}
