package org.schors.lopds.indexer;


public interface WorkObserver {
    public void fileStarted(String filename, long size);
    public void unitProcessed(String fileName, long progress);
    public void fileDone(String filename);
    public void workDone();
}
