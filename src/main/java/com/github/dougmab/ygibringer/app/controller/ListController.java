package com.github.dougmab.ygibringer.app.controller;

import com.github.dougmab.ygibringer.app.model.Account;
import com.github.dougmab.ygibringer.app.model.StatusType;
import com.github.dougmab.ygibringer.app.service.ConfigurationService;
import com.github.dougmab.ygibringer.server.service.AccountManagerService;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ListController {

    private final Map<StatusType, VBox> listMap = new HashMap<>();
    private final Map<Account, Node> accountNodeMap = new HashMap<>();
    private boolean isAccountsInserted = false;

    @FXML
    private VBox concludedList;

    @FXML
    private VBox inProgressList;

    @FXML
    private VBox pendingList;

    @FXML
    private Button saveBtn;

    @FXML
    private Label totalCount;

    @FXML
    public void initialize() {
        listMap.put(StatusType.PENDING, pendingList);
        listMap.put(StatusType.MANAGING, inProgressList);
        listMap.put(StatusType.SUCCESS, concludedList);
        listMap.put(StatusType.ERROR, concludedList);

        if (ConfigurationService.getManagerState() != null) {
           insertAccounts();
        }
    }

    public void insertAccounts() {
        if (isAccountsInserted) return;

        clearLists();
        for (Iterator<Account> it = AccountManagerService.getIterator(); it.hasNext(); ) {
            Account account = it.next();
            System.out.println(account);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/account.fxml"));

            try {
                Node accountNode = loader.load();
                AccountController controller = loader.getController();
                controller.init(account);
                accountNodeMap.put(account, accountNode);
                account.statusProperty().addListener((obs, oldStatus, newStatus) -> {
                    Platform.runLater(() -> {
                        System.out.println("Status updated");
                        listMap.get(oldStatus.getType()).getChildren().remove(accountNode);
                        listMap.get(newStatus.getType()).getChildren().add(accountNode);
                    });
                });

                listMap.get(account.getStatus().getType()).getChildren().add(accountNode);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        isAccountsInserted = true;
    }

    @FXML
    void saveReport(ActionEvent event) {

    }

    @FXML
    void resetServer(ActionEvent event) {
        AccountManagerService.resetManager();
        ConfigurationService.updateManagerState();
        clearLists();
        isAccountsInserted = false;
    }

    public void clearLists() {
        inProgressList.getChildren().clear();
        concludedList.getChildren().clear();
        pendingList.getChildren().clear();
    }


}
