package com.kwarcinski.worker.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kwarcinski.exchanges.bitbay.entity.Market;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class GlobalTicker {

    private String currencyPair;

    private String currencyFirst;

    private String currencySecond;

    private String exchange;

    private String time;

    private String bestAsk;

    private String bestBid;


    public Double getBidDouble(){
        return Double.parseDouble(bestBid);
    }

    public Double getAskDouble(){
        return Double.parseDouble(bestAsk);
    }
}
