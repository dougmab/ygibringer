package com.github.dougmab.ygibringer.app.controller;

import com.github.dougmab.ygibringer.Server;
import com.github.dougmab.ygibringer.app.service.ConfigurationService;
import com.github.dougmab.ygibringer.app.service.NotificationService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class AppController {

    @FXML
    private GridPane titleBar;

    @FXML
    private Button btnClose;

    @FXML
    private Button btnMinimize;

    @FXML
    private Button btnMaximize;

    @FXML
    private Button startBtn;

    @FXML
    private Button listBtn;

    @FXML
    private Button settingsBtn;

    @FXML
    private StackPane viewDisplay;

    @FXML
    private VBox messageList;

    @FXML
    private BorderPane messagePane;

    private record View(Node pane, Object controller) {};
    private final Map<Button, View> views = new HashMap<>();
    private Node currentView;

    private Stage stage;
    private double xOffset = 0;
    private double yOffset = 0;

    private boolean isMaximized = false;
    private double previousWidth;
    private double previousHeight;
    private double previousX;
    private double previousY;

    @FXML
    public void initialize() {
        views.put(listBtn, getView("/view/list.fxml"));
        views.put(settingsBtn, getView("/view/settings.fxml"));

        NotificationService.setMessageDisplay(messageList);

        listBtn.fire();

        titleBar.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        titleBar.setOnMouseDragged(event -> {
            if (isMaximized) setMaximized(false);
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });

        btnMinimize.setOnAction(event -> stage.setIconified(true));
        btnMaximize.setOnAction(event -> setMaximized(!isMaximized));

        btnClose.setOnAction(event -> {
            if (Server.isRunning()) Server.shutdown();
            stage.close();
        });
    }

    private View getView(String fxmlPath) {
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

        Node lastView = currentView;
        currentView = view.pane;

            viewDisplay.getChildren().remove(lastView);
            viewDisplay.getChildren().add(view.pane);
            messagePane.toFront();
    }

    @FXML
    void toggleServer(ActionEvent event) {
        if (ConfigurationService.getConfig().inputFile == null) {
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

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setMaximized(boolean maximized) {
        if (maximized == isMaximized) return;
        isMaximized = maximized;
        if (!isMaximized) {
            // Restaurar a janela ao seu tamanho e posição anteriores
            stage.setWidth(previousWidth);
            stage.setHeight(previousHeight);
            stage.setX(previousX);
            stage.setY(previousY);
            return;
        }
        // Salvar o tamanho e posição atuais
        previousWidth = stage.getWidth();
        previousHeight = stage.getHeight();
        previousX = stage.getX();
        previousY = stage.getY();

        // Obter as dimensões da tela
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();

        // Definir o estágio para preencher a área de trabalho
        stage.setX(screenBounds.getMinX());
        stage.setY(screenBounds.getMinY());
        stage.setWidth(screenBounds.getWidth());
        stage.setHeight(screenBounds.getHeight());

        isMaximized = true;
    }
}
