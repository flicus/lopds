package org.schors.lopds.indexer;

import java.io.File;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.Queue;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ZipFileList {

    private Queue<File> fileList;
    private ZipFile currentZipFile;
    private ZipEntryList entryList;

    private WorkObserver observer;
    private long progress = 1;
    private boolean firstFile = true;

    public ZipFileList(File[] fileList, WorkObserver observer) {
        this.fileList = new LinkedList<File>();
        this.observer = observer;
        for (File item : fileList) {
            this.fileList.add(item);
        }

        currentZipFile = nextZip();
        if (currentZipFile != null) entryList = new ZipEntryList(currentZipFile);
    }

    public ZipFile getCurrentZipFile() {
        return currentZipFile;
    }

    public ZipEntry nextZipEntry() {
        if (firstFile) {
            firstFile = false;
            observer.fileStarted(currentZipFile.getName(), currentZipFile.size());
        }

        ZipEntry zipEntry = null;

        if (currentZipFile != null) {
            while (zipEntry == null && currentZipFile != null) {
                while (zipEntry == null && entryList.hasMore()) {
                    zipEntry = entryList.nextEntry();
                    observer.unitProcessed(currentZipFile.getName(), progress++);
                }
                if (zipEntry == null) {
                    observer.fileDone(currentZipFile.getName());
                    progress = 1;
                    currentZipFile = nextZip();
                    if (currentZipFile != null) {
                        observer.fileStarted(currentZipFile.getName(), currentZipFile.size());
                        entryList = new ZipEntryList(currentZipFile);
                    }
                }
            }
        }

        if (zipEntry == null) observer.workDone();

        return zipEntry;
    }

    private ZipFile nextZip() {
        ZipFile zipFile = null;
        while (zipFile == null && fileList.size() > 0) {
            File file = fileList.remove();
            try {
                zipFile = new ZipFile(file);
            } catch (Exception e) {

            }
        }
        return zipFile;
    }


    private class ZipEntryList {

        private Enumeration<? extends ZipEntry> entries;

        public ZipEntryList(ZipFile parent) {
            this.entries = parent.entries();
        }

        public boolean hasMore() {
            return entries.hasMoreElements();
        }

        public ZipEntry nextEntry() {
            ZipEntry entry = null;
            if (entries.hasMoreElements()) {
                entry = entries.nextElement();
                while (entry != null && entry.isDirectory()) entry = entries.nextElement();
            }

            return entry;
        }
    }
}