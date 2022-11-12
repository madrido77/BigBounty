package com.kwarcinski.exchanges.bitstamp;

import com.google.gson.Gson;
import com.kwarcinski.exchanges.Exchange;
import com.kwarcinski.exchanges.bitstamp.entities.Tick;
import com.kwarcinski.network.HttpReader;
import com.kwarcinski.util.CurrencySymbolUtil;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Component
public class BitStampApi implements Exchange {
    public static Logger logger = Logger.getGlobal();

    private String MAIN_URL = "https://www.bitstamp.net/api/v2";
    private String TICKER_URL = MAIN_URL + "/ticker";

    private String[] curenciesPairs = {"btcusd", "btceur", "eurusd", "xrpusd", "xrpeur", "xrpbtc", "ltcusd", "ltceur", "ltcbtc", "ethusd", "etheur", "ethbtc", "bchusd", "bcheur", "bchbtc"};

    public Tick getTickerSingle() throws IOException {

        Map<String, String> headers = new HashMap<>();

        String data = HttpReader.getUrlSource(TICKER_URL + "/" + curenciesPairs[0], headers);

        Tick tick = new Gson().fromJson(data, Tick.class);
//        logger.info(tick.toString());
        return tick;
    }


    public Map<String, Tick> getTickerAll() throws IOException {

        Map<String, String> headers = new HashMap<>();

        Map<String, Tick> ticks = Arrays.stream(curenciesPairs).parallel().map(f -> {
            try {
                String data = HttpReader.getUrlSource(TICKER_URL + "/" + f, headers);
                Tick tick = new Gson().fromJson(data, Tick.class);
                tick.setSymbol((CurrencySymbolUtil.divideCurrencySymbol(f)));
                return tick;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }).filter(f -> f != null).collect(Collectors.toMap(Tick::getSymbol, Function.identity()));
//        logger.info(ticks.toString());
        return ticks;
    }
}
