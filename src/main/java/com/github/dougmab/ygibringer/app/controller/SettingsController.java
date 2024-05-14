package com.github.dougmab.ygibringer.app.controller;

import com.github.dougmab.ygibringer.app.service.ConfigurationService;
import com.github.dougmab.ygibringer.server.model.ConfigurationProperties;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.nio.file.Path;

public class SettingsController {

    @FXML
    private Button addStatusBtn;

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

    private File inputFile;

    @FXML
    public void initialize() {
        ConfigurationProperties configs = ConfigurationService.get();

        if (configs == null) return;

        inputFile = configs.inputFile;
        chooseInputBtn.setText(inputFile.getName());

        outputSrcField.setText(Path.of(configs.outputFileName).getFileName().toString());
        errorSrcField.setText(Path.of(configs.errorFileName).getFileName().toString());
        regexStrField.setText(configs.regexStr);

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
        ConfigurationService.setConfig(inputFile, outputSrcField.getText(), errorSrcField.getText(), regexStrField.getText());
    }

}
