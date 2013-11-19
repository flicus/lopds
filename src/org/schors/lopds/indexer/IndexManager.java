package org.schors.lopds.indexer;

import org.schors.lopds.Configuration;
import org.schors.lopds.json.ArchiveInfoRes;
import org.schors.lopds.json.ArchiveRecord;
import org.schors.lopds.json.FileDone;
import org.schors.lopds.json.FileInProgress;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class IndexManager {

    private LibraryTraverser traverser = new LibraryTraverserImpl();
    private LibraryIndexer indexer = new LibraryIndexer(traverser);
    private ReentrantLock stateLock = new ReentrantLock();
    private ReentrantLock progressLock = new ReentrantLock();

    private List<FileDone> filesDone = new ArrayList<>();
    private FileInProgress currentRecord = null;
    private int done = 0;

    private State state = State.ready;

    public enum State {
        ready,
        opened,
        inprocess
    }

    public IndexManager() {

    }

    public String openArchive(List<ArchiveRecord> list) {
        String result = null;
        Configuration cfg = Configuration.getInstance();
        if (cfg.getArchiveDirectory() == null || "".equals(cfg.getArchiveDirectory())) {
            return "Invalid archive directory";
        }

        //stateLock.lock();
        try {
            if (state.equals(State.inprocess)) {
                return "Already processing archive";
            }
            File[] files = traverser.init(new File(cfg.getArchiveDirectory()), new IndexWorkObserver());
            if (files != null) {
                for (File file : files) {
                    ArchiveRecord ar = new ArchiveRecord(file.getName(), file.length());
                    list.add(ar);
                }
            }
            state = State.opened;
        } catch (Exception e) {
            result = e.getMessage();
        } finally {
          //  stateLock.unlock();
        }
        return result;
    }

    public String startIndexing() {
        String result = null;
        Configuration cfg = Configuration.getInstance();
        if (cfg.getIndexDirectory() == null || "".equals(cfg.getIndexDirectory())) {
            return "Invalid index file path";
        }

        //stateLock.lock();
        try {
            if (!state.equals(State.opened)) {
                return String.format("Invalid state: %s", state.name());
            }
            filesDone.clear();
            done = 0;
            indexer.setPath2index(new File(cfg.getIndexDirectory()));
            indexer.index();
            state = State.inprocess;
        } catch (Exception e) {
            result = e.getMessage();
        } finally {
          //  stateLock.unlock();
        }
        return result;
    }

    public String getStatus() {
        //stateLock.lock();
        try {
            return state.name();
        } finally {
          //  stateLock.unlock();
        }
    }

    public String getProgress(ArchiveInfoRes res) {
        //stateLock.lock();
//        try {
//            if (!state.equals(State.inprocess))
//                return "Not processing anything";
//        } finally {
//          //  stateLock.unlock();
//        }

        //progressLock.lock();
        try {
            res.setWorkDone(done);
            res.getFilesDone().addAll(filesDone);
            filesDone.clear();
            if (currentRecord != null) {
                res.getFileInProgress().setFileName(currentRecord.getFileName());
                res.getFileInProgress().setSize(currentRecord.getSize());
                res.getFileInProgress().setProcessed(currentRecord.getProcessed());
            }
        } finally {
          //  progressLock.unlock();
        }
        return null;
    }

    private static class Singleton {
        public static IndexManager singeton = new IndexManager();
    }

    public static IndexManager getInstanse() {
        return Singleton.singeton;
    }

    private class IndexWorkObserver implements WorkObserver {

        @Override
        public void fileStarted(String filename, long size) {
            //progressLock.lock();
            try {
                File f = new File(filename);
                currentRecord = new FileInProgress(f.getName(), size, 0);
            } finally {
              //  progressLock.unlock();
            }
        }

        @Override
        public void unitProcessed(String fileName, long progress) {
            //progressLock.lock();
            try {
                currentRecord.setProcessed(progress);
            } finally {
              //  progressLock.unlock();
            }
        }

        @Override
        public void fileDone(String filename) {
            //progressLock.lock();
            try {
                File f = new File(filename);
                filesDone.add(new FileDone(f.getName(), currentRecord.getSize()));
                currentRecord = null;
            } finally {
              //  progressLock.unlock();
            }
        }

        @Override
        public void workDone() {
            done = 1;
            state = State.ready;
        }
    }
}
