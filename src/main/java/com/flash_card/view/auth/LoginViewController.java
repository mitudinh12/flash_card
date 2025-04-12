package com.flash_card.view.auth;

import com.flash_card.framework.ViewController;
import com.flash_card.localization.Localization;
import com.flash_card.view_model.entity.EntityManagerViewModel;
import com.flash_card.view_model.user_auth.UserAuthViewModel;
import com.flash_card.view_model.user_auth.AuthSessionViewModel;
import jakarta.persistence.EntityManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.concurrent.Task;

import java.io.IOException;

/**
 * Controller for the login view.
 * Handles login with Google via OAuth2, displays authentication status,
 * and supports UI language switching using localization.
 */
public class LoginViewController extends ViewController {

    /**
     * Timeout duration in seconds for waiting for the user to complete Google login.
     */
    private static final int LOGIN_TIMEOUT_SECONDS = 15;

    /**
     * Interval in milliseconds for countdown updates shown to the user.
     */
    private static final int COUNTDOWN_INTERVAL_MS = 1000;

    /**
     * The entity manager used for database interaction.
     */
    private final EntityManager entityManager = EntityManagerViewModel.getEntityManager();

    /**
     * ViewModel for handling user authentication logic.
     */
    private final UserAuthViewModel userAuthViewModel = UserAuthViewModel.getInstance(entityManager);

    /**
     * ViewModel for managing the user's session state.
     */
    private final AuthSessionViewModel authSessionViewModel = AuthSessionViewModel.getInstance();

    /**
     * Singleton instance for managing localized resources.
     */
    private final Localization localization = Localization.getInstance();

    /**
     * Button that triggers the login process when clicked.
     */
    @FXML
    private Button loginButton;

    /**
     * Label used to display the countdown timer during login.
     */
    @FXML
    private Label countdownLabel;


    /**
     * Handles the login button click event.
     * Initiates the OAuth2 login flow and shows a countdown timer.
     *
     * @param event the action event triggered by the button click
     */
    @FXML
    private void loginWithGoogle(final ActionEvent event) {
        loginButton.setDisable(true);
        countdownLabel.setVisible(true);
        startCountdown(LOGIN_TIMEOUT_SECONDS);

        userAuthViewModel.openIdConnectWithGoogle(
                () -> { // Success callback
                    if (authSessionViewModel.isAuthenticated()) {
                        displayHomepage(event);
                    }
                    resetLoginUI();
                },
                () -> { // Failure callback
                    showLoginCancelledMessage();
                    resetLoginUI();
                }
        );
    }

    /**
     * Starts a countdown timer that updates the UI every second.
     *
     * @param seconds the number of seconds for the countdown
     */
    private void startCountdown(final int seconds) {
        Task<Void> countdownTask = new Task<>() {
            @Override
            protected Void call() {
                for (int i = seconds; i > 0; i--) {
                    final int remaining = i;
                    javafx.application.Platform.runLater(() ->
                            countdownLabel.setText(String.format(localization.getMessage("tryAgainWait"), remaining))
                    );

                    try {
                        Thread.sleep(COUNTDOWN_INTERVAL_MS);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                javafx.application.Platform.runLater(() ->
                        countdownLabel.setText(localization.getMessage("tryAgainNow"))
                );
                return null;
            }
        };

        new Thread(countdownTask).start();
    }

    /**
     * Resets the login UI by enabling the button and hiding the countdown label.
     */
    private void resetLoginUI() {
        loginButton.setDisable(false);
        countdownLabel.setVisible(false);
        countdownLabel.setText("");
    }

    /**
     * Displays an information alert when the login is canceled or fails.
     */
    private void showLoginCancelledMessage() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(localization.getMessage("loginCancelled"));
        alert.setHeaderText(null);
        alert.setContentText(localization.getMessage("loginFail"));
        alert.showAndWait();
    }

    /**
     * Switches the application's language based on the selected language name.
     *
     * @param language the display name of the selected language
     */
    protected void switchLocale(final String language) {
        String langCode;
        switch (language) {
            case "Suomi":
                langCode = "fi";
                break;
            case "ภาษาไทย":
                langCode = "th";
                break;
            case "한국인":
                langCode = "ko";
                break;
            case "Tiếng Việt":
                langCode = "vi";
                break;
            default:
                langCode = "en";
                break;
        }
        localization.setLocaleByLanguage(langCode);
        reloadCurrentView();
    }

    /**
     * Reloads the current FXML view with the updated locale resources.
     */
    protected void reloadCurrentView() {
        Scene scene = languageComboBox.getScene();
        String fxmlFile = "/com/flash_card/fxml/login.fxml";

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            loader.setResources(localization.getBundle());
            Parent root = loader.load();
            root.setUserData(fxmlFile);
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets the FXML file path used for reloading the view and attaches the path to the scene.
     *
     * @param fxmlFilePath the path to the FXML file
     */
    public void setReloadFxml(final String fxmlFilePath) {
        languageComboBox.setValue(localization.getMessage("language"));
        languageComboBox.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null) {
                Parent root = languageComboBox.getScene().getRoot();
                root.setUserData(fxmlFilePath);
            }
        });
    }
}
