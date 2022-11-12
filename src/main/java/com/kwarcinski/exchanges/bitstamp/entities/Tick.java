package com.kwarcinski.exchanges.bitstamp.entities;

import lombok.Data;
import lombok.ToString;


@Data
public class Tick {

    private String symbol;

    private String high;

    private String last;

    private String timestamp;

    private String bid;

    private String vwap;

    private String volume;

    private String low;

    private String ask;

    private String open;


}
