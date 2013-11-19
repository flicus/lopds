package org.schors.lopds.json;

public class FileInProgress {
    private String fileName;
    private long size;
    private long processed;

    public FileInProgress() {
    }

    public FileInProgress(String fileName, long size, long processed) {
        this.fileName = fileName;
        this.size = size;
        this.processed = processed;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public long getProcessed() {
        return processed;
    }

    public void setProcessed(long processed) {
        this.processed = processed;
    }

    @Override
    public String toString() {
        return "FileInProgress{" +
                "fileName='" + fileName + '\'' +
                ", size=" + size +
                ", processed=" + processed +
                '}';
    }
}
