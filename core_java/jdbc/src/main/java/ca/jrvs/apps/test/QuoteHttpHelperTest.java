package ca.jrvs.apps.test;

import ca.jrvs.apps.stock_quote.Quote;
import ca.jrvs.apps.stock_quote.QuoteHttpHelper;

import java.net.http.HttpClient;

public class QuoteHttpHelperTest {

    private static final String API_KEY = "YOUR_API_KEY";


    public static void main(String[] args) {

        QuoteHttpHelper helper =
                new QuoteHttpHelper(
                        API_KEY,
                        HttpClient.newHttpClient()
                );


        testValidSymbol(helper);

        testInvalidSymbol(helper);

        testNullSymbol(helper);

        testEmptySymbol(helper);

    }


    /**
     * Test successful API call
     */
    private static void testValidSymbol(QuoteHttpHelper helper) {

        try {

            Thread.sleep(12000); // wait 12 seconds

            Quote quote =
                    helper.fetchQuoteInfo("MSFT");


            if(quote != null &&
                    quote.getTicker() != null) {

                System.out.println(
                        "PASSED: Valid symbol returns quote"
                );

                System.out.println(quote);

            }


        } catch(Exception e) {

            System.out.println(
                    "FAILED: Valid symbol test"
            );

            e.printStackTrace();
        }
    }



    /**
     * Test invalid stock ticker
     */
    private static void testInvalidSymbol(QuoteHttpHelper helper) {

        try {

            helper.fetchQuoteInfo("XYZXYZ");


            System.out.println(
                    "FAILED: Invalid symbol should throw exception"
            );


        } catch(IllegalArgumentException e) {


            System.out.println(
                    "PASSED: Invalid symbol throws exception"
            );

        }
    }



    /**
     * Test null input
     */
    private static void testNullSymbol(QuoteHttpHelper helper) {

        try {

            helper.fetchQuoteInfo(null);


            System.out.println(
                    "FAILED: Null input should throw exception"
            );


        } catch(IllegalArgumentException e) {


            System.out.println(
                    "PASSED: Null input throws exception"
            );

        }

    }



    /**
     * Test empty string input
     */
    private static void testEmptySymbol(QuoteHttpHelper helper) {

        try {

            helper.fetchQuoteInfo("");


            System.out.println(
                    "FAILED: Empty input should throw exception"
            );


        } catch(IllegalArgumentException e) {


            System.out.println(
                    "PASSED: Empty input throws exception"
            );

        }

    }

}