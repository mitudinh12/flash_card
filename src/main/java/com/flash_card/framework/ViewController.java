package com.flash_card.framework;
import com.flash_card.view.homepage.HomePageController;
import com.flash_card.view_model.user_auth.AuthSessionViewModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public abstract class ViewController {
    private final AuthSessionViewModel authSessionViewModel = AuthSessionViewModel.getInstance();

    @FXML
    protected Label userName;

    @FXML
    protected void handleMenuClick(MouseEvent event) {
        Node clickedNode = (Node) event.getTarget();
        Node parentNode = clickedNode;

        while (parentNode != null && parentNode.getId() == null) {
            parentNode = parentNode.getParent();
        }

        if (parentNode == null) {
            System.err.println("No valid parent with an ID found.");
            return;
        }

        String id = parentNode.getId();
        String fxmlFile = "";

        switch (id) {
            case "home":
                fxmlFile = "/com/flash_card/fxml/home.fxml";
                break;

//            case "class":
//                    fxmlFile = "/com/flash_card/fxml/class.fxml";
//                    break;

            case "teacher":
                fxmlFile = "/com/flash_card/fxml/teacher-mode.fxml";

                break;


            case "createFlashcardSet":
                fxmlFile = "/com/flash_card/fxml/create-flashcard-set.fxml";
                break;
        }

        if (!fxmlFile.isEmpty()) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlFile));
                Parent root = fxmlLoader.load();
                Scene scene = parentNode.getScene();
                scene.setRoot(root);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        event.consume();
    }

    // method to display login page
    protected void displayLoginPage(ActionEvent event) {
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

    // method to handleLogout button
    @FXML
    protected void handleLogout(ActionEvent event) {
        authSessionViewModel.logout();
        displayLoginPage(event);
    }

    protected void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    protected void goToPage(String fxmlFile, Scene scene) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setUserName() {
        String userFirstName = authSessionViewModel.getVerifiedUserInfo().get("firstName");
        userName.setText("Hi, " + userFirstName);
    }


    @FXML
    protected void displayHomepage(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/flash_card/fxml/home.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
