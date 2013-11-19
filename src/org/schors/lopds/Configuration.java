package org.schors.lopds;

public class Configuration {

    //private String archiveDirectory = "\\\\porosek\\video\\_Lib.rus.ec - Официальная\\lib.rus.ec";//"d:\\tmp\\lopds_lib";
    private String archiveDirectory = "d:\\tmp\\lib.rus.ec";//"d:\\tmp\\lopds_lib";
    //private String archiveDirectory = "d:\\tmp\\lopds_lib";
    private String indexDirectory = "c:\\temp\\";
    private String jdbcUrl = "jdbc:h2:d:/tmp/db/opds";
    private String jdbcUserName = "sa";
    private String jdbcPassword = "";

    public Configuration() {
    }

    public Configuration(String archiveDirectory, String indexDirectory, String jdbcUrl, String jdbcUserName, String jdbcPassword) {
        this.archiveDirectory = archiveDirectory;
        this.indexDirectory = indexDirectory;
        this.jdbcUrl = jdbcUrl;
        this.jdbcUserName = jdbcUserName;
        this.jdbcPassword = jdbcPassword;
    }

    public String getArchiveDirectory() {
        return archiveDirectory;
    }

    public void setArchiveDirectory(String archiveDirectory) {
        this.archiveDirectory = archiveDirectory;
    }

    public String getIndexDirectory() {
        return indexDirectory;
    }

    public void setIndexDirectory(String indexDirectory) {
        this.indexDirectory = indexDirectory;
    }

    private static class Singleton {
        public static Configuration singleton = new Configuration();
    }

    public static Configuration getInstance() {
        return Singleton.singleton;
    }

    public String getJdbcUrl() {
        return jdbcUrl;
    }

    public void setJdbcUrl(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }

    public String getJdbcUserName() {
        return jdbcUserName;
    }

    public void setJdbcUserName(String jdbcUserName) {
        this.jdbcUserName = jdbcUserName;
    }

    public String getJdbcPassword() {
        return jdbcPassword;
    }

    public void setJdbcPassword(String jdbcPassword) {
        this.jdbcPassword = jdbcPassword;
    }

    @Override
    public String toString() {
        return "Configuration{" +
                "archiveDirectory='" + archiveDirectory + '\'' +
                ", indexDirectory='" + indexDirectory + '\'' +
                ", jdbcUrl='" + jdbcUrl + '\'' +
                ", jdbcUserName='" + jdbcUserName + '\'' +
                ", jdbcPassword='" + jdbcPassword + '\'' +
                '}';
    }
}
