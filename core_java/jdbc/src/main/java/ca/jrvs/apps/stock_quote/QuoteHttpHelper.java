package ca.jrvs.apps.stock_quote;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Timestamp;
import java.time.Instant;
import java.io.IOException;


/**
 * Helper class responsible for communicating with Alpha Vantage API.
 *
 * Responsibilities:
 * - Build HTTP request
 * - Send request
 * - Receive JSON response
 * - Convert JSON into Java objects using Jackson
 */
public class QuoteHttpHelper {


    private static final String BASE_URL =
            "https://www.alphavantage.co/query?function=GLOBAL_QUOTE";


    private final String apiKey;
    private final HttpClient client;
    private final ObjectMapper mapper;


    public QuoteHttpHelper(String apiKey, HttpClient  client) {

        this.apiKey = apiKey;
        this.client = client;
        this.mapper = new ObjectMapper();

    }


    public Quote fetchQuoteInfo(String symbol)
            throws IllegalArgumentException {


        // Validate user input before making API call
        if(symbol == null || symbol.isEmpty()){
            throw new IllegalArgumentException(
                    "Symbol cannot be empty"
            );
        }


        String url =
                BASE_URL +
                        "&symbol=" +
                        symbol +
                        "&apikey=" +
                        apiKey;


        HttpRequest request =
                HttpRequest.newBuilder()
                        .uri(URI.create(url))
                        .GET()
                        .build();


        try {

            HttpResponse<String> response =
                    client.send(
                            request,
                            HttpResponse.BodyHandlers.ofString()
                    );

            if(response.statusCode() != 200){
                throw new IOException(
                        "HTTP error: " + response.statusCode()
                );
            }


            String json = response.body();

            if(json == null){
                throw new IllegalArgumentException(
                        "Empty response received"
                );
            }
            GlobalQuoteResponse responseObject =
                    mapper.readValue(
                            json,
                            GlobalQuoteResponse.class
                    );


            Quote quote =
                    responseObject.getQuote();


            if(quote == null ||
                    quote.getTicker() == null){

                throw new IllegalArgumentException(
                        "Invalid stock symbol"
                );
            }

            quote.setTimestamp(
                    Timestamp.from(Instant.now())
            );


            return quote;


        } catch(IOException | InterruptedException e){

            throw new IllegalArgumentException(
                    "Failed to fetch quote",
                    e
            );
        }

    }
}