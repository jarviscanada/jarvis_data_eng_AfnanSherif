package ca.jrvs.apps.stock_quote.service;


import ca.jrvs.apps.stock_quote.dao.PositionDao;
import ca.jrvs.apps.stock_quote.model.Position;

import java.util.Optional;


public class PositionService {


    private final PositionDao dao;


    public PositionService(PositionDao dao){

        this.dao = dao;

    }



    public Position buy(
            String ticker,
            int numberOfShares,
            double price
    ){


        if(ticker == null || ticker.isBlank()){

            throw new IllegalArgumentException(
                    "Ticker cannot be null"
            );

        }


        if(numberOfShares <= 0){

            throw new IllegalArgumentException(
                    "Shares must be positive"
            );

        }


        if(price <= 0){

            throw new IllegalArgumentException(
                    "Price must be positive"
            );

        }



        double cost =
                numberOfShares * price;



        Optional<Position> existing =
                dao.findById(ticker);



        Position position;



        if(existing.isPresent()){


            position = existing.get();


            position.setNumOfShares(
                    position.getNumOfShares()
                            + numberOfShares
            );


            position.setValuePaid(
                    position.getValuePaid()
                            + cost
            );


        }else{


            position = new Position();


            position.setTicker(ticker);


            position.setNumOfShares(
                    numberOfShares
            );


            position.setValuePaid(
                    cost
            );

        }


        return dao.save(position);

    }




    public Iterable<Position> viewPortfolio(){

        return dao.findAll();

    }




    public void sell(String ticker){


        if(ticker == null || ticker.isBlank()){

            throw new IllegalArgumentException(
                    "Ticker cannot be null"
            );

        }


        Optional<Position> position =
                dao.findById(ticker);



        if(position.isEmpty()){

            throw new IllegalArgumentException(
                    "You do not own any shares of "
                            + ticker
            );

        }


        dao.deleteById(ticker);

    }

}