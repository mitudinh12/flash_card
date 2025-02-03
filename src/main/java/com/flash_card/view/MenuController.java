package com.flash_card.view;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class MenuController {

    @FXML
    private void handleMenuClick(MouseEvent event) {
        String id = ((Text) event.getSource()).getId();
        String fxmlFile = "";

        switch (id) {
            case "home":
                fxmlFile = "/com/flash_card/fxml/home.fxml";
                break;
            case "class":
                fxmlFile = "/com/flash_card/fxml/class.fxml";
                break;
            case "flashcard":
                fxmlFile = "/com/flash_card/fxml/flashcard.fxml";
                break;
            case "teacher":
                fxmlFile = "/com/flash_card/fxml/teacher-mode.fxml";
                break;
            case "createFlashcardSet":
                fxmlFile = "/com/flash_card/fxml/create_flashcard.fxml";
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
}
