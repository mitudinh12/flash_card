package com.flash_card.framework;
import com.flash_card.view_model.user_auth.AuthSessionViewModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public abstract class ViewController {
    private final AuthSessionViewModel authSessionViewModel = AuthSessionViewModel.getInstance();

    @FXML
    private void handleMenuClick(MouseEvent event) {
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
                System.out.println("Home");
                break;
                /*
            case "class":
                fxmlFile = "/com/flash_card/fxml/class.fxml";
                break;
                */

            case "flashcard":
                fxmlFile = "/com/flash_card/fxml/flashcard.fxml";
                System.out.println("flashcard");
                break;
            /*
            case "teacher":
                fxmlFile = "/com/flash_card/fxml/teacher-mode.fxml";
                break;

             */
            case "createFlashcardSet":
                fxmlFile = "/com/flash_card/fxml/create-flashcard-set.fxml";
                System.out.println("create");
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

    // method to handleLogout button
    @FXML
    private void handleLogout(ActionEvent event) {
        authSessionViewModel.logout();
        displayLoginPage(event);
    }
}
