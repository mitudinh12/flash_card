package com.flash_card.view.auth;

import com.flash_card.localization.Localization;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * JavaFX entry point for the login view.
 * Loads the login FXML layout and applies localization.
 */
public class LoginView extends Application {

    /**
     * The primary stage for the application.
     * This static reference allows other views to access and update the main window.
     */
    private static Stage stage;

    /**
     * Singleton instance for localization management.
     */
    private final Localization localization = Localization.getInstance();

    /**
     * Starts the JavaFX application by initializing and displaying the login screen.
     *
     * @param primaryStage the primary window provided by the JavaFX runtime
     */
    @Override
    public void start(final Stage primaryStage) {
        stage = primaryStage;
        try {
            String fxmlPath = "/com/flash_card/fxml/login.fxml";
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlPath));
            fxmlLoader.setResources(localization.getBundle());
            Parent root = fxmlLoader.load();
            LoginViewController controller = fxmlLoader.getController();
            controller.setReloadFxml(fxmlPath);
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the primary stage instance.
     * This method allows other classes to access the stage for UI updates.
     *
     * @return the primary {@link Stage} of the application
     */
    public static Stage getStage() {
        return stage;
    }
}
