import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ForkJoinPool;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.ProgressBarTableCell;
import javafx.stage.*;

public class MainWindowController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TableView<ImageProcessingJob> filesTableView;

    @FXML
    private TableColumn<?, ?> noColumn;

    @FXML
    private TableColumn<ImageProcessingJob, String> filenameColumn;

    @FXML
    private TableColumn<?, ?> typeColumn;

    @FXML
    private TableColumn<?, ?> sizeColumn;

    @FXML
    private TableColumn<ImageProcessingJob, Double> progressColumn;

    @FXML
    private TableColumn<ImageProcessingJob, String> statusColumn;

    @FXML
    private Button selectFilesButton;


    @FXML
    private Button convertButton;
    private ObservableList<ImageProcessingJob> jobs;
    private Config config;

    @FXML
    void convertButtonOnAction(ActionEvent event) {
        Stage configWindowPrimaryStage = new Stage();
        FXMLLoader configWindowLoader = new FXMLLoader(getClass().getResource("ConfigWindow.fxml"));
        ConfigWindowController configWindowController = configWindowLoader.getController();
        Parent configWindowRoot = null;
        try {
            configWindowRoot = configWindowLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene scene = new Scene(configWindowRoot);
        configWindowPrimaryStage.setTitle("Settings");
        configWindowPrimaryStage.setScene(scene);
        configWindowPrimaryStage.initStyle(StageStyle.UTILITY);
        configWindowPrimaryStage.setResizable(false);
        configWindowPrimaryStage.initModality(Modality.APPLICATION_MODAL);
        configWindowController.setConfigObject(config);
        configWindowPrimaryStage.showAndWait();
        if(!config.isConfigSet()){
            return;
        }
        new Thread(this::convert).start();
    }

    @FXML
    void selectFilesButtonOnAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JPG images", "*.jpg"));
        List<File> selectedFiles = fileChooser.showOpenMultipleDialog(((Node)event.getSource()).getScene().getWindow());
        if(selectedFiles == null) {
            return;
        }
        selectedFiles.forEach(file -> jobs.add(new ImageProcessingJob(file)));
    }


    public MainWindowController() {
        this.jobs = null;
        config = new Config();
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
        assert convertButton != null : "fx:id=\"convertButton\" was not injected: check your FXML file 'MainWindow.fxml'.";
        filenameColumn.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getFile().getName()));
        statusColumn.setCellValueFactory(p -> p.getValue().statusProperty());
        progressColumn.setCellFactory(ProgressBarTableCell.<ImageProcessingJob>forTableColumn()); // What's that?
        progressColumn.setCellValueFactory(p->p.getValue().progressProperty().asObject());
        jobs = FXCollections.observableArrayList();
        filesTableView.setItems(jobs);



    }


    void convert(){
        ForkJoinPool pool;
        if(config.getThreadNumber() > 0){
            pool = new ForkJoinPool(config.getThreadNumber());
        }
        jobs.parallelStream().forEach(job -> job.toGrayscale(config.getOutputDir()));
    }
}
