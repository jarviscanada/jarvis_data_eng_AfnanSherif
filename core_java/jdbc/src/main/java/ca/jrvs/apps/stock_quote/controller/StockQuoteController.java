package ca.jrvs.apps.stock_quote.controller;


import ca.jrvs.apps.stock_quote.model.Position;
import ca.jrvs.apps.stock_quote.model.Quote;
import ca.jrvs.apps.stock_quote.service.PositionService;
import ca.jrvs.apps.stock_quote.service.QuoteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.Scanner;



public class StockQuoteController {


    private static final Logger logger =
            LoggerFactory.getLogger(StockQuoteController.class);



    private final QuoteService quoteService;

    private final PositionService positionService;



    public StockQuoteController(
            QuoteService quoteService,
            PositionService positionService) {


        this.quoteService = quoteService;

        this.positionService = positionService;

    }




    /**
     * Starts the command line user interface.
     */
    public void initClient() {

        Scanner scanner = new Scanner(System.in);

        boolean running = true;

        while (running) {

            System.out.println("\n=== Stock Quote App ===");
            System.out.println("1. View stock quote");
            System.out.println("2. Buy shares");
            System.out.println("3. Sell shares");
            System.out.println("4. View portfolio");
            System.out.println("q. Quit");
            System.out.print("Enter choice: ");

            String choice = scanner.nextLine().trim().toUpperCase();

            switch (choice) {

                case "1":
                    handleViewQuote(scanner);
                    if (!promptNextAction(scanner)) {
                        running = false;
                    }
                    break;

                case "2":
                    handleBuy(scanner);
                    if (!promptNextAction(scanner)) {
                        running = false;
                    }
                    break;

                case "3":
                    handleSell(scanner);
                    if (!promptNextAction(scanner)) {
                        running = false;
                    }
                    break;

                case "4":
                    handleViewPortfolio();
                    if (!promptNextAction(scanner)) {
                        running = false;
                    }
                    break;

                case "Q":
                    running = false;
                    System.out.println("Goodbye!");
                    break;

                default:
                    System.out.println("Invalid choice.");
            }
        }

        scanner.close();
    }




    private void printMenu() {


        System.out.println();
        System.out.println("=== Stock Quote App ===");
        System.out.println("--- Menu ---");
        System.out.println("1. View stock quote");
        System.out.println("2. Buy shares");
        System.out.println("3. Sell shares");
        System.out.println("4. View portfolio");
        System.out.println("q. Quit");

    }





    private void handleViewQuote(
            Scanner scanner) {


        System.out.print(
                "Enter ticker symbol: "
        );


        String ticker =
                scanner.nextLine()
                        .trim()
                        .toUpperCase();



        Optional<Quote> quote =
                quoteService
                        .fetchQuoteDataFromAPI(ticker);



        if(quote.isPresent()) {


            Quote q =
                    quote.get();



            System.out.println();
            System.out.println("Quote Information");
            System.out.println("------------------");

            System.out.println(
                    "Ticker: " + q.getTicker()
            );


            System.out.println(
                    "Price: " + q.getPrice()
            );


            System.out.println(
                    "Open: " + q.getOpen()
            );


            System.out.println(
                    "High: " + q.getHigh()
            );


            System.out.println(
                    "Low: " + q.getLow()
            );


            System.out.println(
                    "Volume: " + q.getVolume()
            );


            System.out.println(
                    "Previous Close: "
                            + q.getPreviousClose()
            );


            System.out.println(
                    "Change: "
                            + q.getChange()
            );


            System.out.println(
                    "Change Percent: "
                            + q.getChangePercent()
            );


            System.out.println(
                    "Trading Day: "
                            + q.getLatestTradingDay()
            );


        } else {


            System.out.println(
                    "Could not find quote for: "
                            + ticker
            );

        }

    }





    private void handleBuy(
            Scanner scanner) {


        System.out.print(
                "Enter ticker symbol: "
        );


        String ticker =
                scanner.nextLine()
                        .trim()
                        .toUpperCase();



        Optional<Quote> quote =
                quoteService
                        .fetchQuoteDataFromAPI(ticker);



        if(quote.isEmpty()) {


            System.out.println(
                    "Could not find quote for: "
                            + ticker
            );


            return;

        }



        System.out.println(
                "Current price: "
                        + quote.get().getPrice()
        );



        System.out.print(
                "Number of shares: "
        );



        try {


            int shares =
                    Integer.parseInt(
                            scanner.nextLine()
                                    .trim()
                    );



            Position position =
                    positionService.buy(
                            ticker,
                            shares,
                            Double.parseDouble(
                                    quote.get()
                                            .getPrice()
                            )
                    );



            System.out.println(
                    "Purchase successful!"
            );


            System.out.println(
                    "Total shares: "
                            + position.getNumOfShares()
            );


            System.out.println(
                    "Total value paid: "
                            + position.getValuePaid()
            );



        } catch(NumberFormatException e) {


            System.out.println(
                    "Invalid number of shares"
            );


        } catch(IllegalArgumentException e) {


            System.out.println(
                    "Buy failed: "
                            + e.getMessage()
            );

        }

    }





    private void handleSell(
            Scanner scanner) {


        System.out.print(
                "Enter ticker symbol: "
        );


        String ticker =
                scanner.nextLine()
                        .trim()
                        .toUpperCase();



        try {


            positionService.sell(ticker);



            System.out.println(
                    "Sold all shares of "
                            + ticker
            );



        } catch(IllegalArgumentException e) {


            System.out.println(
                    "Sell failed: "
                            + e.getMessage()
            );

        }

    }





    private void handleViewPortfolio() {


        Iterable<Position> positions =
                positionService.viewPortfolio();



        boolean empty = true;



        System.out.println();

        System.out.println(
                "Portfolio"
        );

        System.out.println(
                "---------"
        );



        for(Position position : positions) {


            empty = false;


            System.out.println(
                    "Ticker: "
                            + position.getTicker()
            );


            System.out.println(
                    "Shares: "
                            + position.getNumOfShares()
            );


            System.out.println(
                    "Value Paid: "
                            + position.getValuePaid()
            );


            System.out.println();

        }



        if(empty) {


            System.out.println(
                    "No positions held."
            );

        }

    }
    private boolean promptNextAction(Scanner scanner) {

        while (true) {
            System.out.println();
            System.out.println("--------------------------------");
            System.out.println("Press M to return to the menu");
            System.out.println("Press Q to quit");
            System.out.print("Choice: ");

            String input = scanner.nextLine().trim().toUpperCase();

            switch (input) {
                case "M":
                    return true;

                case "Q":
                    System.out.println("Goodbye!");
                    return false;

                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

}