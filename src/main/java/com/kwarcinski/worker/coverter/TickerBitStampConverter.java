package com.kwarcinski.worker.coverter;

import com.kwarcinski.exchanges.bitstamp.BitStampApi;
import com.kwarcinski.exchanges.bitstamp.entities.Tick;
import com.kwarcinski.worker.entity.GlobalTicker;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class TickerBitStampConverter extends AbstractTickerConverter<BitStampApi, GlobalTicker> {

    @Override
    public GlobalTicker getGlobalTicker() throws IOException {
        Tick tick = api.getTickerSingle();
        return GlobalTicker.builder()
                .bestAsk(tick.getAsk().toString())
                .bestBid(tick.getBid().toString())
                .exchange("BitStamp")
                .time(LocalDateTime.now().toString())
                .build();
    }

    @Override
    public Map<String, GlobalTicker> getGlobalTickerAll() throws IOException {
        Map<String, Tick> ticks = api.getTickerAll();
        Map<String, GlobalTicker> tickersMap = ticks.entrySet().stream().map(f -> GlobalTicker.builder()
                .bestAsk(f.getValue().getAsk().toString())
                .bestBid(f.getValue().getBid().toString())
                .currencyPair(f.getKey())
//                .currencySecond(f.getKey().split("-")[0])
//                .currencyFirst(f.getKey().split("-")[1])
                .exchange("BitStamp")
                .time(LocalDateTime.now().toString())
                .build()).collect(Collectors.toMap(GlobalTicker::getCurrencyPair, item -> item));
        return tickersMap;
    }

}
