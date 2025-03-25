package com.flash_card.view.auth;

import com.flash_card.localization.Localization;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginView extends Application {
    private static Stage stage;
    private Localization localization = Localization.getInstance();
    @Override
    public void start(Stage primaryStage) {
        this.stage = primaryStage;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/flash_card/fxml/login.fxml"));
            fxmlLoader.setResources(localization.getBundle());
            Parent root = fxmlLoader.load();
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Allow other views to access the stage
    public static Stage getStage() {
        return stage;
    }

}
