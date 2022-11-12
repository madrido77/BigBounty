package com.kwarcinski.worker.coverter;

import com.kwarcinski.exchanges.bitbay.BitBayApi;
import com.kwarcinski.exchanges.bitbay.entity.Ticker;
import com.kwarcinski.worker.entity.GlobalTicker;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class TickerBitBayConverter extends AbstractTickerConverter<BitBayApi, GlobalTicker> {


    @Override
    public GlobalTicker getGlobalTicker() throws IOException {
        Ticker tick = api.getTickerSingle();
        return GlobalTicker.builder()
                .bestAsk(tick.getAsk().toString())
                .bestBid(tick.getBid().toString())
                .exchange("BitBay")
                .time(LocalDateTime.now().toString())
                .build();
    }

    @Override
    public Map<String, GlobalTicker> getGlobalTickerAll() throws IOException {

        Map tickersMap = api.getTickerAll().entrySet().stream().map(f -> {
            try {
                return GlobalTicker.builder()
                        .bestAsk(f.getValue().getLowestAsk())
                        .bestBid(f.getValue().getHighestBid())
                        .currencyPair(f.getKey())
//                        .currencySecond(f.getKey().split("-")[0])
//                        .currencyFirst(f.getKey().split("-")[1])
                        .exchange("BitBay")
                        .time(LocalDateTime.now().toString())
                        .build();
            }catch (Exception e){
                System.err.println(f.toString());
            }
            return GlobalTicker.builder().build();
        }  ).collect(Collectors.toMap(GlobalTicker::getCurrencyPair, Function.identity()));
        return tickersMap;
    }


}
