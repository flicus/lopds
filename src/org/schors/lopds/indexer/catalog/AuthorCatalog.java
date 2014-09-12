package org.schors.lopds.indexer.catalog;

/**
 * Created with IntelliJ IDEA.
 * User: flicus
 * Date: 04.08.13
 * Time: 21:09
 * To change this template use File | Settings | File Templates.
 */
public class AuthorCatalog extends CatalogItem {

    public void indexEntry(String lastName, Integer authorId) {
        String nodeIndex = lastName.toLowerCase().substring(0, 1);
        if (lastName.length() > 0) {
            String remains = lastName.toLowerCase().substring(1, lastName.length());
            NodeEntry node = getOrCreateNode(nodeIndex);
            node.indexEntry(remains, authorId);
        } else {
            LeafEntry leaf = new LeafEntry(authorId);
            leafs.add(leaf);
        }
        count++;
    }
}
