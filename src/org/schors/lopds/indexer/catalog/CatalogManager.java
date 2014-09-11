package org.schors.lopds.indexer.catalog;

import java.util.List;
import java.util.Map;

/**
 * Created by flicus on 31.08.14.
 */
public interface CatalogManager {

    public void indexAuthor(String lastName, Integer authorId);
    public Map<Integer, List<NodeEntry>> getLevels();

}
