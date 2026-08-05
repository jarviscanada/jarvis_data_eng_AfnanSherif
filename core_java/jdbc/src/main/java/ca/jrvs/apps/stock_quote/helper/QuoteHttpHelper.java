package ca.jrvs.apps.stock_quote.helper;

import ca.jrvs.apps.stock_quote.GlobalQuoteResponse;
import ca.jrvs.apps.stock_quote.model.Quote;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;

public class QuoteHttpHelper {

    private static final Logger logger =
            LoggerFactory.getLogger(QuoteHttpHelper.class);

    private static final String BASE_URL =
            "https://www.alphavantage.co/query?function=GLOBAL_QUOTE";

    private final String apiKey;
    private final OkHttpClient client;
    private final ObjectMapper mapper;

    public QuoteHttpHelper(String apiKey, OkHttpClient client) {

        this.apiKey = apiKey;
        this.client = client;
        this.mapper = new ObjectMapper();
    }

    /**
     * Fetch latest quote data from Alpha Vantage API.
     *
     * @param symbol stock ticker symbol
     * @return Quote object containing stock information
     */
    public Quote fetchQuoteInfo(String symbol)
            throws IllegalArgumentException {

        // Validate ticker input
        if (symbol == null || symbol.trim().isEmpty()) {

            logger.warn(
                    "Quote request rejected because symbol is null or empty"
            );

            throw new IllegalArgumentException(
                    "Symbol cannot be null or empty"
            );
        }

        logger.info(
                "Fetching quote information for symbol: {}",
                symbol
        );

        String url =
                BASE_URL +
                        "&symbol=" +
                        symbol +
                        "&apikey=" +
                        apiKey;

        Request request =
                new Request.Builder()
                        .url(url)
                        .get()
                        .build();

        try (Response response =
                     client.newCall(request).execute()) {

            // Validate HTTP response
            if (!response.isSuccessful()) {

                logger.error(
                        "Alpha Vantage request failed for symbol {} with HTTP status {}",
                        symbol,
                        response.code()
                );

                throw new IOException(
                        "HTTP error code: " + response.code()
                );
            }

            logger.info(
                    "Alpha Vantage request successful for symbol: {}",
                    symbol
            );

            if (response.body() == null) {

                logger.error(
                        "Alpha Vantage returned an empty response body for symbol: {}",
                        symbol
                );

                throw new IOException(
                        "Response body is empty"
                );
            }

            String json =
                    response.body().string();

            // Convert JSON wrapper into Java object
            GlobalQuoteResponse globalResponse =
                    mapper.readValue(
                            json,
                            GlobalQuoteResponse.class
                    );

            Quote quote =
                    globalResponse.getQuote();

            // Validate API returned stock data
            if (quote == null ||
                    quote.getTicker() == null ||
                    quote.getTicker().trim().isEmpty()) {

                logger.warn(
                        "Alpha Vantage returned no valid quote for symbol: {}",
                        symbol
                );

                throw new IllegalArgumentException(
                        "Invalid stock symbol: " + symbol
                );
            }

            // Alpha Vantage does not provide API call timestamp
            quote.setTimestamp(
                    Timestamp.from(
                            Instant.now()
                    )
            );

            logger.info(
                    "Quote information retrieved successfully for symbol: {}",
                    symbol
            );

            return quote;

        } catch (IOException e) {

            logger.error(
                    "Failed to fetch quote information for symbol: {}",
                    symbol,
                    e
            );

            throw new IllegalArgumentException(
                    "Failed to fetch quote information",
                    e
            );
        }
    }
}
