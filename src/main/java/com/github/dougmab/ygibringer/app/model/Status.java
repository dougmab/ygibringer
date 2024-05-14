package com.github.dougmab.ygibringer.app.model;

import java.io.Serial;
import java.io.Serializable;

public class Status implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private static final Status pending = new Status("Pendente", "PENDENTE", StatusType.PENDING);
    private static final Status managing = new Status("Em Progresso", "EM PROGRESSO", StatusType.MANAGING);

    private String title;
    private String value;
    private StatusType type;

    public Status(String title, String value, StatusType type) {
        this.title = title;
        this.value = value;
        this.type = type;
    }

    public Status() {}

    public String getTitle() {
        return title;
    }

    public String getValue() {
        return value;
    }

    public StatusType getType() {
        return type;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setValue(String value) {
            this.value = value;
        }

    public void setType(StatusType type) {
            this.type = type;
        }

    public static Status pending() {
        return pending;
    }

    public static Status managing() {
        return managing;
    }

    @Override
    public String toString() {
        return String.format("""
                Status=(
                    title=%s
                    value=%s
                    type=%s
                )""", getTitle(), getValue(), getType());
    }
}
