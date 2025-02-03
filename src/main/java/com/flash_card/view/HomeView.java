package com.flash_card.view;

import com.flash_card.Main;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HomeView extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/com/flash_card/fxml/home.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("SUPERFLASH");
        stage.setScene(scene);
        stage.show();
    }
}
