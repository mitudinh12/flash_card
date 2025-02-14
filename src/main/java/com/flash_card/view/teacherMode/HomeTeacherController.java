package com.flash_card.view.teacherMode;

import com.flash_card.framework.ViewController;
import com.flash_card.view_model.entity.EntityManagerViewModel;
import com.flash_card.view_model.user_auth.AuthSessionViewModel;
import jakarta.persistence.EntityManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class HomeTeacherController extends ViewController {
    private AuthSessionViewModel authSessionViewModel = AuthSessionViewModel.getInstance();
    EntityManager entityManager = EntityManagerViewModel.getEntityManager();
    private int currentPage = 0;
    private final int pageSize = 6;

    @FXML
    private Label userName;

    @FXML
    private Button logoutButton;

    @FXML
    private Button createClassButton;

    @FXML
    private Button backButton;

    @FXML
    private Button nextButton;
    @FXML
    private ImageView backIcon;

    @FXML
    private ImageView nextIcon;

    @FXML
    private VBox listClasses;

    @FXML
    private void goNext(ActionEvent event) {

    }

    @FXML
    private void goBack(ActionEvent event) {

    }

    @FXML
    private void gotoCreateClass() {
        goToPage("/com/flash_card/fxml/create-class.fxml", createClassButton.getScene());
    }
}
