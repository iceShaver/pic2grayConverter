import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXMLLoader;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageConverter {
    BufferedImage originalImage;
    private DoubleProperty progressProperty;

    public ImageConverter(BufferedImage originalImage) {
        this.originalImage = originalImage;
        progressProperty = new SimpleDoubleProperty(0.0);
    }


}
