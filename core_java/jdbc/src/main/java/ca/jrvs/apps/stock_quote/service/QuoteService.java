package ca.jrvs.apps.stock_quote.service;

import ca.jrvs.apps.stock_quote.dao.QuoteDao;
import ca.jrvs.apps.stock_quote.helper.QuoteHttpHelper;
import ca.jrvs.apps.stock_quote.model.Quote;

import java.util.Optional;

public class QuoteService {


    private final QuoteDao dao;
    private final QuoteHttpHelper httpHelper;


    public QuoteService(
            QuoteDao dao,
            QuoteHttpHelper httpHelper
    ){

        this.dao = dao;
        this.httpHelper = httpHelper;

    }


    public Optional<Quote> fetchQuoteDataFromAPI(String ticker){


        if(ticker == null || ticker.isBlank()){

            throw new IllegalArgumentException(
                    "Ticker cannot be null or empty"
            );

        }


        try{

            Quote quote =
                    httpHelper.fetchQuoteInfo(ticker);


            dao.save(quote);


            return Optional.of(quote);


        }catch(IllegalArgumentException e){

            return Optional.empty();

        }

    }

}