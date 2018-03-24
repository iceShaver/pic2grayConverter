import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

public class MainWindowController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TableView<?> filesTableView;

    @FXML
    private TableColumn<?, ?> noColumn;

    @FXML
    private TableColumn<?, ?> filenameColumn;

    @FXML
    private TableColumn<?, ?> typeColumn;

    @FXML
    private TableColumn<?, ?> sizeColumn;

    @FXML
    private TableColumn<?, ?> progressColumn;

    @FXML
    private TableColumn<?, ?> statusColumn;

    @FXML
    private Button selectFilesButton;

    @FXML
    private Button selectTargetDirectoryButton;

    @FXML
    private Button convertButton;

    @FXML
    private Label outputDirectoryLabel;
    private File targetDirectory;

    @FXML
    void convertButtonOnAction(ActionEvent event) {
        if(targetDirectory == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("You have to specify output directory first");
            alert.setTitle("Output dir is not specified");
            alert.show();
        }

    }

    @FXML
    void selectFilesButtonOnAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JPG images", "*.jpg"));
        List<File> selectedFile = fileChooser.showOpenMultipleDialog(null);
    }

    @FXML
    void selectTargetDirectoryButtonOnAction(ActionEvent event) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        targetDirectory = directoryChooser.showDialog(null);
        outputDirectoryLabel.setText(targetDirectory == null ? "Unspecified output dir" : targetDirectory.getPath());
    }

    @FXML
    void initialize() {
        assert filesTableView != null : "fx:id=\"filesTableView\" was not injected: check your FXML file 'MainWindow.fxml'.";
        assert noColumn != null : "fx:id=\"noColumn\" was not injected: check your FXML file 'MainWindow.fxml'.";
        assert filenameColumn != null : "fx:id=\"filenameColumn\" was not injected: check your FXML file 'MainWindow.fxml'.";
        assert typeColumn != null : "fx:id=\"typeColumn\" was not injected: check your FXML file 'MainWindow.fxml'.";
        assert sizeColumn != null : "fx:id=\"sizeColumn\" was not injected: check your FXML file 'MainWindow.fxml'.";
        assert progressColumn != null : "fx:id=\"progressColumn\" was not injected: check your FXML file 'MainWindow.fxml'.";
        assert statusColumn != null : "fx:id=\"statusColumn\" was not injected: check your FXML file 'MainWindow.fxml'.";
        assert selectFilesButton != null : "fx:id=\"selectFilesButton\" was not injected: check your FXML file 'MainWindow.fxml'.";
        assert selectTargetDirectoryButton != null : "fx:id=\"selectTargetDirectoryButton\" was not injected: check your FXML file 'MainWindow.fxml'.";
        assert convertButton != null : "fx:id=\"convertButton\" was not injected: check your FXML file 'MainWindow.fxml'.";
        assert outputDirectoryLabel != null : "fx:id=\"outputDirectoryLabel\" was not injected: check your FXML file 'MainWindow.fxml'.";

    }
}
