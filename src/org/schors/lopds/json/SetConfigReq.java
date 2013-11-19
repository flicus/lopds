package org.schors.lopds.json;

public class SetConfigReq {

    private String archiveDirectory;
    private String indexFile;

    public SetConfigReq() {
    }

    public SetConfigReq(String archiveDirectory, String indexFile) {
        this.archiveDirectory = archiveDirectory;
        this.indexFile = indexFile;
    }

    public String getArchiveDirectory() {
        return archiveDirectory;
    }

    public void setArchiveDirectory(String archiveDirectory) {
        this.archiveDirectory = archiveDirectory;
    }

    public String getIndexFile() {
        return indexFile;
    }

    public void setIndexFile(String indexFile) {
        this.indexFile = indexFile;
    }

    @Override
    public String toString() {
        return "SetConfigReq{" +
                "archiveDirectory='" + archiveDirectory + '\'' +
                ", indexFile='" + indexFile + '\'' +
                '}';
    }
}
