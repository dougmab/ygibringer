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

        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/app.fxml")));
        Scene scene = new Scene(root);

//        stage.setResizable(false);
//        stage.initStyle(StageStyle.UNDECORATED);
        stage.setTitle("YgiBringer");

        stage.getIcons().addAll(
            new Image("static/icons/icon16.png"),
            new Image("static/icons/icon32.png"),
            new Image("static/icons/icon64.png"),
            new Image("static/icons/icon256.png")
        );

        stage.setScene(scene);
        stage.show();

        stage.setOnCloseRequest((event) -> {
            if (Server.isRunning()) Server.shutdown();
        });
    }

    public static void main(String[] args) {
        launch(new String[0]);
    }
}
