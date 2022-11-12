package com.kwarcinski.exchanges.bitbay.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class Ticker {

    private String symbol;
    @SerializedName("max")
    @Expose
    private Double max;
    @SerializedName("min")
    @Expose
    private Double min;
    @SerializedName("last")
    @Expose
    private Double last;
    @SerializedName("bid")
    @Expose
    private Double bid;
    @SerializedName("ask")
    @Expose
    private Double ask;
    @SerializedName("vwap")
    @Expose
    private Double vwap;
    @SerializedName("average")
    @Expose
    private Double average;
    @SerializedName("volume")
    @Expose
    private Double volume;
}