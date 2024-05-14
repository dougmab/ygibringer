package com.github.dougmab.ygibringer.app.controller;

import com.github.dougmab.ygibringer.app.model.Account;
import com.github.dougmab.ygibringer.app.model.StatusType;
import com.github.dougmab.ygibringer.server.service.AccountManagerService;
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
    }

    public void insertAccounts() {
        inProgressList.getChildren().clear();
        concludedList.getChildren().clear();
        pendingList.getChildren().clear();

        for (Iterator<Account> it = AccountManagerService.getIterator(); it.hasNext(); ) {
            Account account = it.next();
            System.out.println(account);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/account.fxml"));

            try {
                Node accountNode = loader.load();
                AccountController controller = loader.getController();
                controller.init(account);

                account.statusProperty().addListener((obs, oldStatus, newStatus) -> {
                    listMap.get(oldStatus.getType()).getChildren().remove(accountNode);
                    listMap.get(newStatus.getType()).getChildren().add(accountNode);
                });

                listMap.get(account.getStatus().getType()).getChildren().add(accountNode);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
            System.out.println("------------");
    }

    @FXML
    void saveReport(ActionEvent event) {

    }

}
