package ca.jrvs.apps.stock_quote.service;

import ca.jrvs.apps.stock_quote.dao.QuoteDao;
import ca.jrvs.apps.stock_quote.helper.QuoteHttpHelper;
import ca.jrvs.apps.stock_quote.model.Quote;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class QuoteService {

    private static final Logger logger =
            LoggerFactory.getLogger(QuoteService.class);

    private final QuoteDao dao;
    private final QuoteHttpHelper httpHelper;

    public QuoteService(
            QuoteDao dao,
            QuoteHttpHelper httpHelper
    ) {

        this.dao = dao;
        this.httpHelper = httpHelper;
    }

    public Optional<Quote> fetchQuoteDataFromAPI(String ticker) {

        if (ticker == null || ticker.isBlank()) {

            logger.warn("Quote request rejected because ticker is null or empty");

            throw new IllegalArgumentException(
                    "Ticker cannot be null or empty"
            );
        }

        logger.info("Fetching quote data for ticker: {}", ticker);

        try {

            Quote quote =
                    httpHelper.fetchQuoteInfo(ticker);

            logger.info(
                    "Quote data retrieved successfully for ticker: {}",
                    ticker
            );

            dao.save(quote);

            logger.info(
                    "Quote saved successfully for ticker: {}",
                    ticker
            );

            return Optional.of(quote);

        } catch (IllegalArgumentException e) {

            logger.error(
                    "Failed to fetch or save quote for ticker: {}",
                    ticker,
                    e
            );

            return Optional.empty();
        }
    }
}