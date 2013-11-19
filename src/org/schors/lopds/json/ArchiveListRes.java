package org.schors.lopds.json;

import java.util.List;

public class ArchiveListRes extends Response {
    private List<ArchiveRecord> archiveList;

    public ArchiveListRes() {
    }

    public ArchiveListRes(List<ArchiveRecord> archiveList) {
        this.archiveList = archiveList;
    }

    public List<ArchiveRecord> getArchiveList() {
        return archiveList;
    }

    public void setArchiveList(List<ArchiveRecord> archiveList) {
        this.archiveList = archiveList;
    }

    @Override
    public String toString() {
        return "ArchiveListRes{" +
                "archiveList=" + archiveList +
                '}';
    }
}
