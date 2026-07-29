package ca.jrvs.apps.stock_quote.dao;


import ca.jrvs.apps.stock_quote.model.Quote;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.*;
import java.util.*;


public class QuoteDao implements CrudDao<Quote, String> {

    private static final Logger logger =
            LoggerFactory.getLogger(QuoteDao.class);

    private final Connection connection;


    private static final String UPSERT =
            "INSERT INTO quote " +
                    "(symbol, open, high, low, price, volume, latest_trading_day, previous_close, change, change_percent, timestamp) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) " +
                    "ON CONFLICT(symbol) DO UPDATE SET " +
                    "open = EXCLUDED.open, " +
                    "high = EXCLUDED.high, " +
                    "low = EXCLUDED.low, " +
                    "price = EXCLUDED.price, " +
                    "volume = EXCLUDED.volume, " +
                    "latest_trading_day = EXCLUDED.latest_trading_day, " +
                    "previous_close = EXCLUDED.previous_close, " +
                    "change = EXCLUDED.change, " +
                    "change_percent = EXCLUDED.change_percent, " +
                    "timestamp = EXCLUDED.timestamp";


    private static final String FIND_BY_ID =
            "SELECT * FROM quote WHERE symbol = ?";


    private static final String FIND_ALL =
            "SELECT * FROM quote";


    private static final String DELETE_BY_ID =
            "DELETE FROM quote WHERE symbol = ?";


    private static final String DELETE_ALL =
            "DELETE FROM quote";


    public QuoteDao(Connection connection) {
        this.connection = connection;
    }



    @Override
    public Quote save(Quote quote) throws IllegalArgumentException {

        if (quote == null ||
                quote.getTicker() == null ||
                quote.getTicker().isEmpty()) {

            throw new IllegalArgumentException(
                    "Quote or ticker cannot be null/empty"
            );
        }

        try (PreparedStatement ps =
                     connection.prepareStatement(UPSERT)) {


            ps.setString(1, quote.getTicker());


            // String -> database NUMERIC
            ps.setDouble(2, Double.parseDouble(quote.getOpen()));
            ps.setDouble(3, Double.parseDouble(quote.getHigh()));
            ps.setDouble(4, Double.parseDouble(quote.getLow()));
            ps.setDouble(5, Double.parseDouble(quote.getPrice()));


            // String -> database INT
            ps.setInt(6, Integer.parseInt(quote.getVolume()));


            ps.setDate(
                    7,
                    quote.getLatestTradingDay()
            );


            ps.setDouble(
                    8,
                    Double.parseDouble(quote.getPreviousClose())
            );


            ps.setDouble(
                    9,
                    Double.parseDouble(quote.getChange())
            );


            ps.setString(
                    10,
                    quote.getChangePercent()
            );


            ps.setTimestamp(
                    11,
                    quote.getTimestamp()
            );


            ps.executeUpdate();


            return quote;


        } catch (SQLException e) {
            logger.error("Failed to save quote {}", quote.getTicker(), e);
            throw new RuntimeException("Failed to save quote", e);
        }

    }





    @Override
    public Optional<Quote> findById(String id) throws IllegalArgumentException{

        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException(
                    "Symbol cannot be null/empty"
            );
        }


        try (PreparedStatement ps =
                     connection.prepareStatement(FIND_BY_ID)) {


            ps.setString(1, id);


            ResultSet rs = ps.executeQuery();


            if (rs.next()) {

                return Optional.of(
                        mapRowToQuote(rs)
                );

            }


            return Optional.empty();


        } catch (SQLException e) {
            logger.error("Failed to find quote {}", id, e);
            throw new RuntimeException("Failed finding quote", e);
        }

    }





    @Override
    public Iterable<Quote> findAll() {


        List<Quote> quotes = new ArrayList<>();


        try (PreparedStatement ps =
                     connection.prepareStatement(FIND_ALL)) {


            ResultSet rs = ps.executeQuery();


            while (rs.next()) {

                quotes.add(
                        mapRowToQuote(rs)
                );

            }


            return quotes;


        } catch (SQLException e) {
            logger.error("Failed to retrieve all quotes", e);
            throw new RuntimeException("Failed finding quotes", e);
        }

    }





    @Override
    public void deleteById(String id) throws IllegalArgumentException {


        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException(
                    "Symbol cannot be null/empty"
            );
        }


        try (PreparedStatement ps =
                     connection.prepareStatement(DELETE_BY_ID)) {


            ps.setString(1, id);

            ps.executeUpdate();


        } catch (SQLException e) {
            logger.error("Failed to delete quote {}", id, e);
            throw new RuntimeException("Failed deleting quote", e);
        }

    }





    @Override
    public void deleteAll() {


        try (PreparedStatement ps =
                     connection.prepareStatement(DELETE_ALL)) {


            ps.executeUpdate();


        } catch (SQLException e) {
            logger.error("Failed to delete all quotes", e);
            throw new RuntimeException("Failed deleting quotes", e);
        }

    }





    private Quote mapRowToQuote(ResultSet rs)
            throws SQLException {


        Quote quote = new Quote();


        quote.setTicker(
                rs.getString("symbol")
        );


        // Database NUMERIC -> String model
        quote.setOpen(
                String.valueOf(
                        rs.getDouble("open")
                )
        );


        quote.setHigh(
                String.valueOf(
                        rs.getDouble("high")
                )
        );


        quote.setLow(
                String.valueOf(
                        rs.getDouble("low")
                )
        );


        quote.setPrice(
                String.valueOf(
                        rs.getDouble("price")
                )
        );


        quote.setVolume(
                String.valueOf(
                        rs.getInt("volume")
                )
        );


        quote.setLatestTradingDay(
                rs.getDate("latest_trading_day")
        );


        quote.setPreviousClose(
                String.valueOf(
                        rs.getDouble("previous_close")
                )
        );


        quote.setChange(
                String.valueOf(
                        rs.getDouble("change")
                )
        );


        quote.setChangePercent(
                rs.getString("change_percent")
        );


        quote.setTimestamp(
                rs.getTimestamp("timestamp")
        );


        return quote;

    }

}