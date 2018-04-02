import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
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
    private Label threadNumberLabel;

    @FXML
    private RadioButton sequentialRadioButton;

    @FXML
    private ToggleGroup processingModeGroup;

    @FXML
    private RadioButton parallelDefaultRadioButton;

    @FXML
    private RadioButton parallelUserRadioButton;

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
        if (config.isSet()) {
            config.setIsSet(false);
            outputDirLabel.setText(config.getOutputDir().getAbsolutePath());
            convertButton.setDisable(false);
        }

    }

    @FXML
    void convertButtonOnAction(ActionEvent event) {
        if (((RadioButton) processingModeGroup.getSelectedToggle()).getId().equals(parallelDefaultRadioButton.getId())) {
            config.setProcessingMode(ProcessingMode.PARALLEL_DEFAULT);
        } else if (((RadioButton) processingModeGroup.getSelectedToggle()).getId().equals(parallelUserRadioButton.getId())) {
            config.setProcessingMode(ProcessingMode.PARALLEL_USER);
        } else {
            config.setProcessingMode(ProcessingMode.SEQUENTIAL);
        }
        config.setThreadsNumber(threadNumberSpinner.getValue());
        config.setIsSet(true);
        ((Stage) convertButton.getScene().getWindow()).close();
    }

    @FXML
    void initialize() {
        assert threadNumberLabel != null : "fx:id=\"threadNumerLabel\" was not injected: check your FXML file 'ConfigWindow.fxml'.";
        assert threadNumberSpinner != null : "fx:id=\"threadNumberSpinner\" was not injected: check your FXML file 'ConfigWindow.fxml'.";
        assert selectOutputDirButton != null : "fx:id=\"selectOutputDirButton\" was not injected: check your FXML file 'ConfigWindow.fxml'.";
        assert outputDirLabel != null : "fx:id=\"outputDirLabel\" was not injected: check your FXML file 'ConfigWindow.fxml'.";
        assert sequentialRadioButton != null : "fx:id=\"sequentialRadioButton\" was not injected: check your FXML file 'ConfigWindow.fxml'.";
        assert processingModeGroup != null : "fx:id=\"processingModeGroup\" was not injected: check your FXML file 'ConfigWindow.fxml'.";
        assert parallelDefaultRadioButton != null : "fx:id=\"parallelDefaultRadioButton\" was not injected: check your FXML file 'ConfigWindow.fxml'.";
        assert parallelUserRadioButton != null : "fx:id=\"parallelUserRadioButton\" was not injected: check your FXML file 'ConfigWindow.fxml'.";
        assert convertButton != null : "fx:id=\"convertButton\" was not injected: check your FXML file 'ConfigWindow.fxml'.";
        threadNumberSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, Integer.MAX_VALUE, Runtime.getRuntime().availableProcessors()));
        processingModeGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (((RadioButton) newValue).getId().equals(parallelUserRadioButton.getId())) {
                threadNumberSpinner.setDisable(false);
                threadNumberLabel.setDisable(false);
            } else {
                threadNumberSpinner.setDisable(true);
                threadNumberLabel.setDisable(true);
            }
        });
    }
}
