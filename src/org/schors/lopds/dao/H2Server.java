package org.schors.lopds.dao;

import org.apache.log4j.Logger;
import org.h2.tools.Server;

import java.sql.SQLException;

public class H2Server {

    private final static Logger log = Logger.getLogger(H2Server.class);
    private Server server;

    public H2Server() {
        try {
            server = Server.createTcpServer(new String[]{"-tcpPort", "9123"});
        } catch (SQLException e) {
            log.error(e,e);
        }
    }

    public void startServer() throws SQLException {
        server.start();
    }

    private static class Singleton {
        public static final H2Server instance = new H2Server();
    }

    public static H2Server getInstance() {
        return Singleton.instance;
    }
}
