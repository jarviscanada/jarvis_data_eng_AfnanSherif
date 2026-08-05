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

        logger.info("Stock Quote client started");

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

            logger.info("User selected option: {}", choice);

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
                    logger.info("User exited the application");
                    System.out.println("Goodbye!");
                    break;

                default:
                    logger.warn("Invalid menu choice: {}", choice);
                    System.out.println("Invalid choice.");
            }
        }

        scanner.close();

        logger.info("Stock Quote client stopped");
    }

    private void handleViewQuote(Scanner scanner) {

        System.out.print("Enter ticker symbol: ");

        String ticker =
                scanner.nextLine()
                        .trim()
                        .toUpperCase();

        logger.info("Requesting quote for ticker: {}", ticker);

        Optional<Quote> quote =
                quoteService.fetchQuoteDataFromAPI(ticker);

        if (quote.isPresent()) {

            Quote q = quote.get();

            logger.info("Quote retrieved successfully for ticker: {}", ticker);

            System.out.println();
            System.out.println("Quote Information");
            System.out.println("------------------");

            System.out.println("Ticker: " + q.getTicker());
            System.out.println("Price: " + q.getPrice());
            System.out.println("Open: " + q.getOpen());
            System.out.println("High: " + q.getHigh());
            System.out.println("Low: " + q.getLow());
            System.out.println("Volume: " + q.getVolume());
            System.out.println("Previous Close: " + q.getPreviousClose());
            System.out.println("Change: " + q.getChange());
            System.out.println("Change Percent: " + q.getChangePercent());
            System.out.println("Trading Day: " + q.getLatestTradingDay());

        } else {

            logger.warn("No quote found for ticker: {}", ticker);

            System.out.println(
                    "Could not find quote for: " + ticker
            );
        }
    }

    private void handleBuy(Scanner scanner) {

        System.out.print("Enter ticker symbol: ");

        String ticker =
                scanner.nextLine()
                        .trim()
                        .toUpperCase();

        logger.info("Buy request started for ticker: {}", ticker);

        Optional<Quote> quote =
                quoteService.fetchQuoteDataFromAPI(ticker);

        if (quote.isEmpty()) {

            logger.warn("Cannot complete buy request because no quote was found for ticker: {}",
                    ticker);

            System.out.println(
                    "Could not find quote for: " + ticker
            );

            return;
        }

        System.out.println(
                "Current price: " + quote.get().getPrice()
        );

        System.out.print("Number of shares: ");

        try {

            int shares =
                    Integer.parseInt(
                            scanner.nextLine().trim()
                    );

            Position position =
                    positionService.buy(
                            ticker,
                            shares,
                            Double.parseDouble(
                                    quote.get().getPrice()
                            )
                    );

            logger.info(
                    "Buy successful for ticker: {}, shares: {}",
                    ticker,
                    shares
            );

            System.out.println("Purchase successful!");

            System.out.println(
                    "Total shares: "
                            + position.getNumOfShares()
            );

            System.out.println(
                    "Total value paid: "
                            + position.getValuePaid()
            );

        } catch (NumberFormatException e) {

            logger.warn(
                    "Invalid number of shares entered for ticker: {}",
                    ticker
            );

            System.out.println(
                    "Invalid number of shares"
            );

        } catch (IllegalArgumentException e) {

            logger.error(
                    "Buy failed for ticker: {}",
                    ticker,
                    e
            );

            System.out.println(
                    "Buy failed: " + e.getMessage()
            );
        }
    }

    private void handleSell(Scanner scanner) {

        System.out.print("Enter ticker symbol: ");

        String ticker =
                scanner.nextLine()
                        .trim()
                        .toUpperCase();

        logger.info("Sell request started for ticker: {}", ticker);

        try {

            positionService.sell(ticker);

            logger.info(
                    "Sell successful for ticker: {}",
                    ticker
            );

            System.out.println(
                    "Sold all shares of " + ticker
            );

        } catch (IllegalArgumentException e) {

            logger.error(
                    "Sell failed for ticker: {}",
                    ticker,
                    e
            );

            System.out.println(
                    "Sell failed: " + e.getMessage()
            );
        }
    }

    private void handleViewPortfolio() {

        logger.info("Portfolio view requested");

        Iterable<Position> positions =
                positionService.viewPortfolio();

        boolean empty = true;

        System.out.println();
        System.out.println("Portfolio");
        System.out.println("---------");

        for (Position position : positions) {

            empty = false;

            System.out.println(
                    "Ticker: " + position.getTicker()
            );

            System.out.println(
                    "Shares: " + position.getNumOfShares()
            );

            System.out.println(
                    "Value Paid: " + position.getValuePaid()
            );

            System.out.println();
        }

        if (empty) {

            logger.info("Portfolio is empty");

            System.out.println("No positions held.");

        } else {

            logger.info("Portfolio retrieved successfully");
        }
    }

    private boolean promptNextAction(Scanner scanner) {

        while (true) {

            System.out.println();
            System.out.println("--------------------------------");
            System.out.println("Press M to return to the menu");
            System.out.println("Press Q to quit");
            System.out.print("Choice: ");

            String input =
                    scanner.nextLine()
                            .trim()
                            .toUpperCase();

            switch (input) {

                case "M":
                    logger.info("User returned to the main menu");
                    return true;

                case "Q":
                    logger.info("User exited the application");
                    System.out.println("Goodbye!");
                    return false;

                default:
                    logger.warn("Invalid next-action choice: {}", input);
                    System.out.println("Invalid choice.");
            }
        }
    }
}

