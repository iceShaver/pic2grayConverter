import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

public class ConfigWindowController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Spinner<Integer> threadNumberSpinner;

    @FXML
    private Button selectOutputDirButton;

    @FXML
    private Label outputDirLabel;

    @FXML
    private Button convertButton;

    private Config config;

    @FXML
    void selectOutputDirButtonOnAction(ActionEvent event) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File file = directoryChooser.showDialog(((Node) event.getSource()).getScene().getWindow());
        if (file == null) {
            return;
        }
        outputDirLabel.setText(file.getAbsolutePath());
        config.setOutputDir(file);
        convertButton.setDisable(false);
    }

    void setConfigObject(Config config) {
        this.config = config;
    }

    @FXML
    void convertButtonOnAction(ActionEvent event){
        config.setThreadNumber(threadNumberSpinner.getValue());
        ((Stage)convertButton.getScene().getWindow()).close();
    }

    @FXML
    void initialize() {
        assert threadNumberSpinner != null : "fx:id=\"threadNumberSpinner\" was not injected: check your FXML file 'ConfigWindow.fxml'.";
        assert selectOutputDirButton != null : "fx:id=\"selectOutputDirButton\" was not injected: check your FXML file 'ConfigWindow.fxml'.";
        assert outputDirLabel != null : "fx:id=\"outputDirLabel\" was not injected: check your FXML file 'ConfigWindow.fxml'.";
        threadNumberSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, Integer.MAX_VALUE, 8));

    }
}
