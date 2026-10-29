package com.smartcart.dao;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * JDBC Database Connection Utility.
 * Connects to MySQL using JDBC PreparedStatement transactions.
 * Includes automatic diagnostic checks to gracefully notify if MySQL service is offline.
 */
public class DBUtil {
    private static final Logger LOGGER = Logger.getLogger(DBUtil.class.getName());

    private static String dbUrl = "jdbc:mysql://localhost:3306/smartcart_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static String dbUser = "root";
    private static String dbPassword = "";
    private static String dbDriver = "com.mysql.cj.jdbc.Driver";
    
    private static Boolean mySqlAvailable = null;

    static {
        loadProperties();
        initDriver();
    }

    private static void loadProperties() {
        try (InputStream is = DBUtil.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (is != null) {
                Properties props = new Properties();
                props.load(is);
                if (props.getProperty("db.url") != null) dbUrl = props.getProperty("db.url");
                if (props.getProperty("db.user") != null) dbUser = props.getProperty("db.user");
                if (props.getProperty("db.password") != null) dbPassword = props.getProperty("db.password");
                if (props.getProperty("db.driver") != null) dbDriver = props.getProperty("db.driver");
            }
        } catch (Exception e) {
            LOGGER.log(Level.FINE, "No external db.properties found, using default configuration.");
        }
    }

    private static void initDriver() {
        try {
            Class.forName(dbDriver);
        } catch (ClassNotFoundException e) {
            LOGGER.log(Level.WARNING, "MySQL JDBC Driver not loaded in classpath. In-memory data store will be active.");
        }
    }

    /**
     * Checks if MySQL database is currently reachable.
     */
    public static synchronized boolean isMySqlAvailable() {
        if (mySqlAvailable != null) {
            return mySqlAvailable;
        }
        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword)) {
            mySqlAvailable = (conn != null && !conn.isClosed());
            LOGGER.info("Connected successfully to MySQL database: " + dbUrl);
            return true;
        } catch (Throwable t) {
            mySqlAvailable = false;
            LOGGER.info("MySQL service offline or unreachable (" + t.getMessage() + "). Using embedded memory-backed repository.");
            return false;
        }
    }

    /**
     * Obtains an active JDBC Connection to MySQL.
     * @throws SQLException if connection fails
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(dbUrl, dbUser, dbPassword);
    }

    public static void close(AutoCloseable... closeables) {
        for (AutoCloseable c : closeables) {
            if (c != null) {
                try {
                    c.close();
                } catch (Exception e) {
                    LOGGER.log(Level.FINE, "Error closing resource", e);
                }
            }
        }
    }
}
