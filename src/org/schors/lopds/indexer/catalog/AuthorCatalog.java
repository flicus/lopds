package org.schors.lopds.indexer.catalog;

import org.schors.lopds.model.Book;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: flicus
 * Date: 04.08.13
 * Time: 21:09
 * To change this template use File | Settings | File Templates.
 */
public class AuthorCatalog implements CatalogManager {
    public final static int MAX_PAGE_SIZE = 3;
    private Map<String, BasicEntry> roots = new HashMap<>();
    private Map<Integer, List<BasicEntry>> levels = new HashMap<>();

    public void indexAuthor(String lastName, Integer authorId) {
        String nodeIndex = lastName.toLowerCase().substring(0, 1);
        if (lastName.length() > 1) {
            String remains = lastName.toLowerCase().substring(1, lastName.length());
            BasicEntry node = roots.get(nodeIndex);
            if (node == null) {
                node = new NodeEntry();
                roots.put(nodeIndex, node);
            }
            ((NodeEntry)node).indexEntry(remains, authorId);
        } else {
            BasicEntry node = roots.get(nodeIndex);
            if (node == null) {
                node = new NodeEntry();
                roots.put(nodeIndex, node);
            }
            LeafEntry entry = new LeafEntry(authorId);
            ((NodeEntry)node).addChild(entry);
        }
    }

    @Override
    public Map<Integer, List<NodeEntry>> getLevels() {
        return null;
    }


}
