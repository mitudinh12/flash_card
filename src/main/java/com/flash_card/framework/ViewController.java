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
/**
 * Abstract base class for all view controllers in the application.
 * Provides common functionality for handling localization, menu navigation, and user session management.
 */
public abstract class ViewController {
    /**
     * ViewModel for managing user authentication sessions.
     */
    protected final AuthSessionViewModel authSessionViewModel = AuthSessionViewModel.getInstance();
    /**
     * Singleton instance of the Localization class for managing localization.
     */
    protected final Localization localization = Localization.getInstance();
    /**
     * Label to display the user's name.
     */
    @FXML
    protected Label userName;
    /**
     * ComboBox for selecting the application language.
     */
    @FXML
    protected ComboBox<String> languageComboBox;
    /**
     * Handles the language change event triggered by the ComboBox.
     * Updates the application's locale
     */
    @FXML
    protected void handleLanguageChange() {
        String selectedLanguage = languageComboBox.getValue();
        switchLocale(selectedLanguage);
    }
    /**
     * Switches the application's locale based on the selected language.
     *
     * @param language the selected language
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
     * Reloads the current view to apply the updated locale.
     */
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
    /**
     * Sets the FXML file path for reloading the view and initializes the language ComboBox.
     *
     * @param fxmlFilePath the path to the FXML file
     */
    public void setReloadFxml(final String fxmlFilePath) {
        languageComboBox.setValue(localization.getMessage("language")); //display chosen or default language in combobox
        languageComboBox.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null) {
                Parent root = languageComboBox.getScene().getRoot();
                root.setUserData(fxmlFilePath);
            }
        });
    }
    /**
     * Handles menu click events and navigates to the corresponding view.
     *
     * @param event the mouse event triggered by clicking a menu item
     */
    @FXML
    protected void handleMenuClick(final MouseEvent event) {
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
            default:
                fxmlFile = "/com/flash_card/fxml/home.fxml";
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
    /**
     * Displays the login page.
     *
     * @param event the action event triggered by the logout button
     */
    // method to display login page
    protected void displayLoginPage(final ActionEvent event) {
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

    /**
     * Handles the logout action by ending the user session and displaying the login page.
     *
     * @param event the action event triggered by the logout button
     */
    // method to handleLogout button
    @FXML
    protected void handleLogout(final ActionEvent event) {
        authSessionViewModel.logout();
        displayLoginPage(event);
    }
    /**
     * Displays an alert dialog with the specified title and message.
     *
     * @param title   the title of the alert
     * @param message the message to display in the alert
     */
    protected void showAlert(final String title, final String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    /**
     * Navigates to the specified page by loading the corresponding FXML file.
     *
     * @param fxmlFile the path to the FXML file
     * @param scene    the current scene
     */
    protected void goToPage(final String fxmlFile, final Scene scene) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            loader.setResources(localization.getBundle());
            Parent root = loader.load();
            scene.setRoot(root);
            setReloadFxml(fxmlFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Sets the user's name in the userName label.
     */
    public void setUserName() {
        String userFirstName = authSessionViewModel.getVerifiedUserInfo().get("firstName");
        userName.setText(localization.getMessage("Hi") + ", " + userFirstName);
    }

    /**
     * Displays the homepage.
     *
     * @param event the action event triggered by the homepage button
     */
    @FXML
    protected void displayHomepage(final ActionEvent event) {
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
