public enum FileStatus {
    WAITING("Waiting"),
    PROCESSING("Processing"),
    FINISHED("Finished");

    private String value;

    FileStatus(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
