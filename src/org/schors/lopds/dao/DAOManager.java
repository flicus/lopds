package org.schors.lopds.dao;

import org.apache.log4j.Logger;
import org.h2.jdbcx.JdbcConnectionPool;
import org.schors.lopds.Configuration;
import org.schors.lopds.model.Book;

import java.sql.*;

public class DAOManager {
    private final static int MAX_DEPTH = 4;

    private final static int A_ROOT = 0;
    private final static int A_NODE = 1;
    private final static int A_AUTHOR = 2;
    private final static int A_BOOK = 3;


    private Logger log = Logger.getLogger(DAOManager.class);
    private JdbcConnectionPool pool;

    public DAOManager() {
        Configuration cfg = Configuration.getInstance();
        try {
            //H2Server.getInstance().startServer();
            Class.forName("org.h2.Driver");
            //} catch (SQLException e) {
            //  log.error(e, e);

        } catch (ClassNotFoundException e) {
            log.error(e, e);
        }

        pool = JdbcConnectionPool.create(cfg.getJdbcUrl(), cfg.getJdbcUserName(), cfg.getJdbcPassword());
    }

    public void init() {
        if (!dbExist())
            createTables();
    }

    private static class Singleton {
        public static DAOManager instance = new DAOManager();
    }

    public static DAOManager getInstance() {
        return Singleton.instance;
    }

    public void createTables() {
        String createSettings = "CREATE TABLE IF NOT EXISTS settings (id int primary key, archiveDirectory varchar(255), indexDirectory varchar(255))";
        String createGenre = "CREATE TABLE IF NOT EXISTS genre (id int primary key, name varchar(255))";
        String createAuthor = "CREATE TABLE IF NOT EXISTS author (id int primary key, firstname varchar(255), middlename varchar(255), lastname varchar(255))";
        String createArchive = "CREATE TABLE IF NOT EXISTS archive (id int primary key, name varchar(255))";
        String createBooks = "CREATE TABLE IF NOT EXISTS books (id int primary key, title varchar(255), language varchar(30), filename varchar(255), genre int REFERENCES genre (id), author int REFERENCES author (id), archive int REFERENCES archive (id))";
        String createAcatalog = "CREATE TABLE IF NOT EXISTS acatalog (id int primary key, name varchar(10), type int, parentid int)";
        String createIndex1 = "CREATE INDEX genre_index ON books (genre ASC NULLS LAST)";
        String createIndex2 = "CREATE INDEX author_index ON books (author ASC NULLS LAST)";

        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {

            stmt.execute(createSettings);
            stmt.execute(createArchive);
            stmt.execute(createAuthor);
            stmt.execute(createGenre);
            stmt.execute(createBooks);
            stmt.execute(createAcatalog);
            stmt.execute(createIndex1);
            stmt.execute(createIndex2);
            stmt.execute("create sequence genreSeq start with 1");
            stmt.execute("create sequence authorSeq start with 1");
            stmt.execute("create sequence archiveSeq start with 1");
            stmt.execute("create sequence booksSeq start with 1");
            stmt.execute("create sequence acSeq start with 1");

            stmt.executeUpdate("insert into author values (0, 'unknown', 'unknown', 'unknown')");
            stmt.executeUpdate("insert into genre values (0, 'unknown')");

        } catch (SQLException e) {
            log.error(e, e);
        }

    }

