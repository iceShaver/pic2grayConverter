import java.io.File;

public class Config {
    private int threadNumber;
    private File outputDir;
    private boolean isConfigSet;

    public int getThreadNumber() {
        return threadNumber;
    }

    public void setThreadNumber(int threadNumber) {
        this.threadNumber = threadNumber;
    }

    public File getOutputDir() {
        return outputDir;
    }

    public void setOutputDir(File outputDir) {
        this.outputDir = outputDir;
    }

    public boolean isConfigSet() {
        return isConfigSet;
    }

    public void setConfigSet(boolean configSet) {
        isConfigSet = configSet;
    }
}
