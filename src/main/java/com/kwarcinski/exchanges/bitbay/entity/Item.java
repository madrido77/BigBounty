
package com.kwarcinski.exchanges.bitbay.entity;

import lombok.Data;
import lombok.ToString;

@Data
public class Item {

    private String symbol;

    private Market market;

    private String time;

    private String highestBid;

    private String lowestAsk;

    private String rate;

    private String previousRate;


}
