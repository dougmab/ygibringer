package com.github.dougmab.ygibringer.app.controller;

import com.github.dougmab.ygibringer.Server;
import com.github.dougmab.ygibringer.app.model.Account;
import com.github.dougmab.ygibringer.app.service.ConfigurationService;
import com.github.dougmab.ygibringer.server.model.ConfigurationProperties;
import com.github.dougmab.ygibringer.server.service.AccountManagerService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.Objects;

public class PanelController {

    @FXML
    private VBox accountHistory;

    @FXML
    private TextField errorSrcField;

    @FXML
    private Button inputSelector;

    @FXML
    private TextField outputSrcField;

    @FXML
    private TextArea regexSrcField;

    @FXML
    private ImageView consoleIcon;

    @FXML
    private Button serverButton;

    private File inputFile;

    @FXML
    public void initialize() {
        ConfigurationProperties configs = ConfigurationService.get();

        if (configs == null) return;

        inputFile = configs.inputFile;
        inputSelector.setText(inputFile.getName());

        outputSrcField.setText(Path.of(configs.outputFileName).getFileName().toString());
        errorSrcField.setText(Path.of(configs.errorFileName).getFileName().toString());
        regexSrcField.setText(configs.regexStr);

        System.out.println(configs);
    }

    @FXML
    void OpenConsole(MouseEvent event) {

    }

    @FXML
    void selectInput(ActionEvent event) {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Arquivo de Texto (.txt)","*.txt"));
        inputFile = fc.showOpenDialog(new Stage());

        if (inputFile == null) return;

        inputSelector.setText(inputFile.getName());
    }

    @FXML
    void toggleServer(ActionEvent event) {
        if (inputFile == null) {
            System.out.println("Input is null");
            return;
        }

        var styleClasses = serverButton.getStyleClass();

        if (Server.isRunning()) {
            styleClasses.remove("button-sh");
            styleClasses.add("button-st");
            serverButton.setText("Iniciar");

            Server.shutdown();
            return;
        }

        accountHistory.getChildren().clear();

        ConfigurationService.setConfig(inputFile, outputSrcField.getText(), errorSrcField.getText(), regexSrcField.getText());

        styleClasses.remove("button-st");
        styleClasses.add("button-sh");
        serverButton.setText("Terminar");

        Server.start(new String[0]);

        try {
            populateHistory();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void populateHistory() throws IOException {
        int index = 0;
        for (Iterator<Account> it = AccountManagerService.getIterator(); it.hasNext(); ) {
            Account account = it.next();

            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/view/account.fxml")));
            Node component = loader.load();
            AccountController controller = loader.getController();

            controller.init(account, ++index);

            accountHistory.getChildren().add(component);
        }
    }

}
