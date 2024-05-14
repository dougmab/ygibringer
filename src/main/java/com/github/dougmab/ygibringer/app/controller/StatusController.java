package com.github.dougmab.ygibringer.app.controller;

import com.github.dougmab.ygibringer.app.model.Status;
import com.github.dougmab.ygibringer.app.model.StatusType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.util.function.Consumer;

public class StatusController {

    ObservableList<StatusType> typeList = FXCollections.observableArrayList(StatusType.ERROR, StatusType.SUCCESS);

    @FXML
    private GridPane root;

    @FXML
    private TextField titleField;

    @FXML
    private ChoiceBox<StatusType> typeChoice;

    @FXML
    private TextField valueField;

    private Status status = new Status();
    public Consumer<Status> deleteConsumer;

    @FXML
    public void initialize() {
        typeChoice.setItems(typeList);

        titleField.textProperty().addListener((obs, oldValue, newValue) -> {
            status.setTitle(newValue);
        });
        valueField.textProperty().addListener((obs, old, newValue) -> {
            status.setValue(newValue);
        });
        typeChoice.valueProperty().addListener((obs, old, newValue) -> {
            status.setType(newValue);
        });
    }

    public void init(Status status, Consumer<Status> delete) {
        this.status = status;
        titleField.setText(status.getTitle());
        valueField.setText(status.getValue());
        typeChoice.setValue(status.getType());
        deleteConsumer = delete;
    }

    public void setConsumers(Consumer<Status> add, Consumer<Status> delete) {
        add.accept(status);
        deleteConsumer = delete;
    }

    @FXML
    void delete(ActionEvent event) {
        deleteConsumer.accept(status);
        VBox parent = (VBox) root.getParent();
        parent.getChildren().remove(root);
    }

}
