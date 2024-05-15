package com.github.dougmab.ygibringer.app.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.io.Serial;
import java.io.Serializable;

public class Account implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final String login;
    private final String password;
    private transient ObjectProperty<Status> statusProperty;
    private Status status = Status.pending();

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

    public void setStatus(Status statusProperty) {
        this.statusProperty.set(statusProperty);
    }

    public ObjectProperty<Status> statusProperty() {
        return statusProperty;
    }

    public void syncStatus() {
        statusProperty = new SimpleObjectProperty<>(status);
        statusProperty.addListener((obs, oldValue, newValue) -> {
            status = newValue;
        });
    }

    @Override
    public String toString() {
        return String.format("login: %s%nsenha: %s (%s)%n%n", getLogin(), getPassword(), getStatus().getValue());
    }

}
