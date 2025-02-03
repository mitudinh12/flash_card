package com.flash_card.view.homepage;


import com.flash_card.view.MenuController;
import com.flash_card.view_model.user_auth.AuthSessionViewModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class HomePageController extends MenuController {
    private static final Logger log = LoggerFactory.getLogger(HomePageController.class);
    private final AuthSessionViewModel authSessionViewModel = AuthSessionViewModel.getInstance();

    @FXML
    private Label userName;

    @FXML
    private Button logoutButton;

    @FXML
    private ListView<Object> listFlashcards;

    @FXML
    private void initialize() {
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
