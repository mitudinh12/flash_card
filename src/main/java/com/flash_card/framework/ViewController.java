package com.flash_card.framework;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

import java.io.IOException;

public abstract class ViewController {

    @FXML
    protected void handleMenuClick(MouseEvent event) {
        String id = ((Text) event.getSource()).getId();
        String fxmlFile = "";

        switch (id) {
            case "home":
                fxmlFile = "/com/flash_card/fxml/home.fxml";
                break;
                /*
            case "class":
                fxmlFile = "/com/flash_card/fxml/class.fxml";
                break;
                */

            case "flashcard":
                fxmlFile = "/com/flash_card/fxml/flashcard.fxml";
                break;
            /*
            case "teacher":
                fxmlFile = "/com/flash_card/fxml/teacher-mode.fxml";
                break;

             */
            case "createFlashcardSet":
                fxmlFile = "/com/flash_card/fxml/create-flashcard-set.fxml";
                break;
        }

        if (!fxmlFile.isEmpty()) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlFile));
                Parent root = fxmlLoader.load();
                Scene scene = ((Text) event.getSource()).getScene().getWindow().getScene();
                scene.setRoot(root);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
}
