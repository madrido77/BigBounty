
package com.kwarcinski.exchanges.okcoin.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.ToString;

@Data
public class Ticker {

    @SerializedName("best_ask")
    @Expose
    private String bestAsk;
    @SerializedName("best_bid")
    @Expose
    private String bestBid;
    @SerializedName("instrument_id")
    @Expose
    private String instrumentId;
    @SerializedName("product_id")
    @Expose
    private String productId;
    @SerializedName("last")
    @Expose
    private String last;
    @SerializedName("last_qty")
    @Expose
    private String lastQty;
    @SerializedName("ask")
    @Expose
    private String ask;
    @SerializedName("best_ask_size")
    @Expose
    private String bestAskSize;
    @SerializedName("bid")
    @Expose
    private String bid;
    @SerializedName("best_bid_size")
    @Expose
    private String bestBidSize;
    @SerializedName("open_24h")
    @Expose
    private String open24h;
    @SerializedName("high_24h")
    @Expose
    private String high24h;
    @SerializedName("low_24h")
    @Expose
    private String low24h;
    @SerializedName("base_volume_24h")
    @Expose
    private String baseVolume24h;
    @SerializedName("timestamp")
    @Expose
    private String timestamp;
    @SerializedName("quote_volume_24h")
    @Expose
    private String quoteVolume24h;

}
