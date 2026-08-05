package ca.jrvs.apps.stock_quote;

import ca.jrvs.apps.stock_quote.model.Quote;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Wrapper DTO for Alpha Vantage API response.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GlobalQuoteResponse {

    @JsonProperty("Global Quote")
    private Quote quote;


    public Quote getQuote(){
        return quote;
    }


    public void setQuote(Quote quote){
        this.quote = quote;
    }

}