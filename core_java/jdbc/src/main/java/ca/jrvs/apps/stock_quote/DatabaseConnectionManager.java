package main.java.ca.jrvs.apps.stock_quote;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnectionManager {

    private final String url;
    private final String user;
    private final String password;

    public DatabaseConnectionManager(String host,
                                     String port,
                                     String database,
                                     String user,
                                     String password) {

        this.url = "jdbc:postgresql://" + host + ":" + port + "/" + database;
        this.user = user;
        this.password = password;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
}