package ca.jrvs.apps.stock_quote.dao;

import ca.jrvs.apps.stock_quote.model.Position;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class PositionDao implements CrudDao<Position, String> {


    private static final Logger logger =
            LoggerFactory.getLogger(PositionDao.class);


    private final Connection connection;


    private static final String UPSERT =
            "INSERT INTO position(symbol, number_of_shares, value_paid) "
                    + "VALUES (?, ?, ?) "
                    + "ON CONFLICT(symbol) DO UPDATE SET "
                    + "number_of_shares = EXCLUDED.number_of_shares, "
                    + "value_paid = EXCLUDED.value_paid";


    private static final String FIND_BY_ID =
            "SELECT * FROM position WHERE symbol = ?";


    private static final String FIND_ALL =
            "SELECT * FROM position";


    private static final String DELETE_BY_ID =
            "DELETE FROM position WHERE symbol = ?";


    private static final String DELETE_ALL =
            "DELETE FROM position";



    public PositionDao(Connection connection) {

        this.connection = connection;

    }



    /**
     * Saves or updates a position using UPSERT.
     */
    @Override
    public Position save(Position entity)
            throws IllegalArgumentException {


        if(entity == null
                || entity.getTicker() == null
                || entity.getTicker().isBlank()) {

            throw new IllegalArgumentException(
                    "Position or ticker cannot be null or blank"
            );

        }



        try(PreparedStatement ps =
                    connection.prepareStatement(UPSERT)){


            ps.setString(
                    1,
                    entity.getTicker()
            );


            ps.setInt(
                    2,
                    entity.getNumOfShares()
            );


            ps.setDouble(
                    3,
                    entity.getValuePaid()
            );


            ps.executeUpdate();


            return entity;



        }catch(SQLException e){


            logger.error(
                    "Failed to save position {}",
                    entity.getTicker(),
                    e
            );


            throw new RuntimeException(
                    "Failed to save position",
                    e
            );

        }

    }





    /**
     * Finds a position by ticker symbol.
     */
    @Override
    public Optional<Position> findById(String id)
            throws IllegalArgumentException {


        if(id == null || id.isBlank()){

            throw new IllegalArgumentException(
                    "Ticker cannot be null or blank"
            );

        }



        try(PreparedStatement ps =
                    connection.prepareStatement(FIND_BY_ID)){


            ps.setString(
                    1,
                    id
            );


            ResultSet rs =
                    ps.executeQuery();



            if(rs.next()){


                return Optional.of(
                        mapRowToPosition(rs)
                );

            }



            return Optional.empty();



        }catch(SQLException e){


            logger.error(
                    "Failed to find position {}",
                    id,
                    e
            );


            throw new RuntimeException(
                    "Failed finding position",
                    e
            );

        }

    }





    /**
     * Retrieves all positions.
     */
    @Override
    public Iterable<Position> findAll(){


        List<Position> positions =
                new ArrayList<>();



        try(PreparedStatement ps =
                    connection.prepareStatement(FIND_ALL)){


            ResultSet rs =
                    ps.executeQuery();



            while(rs.next()){


                positions.add(
                        mapRowToPosition(rs)
                );

            }



            return positions;



        }catch(SQLException e){


            logger.error(
                    "Failed to retrieve positions",
                    e
            );


            throw new RuntimeException(
                    "Failed finding positions",
                    e
            );

        }

    }





    /**
     * Deletes a position by ticker symbol.
     */
    @Override
    public void deleteById(String id)
            throws IllegalArgumentException {


        if(id == null || id.isBlank()){

            throw new IllegalArgumentException(
                    "Ticker cannot be null or blank"
            );

        }



        try(PreparedStatement ps =
                    connection.prepareStatement(DELETE_BY_ID)){


            ps.setString(
                    1,
                    id
            );


            ps.executeUpdate();



        }catch(SQLException e){


            logger.error(
                    "Failed to delete position {}",
                    id,
                    e
            );


            throw new RuntimeException(
                    "Failed deleting position",
                    e
            );

        }

    }





    /**
     * Deletes all positions.
     */
    @Override
    public void deleteAll(){


        try(PreparedStatement ps =
                    connection.prepareStatement(DELETE_ALL)){


            ps.executeUpdate();



        }catch(SQLException e){


            logger.error(
                    "Failed to delete all positions",
                    e
            );


            throw new RuntimeException(
                    "Failed deleting positions",
                    e
            );

        }

    }





    /**
     * Maps database row into Position object.
     */
    private Position mapRowToPosition(ResultSet rs)
            throws SQLException {


        Position position =
                new Position();



        position.setTicker(
                rs.getString("symbol")
        );


        position.setNumOfShares(
                rs.getInt("number_of_shares")
        );


        position.setValuePaid(
                rs.getDouble("value_paid")
        );



        return position;

    }

}