package com.github.dougmab.ygibringer.app.controller;

import com.github.dougmab.ygibringer.Server;
import com.github.dougmab.ygibringer.app.service.ConfigurationService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class AppController {

    @FXML
    private Button startBtn;

    @FXML
    private Button listBtn;

    @FXML
    private Label serverStatus;

    @FXML
    private Button settingsBtn;

    @FXML
    private Button terminalBtn;

    @FXML
    private StackPane viewDisplay;

    private record View(Node pane, Object controller) {};
    private final Map<Button, View> views = new HashMap<>();
    private Node currentView;

    @FXML
    public void initialize() {
        views.put(listBtn, getView("/view/list.fxml"));
        views.put(settingsBtn, getView("/view/settings.fxml"));

        listBtn.fire();
    }

    public View getView(String fxmlPath) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));

        try {
            return new View(loader.load(), loader.getController());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void changeView(ActionEvent event) {
        View view = views.get((Button) event.getSource());

        if (view.pane.equals(currentView)) return;

        currentView = view.pane;

            viewDisplay.getChildren().clear();
            viewDisplay.getChildren().add(view.pane);
    }

    @FXML
    void toggleServer(ActionEvent event) {
        if (ConfigurationService.get().inputFile == null) {
            System.out.println("Input is null");
            return;
        }

        var styleClasses = startBtn.getStyleClass();
        ImageView icon = (ImageView) startBtn.getGraphic();

        if (Server.isRunning()) {
            styleClasses.remove("btn-red");
            styleClasses.add("btn-green");
            icon.setImage(new Image(Path.of("static", "img", "rocket.png").toString()));

            Server.shutdown();
            return;
        }

        styleClasses.remove("btn-green");
        styleClasses.add("btn-red");
        icon.setImage(new Image(Path.of("static", "img", "rocket_launch.png").toString()));

        Server.start(new String[0]);

        View listView = views.get(listBtn);

        ListController controller = (ListController) listView.controller;
        controller.insertAccounts();
    }

}
