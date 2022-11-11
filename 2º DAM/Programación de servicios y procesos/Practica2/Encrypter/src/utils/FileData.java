package utils;

public class FileData {
    private String fileName;
    private String path;

    public FileData(String fileName, String path) {
        this.fileName = fileName;
        this.path = path;
    }

    public String getFileName() {
        return fileName;
    }

    public String getPath() {
        return path;
    }

    @Override
    public String toString() {
        return this.fileName;
    }
}
