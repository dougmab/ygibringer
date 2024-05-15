package com.github.dougmab.ygibringer.app.controller;

import com.github.dougmab.ygibringer.app.model.Account;
import com.github.dougmab.ygibringer.app.model.StatusType;
import com.github.dougmab.ygibringer.app.service.ConfigurationService;
import com.github.dougmab.ygibringer.app.service.NotificationService;
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
import java.util.*;

public class ListController {

    private final Map<StatusType, VBox> listMap = new HashMap<>();
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
        int count = 0;
        for (Iterator<Account> it = AccountManagerService.getIterator(); it.hasNext(); ) {
            Account account = it.next();
            count++;

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/account.fxml"));

            try {
                Node accountNode = loader.load();
                AccountController controller = loader.getController();
                controller.init(account);
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
        totalCount.setText(Integer.toString(count));
    }

    @FXML
    void saveReport(ActionEvent event) {
        boolean isReportSaved = AccountManagerService.getInstance().saveAccountsReport();

        if(isReportSaved) {
            NotificationService.send("Relatório salvo com sucesso");
            return;
        }

        NotificationService.send("Erro ao salvar o relatório");
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
        totalCount.setText("0");
    }

}
