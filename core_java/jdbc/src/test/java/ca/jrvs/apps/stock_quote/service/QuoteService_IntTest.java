package ca.jrvs.apps.stock_quote.service;


import ca.jrvs.apps.stock_quote.dao.QuoteDao;
import ca.jrvs.apps.stock_quote.helper.QuoteHttpHelper;
import ca.jrvs.apps.stock_quote.model.Quote;

import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Timestamp;
import java.sql.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;



public class QuoteService_IntTest {


    private static Connection connection;


    private QuoteDao quoteDao;


    private QuoteHttpHelper httpHelper;


    private QuoteService quoteService;




    @BeforeAll
    public static void setupDatabase()
            throws Exception {


        connection =
                DriverManager.getConnection(
                        "jdbc:postgresql://localhost:5432/stock_quote",
                        "postgres",
                        "password"
                );

    }





    @BeforeEach
    public void setup(){


        quoteDao =
                new QuoteDao(connection);


        httpHelper =
                mock(QuoteHttpHelper.class);



        quoteService =
                new QuoteService(
                        quoteDao,
                        httpHelper
                );



        quoteDao.deleteAll();

    }





    @AfterEach
    public void cleanup(){


        quoteDao.deleteAll();

    }





    @AfterAll
    public static void close()
            throws Exception {


        connection.close();

    }







    private Quote createQuote(){


        Quote quote = new Quote();


        quote.setTicker("MSFT");

        quote.setOpen("380");

        quote.setHigh("390");

        quote.setLow("375");

        quote.setPrice("385");

        quote.setVolume("10000");

        quote.setLatestTradingDay(
                Date.valueOf("2026-07-27")
        );

        quote.setPreviousClose("382");

        quote.setChange("3");

        quote.setChangePercent("0.5%");

        quote.setTimestamp(
                new Timestamp(
                        System.currentTimeMillis()
                )
        );


        return quote;

    }







    @Test
    public void fetchQuoteDataFromAPI_validTicker_savesQuote(){


        Quote quote =
                createQuote();



        when(httpHelper.fetchQuoteInfo("MSFT"))
                .thenReturn(quote);



        Optional<Quote> result =
                quoteService.fetchQuoteDataFromAPI("MSFT");



        assertTrue(
                result.isPresent()
        );



        assertEquals(
                "MSFT",
                result.get().getTicker()
        );



        Optional<Quote> databaseQuote =
                quoteDao.findById("MSFT");



        assertTrue(
                databaseQuote.isPresent()
        );

    }








    @Test
    public void fetchQuoteDataFromAPI_invalidTicker_returnsEmpty(){



        when(httpHelper.fetchQuoteInfo("INVALID"))
                .thenThrow(
                        new IllegalArgumentException(
                                "Invalid ticker"
                        )
                );



        Optional<Quote> result =
                quoteService.fetchQuoteDataFromAPI(
                        "INVALID"
                );



        assertTrue(
                result.isEmpty()
        );


        verify(
                httpHelper
        ).fetchQuoteInfo("INVALID");


    }





    @Test
    public void fetchQuoteDataFromAPI_existingQuote_updatesQuote(){


        Quote quote =
                createQuote();



        quoteDao.save(quote);



        quote.setPrice("400");



        when(httpHelper.fetchQuoteInfo("MSFT"))
                .thenReturn(quote);



        Optional<Quote> result =
                quoteService.fetchQuoteDataFromAPI("MSFT");



        assertTrue(
                result.isPresent()
        );



        Quote saved =
                quoteDao.findById("MSFT")
                        .get();



        assertEquals(
                "400.0",
                saved.getPrice()
        );

    }


}