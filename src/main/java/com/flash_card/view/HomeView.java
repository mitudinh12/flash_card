package com.flash_card.view;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;

public class HomeView extends Application {
    private static Stage stage;
    //FXML UI components
    @FXML
    private HBox createFlashcardsHBox;

    @FXML
    // Open create flashcard set view
    private void createFlashcardSet(MouseEvent event) {
        System.out.println("Create Flashcard Set clicked!");
        try {
            //Load create flashcard set view scene
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/flash_card/fxml/create_flashcard-set.fxml"));
            Parent flashcardSetRoot = loader.load();
            Scene scene = new Scene(flashcardSetRoot);
            stage.setScene(scene);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    @Override
    public void start(Stage primaryStage) {
        stage = primaryStage;
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/flash_card/fxml/home.fxml")); //load home page first
            //Set up stage
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Flashcard Application");
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
