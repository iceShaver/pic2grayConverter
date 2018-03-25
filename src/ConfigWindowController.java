import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;

public class ConfigWindowController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Spinner<?> threadNumberSpinner;

    @FXML
    private Button selectOutputDirButton;
    private Config config;

    @FXML
    void selectOutputDirButtonOnAction(ActionEvent event) {

    }
    void setConfigObject(Config config){
        this.config = config;
    }
    @FXML
    void initialize() {
        assert threadNumberSpinner != null : "fx:id=\"threadNumberSpinner\" was not injected: check your FXML file 'ConfigWindow.fxml'.";
        assert selectOutputDirButton != null : "fx:id=\"selectOutputDirButton\" was not injected: check your FXML file 'ConfigWindow.fxml'.";

    }
}
