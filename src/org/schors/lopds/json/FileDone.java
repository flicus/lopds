package org.schors.lopds.json;

public class FileDone {
    private String fileName;
    private long size;

    public FileDone() {
    }

    public FileDone(String fileName, long size) {
        this.fileName = fileName;
        this.size = size;
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

    @Override
    public String toString() {
        return "FileDone{" +
                "fileName='" + fileName + '\'' +
                ", size=" + size +
                '}';
    }
}
