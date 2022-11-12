package com.kwarcinski.worker.coverter;

import com.kwarcinski.exchanges.coinbase.CoinBaseApi;
import com.kwarcinski.exchanges.coinbase.entities.Data;
import com.kwarcinski.exchanges.coinbase.entities.Ticker;
import com.kwarcinski.worker.entity.GlobalTicker;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class TickerCoinbaseConverter extends AbstractTickerConverter<CoinBaseApi, GlobalTicker> {

    @Override
    public GlobalTicker getGlobalTicker() throws IOException {
        Data tickBuy = api.getTickerSingleBuy().getData();
        Data tickSell = api.getTickerSingleSell().getData();
        return GlobalTicker.builder()
                .bestAsk(tickBuy.getAmount().toString())
                .bestBid(tickSell.getAmount().toString())
                .exchange("Coinbase")
                .time(LocalDateTime.now().toString())
                .build();
    }
    @Override
    public Map<String, GlobalTicker> getGlobalTickerAll() throws IOException {
        Map<String, Ticker> ticks = api.getTickerAll();
        Map<String, GlobalTicker> tickersMap = ticks.entrySet().stream().map(f -> GlobalTicker.builder()
                .bestAsk(f.getValue().getBuy().getData().getAmount().toString())
                .bestBid(f.getValue().getSell().getData().getAmount().toString())
                .currencyPair(f.getKey())
//                .currencySecond(f.getKey().split("-")[0])
//                .currencyFirst(f.getKey().split("-")[1])
                .exchange("Coinbase")
                .time(LocalDateTime.now().toString())
                .build()).collect(Collectors.toMap(GlobalTicker::getCurrencyPair, item -> item));
        return tickersMap;
    }

}
