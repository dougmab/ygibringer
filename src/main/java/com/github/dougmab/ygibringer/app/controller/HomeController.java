package com.github.dougmab.ygibringer.app.controller;

import com.github.dougmab.ygibringer.Server;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;

public class HomeController {

    @FXML
    private Hyperlink docLink;

    @FXML
    private Button resetBtn;

    @FXML
    private Button toggleBtn;

    @FXML
    public void initialize() {
        if (Server.isRunning()) {
            var styleClasses = toggleBtn.getStyleClass();
            styleClasses.remove("btn-green");
            styleClasses.add("btn-red");
            toggleBtn.setText("Terminar");
        }
    }

    @FXML
    void openDocsOnBrowser(ActionEvent event) {

    }

    @FXML
    void resetServer(ActionEvent event) {

    }


}
