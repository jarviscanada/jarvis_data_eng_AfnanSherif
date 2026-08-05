package ca.jrvs.apps.stock_quote.service;

import ca.jrvs.apps.stock_quote.dao.PositionDao;
import ca.jrvs.apps.stock_quote.model.Position;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class PositionService {

    private static final Logger logger =
            LoggerFactory.getLogger(PositionService.class);

    private final PositionDao dao;

    public PositionService(PositionDao dao) {
        this.dao = dao;
    }

    public Position buy(
            String ticker,
            int numberOfShares,
            double price
    ) {

        if (ticker == null || ticker.isBlank()) {

            logger.warn("Buy request rejected because ticker is null or empty");

            throw new IllegalArgumentException(
                    "Ticker cannot be null"
            );
        }

        if (numberOfShares <= 0) {

            logger.warn(
                    "Buy request rejected for ticker {} because share count is invalid: {}",
                    ticker,
                    numberOfShares
            );

            throw new IllegalArgumentException(
                    "Shares must be positive"
            );
        }

        if (price <= 0) {

            logger.warn(
                    "Buy request rejected for ticker {} because price is invalid: {}",
                    ticker,
                    price
            );

            throw new IllegalArgumentException(
                    "Price must be positive"
            );
        }

        logger.info(
                "Processing buy request for ticker: {}, shares: {}, price: {}",
                ticker,
                numberOfShares,
                price
        );

        double cost =
                numberOfShares * price;

        Optional<Position> existing =
                dao.findById(ticker);

        Position position;

        if (existing.isPresent()) {

            position = existing.get();

            logger.info(
                    "Existing position found for ticker: {}. Updating position",
                    ticker
            );

            position.setNumOfShares(
                    position.getNumOfShares()
                            + numberOfShares
            );

            position.setValuePaid(
                    position.getValuePaid()
                            + cost
            );

        } else {

            logger.info(
                    "No existing position found for ticker: {}. Creating new position",
                    ticker
            );

            position = new Position();

            position.setTicker(ticker);

            position.setNumOfShares(
                    numberOfShares
            );

            position.setValuePaid(
                    cost
            );
        }

        Position savedPosition = dao.save(position);

        logger.info(
                "Buy completed successfully for ticker: {}",
                ticker
        );

        return savedPosition;
    }

    public Iterable<Position> viewPortfolio() {

        logger.info("Retrieving portfolio");

        Iterable<Position> positions =
                dao.findAll();

        logger.info("Portfolio retrieved successfully");

        return positions;
    }

    public void sell(String ticker) {

        if (ticker == null || ticker.isBlank()) {

            logger.warn(
                    "Sell request rejected because ticker is null or empty"
            );

            throw new IllegalArgumentException(
                    "Ticker cannot be null"
            );
        }

        logger.info(
                "Processing sell request for ticker: {}",
                ticker
        );

        Optional<Position> position =
                dao.findById(ticker);

        if (position.isEmpty()) {

            logger.warn(
                    "Sell request failed because no position exists for ticker: {}",
                    ticker
            );

            throw new IllegalArgumentException(
                    "You do not own any shares of "
                            + ticker
            );
        }

        dao.deleteById(ticker);

        logger.info(
                "Sell completed successfully for ticker: {}",
                ticker
        );
    }
}
