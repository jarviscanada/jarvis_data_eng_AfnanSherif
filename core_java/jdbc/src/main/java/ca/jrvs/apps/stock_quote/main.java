package main.java.ca.jrvs.apps.stock_quote;

import main.java.ca.jrvs.apps.stock_quote.DatabaseConnectionManager;
import java.sql.Connection;

public class main {

    public static void main(String[] args) {

        DatabaseConnectionManager manager =
                new DatabaseConnectionManager(
                        "localhost",
                        "5432",
                        "stock_quote",
                        "postgres",
                        "password");

        try (Connection conn = manager.getConnection()) {

            if (conn != null) {
                System.out.println("Connected successfully!");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
