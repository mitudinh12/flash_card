package com.flash_card.framework;
import com.flash_card.localization.Localization;
import com.flash_card.view_model.user_auth.AuthSessionViewModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public abstract class ViewController {
    protected final AuthSessionViewModel authSessionViewModel = AuthSessionViewModel.getInstance();
    protected final Localization localization = Localization.getInstance();

    @FXML
    protected Label userName;
    @FXML
    protected ComboBox<String> languageComboBox;

    @FXML
    protected void handleLanguageChange() {
        String selectedLanguage = languageComboBox.getValue();
        switchLocale(selectedLanguage);
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
        Parent root = scene.getRoot();
        String fxmlFile = (String) root.getUserData();

        if (fxmlFile == null) {
            System.err.println("No FXML file path set in UserData.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            loader.setResources(localization.getBundle());
            root = loader.load();
            root.setUserData(fxmlFile); // Set the UserData property
            scene.setRoot(root);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void setReloadFxml(String fxmlFilePath) {
        languageComboBox.setValue(localization.getMessage("language")); //display chosen or default language in combobox
        languageComboBox.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null) {
                Parent root = languageComboBox.getScene().getRoot();
                root.setUserData(fxmlFilePath);
            }
        });
    }

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

            case "student":
                fxmlFile = "/com/flash_card/fxml/student-class.fxml";
                break;

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
                fxmlLoader.setResources(localization.getBundle());
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
            loader.setResources(localization.getBundle());
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
            loader.setResources(localization.getBundle());
            Parent root = loader.load();
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setUserName() {
        String userFirstName = authSessionViewModel.getVerifiedUserInfo().get("firstName");
        userName.setText(localization.getMessage("Hi") + ", " + userFirstName);
    }


    @FXML
    protected void displayHomepage(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/flash_card/fxml/home.fxml"));
            loader.setResources(localization.getBundle());
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
