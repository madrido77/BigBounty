
package com.kwarcinski.exchanges.bitfinex.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;

@Data
public class Ticker {

    private String SYMBOL;
    private String BID;
    private String BID_SIZE;
    private String ASK;
    private String ASK_SIZE;
    private String DAILY_CHANGE;
    private String DAILY_CHANGE_RELATIVE;
    private String LAST_PRICE;
    private String VOLUME;
    private String HIGH;
    private String LOW;

    public Ticker(String SYMBOL, String BID, String BID_SIZE, String ASK, String ASK_SIZE, String DAILY_CHANGE, String DAILY_CHANGE_RELATIVE, String LAST_PRICE, String VOLUME, String HIGH, String LOW) {
        this.SYMBOL = SYMBOL;
        this.BID = BID;
        this.BID_SIZE = BID_SIZE;
        this.ASK = ASK;
        this.ASK_SIZE = ASK_SIZE;
        this.DAILY_CHANGE = DAILY_CHANGE;
        this.DAILY_CHANGE_RELATIVE = DAILY_CHANGE_RELATIVE;
        this.LAST_PRICE = LAST_PRICE;
        this.VOLUME = VOLUME;
        this.HIGH = HIGH;
        this.LOW = LOW;
    }
}
