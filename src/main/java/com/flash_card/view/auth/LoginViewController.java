package com.flash_card.view.auth;

import com.flash_card.view.homepage.HomePageController;
import com.flash_card.view_model.user_auth.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.concurrent.Task;

public class LoginViewController {
    UserAuthViewModel userAuthViewModel = UserAuthViewModel.getInstance();
    AuthSessionViewModel authSessionViewModel = AuthSessionViewModel.getInstance();

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
                            countdownLabel.setText("Please wait " + remaining + " seconds to try again...")
                    );

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                javafx.application.Platform.runLater(() -> countdownLabel.setText("You can try again now."));
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
        alert.setTitle("Login Cancelled");
        alert.setHeaderText(null);
        alert.setContentText("The login process was not completed. Please try again.");
        alert.showAndWait();
    }

    void displayHomepage(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/homepage.fxml"));
            Parent root = loader.load();
            HomePageController controller = loader.getController();
            String userFirstName = authSessionViewModel.getVerifiedUserInfo().get("firstName");
            controller.setUserName(userFirstName);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
