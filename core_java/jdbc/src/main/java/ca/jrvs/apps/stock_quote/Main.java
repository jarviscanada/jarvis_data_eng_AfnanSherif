package ca.jrvs.apps.stock_quote;

import ca.jrvs.apps.stock_quote.controller.StockQuoteController;
import ca.jrvs.apps.stock_quote.dao.PositionDao;
import ca.jrvs.apps.stock_quote.dao.QuoteDao;
import ca.jrvs.apps.stock_quote.helper.QuoteHttpHelper;
import ca.jrvs.apps.stock_quote.service.PositionService;
import ca.jrvs.apps.stock_quote.service.QuoteService;

import okhttp3.OkHttpClient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.sql.Connection;
import java.util.Properties;


public class Main {

    private static final Logger logger =
            LoggerFactory.getLogger(Main.class);


    public static void main(String[] args) {

        Properties properties = new Properties();


        try (InputStream input =
                     Main.class
                             .getClassLoader()
                             .getResourceAsStream("properties.txt")) {


            if (input == null) {
                throw new RuntimeException(
                        "properties.txt not found"
                );
            }


            properties.load(input);


            // Database properties
            String server =
                    properties.getProperty("server");

            String port =
                    properties.getProperty("port");

            String database =
                    properties.getProperty("database");

            String username =
                    properties.getProperty("username");

            String password =
                    properties.getProperty("password");


            // API key
            String apiKey =
                    properties.getProperty("api-key");


            if (apiKey == null || apiKey.isEmpty()) {
                throw new RuntimeException(
                        "API key missing from properties.txt"
                );
            }



            /*
             * Database connection
             */
            DatabaseConnectionManager manager =
                    new DatabaseConnectionManager(
                            server,
                            port,
                            database,
                            username,
                            password
                    );


            try (Connection connection =
                         manager.getConnection()) {


                logger.info(
                        "Database connected successfully"
                );


                /*
                 * DAO Layer
                 */
                QuoteDao quoteDao =
                        new QuoteDao(connection);


                PositionDao positionDao =
                        new PositionDao(connection);



                /*
                 * HTTP Layer
                 */
                OkHttpClient httpClient =
                        new OkHttpClient();


                QuoteHttpHelper quoteHttpHelper =
                        new QuoteHttpHelper(
                                apiKey,
                                httpClient
                        );



                /*
                 * Service Layer
                 */
                QuoteService quoteService =
                        new QuoteService(
                                quoteDao,
                                quoteHttpHelper
                        );


                PositionService positionService =
                        new PositionService(
                                positionDao
                        );



                /*
                 * Controller Layer
                 */
                StockQuoteController controller =
                        new StockQuoteController(
                                quoteService,
                                positionService
                        );


                /*
                 * Start application
                 */
                controller.initClient();


            }


        } catch (Exception e) {

            logger.error(
                    "Application failed to start",
                    e
            );

        }

    }
}