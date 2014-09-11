package org.schors.lopds.indexer.catalog;

/**
 * Created by flicus on 11.09.14.
 */
public class ChildWrapper {
    private String index;
    private Integer authorId;

    public ChildWrapper(String index, Integer authorId) {
        this.index = index;
        this.authorId = authorId;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public Integer getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Integer authorId) {
        this.authorId = authorId;
    }

    @Override
    public String toString() {
        return "ChildWrapper{" +
                "index='" + index + '\'' +
                ", authorId=" + authorId +
                '}';
    }
}
