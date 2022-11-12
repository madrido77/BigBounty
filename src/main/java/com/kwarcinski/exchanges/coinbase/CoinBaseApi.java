package com.kwarcinski.exchanges.coinbase;

import com.google.gson.Gson;
import com.kwarcinski.exchanges.Exchange;
import com.kwarcinski.exchanges.bitstamp.entities.Tick;
import com.kwarcinski.exchanges.coinbase.entities.TickData;
import com.kwarcinski.exchanges.coinbase.entities.Ticker;
import com.kwarcinski.network.HttpReader;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Component
public class CoinBaseApi implements Exchange {
    public static Logger logger = Logger.getGlobal();

    private String MAIN_URL = "https://api.coinbase.com/v2/prices";
    private String BUY_URL = "/buy";
    private String SELL_URL = "/sell";
    private String SPOT_URL = "/spot";


    private String[] curenciesPairs = {"BTC-USD", "BTC-EUR", "XRP-USD", "XRP-EUR", "LTC-USD", "LTC-EUR",  "ETH-USD", "ETH-EUR", "BCH-USD", "BCH-EUR"};

    public TickData getTickerSingleBuy() throws IOException {

        Map<String, String> headers = new HashMap<>();

        String data = HttpReader.getUrlSource(MAIN_URL + "/" + curenciesPairs[0] + BUY_URL, headers);

        TickData tick = new Gson().fromJson(data, TickData.class);
//        logger.info(tick.toString());
        return tick;
    }

    public TickData getTickerSingleSell() throws IOException {

        Map<String, String> headers = new HashMap<>();

        String data = HttpReader.getUrlSource(MAIN_URL + "/" + curenciesPairs[0] + SELL_URL, headers);

        TickData tick = new Gson().fromJson(data, TickData.class);
//        logger.info(tick.toString());
        return tick;
    }

    public Map<String, Ticker> getTickerAll() throws IOException {

        Map<String, String> headers = new HashMap<>();

        Map<String, Ticker> ticks = Arrays.stream(curenciesPairs).parallel().map(f -> {
            try {
                String dataSell = HttpReader.getUrlSource(MAIN_URL + "/" + f + "/" + SELL_URL , headers);
                TickData tickSell = new Gson().fromJson(dataSell, TickData.class);
                String dataBuy = HttpReader.getUrlSource(MAIN_URL + "/" + f + "/" + BUY_URL , headers);
                TickData tickBuy = new Gson().fromJson(dataBuy, TickData.class);
                return Ticker.builder().symbol(f).sell(tickSell).buy(tickBuy).build();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }).filter(f -> f != null).collect(Collectors.toMap(Ticker::getSymbol, Function.identity()));
//        logger.info(ticks.toString());
        return ticks;
    }
}
