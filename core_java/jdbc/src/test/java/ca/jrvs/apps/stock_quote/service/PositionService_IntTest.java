package ca.jrvs.apps.stock_quote.service;


import ca.jrvs.apps.stock_quote.dao.PositionDao;
import ca.jrvs.apps.stock_quote.model.Position;

import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


public class PositionService_IntTest {


    private static Connection connection;

    private PositionDao positionDao;

    private PositionService positionService;



    @BeforeAll
    public static void setupDatabase() throws Exception {


        connection =
                DriverManager.getConnection(
                        "jdbc:postgresql://localhost:5432/stock_quote",
                        "postgres",
                        "password"
                );

    }




    @BeforeEach
    public void setup() throws Exception {


        positionDao =
                new PositionDao(connection);


        positionService =
                new PositionService(positionDao);



        // Delete child table first because of FK constraint
        positionDao.deleteAll();


        // Remove old quotes
        connection.createStatement()
                .execute("DELETE FROM quote");



        // Insert required quote before creating position
        insertQuote();

    }




    @AfterEach
    public void cleanup() throws Exception {


        // Delete child first
        positionDao.deleteAll();


        // Delete parent second
        connection.createStatement()
                .execute("DELETE FROM quote");

    }





    @AfterAll
    public static void closeConnection()
            throws Exception {


        connection.close();

    }






    private void insertQuote() throws Exception {


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
                    CURRENT_DATE,
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







    @Test
    public void buy_newPosition_createsPosition(){


        Position position =
                positionService.buy(
                        "MSFT",
                        10,
                        300
                );



        assertEquals(
                "MSFT",
                position.getTicker()
        );


        assertEquals(
                10,
                position.getNumOfShares()
        );


        assertEquals(
                3000,
                position.getValuePaid()
        );

    }







    @Test
    public void buy_existingPosition_accumulatesShares(){


        positionService.buy(
                "MSFT",
                10,
                300
        );



        Position updated =
                positionService.buy(
                        "MSFT",
                        5,
                        400
                );



        assertEquals(
                15,
                updated.getNumOfShares()
        );



        assertEquals(
                5000,
                updated.getValuePaid()
        );

    }







    @Test
    public void viewPortfolio_returnsPositions(){


        positionService.buy(
                "MSFT",
                10,
                300
        );



        Iterable<Position> positions =
                positionService.viewPortfolio();



        assertTrue(
                positions.iterator().hasNext()
        );

    }







    @Test
    public void sell_existingPosition_deletesPosition(){


        positionService.buy(
                "MSFT",
                10,
                300
        );



        positionService.sell("MSFT");



        Optional<Position> result =
                positionDao.findById("MSFT");



        assertTrue(
                result.isEmpty()
        );

    }







    @Test
    public void sell_nonExistingPosition_throwsException(){


        assertThrows(
                IllegalArgumentException.class,
                () -> positionService.sell("AAPL")
        );

    }

}