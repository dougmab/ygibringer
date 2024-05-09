package com.github.dougmab.ygibringer;

import com.github.dougmab.ygibringer.app.service.ConfigurationService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Objects;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        ConfigurationService.load();

        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/panel.fxml")));
        Scene scene = new Scene(root);

        stage.setResizable(false);
        stage.setTitle("YgiBringer");

        Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/static/img/icon.png")));
        stage.getIcons().add(icon);

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(new String[0]);
    }
}