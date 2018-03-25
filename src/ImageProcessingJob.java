import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class ImageProcessingJob {
    private File file;
    private SimpleStringProperty status;
    private DoubleProperty progress;

    public ImageProcessingJob(File file) {
        this.file = file;
        this.status = new SimpleStringProperty(FileStatus.WAITING.toString());
        this.progress = new SimpleDoubleProperty(0.0);
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getStatus() {
        return status.get();
    }

    public SimpleStringProperty statusProperty() {
        return status;
    }

    public void setStatus(String status) {
        this.status.set(status);
    }

    public double getProgress() {
        return progress.get();
    }

    public DoubleProperty progressProperty() {
        return progress;
    }

    public void setProgress(double progress) {
        this.progress.set(progress);
    }

    public void toGrayscale(File outputDir) {
        BufferedImage originalImage = null;
        try {
            originalImage = ImageIO.read(file);
            BufferedImage resultImage = new BufferedImage(
                    originalImage.getWidth(),
                    originalImage.getHeight(),
                    originalImage.getType());
            for (int i = 0; i < originalImage.getWidth(); i++) {
                for (int j = 0; j < originalImage.getHeight(); j++) {
                    Color color = new Color(originalImage.getRGB(i, j));
                    int luminosity = (int) (0.21 * color.getRed() + 0.71 * color.getGreen() + 0.07 * color.getBlue());
                    resultImage.setRGB(i, j, new Color(luminosity, luminosity, luminosity).getRGB());
                }
                double progress = (1.0 + i) / originalImage.getWidth();
                Platform.runLater(()-> this.progress.set(progress));
            }
            Path outputPath = Paths.get(outputDir.getAbsolutePath(), file.getName());
            ImageIO.write(resultImage, "jpg", outputPath.toFile());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}