package com.flash_card.view.auth;

import com.flash_card.framework.ViewController;
import com.flash_card.localization.Localization;
import com.flash_card.view_model.entity.EntityManagerViewModel;
import com.flash_card.view_model.user_auth.*;
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

public class LoginViewController extends ViewController {
    EntityManager entityManager = EntityManagerViewModel.getEntityManager();
    UserAuthViewModel userAuthViewModel = UserAuthViewModel.getInstance(entityManager);
    AuthSessionViewModel authSessionViewModel = AuthSessionViewModel.getInstance();
    Localization localization = Localization.getInstance();

    @FXML
    private Button loginButton;

    @FXML
    private Label countdownLabel;

    @FXML
    private void loginWithGoogle(ActionEvent event) {
        loginButton.setDisable(true);
        countdownLabel.setVisible(true);

        int timeout = 15; // 15 seconds
        startCountdown(timeout);

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

    private void startCountdown(int seconds) {
        Task<Void> countdownTask = new Task<>() {
            @Override
            protected Void call() {
                for (int i = seconds; i > 0; i--) {
                    final int remaining = i;
                    javafx.application.Platform.runLater(() ->
                            countdownLabel.setText(String.format(localization.getMessage("tryAgainWait"), remaining))
                    );

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                javafx.application.Platform.runLater(() -> countdownLabel.setText(localization.getMessage("tryAgainNow")));
                return null;
            }
        };

        new Thread(countdownTask).start();
    }

    private void resetLoginUI() {
        loginButton.setDisable(false);
        countdownLabel.setVisible(false);
        countdownLabel.setText("");
    }

    private void showLoginCancelledMessage() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(localization.getMessage("loginCancelled"));
        alert.setHeaderText(null);
        alert.setContentText(localization.getMessage("loginFail"));
        alert.showAndWait();
    }

    protected void switchLocale(String language) {
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

    protected void reloadCurrentView() {
        Scene scene = languageComboBox.getScene();
        String fxmlFile = ("/com/flash_card/fxml/login.fxml");

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            loader.setResources(localization.getBundle());
            Parent root = loader.load();
            root.setUserData(fxmlFile); // Set the UserData property
            scene.setRoot(root);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setReloadFxml(String fxmlFilePath) {
        languageComboBox.setValue(localization.getMessage("language")); //display chosen or default language in combobox
        languageComboBox.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null) {
                Parent root = languageComboBox.getScene().getRoot();
                root.setUserData(fxmlFilePath);
            }
        });
    }
}
