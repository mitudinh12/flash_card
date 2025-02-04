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
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Collections;
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
            editButton.setOnAction(event -> handleEdit(set));
            shareButton.setOnAction(event -> handleShare(set));

            hbox.getChildren().addAll(setName, learnButton, quizButton, editButton, shareButton);
            flashcardSetsContainer.getChildren().add(hbox);
        }
    }

    private void handleLearn(FlashcardSet set) {
    }

    private void handleQuiz(FlashcardSet set) {
    }

    private void handleEdit(FlashcardSet set) {
    }

    private void handleShare(FlashcardSet set) {
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        authSessionViewModel.logout();
        displayLoginPage(event);
    }

    public void displayLoginPage(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/flash_card/fxml/login.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            System.err.println("Error in display Login page. " + e);
        }
    }

    public void setUserName(String name) {
        userName.setText("Hi, " + name);
    }
}
