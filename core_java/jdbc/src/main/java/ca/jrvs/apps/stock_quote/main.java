package ca.jrvs.apps.stock_quote;

import java.net.http.HttpClient;
import java.sql.Connection;

public class main {

    public static void main(String[] args) {
        String apiKey = "RJI2U9TI75V0OWR8";
        HttpClient client = HttpClient.newHttpClient();
        QuoteHttpHelper helper =
                new QuoteHttpHelper(apiKey, client);
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
        try {


            // Fetch Microsoft stock quote
            Quote quote =
                    helper.fetchQuoteInfo("MSFT");



            // Print returned quote information
            System.out.println("Stock Quote:");
            System.out.println("----------------");
            System.out.println("Ticker: "
                    + quote.getTicker());

            System.out.println("Open: "
                    + quote.getOpen());

            System.out.println("High: "
                    + quote.getHigh());

            System.out.println("Low: "
                    + quote.getLow());

            System.out.println("Price: "
                    + quote.getPrice());

            System.out.println("Volume: "
                    + quote.getVolume());

            System.out.println("Retrieved At: "
                    + quote.getTimestamp());


        } catch (IllegalArgumentException e) {


            // Handles:
            // - invalid stock symbols
            // - API failures
            // - network issues
            System.out.println(
                    "Error fetching quote: "
                            + e.getMessage()
            );

        }
    }
}
