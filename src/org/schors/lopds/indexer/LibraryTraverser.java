package org.schors.lopds.indexer;

import org.schors.lopds.model.Book;

import java.io.File;

public interface LibraryTraverser {
    public File[] init(File libraryFolder, WorkObserver observer) ;

    public Book getNextBook();

    public boolean isMoreBooksAvailable();

    public ZipFileList getZipFileList();
}
