package com.kwarcinski.exchanges;

public enum ExchangeEnum {
    BitStamp("BitStamp"),BitBay("BitBay"),OkCoin("OkCoin"),CoinBase("CoinBase"),BitFinex("BitFinex");

    String name ;

    ExchangeEnum(String name) {
        this.name = name;
    }
}
