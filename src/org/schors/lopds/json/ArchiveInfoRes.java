package org.schors.lopds.json;

import java.util.ArrayList;
import java.util.List;

public class ArchiveInfoRes extends Response {
    private List<FileDone> filesDone = new ArrayList<>();
    private FileInProgress fileInProgress = new FileInProgress();
    private int workDone = 0;

    public ArchiveInfoRes() {
    }

    public ArchiveInfoRes(List<FileDone> filesDone, FileInProgress fileInProgress) {
        this.filesDone = filesDone;
        this.fileInProgress = fileInProgress;
    }

    public List<FileDone> getFilesDone() {
        return filesDone;
    }

    public void setFilesDone(List<FileDone> filesDone) {
        this.filesDone = filesDone;
    }

    public FileInProgress getFileInProgress() {
        return fileInProgress;
    }

    public void setFileInProgress(FileInProgress fileInProgress) {
        this.fileInProgress = fileInProgress;
    }

    public int getWorkDone() {
        return workDone;
    }

    public void setWorkDone(int workDone) {
        this.workDone = workDone;
    }

    @Override
    public String toString() {
        return "ArchiveInfoRes{" +
                "filesDone=" + filesDone +
                ", fileInProgress=" + fileInProgress +
                ", workDone=" + workDone +
                '}';
    }
}
