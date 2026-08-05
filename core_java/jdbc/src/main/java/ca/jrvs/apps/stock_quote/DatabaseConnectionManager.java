package ca.jrvs.apps.stock_quote;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnectionManager {

    private static final Logger logger =
            LoggerFactory.getLogger(DatabaseConnectionManager.class);

    private final String url;
    private final String user;
    private final String password;

    public DatabaseConnectionManager(String host,
                                     String port,
                                     String database,
                                     String user,
                                     String password) {

        this.url =
                "jdbc:postgresql://"
                        + host
                        + ":"
                        + port
                        + "/"
                        + database;

        this.user = user;
        this.password = password;
    }

    public Connection getConnection() throws SQLException {

        logger.info("Attempting to connect to the database");

        try {

            Connection connection =
                    DriverManager.getConnection(
                            url,
                            user,
                            password
                    );

            logger.info("Database connection established successfully");

            return connection;

        } catch (SQLException e) {

            logger.error(
                    "Failed to establish database connection",
                    e
            );

            throw e;
        }
    }
}
