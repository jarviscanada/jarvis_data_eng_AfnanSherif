package ca.jrvs.apps.stock_quote.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Date;
import java.sql.Timestamp;


/**
 * DTO representing stock quote information returned from Alpha Vantage API.
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


    @JsonProperty("07. latest trading day")
    private Date latestTradingDay;


    @JsonProperty("08. previous close")
    private String previousClose;


    @JsonProperty("09. change")
    private String change;


    @JsonProperty("10. change percent")
    private String changePercent;


    private Timestamp timestamp;



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



    public Date getLatestTradingDay() {
        return latestTradingDay;
    }


    public void setLatestTradingDay(Date latestTradingDay) {
        this.latestTradingDay = latestTradingDay;
    }



    public String getPreviousClose() {
        return previousClose;
    }


    public void setPreviousClose(String previousClose) {
        this.previousClose = previousClose;
    }



    public String getChange() {
        return change;
    }


    public void setChange(String change) {
        this.change = change;
    }



    public String getChangePercent() {
        return changePercent;
    }


    public void setChangePercent(String changePercent) {
        this.changePercent = changePercent;
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
                ", latestTradingDay=" + latestTradingDay +
                ", previousClose='" + previousClose + '\'' +
                ", change='" + change + '\'' +
                ", changePercent='" + changePercent + '\'' +
                ", timestamp=" + timestamp +
                '}';

    }
}