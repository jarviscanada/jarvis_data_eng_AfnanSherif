package ca.jrvs.apps.stock_quote.service;


import ca.jrvs.apps.stock_quote.dao.PositionDao;
import ca.jrvs.apps.stock_quote.model.Position;

import ca.jrvs.apps.stock_quote.service.PositionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import java.util.ArrayList;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PositionService_UnitTest {


    @Mock
    private PositionDao dao;


    private PositionService service;



    @BeforeEach
    void setup(){

        MockitoAnnotations.openMocks(this);


        service =
                new PositionService(dao);

    }

    @Test
    void buy_newPosition_success(){


        when(dao.findById("MSFT"))
                .thenReturn(Optional.empty());


        Position saved =
                new Position();

        saved.setTicker("MSFT");
        saved.setNumOfShares(10);
        saved.setValuePaid(1000);



        when(dao.save(any(Position.class)))
                .thenReturn(saved);



        Position result =
                service.buy(
                        "MSFT",
                        10,
                        100
                );



        assertEquals(
                "MSFT",
                result.getTicker()
        );


        assertEquals(
                10,
                result.getNumOfShares()
        );


        assertEquals(
                1000,
                result.getValuePaid()
        );


        verify(dao).save(any(Position.class));

    }

    @Test
    void buy_existingPosition_accumulates(){


        Position existing =
                new Position();

        existing.setTicker("MSFT");
        existing.setNumOfShares(10);
        existing.setValuePaid(1000);



        when(dao.findById("MSFT"))
                .thenReturn(Optional.of(existing));



        when(dao.save(any(Position.class)))
                .thenAnswer(i -> i.getArgument(0));



        Position result =
                service.buy(
                        "MSFT",
                        5,
                        100
                );



        assertEquals(
                15,
                result.getNumOfShares()
        );


        assertEquals(
                1500,
                result.getValuePaid()
        );

    }

    @Test
    void buy_zeroShares_throwsException(){


        assertThrows(
                IllegalArgumentException.class,
                () ->
                        service.buy(
                                "MSFT",
                                0,
                                100
                        )
        );

    }

    @Test
    void viewPortfolio_returnsPositions(){


        when(dao.findAll())
                .thenReturn(
                        new ArrayList<>()
                );


        Iterable<Position> result =
                service.viewPortfolio();


        assertNotNull(result);


        verify(dao).findAll();

    }

    @Test
    void sell_existingPosition_deletes(){


        Position position =
                new Position();


        when(dao.findById("MSFT"))
                .thenReturn(
                        Optional.of(position)
                );



        service.sell("MSFT");


        verify(dao)
                .deleteById("MSFT");

    }

    @Test
    void sell_missingPosition_throwsException(){


        when(dao.findById("MSFT"))
                .thenReturn(Optional.empty());



        assertThrows(
                IllegalArgumentException.class,
                () ->
                        service.sell("MSFT")
        );

    }



}
