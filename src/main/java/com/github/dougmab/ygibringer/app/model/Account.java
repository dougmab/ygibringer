package com.github.dougmab.ygibringer.app.model;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

public class Account implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final String login;
    private final String password;
    private transient ObjectProperty<Status> statusProperty;
    private Status status = Status.pending();
    private transient StringProperty managerProperty;
    private String manager;

    public Account(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.statusProperty.set(status);
        this.status = status;
    }

    public ObjectProperty<Status> statusProperty() {
        return statusProperty;
    }

    public void setManager(String manager) {
        this.manager = manager;
        Platform.runLater(() -> {
        managerProperty.set(manager);
        });
    }

    public String getManager() {
        return manager;
    }

    public StringProperty managerProperty() {
        return managerProperty;
    }

    public void syncProperties() {
        statusProperty = new SimpleObjectProperty<>(status);
        managerProperty = new SimpleStringProperty();
//        statusProperty.addListener((obs, oldValue, newValue) -> {
//            status = newValue;
//        });
        managerProperty.set(manager);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Objects.equals(login, account.login) && Objects.equals(password, account.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(login, password);
    }

    @Override
    public String toString() {
        return String.format("login: %s%nsenha: %s (%s)%n%n", getLogin(), getPassword(), getStatus().getValue());
    }

}
