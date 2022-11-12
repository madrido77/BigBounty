package com.kwarcinski.worker.coverter;

import com.kwarcinski.exchanges.bitfinex.BitfinexApi;
import com.kwarcinski.exchanges.bitfinex.entity.Ticker;
import com.kwarcinski.worker.entity.GlobalTicker;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class TickerBitfinexConverter extends AbstractTickerConverter<BitfinexApi, GlobalTicker> {


    @Override
    public GlobalTicker getGlobalTicker() throws IOException {
        Ticker tick = api.getTickerSingle().get(0);
        return GlobalTicker.builder()
                .bestAsk(tick.getASK().toString())
                .bestBid(tick.getBID().toString())
                .exchange("Bitfinex")
                .time(LocalDateTime.now().toString())
                .build();
    }
    @Override
    public Map<String, GlobalTicker> getGlobalTickerAll() throws IOException {
        Map<String, Ticker> ticks = api.getTickerAll();
        Map<String, GlobalTicker> tickersMap = ticks.entrySet().stream().map(f -> GlobalTicker.builder()
                .bestAsk(f.getValue().getASK().toString())
                .bestBid(f.getValue().getBID().toString())
                .currencyPair(f.getKey())
//                .currencySecond(f.getKey().split("-")[0])
//                .currencyFirst(f.getKey().split("-")[1])
                .exchange("Bitfinex")
                .time(LocalDateTime.now().toString())
                .build()).collect(Collectors.toMap(GlobalTicker::getCurrencyPair, item -> item));
        return tickersMap;
    }

}
