package ca.jrvs.apps.stock_quote.test;

import ca.jrvs.apps.stock_quote.helper.QuoteHttpHelper;
import ca.jrvs.apps.stock_quote.model.Quote;
import okhttp3.OkHttpClient;

public class QuoteHttpHelperTest {

    public static void main(String[] args) {


        String apiKey = "YOUR_ALPHA_VANTAGE_KEY";


        OkHttpClient client =
                new OkHttpClient();


        QuoteHttpHelper helper =
                new QuoteHttpHelper(
                        apiKey,
                        client
                );


        try {

            Quote quote =
                    helper.fetchQuoteInfo("MSFT");


            System.out.println("API call successful");
            System.out.println(quote);


            if(quote.getTicker().equals("MSFT")) {

                System.out.println("PASSED: Valid ticker returned");

            } else {

                System.out.println("FAILED: Wrong ticker");

            }


        } catch(Exception e) {

            System.out.println("FAILED: API request");
            e.printStackTrace();

        }

    }

}