package com.github.dougmab.ygibringer.app.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class Account {
    private final String login;
    private final String password;
    private ObjectProperty<Status> status = new SimpleObjectProperty<>(Status.pending());

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
        return status.get();
    }

    public void setStatus(Status status) {
        this.status.set(status);
    }

    public ObjectProperty<Status> statusProperty() {
        return status;
    }

    @Override
    public String toString() {
        return String.format("login: %s%nsenha: %s (%s)%n%n", getLogin(), getPassword(), getStatus());
    }

}
