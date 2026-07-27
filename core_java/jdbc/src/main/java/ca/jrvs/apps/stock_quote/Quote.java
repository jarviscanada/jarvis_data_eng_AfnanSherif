package ca.jrvs.apps.stock_quote;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.sql.Timestamp;

/**
 * DTO representing stock quote information returned from Alpha Vantage API.
 *
 * Jackson uses @JsonProperty because Alpha Vantage JSON keys
 * contain numbers and spaces (example: "01. symbol").
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Quote {
    @JsonProperty("01. symbol")
    private String ticker;

    @JsonProperty("02. open")
    private String open;

    @JsonProperty("03. high")
    private String high;

    @JsonProperty("04. low")
    private String low;

    @JsonProperty("05. price")
    private String price;

    @JsonProperty("06. volume")
    private String volume;

    private Timestamp timestamp;

    // Getters and Setters
    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }


    public String getOpen() {
        return open;
    }

    public void setOpen(String open) {
        this.open = open;
    }


    public String getHigh() {
        return high;
    }

    public void setHigh(String high) {
        this.high = high;
    }


    public String getLow() {
        return low;
    }

    public void setLow(String low) {
        this.low = low;
    }


    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }


    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }


    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Quote{" +
                "ticker='" + ticker + '\'' +
                ", open='" + open + '\'' +
                ", high='" + high + '\'' +
                ", low='" + low + '\'' +
                ", price='" + price + '\'' +
                ", volume='" + volume + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
