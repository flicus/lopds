package org.schors.lopds.indexer.catalog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: flicus
 * Date: 04.08.13
 * Time: 21:15
 * To change this template use File | Settings | File Templates.
 */
public class NodeEntry extends CatalogItem {
    private List<ChildWrapper> children = new ArrayList<>();

    public void indexEntry(String name, Integer authorId) {
        if (name.length() >= 1) {
            if (children != null) {
                ChildWrapper wrapper = new ChildWrapper(name, authorId);
                children.add(wrapper);
                if (children.size() > AuthorCatalog.MAX_PAGE_SIZE) {
                    String childIndex = null;
                    String childRemains = null;
                    for (ChildWrapper item : children) {
                        childIndex = item.getIndex().toLowerCase().substring(0, 1);
                        childRemains = item.getIndex().toLowerCase().substring(1, item.getIndex().length());
                        NodeEntry node = getOrCreateNode(childIndex);
                        node.indexEntry(childRemains, item.getAuthorId());
                    }
                    children.clear();
                    children = null;
                }
            } else {
                String nodeIndex = name.toLowerCase().substring(0, 1);
                String remains = name.toLowerCase().substring(1, name.length());
                NodeEntry node = getOrCreateNode(nodeIndex);
                node.indexEntry(remains, authorId);
            }
        } else {
            LeafEntry entry = new LeafEntry(authorId);
            leafs.add(entry);
        }
        count++;
    }

    public List<LeafEntry> getLeafs() {
        return leafs;
    }


    @Override
    public void finalize() {
        if (children != null && children.size() > 0) {
            for (ChildWrapper item : children) {
                LeafEntry leaf = new LeafEntry(item.getAuthorId());
                leafs.add(leaf);
            }
            children.clear();
            children = null;
        }
        super.finalize();
    }
}
