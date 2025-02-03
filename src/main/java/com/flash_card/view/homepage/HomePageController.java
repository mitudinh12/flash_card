package com.flash_card.view.homepage;


import com.flash_card.view.auth.LoginView;
import com.flash_card.view.MenuController;
import com.flash_card.view_model.user_auth.AuthSessionViewModel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.event.ActionEvent;
import java.io.IOException;


public class HomePageController extends MenuController {
    private static final Logger log = LoggerFactory.getLogger(HomePageController.class);
    private final AuthSessionViewModel authSessionViewModel = AuthSessionViewModel.getInstance();
    private static Stage stage = LoginView.getStage();

    @FXML
    private Label userName;

    @FXML
    private Button logoutButton;

    @FXML
    private ListView<Object> listFlashcards;

    @FXML
    private void initialize() {
        setUserName(authSessionViewModel.getVerifiedUserInfo().get("firstName"));
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

    @FXML
    // Open create flashcard set view
    private void createFlashcardSet(MouseEvent event) {
        System.out.println("Create Flashcard Set clicked!");
        try {
            //Load create flashcard set view scene
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/flash_card/fxml/create_flashcard-set.fxml"));
            Parent flashcardSetRoot = loader.load();
            Scene scene = new Scene(flashcardSetRoot);
            stage.setScene(scene);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
