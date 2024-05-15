package com.github.dougmab.ygibringer.app.service;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.util.Timer;
import java.util.TimerTask;

public class NotificationService {
    private static VBox messageList;

    public static void setMessageDisplay(VBox messageList) {
        NotificationService.messageList = messageList;
    }

    public static void send(String message) {
        Label messageLabel = new Label(message);
        messageLabel.getStyleClass().add("notification-display");

        Timeline timeline = new Timeline();
        timeline.getKeyFrames().addAll(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(messageLabel.opacityProperty(), 0)
                ),
                new KeyFrame(Duration.seconds(0.2),
                        new KeyValue(messageLabel.opacityProperty(), 1)
                ),
                new KeyFrame(Duration.seconds(3),
                        new KeyValue(messageLabel.opacityProperty(), 1)
                ),
                new KeyFrame(Duration.seconds(5),
                        new KeyValue(messageLabel.opacityProperty(), 0)
                )
        );

        messageList.getChildren().add(messageLabel);

        timeline.play();

        Timer timer = new Timer(true);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                expireLabel(messageLabel);
            }
        }, 5000);
    }

    private static void expireLabel(Label messageLabel) {
        Platform.runLater(() -> {
            messageList.getChildren().remove(messageLabel);
        });
    }
}
