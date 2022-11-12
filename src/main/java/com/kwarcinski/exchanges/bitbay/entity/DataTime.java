package com.kwarcinski.exchanges.bitbay.entity;

import lombok.Data;

@Data
public class DataTime {

    private String time;

    private CandleData candleData;
}
