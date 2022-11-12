package com.kwarcinski.exchanges.okcoin;

import com.google.gson.Gson;
import com.kwarcinski.exchanges.Exchange;
import com.kwarcinski.exchanges.okcoin.entities.Ticker;
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
public class OkCoinApi implements Exchange{
    public static Logger logger = Logger.getGlobal();

    private String MAIN_URL = "https://www.okcoin.com/api/spot/v3";
    private String INSTRUMENTS_URL =   "/instruments";
    private String TICKER_URL =  "/ticker";

    private String [] curenciesPairs = {"BTC-USD"};

    public Ticker getTickerSingle()throws IOException {

        Map<String,String> headers = new HashMap<>();

        String data = HttpReader.getUrlSource(MAIN_URL+INSTRUMENTS_URL+"/" +curenciesPairs[0] + TICKER_URL, headers);

        com.kwarcinski.exchanges.okcoin.entities.Ticker tick = new Gson().fromJson(data, com.kwarcinski.exchanges.okcoin.entities.Ticker.class);
//        logger.info(tick.toString());
        return tick;
    }

    public Map<String, Ticker> getTickerAll()throws IOException {

        Map<String,String> headers = new HashMap<>();

        String data = HttpReader.getUrlSource(MAIN_URL+INSTRUMENTS_URL + TICKER_URL, headers);

        Ticker[] ticksArray = new Gson().fromJson(data, Ticker[].class);
        Map<String, Ticker> ticks = Arrays.stream(ticksArray).collect(Collectors.toMap(Ticker::getInstrumentId, Function.identity()));
        return ticks;
    }
}

