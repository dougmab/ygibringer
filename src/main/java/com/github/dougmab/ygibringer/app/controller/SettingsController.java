package com.github.dougmab.ygibringer.app.controller;

import com.github.dougmab.ygibringer.app.model.Status;
import com.github.dougmab.ygibringer.app.service.ConfigurationService;
import com.github.dougmab.ygibringer.app.service.NotificationService;
import com.github.dougmab.ygibringer.server.model.Configuration;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class SettingsController {

    @FXML
    private Button chooseInputBtn;

    @FXML
    private TextField errorSrcField;

    @FXML
    private TextField outputSrcField;

    @FXML
    private TextArea regexStrField;

    @FXML
    private Button saveBtn;

    @FXML
    private VBox statusList;

    private List<Status> customStatus = new ArrayList<>();
    private final Consumer<Status> addToStatusList = customStatus::add;
    private final Consumer<Status> removeFromStatusList = customStatus::remove;

    private File inputFile;

    @FXML
    public void initialize() {
        Configuration configs = ConfigurationService.getConfig();

        if (configs == null) return;

        inputFile = configs.inputFile;
        chooseInputBtn.setText(inputFile.getName());

        outputSrcField.setText(Path.of(configs.outputFileName).getFileName().toString());
        errorSrcField.setText(Path.of(configs.errorFileName).getFileName().toString());
        regexStrField.setText(configs.regexStr);

        // Loads saved status into nodes
        if (configs.customStatus != null) {
            for (Status status : configs.customStatus) {
                customStatus.add(status);
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/status.fxml"));
                try {
                    Node statusNode = loader.load();
                    StatusController controller = loader.getController();
                    controller.init(status, removeFromStatusList);
                    statusList.getChildren().add(statusNode);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        System.out.println(configs);
    }

    @FXML
    void chooseInputFile() {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Arquivo de Texto (.txt)","*.txt"));
        inputFile = fc.showOpenDialog(new Stage());

        if (inputFile == null) return;

        chooseInputBtn.setText(inputFile.getName());
    }

    @FXML
    void saveOptions(ActionEvent event) {
        ConfigurationService.setConfig(inputFile, outputSrcField.getText(), errorSrcField.getText(), regexStrField.getText(), customStatus);
        NotificationService.send("Configurações salvas com sucesso");
    }

    @FXML
    void addNewStatus(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/status.fxml"));
        try {
            // Loads status component
            Node statusNode = loader.load();
            StatusController controller = loader.getController();
            controller.setConsumers(addToStatusList, removeFromStatusList);
            // inserts component
            statusList.getChildren().add(statusNode);
            System.out.println(customStatus);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
