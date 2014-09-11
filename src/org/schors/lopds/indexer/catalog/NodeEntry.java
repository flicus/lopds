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
public class NodeEntry extends BasicEntry {

    private List<LeafEntry> leafs = new ArrayList<>();
    private List<ChildWrapper> children = new ArrayList<>();
    private Map<String, NodeEntry> branches = new HashMap<>();

    public void indexEntry(String name, Integer authorId) {
        String nodeIndex = name.toLowerCase().substring(0, 1);
        if (name.length() > 1) {
            String remains = name.toLowerCase().substring(1, name.length());
            if (children != null) {
                ChildWrapper wrapper = new ChildWrapper(remains, authorId);
                children.add(wrapper);
                if (children.size() > AuthorCatalog.MAX_PAGE_SIZE) {
                    for (ChildWrapper item : children) {
                        NodeEntry node = getOrCreateNode(nodeIndex);
                        node.indexEntry(item.getIndex(), item.getAuthorId());
                    }
                    children.clear();
                    children = null;
                }
            } else {
                NodeEntry node = getOrCreateNode(nodeIndex);
                node.indexEntry(remains, authorId);
            }
        } else {
            NodeEntry node = getOrCreateNode(nodeIndex);
            LeafEntry entry = new LeafEntry(authorId);
            node.addChild(entry);
        }
    }

    private NodeEntry getOrCreateNode(String index) {
        NodeEntry node = branches.get(index);
        if (node == null) {
            node = new NodeEntry();
            branches.put(index, node);
        }
        return node;
    }


    public void addChild(LeafEntry entry) {
        leafs.add(entry);
    }
}
