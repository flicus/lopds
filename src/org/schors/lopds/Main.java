package org.schors.lopds;

import org.schors.lopds.dao.DAOManager;
import org.schors.lopds.indexer.LibraryIndexer;
import org.schors.lopds.indexer.LibraryTraverser;
import org.schors.lopds.indexer.LibraryTraverserImpl;
import org.schors.lopds.indexer.WorkObserver;
import org.schors.lopds.web.WebServer;

import java.io.File;
import java.io.IOException;
import java.util.TreeMap;
import java.util.concurrent.ScheduledExecutorService;

public class Main {
    public static void main(String[] args) {

//        String t = "/start/st2/1";
//        String tt = "/st/";
//
//        String[] a = t.split("/");
//        a= tt.split("/");


        Configuration cfg = Configuration.getInstance();
        //LibraryTraverser traverser = new LibraryTraverserImpl();
        //LibraryIndexer indexer = new LibraryIndexer(traverser);
        DAOManager.getInstance().init();
//        traverser.init(new File(cfg.getArchiveDirectory()), new WorkObserver() {
//
//            @Override
//            public void fileStarted(String filename, long size) {
//                //To change body of implemented methods use File | Settings | File Templates.
//            }
//
//            @Override
//            public void unitProcessed(String fileName, long progress) {
//                //To change body of implemented methods use File | Settings | File Templates.
//            }
//
//            @Override
//            public void fileDone(String filename) {
//                //To change body of implemented methods use File | Settings | File Templates.
//            }
//
//            @Override
//            public void workDone() {
//                System.out.println("done");
//            }
//        });
//        indexer.setPath2index(new File(cfg.getIndexDirectory()));
//        indexer.index();
//        DAOManager.getInstance().init();
        try {
            WebServer server = new WebServer(8080, "d:\\tmp\\webroot");
            server.setDaemon(false);
            server.start();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }
}
