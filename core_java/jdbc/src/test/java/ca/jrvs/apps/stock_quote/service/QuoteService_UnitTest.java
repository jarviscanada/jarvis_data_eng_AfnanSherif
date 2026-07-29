package ca.jrvs.apps.stock_quote.service;

import ca.jrvs.apps.stock_quote.dao.QuoteDao;
import ca.jrvs.apps.stock_quote.helper.QuoteHttpHelper;
import ca.jrvs.apps.stock_quote.model.Quote;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class QuoteService_UnitTest {


    @Mock
    private QuoteDao dao;


    @Mock
    private QuoteHttpHelper httpHelper;


    private QuoteService service;



    @BeforeEach
    public void setup(){

        MockitoAnnotations.openMocks(this);

        service =
                new QuoteService(
                        dao,
                        httpHelper
                );
    }

    @Test
    public void fetchQuoteDataFromAPI_success(){


        Quote quote = new Quote();

        quote.setTicker("MSFT");


        when(httpHelper.fetchQuoteInfo("MSFT"))
                .thenReturn(quote);


        when(dao.save(quote))
                .thenReturn(quote);



        Optional<Quote> result =
                service.fetchQuoteDataFromAPI("MSFT");



        assertTrue(result.isPresent());

        assertEquals(
                "MSFT",
                result.get().getTicker()
        );



        verify(httpHelper)
                .fetchQuoteInfo("MSFT");


        verify(dao)
                .save(quote);

    }

    @Test
    public void fetchQuoteDataFromAPI_invalidTicker(){


        when(httpHelper.fetchQuoteInfo("BAD"))
                .thenThrow(
                        new IllegalArgumentException()
                );



        Optional<Quote> result =
                service.fetchQuoteDataFromAPI("BAD");



        assertTrue(
                result.isEmpty()
        );


        verify(dao, never())
                .save(any());

    }

    @Test
    public void fetchQuoteDataFromAPI_saveFailure(){


        Quote quote = new Quote();

        quote.setTicker("MSFT");



        when(httpHelper.fetchQuoteInfo("MSFT"))
                .thenReturn(quote);



        when(dao.save(quote))
                .thenThrow(
                        new RuntimeException()
                );



        assertThrows(
                RuntimeException.class,
                () ->
                        service.fetchQuoteDataFromAPI("MSFT")
        );

    }



}
