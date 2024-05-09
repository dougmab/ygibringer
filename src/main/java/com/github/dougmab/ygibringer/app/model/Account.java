package com.github.dougmab.ygibringer.app.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Account {
    private final String login;
    private final String password;
    private final StringProperty status = new SimpleStringProperty();

    public Account(String login, String password) {
        this.login = login;
        this.password = password;
        status.set("PENDENTE");
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getStatus() {
        return status.get();
    }

    public void setStatus(String status) {
        this.status.set(status);
    }

    public StringProperty statusProperty() {
        return status;
    }

    @Override
    public String toString() {
        return String.format("login: %s%nsenha: %s (%s)%n%n", getLogin(), getPassword(), getStatus());
    }
}
