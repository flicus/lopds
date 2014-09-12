package org.schors.lopds.indexer.catalog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by flicus on 12.09.14.
 */
public abstract class CatalogItem {
    public final static int MAX_PAGE_SIZE = 3;
    protected List<LeafEntry> leafs = new ArrayList<>();
    protected Map<String, NodeEntry> branches = new HashMap<>();
    protected long count = 0;

    public Map<String, NodeEntry> getNodes() {
        return branches;
    }

    public List<LeafEntry> getLeafs() {
        return leafs;
    }

    public long getCount() {
        return count;
    }

    protected NodeEntry getOrCreateNode(String index) {
        NodeEntry node = branches.get(index);
        if (node == null) {
            node = new NodeEntry();
            branches.put(index, node);
        }
        return node;
    }

    public void finalize() {
        if (branches.size() > 0) {
            for (NodeEntry entry : branches.values()) {
                entry.finalize();
            }
        }
    }

    public abstract void indexEntry(String lastName, Integer authorId);

}
