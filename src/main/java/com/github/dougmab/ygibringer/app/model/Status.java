package com.github.dougmab.ygibringer.app.model;

import java.util.ArrayList;
import java.util.List;

public class Status {
    private static final List<Status> customStatus = new ArrayList<>();
    private static final Status pending = new Status("Pendente", "PENDENTE", StatusType.PENDING);
    private static final Status managing = new Status("Em Progresso", "EM PROGRESSO", StatusType.MANAGING);

    private final String title;
    private final String value;
    private final StatusType type;

    public Status(String title, String value, StatusType type) {
        this.title = title;
        this.value = value;
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public String getValue() {
        return value;
    }

    public StatusType getType() {
        return type;
    }

    public static Status pending() {
        return pending;
    }

    public static Status managing() {
        return managing;
    }

    public static Status getStatusByIndex(int statusIndex) {
        return customStatus.get(statusIndex);
    }
}