    public int addBook(Book newBook) {
        int bookId = 0;
        try (Connection conn = getConnection()) {
            // genre
            int genreId = 0;    // if some error with genre, use default one
            if (newBook.getGenre() != null && newBook.getGenre().trim().length() > 0)
                try {
                    String genre = newBook.getGenre().trim();
                    PreparedStatement pstmt = conn.prepareStatement("select id from genre where name like ?");
                    pstmt.setString(1, genre);
                    ResultSet rs = pstmt.executeQuery();
                    if (rs.next()) {
                        genreId = rs.getInt(1);
                    } else {
                        Statement stmt = conn.createStatement();
                        ResultSet rs1 = stmt.executeQuery("select nextval('genreSeq')");
                        if (rs1.next())
                            genreId = rs1.getInt(1);
                        rs1.close();
                        stmt.close();

                        PreparedStatement pstmt1 = conn.prepareStatement("insert into genre values (?, ?)");
                        pstmt1.setInt(1, genreId);
                        pstmt1.setString(2, genre);

                        pstmt1.executeUpdate();
                        pstmt1.close();
                    }
                    rs.close();
                    pstmt.close();
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error(e, e);
                }

            //author
            int authorId = 0;   // if some error with author, use default one
            if (newBook.getFirstName() != null && newBook.getLastName() != null && newBook.getMiddleName() != null) {
                try {
                    StringBuilder sb = new StringBuilder();
                    sb.append("select id from author where ");
                    boolean andFlag = false;
                    if (newBook.getFirstName() != null) {
                        sb.append("firstname = ? ");
                        andFlag = true;
                    }
                    if (newBook.getLastName() != null) {
                        if (andFlag) sb.append("and ");
                        sb.append("lastname = ? ");
                        andFlag = true;
                    }
                    if (newBook.getMiddleName() != null) {
                        if (andFlag) sb.append("and ");
                        sb.append("middlename = ?");
                    }

                    PreparedStatement pstmt = conn.prepareStatement(sb.toString());
                    int i = 1;
                    if (newBook.getFirstName() != null)
                        pstmt.setString(i++, newBook.getFirstName());
                    if (newBook.getLastName() != null)
                        pstmt.setString(i++, newBook.getLastName());
                    if (newBook.getMiddleName() != null)
                        pstmt.setString(i, newBook.getMiddleName());

                    ResultSet rs = pstmt.executeQuery();
                    if (rs.next()) {
                        authorId = rs.getInt(1);
                    } else {
                        Statement stmt = conn.createStatement();
                        ResultSet rs1 = stmt.executeQuery("select nextval('authorSeq')");
                        if (rs1.next())
                            authorId = rs1.getInt(1);
                        rs1.close();
                        stmt.close();

                        PreparedStatement pstmt1 = conn.prepareStatement("insert into author values (?, ?, ?, ?)");
                        pstmt1.setInt(1, authorId);
                        pstmt1.setString(2, newBook.getFirstName());
                        pstmt1.setString(3, newBook.getMiddleName());
                        pstmt1.setString(4, newBook.getLastName());

                        pstmt1.executeUpdate();
                        pstmt1.close();
                    }
                    rs.close();
                    pstmt.close();
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error(e, e);
                }
            }

            try {
                //archive
                // if some error with archive - do not persist book
                int archiveId = 0;
                PreparedStatement pstmt = conn.prepareStatement("select id from archive where name = ?");
                pstmt.setString(1, newBook.getArchive());
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    archiveId = rs.getInt(1);
                } else {
                    Statement stmt = conn.createStatement();
                    ResultSet rs1 = stmt.executeQuery("select nextval('archiveSeq')");
                    if (rs1.next())
                        archiveId = rs1.getInt(1);
                    rs1.close();
                    stmt.close();

                    PreparedStatement pstmt1 = conn.prepareStatement("insert into archive values (?, ?)");
                    pstmt1.setInt(1, archiveId);
                    pstmt1.setString(2, newBook.getArchive());

                    pstmt1.executeUpdate();
                    pstmt1.close();
                }
                rs.close();
                pstmt.close();

                //book
                Statement stmt = conn.createStatement();
                rs = stmt.executeQuery("select nextval('booksSeq')");
                if (rs.next())
                    bookId = rs.getInt(1);
                rs.close();
                stmt.close();
                pstmt = conn.prepareStatement("insert into books values (?, ?, ?, ?, ?, ?, ?)");
                pstmt.setInt(1, bookId);
                pstmt.setString(2, newBook.getTitle());
                pstmt.setString(3, newBook.getLanguage());
                pstmt.setString(4, newBook.getFileName());
                pstmt.setInt(5, genreId);
                pstmt.setInt(6, authorId);
                pstmt.setInt(7, archiveId);

                pstmt.executeUpdate();
                pstmt.close();

                //update autor catalog
                int pathSize = newBook.getLastName().length() > MAX_DEPTH ? MAX_DEPTH : newBook.getLastName().length();
                String[] pathArray = new String[pathSize];
                for (int i = 0; i <= pathSize; i++) {
                    pathArray[i] = newBook.getLastName().substring(0, i + 1);
                }



            } catch (Exception e) {
                e.printStackTrace();
                log.error(e, e);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            log.error(e, e);
        }
        return bookId;
    }


    public Connection getConnection() throws SQLException {
        return pool.getConnection();
    }

    public boolean dbExist() {
        boolean result = false;
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            result = stmt.execute("SELECT * FROM settings");
        } catch (SQLException e) {
            log.error(e, e);
        }
        return result;
    }

    private int getOrCreateAEntry(String entryName) {
        int entryId = -1;
        try (Connection conn = getConnection(); ) {
            int tmpId = 0;
            PreparedStatement preparedStatement = conn.prepareStatement("select id from acatalog where name = ?");
            preparedStatement.setString(1, entryName);

            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next())
                tmpId = rs.getInt(1);
            else {
                Statement stmt = conn.createStatement();
                ResultSet rs1 = stmt.executeQuery("select nextval('acSeq')");
                if (rs1.next())
                    tmpId = rs1.getInt(1);
                rs1.close();
                stmt.close();
            }

            rs.close();
            preparedStatement.close();

            preparedStatement = conn.prepareStatement("insert into acatalog values (?, ?, ?, ?)");
            preparedStatement.setInt(1, tmpId);
            preparedStatement.setString(2, entryName);
            preparedStatement.setInt(3, entryName.length() == 1 ? A_ROOT : A_NODE);
            preparedStatement.setInt(4, 0);

            preparedStatement.executeUpdate();
            preparedStatement.close();
            entryId = tmpId;
        } catch (SQLException e) {
            log.error(e, e);
        }
        return entryId;
    }

}
