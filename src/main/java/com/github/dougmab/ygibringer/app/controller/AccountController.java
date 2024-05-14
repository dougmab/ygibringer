package com.github.dougmab.ygibringer.app.controller;

import com.github.dougmab.ygibringer.app.model.Account;
import com.github.dougmab.ygibringer.app.model.Status;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

public class AccountController {

    @FXML
    private Label loginLabel;

    @FXML
    private Label passwordLabel;

    @FXML
    private Label managerLabel;

    @FXML
    private Label statusLabel;

    private Status status;

    public void init(Account accountModel) {
        loginLabel.setText(accountModel.getLogin());
        passwordLabel.setText(accountModel.getPassword());
        status = accountModel.getStatus();
    }

    @FXML
    void changeStat(MouseEvent event) {

    }

    @FXML
    void editAccount(MouseEvent event) {

    }
}
