package com.kwarcinski.exchanges.coinbase.entities;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Ticker {

    private String symbol;
    private TickData sell;
    private TickData buy;


}
