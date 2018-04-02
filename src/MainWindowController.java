import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.ProgressBarTableCell;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.ForkJoinPool;

public class MainWindowController {

    private long processingStartTimeMillis;
    @FXML
    private TableView<ImageProcessingJob> filesTableView;
    @FXML
    private TableColumn<ImageProcessingJob, String> filenameColumn;
    @FXML
    private TableColumn<ImageProcessingJob, Double> progressColumn;
    @FXML
    private TableColumn<ImageProcessingJob, String> statusColumn;
    @FXML
    private Button selectFilesButton;

    @FXML
    private Label statusLabel;
    @FXML
    private Label selectedFilesLabel;
    @FXML
    private Button convertButton;
    private ObservableList<ImageProcessingJob> jobs;
    private Config config;

    public MainWindowController() {
        this.jobs = null;
        config = new Config();

    }

    @FXML
    void selectFilesButtonOnAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JPG images", "*.jpg"));
        List<File> selectedFiles = fileChooser.showOpenMultipleDialog(((Node) event.getSource()).getScene().getWindow());
        if (selectedFiles == null) {
            return;
        }
        jobs.clear();
        selectedFiles.forEach(file -> jobs.add(new ImageProcessingJob(file)));
        selectedFilesLabel.setText(String.valueOf(selectedFiles.size()));
    }

    @FXML
    void convertButtonOnAction(ActionEvent event) {
        Stage configWindowPrimaryStage = new Stage();
        FXMLLoader configWindowLoader = new FXMLLoader(getClass().getResource("ConfigWindow.fxml"));
        try {
            Parent configWindowRoot = configWindowLoader.load();
            ConfigWindowController configWindowController = configWindowLoader.getController();
            Scene scene = new Scene(configWindowRoot);
            configWindowPrimaryStage.setTitle("Settings");
            configWindowPrimaryStage.setScene(scene);
            configWindowPrimaryStage.initStyle(StageStyle.UTILITY);
            configWindowPrimaryStage.setResizable(false);
            configWindowPrimaryStage.initModality(Modality.APPLICATION_MODAL);
            configWindowController.setConfigObject(config);
            configWindowPrimaryStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }


        if (!config.isSet()) {
            return;
        }
        processingStartTimeMillis = System.currentTimeMillis();
        statusLabel.setText("Processing");
        switch (config.getProcessingMode()) {
            case PARALLEL_DEFAULT:
                ForkJoinPool.commonPool().submit(() -> {
                    jobs.parallelStream().forEach(job -> job.saveToGrayscale(config.getOutputDir()));
                    Platform.runLater(this::atProcessingFinish);
                });
                break;
            case PARALLEL_USER:
                new ForkJoinPool(config.getThreadsNumber()).submit(() -> {
                    jobs.parallelStream().forEach(job -> job.saveToGrayscale(config.getOutputDir()));
                    Platform.runLater(this::atProcessingFinish);
                });
                break;
            case SEQUENTIAL:
                ForkJoinPool.commonPool().submit(() -> {
                    jobs.forEach((job) -> job.saveToGrayscale(config.getOutputDir()));
                    Platform.runLater(this::atProcessingFinish);
                });
                break;
        }
    }

    private void atProcessingFinish() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss:SS");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        statusLabel.setText("Finished in: " + sdf.format(System.currentTimeMillis() - processingStartTimeMillis));
    }


    @FXML
    void initialize() {
        assert filesTableView != null : "fx:id=\"filesTableView\" was not injected: check your FXML file 'MainWindow.fxml'.";
        assert filenameColumn != null : "fx:id=\"filenameColumn\" was not injected: check your FXML file 'MainWindow.fxml'.";
        assert progressColumn != null : "fx:id=\"progressColumn\" was not injected: check your FXML file 'MainWindow.fxml'.";
        assert statusColumn != null : "fx:id=\"statusColumn\" was not injected: check your FXML file 'MainWindow.fxml'.";
        assert selectFilesButton != null : "fx:id=\"selectFilesButton\" was not injected: check your FXML file 'MainWindow.fxml'.";
        assert convertButton != null : "fx:id=\"convertButton\" was not injected: check your FXML file 'MainWindow.fxml'.";
        assert statusLabel != null : "fx:id=\"statusLabel\" was not injected: check your FXML file 'MainWindow.fxml'.";
        assert selectedFilesLabel != null : "fx:id=\"selectedFilesLabel\" was not injected: check your FXML file 'MainWindow.fxml'.";
        filenameColumn.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getFile().getName()));
        statusColumn.setCellValueFactory(p -> p.getValue().statusProperty());
        progressColumn.setCellFactory(ProgressBarTableCell.<ImageProcessingJob>forTableColumn()); // What's that?
        progressColumn.setCellValueFactory(p -> p.getValue().progressProperty().asObject());
        jobs = FXCollections.observableArrayList();
        filenameColumn.prefWidthProperty().bind(filesTableView.widthProperty().subtract(20).multiply(0.3));
        statusColumn.prefWidthProperty().bind(filesTableView.widthProperty().subtract(20).multiply(0.1));
        progressColumn.prefWidthProperty().bind(filesTableView.widthProperty().subtract(20).multiply(0.6));
        filesTableView.setItems(jobs);
    }


}
