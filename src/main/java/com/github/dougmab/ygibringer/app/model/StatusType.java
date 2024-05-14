package com.github.dougmab.ygibringer.app.model;

public enum StatusType {
    PENDING("pending-color", "pending.png"),
    MANAGING("progress-color", "progress.png"),
    SUCCESS("success-color", "success.png"),
    ERROR("error-color", "error.png");

    private final String cssClass;
    private final String iconPath;

    StatusType(String cssClass, String iconPath) {
        this.cssClass = cssClass;
        this.iconPath = iconPath;
    }

    public String getCssClass() {
        return cssClass;
    }

    public String getIconName() {
        return iconPath;
    }
}
