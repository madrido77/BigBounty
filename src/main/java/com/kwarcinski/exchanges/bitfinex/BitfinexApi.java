package com.kwarcinski.exchanges.bitfinex;

import com.kwarcinski.exchanges.Exchange;
import com.kwarcinski.exchanges.bitfinex.entity.Ticker;
import com.kwarcinski.network.HttpReader;
import com.kwarcinski.util.CurrencySymbolUtil;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Component
public class BitfinexApi implements Exchange {
    public static Logger logger = Logger.getGlobal();

    private String MAIN_URL_V1 = "https://api-pub.bitfinex.com/v1";
    private String MAIN_URL_V2 = "https://api-pub.bitfinex.com/v2";

    private String SYMBOLS_URL = MAIN_URL_V1 + "/symbols";
    private String TICKER_ALL_URL = MAIN_URL_V2 + "/tickers?symbols=ALL";
    private String TICKER_SINGLE_URL = MAIN_URL_V2 + "/tickers?symbols=tBTCUSD";

    public Map<String, Ticker> getTickerAll() throws IOException {

        Map<String, String> headers = new HashMap<>();


        String data = HttpReader.getUrlSource(TICKER_ALL_URL, headers);
        String[] splited = data.replace("[[", "").replace("]]", "").split("\\],\\[");
        Map<String, Ticker> ticks = Arrays.stream(splited).map(f -> {
                    String[] spliteds = f.split(",");
                    return new Ticker(CurrencySymbolUtil.divideCurrencySymbolAndRemove(spliteds[0]), spliteds[1], spliteds[2], spliteds[3], spliteds[4], spliteds[5], spliteds[6], spliteds[7], spliteds[8], spliteds[9], spliteds[10]);
                }
        ).collect(Collectors.toMap(Ticker::getSYMBOL, Function.identity()));
//        logger.info(ticks.toString());
        return ticks;
    }

    public List<Ticker> getTickerSingle() throws IOException {

        Map<String, String> headers = new HashMap<>();


        String data = HttpReader.getUrlSource(TICKER_SINGLE_URL, headers);
        String[] splited = data.replace("[[", "").replace("]]", "").split("\\],\\[");
        List<Ticker> ticks = Arrays.stream(splited).parallel().map(f -> {
                    String[] spliteds = f.split(",");
                    return new Ticker(CurrencySymbolUtil.divideCurrencySymbolAndRemove(spliteds[0]), spliteds[1], spliteds[2], spliteds[3], spliteds[4], spliteds[5], spliteds[6], spliteds[7], spliteds[8], spliteds[9], spliteds[10]);
                }
        ).collect(Collectors.toList());
//        logger.info(ticks.toString());
        return ticks;
    }

    public List<String> getSymbols() throws IOException {


        Map<String, String> headers = new HashMap<>();


        String data = HttpReader.getUrlSource(SYMBOLS_URL, headers);
        String[] splited = data.replace("[", "").replace("]", "").split("\\],\\[");
        List<String> symbols = Arrays.stream(splited).collect(Collectors.toList());
//        logger.info(symbols.toString());
        return symbols;
    }
}
