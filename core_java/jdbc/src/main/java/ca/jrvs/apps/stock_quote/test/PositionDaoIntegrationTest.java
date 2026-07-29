package ca.jrvs.apps.stock_quote.test;


import ca.jrvs.apps.stock_quote.dao.PositionDao;
import ca.jrvs.apps.stock_quote.model.Position;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Optional;


public class PositionDaoIntegrationTest {


    private static Connection connection;
    private static PositionDao positionDao;


    public static void main(String[] args) throws SQLException {


        // Connect to database
        connection = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/stock_quote",
                "postgres",
                "password"
        );


        positionDao = new PositionDao(connection);


        System.out.println("Connected successfully");


        cleanup();

        insertQuote();

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



    /*
     * Insert a quote first because position has a foreign key
     */
    private static void insertQuote() throws SQLException {


        String sql =
                """
                INSERT INTO quote
                (
                    symbol,
                    open,
                    high,
                    low,
                    price,
                    volume,
                    latest_trading_day,
                    previous_close,
                    change,
                    change_percent,
                    timestamp
                )
                VALUES
                (
                    'MSFT',
                    380,
                    390,
                    375,
                    385,
                    10000,
                    '2026-07-27',
                    382,
                    3,
                    '0.5%',
                    CURRENT_TIMESTAMP
                )
                ON CONFLICT(symbol) DO NOTHING
                """;


        connection
                .createStatement()
                .execute(sql);

    }




    private static Position createPosition(){


        Position position = new Position();


        position.setTicker("MSFT");

        position.setNumOfShares(10);

        position.setValuePaid(3000);


        return position;

    }




    private static void testSave(){


        Position position = createPosition();


        Position saved =
                positionDao.save(position);


        if(saved.getTicker().equals("MSFT")){

            System.out.println("PASSED: save()");

        }else{

            System.out.println("FAILED: save()");

        }

    }





    private static void testFindById(){


        Optional<Position> result =
                positionDao.findById("MSFT");



        if(result.isPresent()
                && result.get().getTicker().equals("MSFT")){


            System.out.println("PASSED: findById()");


        }else{


            System.out.println("FAILED: findById()");

        }


    }





    private static void testFindAll(){


        Iterable<Position> positions =
                positionDao.findAll();


        boolean found = false;


        for(Position p : positions){

            if(p.getTicker().equals("MSFT")){

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


        Position position = new Position();


        position.setTicker("MSFT");

        position.setNumOfShares(50);

        position.setValuePaid(15000);



        positionDao.save(position);



        Optional<Position> result =
                positionDao.findById("MSFT");



        if(result.isPresent()
                && result.get().getNumOfShares() == 50){


            System.out.println("PASSED: update/upsert");


        }else{


            System.out.println("FAILED: update/upsert");

        }


    }





    private static void testDeleteById(){


        positionDao.deleteById("MSFT");



        Optional<Position> result =
                positionDao.findById("MSFT");



        if(result.isEmpty()){


            System.out.println("PASSED: deleteById()");


        }else{


            System.out.println("FAILED: deleteById()");

        }


    }





    private static void testDeleteAll(){


        Position position = createPosition();


        positionDao.save(position);


        positionDao.deleteAll();



        Iterable<Position> positions =
                positionDao.findAll();



        if(!positions.iterator().hasNext()){


            System.out.println("PASSED: deleteAll()");


        }else{


            System.out.println("FAILED: deleteAll()");

        }


    }





    private static void cleanup(){

        try {

            positionDao.deleteAll();

            connection.createStatement()
                    .execute("DELETE FROM quote");

        }catch(Exception ignored){

        }

    }

}