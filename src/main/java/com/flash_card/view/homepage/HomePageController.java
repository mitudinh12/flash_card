package com.flash_card.view.homepage;


import com.flash_card.view_model.user_auth.AuthSessionViewModel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.awt.*;
import java.awt.event.ActionEvent;

public class HomePageController {
    private final AuthSessionViewModel authSessionViewModel = AuthSessionViewModel.getInstance();

    @FXML
    private Label userName;

    @FXML
    private ChoiceBox<String> userDropdown;

    @FXML
    private ListView<Object> listFlashcards;

    @FXML
    private void initialize(ActionEvent event) {
        userDropdown.getItems().addAll("Notification", "Logout");
        userDropdown.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                switch (newVal) {
                    case "Notification":
                        System.out.println("Notification for ");
                        break;
                    case "Logout":
                        authSessionViewModel.logout();
                        displayLoginPage(event);
                }
            }
        });
    }

    public void displayLoginPage(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/login.fxml"));
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
