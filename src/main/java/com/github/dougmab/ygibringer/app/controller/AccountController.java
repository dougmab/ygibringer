package com.github.dougmab.ygibringer.app.controller;

import com.github.dougmab.ygibringer.app.model.Account;
import com.github.dougmab.ygibringer.app.model.Status;
import com.github.dougmab.ygibringer.app.model.StatusType;
import com.github.dougmab.ygibringer.app.service.NotificationService;
import com.github.dougmab.ygibringer.server.service.AccountManagerService;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
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

    @FXML
    private Button visibilityButton;

    private Account account;
    private Status status;
    private String password;
    private boolean isPasswordVisible = false;

    public void init(Account accountModel) {
        loginLabel.setText(accountModel.getLogin());
        password = accountModel.getPassword();
        status = accountModel.getStatus();
        account = accountModel;

        if (!status.getType().equals(StatusType.PENDING)) {
            changeStatusLabel(Status.pending(), status);
        }

        accountModel.statusProperty().addListener((obs, oldValue, newValue) -> {
            changeStatusLabel(oldValue, newValue);
        });

        managerLabel.textProperty().bindBidirectional(accountModel.managerProperty());
    }

    @FXML
    void changePasswordVisibility(ActionEvent event) {
        ImageView visibilityIcon = (ImageView) visibilityButton.getGraphic();
        if (isPasswordVisible) {
            visibilityIcon.setImage(new Image(getClass().getResource("/static/img/visibility.png").toString()));
            passwordLabel.setText("•••••••");
            isPasswordVisible = false;
            return;
        }

        visibilityIcon.setImage(new Image(getClass().getResource("/static/img/visibility_off.png").toString()));
        passwordLabel.setText(password);
        isPasswordVisible = true;
    }

    @FXML
    void changeStat(MouseEvent event) {
        if (account.getStatus().getType().equals(StatusType.MANAGING)) return;

        AccountManagerService manager = AccountManagerService.getInstance();

        if (account.getStatus().getType().equals(StatusType.PENDING)) {
            manager.skipAccount(account);
            return;
        }

        manager.returnAccountToPendingQueue(account);
    }

    @FXML
    void editAccount(MouseEvent event) {

    }

    @FXML
    void copyLoginContent(MouseEvent event) {
        ClipboardContent content = new ClipboardContent();
        content.putString(loginLabel.getText());
        Clipboard.getSystemClipboard().setContent(content);
        NotificationService.send("Login copiado para área de transferência");
    }

    @FXML
    void copyPasswordContent(MouseEvent event) {
        ClipboardContent content = new ClipboardContent();
        content.putString(password);
        Clipboard.getSystemClipboard().setContent(content);
        NotificationService.send("Senha copiada para área de transferência");
    }

    private void changeStatusLabel(Status oldStatus, Status newStatus) {
        Platform.runLater(() -> {
            statusLabel.setText(newStatus.getTitle());

            ImageView icon = (ImageView) statusLabel.getGraphic();
            icon.setImage(new Image(getClass().getResource("/static/img/" + newStatus.getType().getIconName()).toString()));

            var classList = statusLabel.getStyleClass();
            classList.remove(oldStatus.getType().getCssClass());
            classList.add(newStatus.getType().getCssClass());
        });
    }
}
