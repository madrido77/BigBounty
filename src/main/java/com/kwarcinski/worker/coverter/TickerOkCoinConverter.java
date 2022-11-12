package com.kwarcinski.worker.coverter;

import com.kwarcinski.exchanges.okcoin.OkCoinApi;
import com.kwarcinski.exchanges.okcoin.entities.Ticker;
import com.kwarcinski.worker.entity.GlobalTicker;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class TickerOkCoinConverter extends AbstractTickerConverter<OkCoinApi, GlobalTicker> {

    @Override
    public GlobalTicker getGlobalTicker() throws IOException {
        Ticker tick = api.getTickerSingle();
        return GlobalTicker.builder()
                .bestAsk(tick.getAsk().toString())
                .bestBid(tick.getBid().toString())
                .exchange("OkCoin")
                .time(LocalDateTime.now().toString())
                .build();
    }
    @Override
    public Map<String, GlobalTicker> getGlobalTickerAll() throws IOException {
        Map<String, Ticker> ticks = api.getTickerAll();
        Map<String, GlobalTicker> tickersMap = ticks.entrySet().stream().map(f -> GlobalTicker.builder()
                .bestAsk(f.getValue().getAsk().toString())
                .bestBid(f.getValue().getBid().toString())
                .currencyPair(f.getKey())
//                .currencySecond(f.getKey().split("-")[0])
//                .currencyFirst(f.getKey().split("-")[1])
                .exchange("OkCoin")
                .time(LocalDateTime.now().toString())
                .build()).collect(Collectors.toMap(GlobalTicker::getCurrencyPair, item -> item));
        return tickersMap;
    }

}
