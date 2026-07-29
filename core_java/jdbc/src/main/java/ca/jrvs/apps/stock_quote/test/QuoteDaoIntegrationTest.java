package ca.jrvs.apps.stock_quote.test;


import ca.jrvs.apps.stock_quote.dao.PositionDao;
import ca.jrvs.apps.stock_quote.dao.QuoteDao;
import ca.jrvs.apps.stock_quote.model.Quote;

import java.sql.*;
import java.util.Optional;


public class QuoteDaoIntegrationTest {


    private static Connection connection;
    private static QuoteDao quoteDao;



    public static void main(String[] args) throws SQLException {


        connection = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/stock_quote",
                "postgres",
                "password"
        );


        quoteDao = new QuoteDao(connection);


        System.out.println("Connected successfully");


        cleanup();


        testSave();

        testFindById();

        testFindAll();

        testUpdate();

        testDeleteById();

        testDeleteAll();


        cleanup();


        connection.close();


        System.out.println("All tests completed");

    }





    private static Quote createQuote(){


        Quote quote = new Quote();


        quote.setTicker("MSFT");


        quote.setOpen("380.00");

        quote.setHigh("390.00");

        quote.setLow("375.00");

        quote.setPrice("385.00");


        quote.setVolume("10000");


        quote.setLatestTradingDay(
                Date.valueOf("2026-07-27")
        );


        quote.setPreviousClose("382.00");


        quote.setChange("3.00");


        quote.setChangePercent("0.5%");


        quote.setTimestamp(
                new Timestamp(
                        System.currentTimeMillis()
                )
        );


        return quote;

    }






    private static void testSave(){


        Quote quote = createQuote();


        Quote saved = quoteDao.save(quote);



        if(saved.getTicker().equals("MSFT")){


            System.out.println("PASSED: save()");


        }else{


            System.out.println("FAILED: save()");

        }

    }






    private static void testFindById(){


        Optional<Quote> result =
                quoteDao.findById("MSFT");



        if(result.isPresent()
                && result.get().getTicker().equals("MSFT")){


            System.out.println("PASSED: findById()");


        }else{


            System.out.println("FAILED: findById()");

        }

    }







    private static void testFindAll(){


        Iterable<Quote> quotes =
                quoteDao.findAll();



        boolean found = false;



        for(Quote q : quotes){


            if(q.getTicker().equals("MSFT")){


                found = true;


            }

        }



        if(found){


            System.out.println("PASSED: findAll()");


        }else{


            System.out.println("FAILED: findAll()");

        }

    }







    private static void testUpdate(){


        Quote quote = createQuote();


        quote.setPrice("400.00");


        quoteDao.save(quote);



        Optional<Quote> result =
                quoteDao.findById("MSFT");



        if(result.isPresent()
                && Double.parseDouble(result.get().getPrice()) == 400.0){


            System.out.println("PASSED: update/upsert");


        }else{


            System.out.println("FAILED: update/upsert");


        }

    }







    private static void testDeleteById(){


        quoteDao.deleteById("MSFT");



        Optional<Quote> result =
                quoteDao.findById("MSFT");



        if(result.isEmpty()){


            System.out.println("PASSED: deleteById()");


        }else{


            System.out.println("FAILED: deleteById()");


        }

    }







    private static void testDeleteAll(){


        quoteDao.save(createQuote());


        quoteDao.deleteAll();



        Iterable<Quote> quotes =
                quoteDao.findAll();



        if(!quotes.iterator().hasNext()){


            System.out.println("PASSED: deleteAll()");


        }else{


            System.out.println("FAILED: deleteAll()");


        }

    }






    private static void cleanup(){

        try {

            PositionDao positionDao =
                    new PositionDao(connection);

            // Delete child table first because of FK constraint
            positionDao.deleteAll();

            // Delete parent table after
            quoteDao.deleteAll();

        }catch(Exception ignored){

        }

    }

}