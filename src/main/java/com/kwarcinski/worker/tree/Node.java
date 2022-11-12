package com.kwarcinski.worker.tree;

import com.kwarcinski.exchanges.ExchangeEnum;
import com.kwarcinski.worker.entity.GlobalTicker;
import lombok.Data;

import java.util.List;

@Data
public class Node {

    private String currency;
    private ExchangeEnum exchange;
    private List<GlobalTicker> ticks;
    private List<Node> nodes;

}
