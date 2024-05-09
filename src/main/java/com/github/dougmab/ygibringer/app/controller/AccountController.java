package com.github.dougmab.ygibringer.app.controller;

import com.github.dougmab.ygibringer.app.model.Account;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.text.Text;

public class AccountController {

    @FXML
    private Label indexDisplay;

    @FXML
    private Text loginText;

    @FXML
    private Text passwordText;

    @FXML
    private Text statusDisplay;

    public void init(Account accountModel, int index) {
        indexDisplay.setText(Integer.toString(index));
        loginText.setText(accountModel.getLogin());
        passwordText.setText(accountModel.getPassword());
        statusDisplay.textProperty().bindBidirectional(accountModel.statusProperty());

        accountModel.statusProperty().addListener((observable, oldValue, newValue) -> changeColorDisplay(newValue));
    }

    private void changeColorDisplay(String status) {
        var styleClasses = statusDisplay.getStyleClass();
        System.out.println(styleClasses);
        System.out.println("aqui");
        styleClasses.clear();
        switch (status) {
            case "OK" -> styleClasses.add("stat-ok");
            case "PENDENTE" -> styleClasses.add("stat-pending");
            case "CONFIGURANDO" -> styleClasses.add("stat-inuse");
            default -> styleClasses.add("stat-error");
        }

        System.out.println(styleClasses);
    }
}
