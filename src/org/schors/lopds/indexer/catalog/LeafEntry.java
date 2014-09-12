package org.schors.lopds.indexer.catalog;

/**
 * Created with IntelliJ IDEA.
 * User: flicus
 * Date: 04.08.13
 * Time: 21:15
 * To change this template use File | Settings | File Templates.
 */
public class LeafEntry {

    private Integer authorId;

    public LeafEntry(Integer authorId) {
        this.authorId = authorId;
    }

    public Integer getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Integer authorId) {
        this.authorId = authorId;
    }

    @Override
    public String toString() {
        return "LeafEntry{" +
                "authorId=" + authorId +
                '}';
    }
}
