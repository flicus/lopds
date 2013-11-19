package org.schors.lopds.indexer;

import org.schors.lopds.dao.DAOManager;
import org.schors.lopds.model.Book;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LibraryIndexer {

    private LibraryTraverser traverser;
    private File path2index;
    private ExecutorService executorService = Executors.newCachedThreadPool();

    public LibraryIndexer(LibraryTraverser traverser) {
        this.traverser = traverser;
    }

    public void setPath2index(File path2index) {
        this.path2index = path2index;
    }

    public void index() {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                long start = System.currentTimeMillis();
                Book book = traverser.getNextBook();

                //int c = 0;
                while (book != null) {
                  //  c++;
                  //  System.out.println(book);
                    DAOManager.getInstance().addBook(book);
                    book = traverser.getNextBook();
                }
                System.out.println(System.currentTimeMillis() - start);
            }
        });
    }

}
