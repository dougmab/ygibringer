package com.github.dougmab.ygibringer.app.controller;

import com.github.dougmab.ygibringer.app.model.Account;
import com.github.dougmab.ygibringer.app.model.Status;
import com.github.dougmab.ygibringer.app.model.StatusType;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.nio.file.Path;

public class AccountController {

    @FXML
    private Label loginLabel;

    @FXML
    private Label passwordLabel;

    @FXML
    private Label managerLabel;

    @FXML
    private Label statusLabel;

    @FXML
    private Button visibilityButton;

    private Status status;
    private String password;
    private boolean isPasswordVisible = false;

    public void init(Account accountModel) {
        loginLabel.setText(accountModel.getLogin());
        password = accountModel.getPassword();
        status = accountModel.getStatus();

        if (!status.getType().equals(StatusType.PENDING)) {
            changeStatusLabel(Status.pending(), status);
        }

        accountModel.statusProperty().addListener((obs, oldValue, newValue) -> {
            changeStatusLabel(oldValue, newValue);
        });
    }

    @FXML
    void changePasswordVisibility(ActionEvent event) {
        ImageView visibilityIcon = (ImageView) visibilityButton.getGraphic();
        if (isPasswordVisible) {
            visibilityIcon.setImage(new Image(Path.of("static", "img", "visibility.png").toString()));
            passwordLabel.setText("•••••••");
            isPasswordVisible = false;
            return;
        }

        visibilityIcon.setImage(new Image(Path.of("static", "img", "visibility_off.png").toString()));
        passwordLabel.setText(password);
        isPasswordVisible = true;
    }

    @FXML
    void changeStat(MouseEvent event) {

    }

    @FXML
    void editAccount(MouseEvent event) {

    }

    private void changeStatusLabel(Status oldStatus, Status newStatus) {
        Platform.runLater(() -> {
            statusLabel.setText(newStatus.getTitle());

            ImageView icon = (ImageView) statusLabel.getGraphic();
            icon.setImage(new Image(Path.of("static", "img", newStatus.getType().getIconName()).toString()));

            var classList = statusLabel.getStyleClass();
            classList.remove(oldStatus.getType().getCssClass());
            classList.add(newStatus.getType().getCssClass());
        });
    }
}
