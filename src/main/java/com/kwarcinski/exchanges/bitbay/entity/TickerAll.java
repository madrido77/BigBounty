
package com.kwarcinski.exchanges.bitbay.entity;

import lombok.Data;

import java.util.Map;

@Data
public class TickerAll {

    private String status;

    private Map<String, Item> items;

}
