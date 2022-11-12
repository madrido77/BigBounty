package com.kwarcinski.exchanges.bitbay;

import com.google.gson.*;
import com.kwarcinski.exchanges.Exchange;
import com.kwarcinski.exchanges.bitbay.entity.History;
import com.kwarcinski.exchanges.bitbay.entity.Item;
import com.kwarcinski.exchanges.bitbay.entity.TickerAll;
import com.kwarcinski.exchanges.bitbay.entity.Ticker;
import com.kwarcinski.exchanges.bitbay.util.APIHashGenerator;
import com.kwarcinski.network.HttpReader;
import com.kwarcinski.util.CurrencySymbolUtil;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Component
public class BitBayApi implements Exchange {
    public static Logger logger = Logger.getGlobal();
    private String apiKey = "5add8d71-ab73-4156-8d07-d4bc43447e74";

    private String MAIN_URL_PRIVATE = "https://api.bitbay.net";
    private String MAIN_URL_PUBLIC = "https://bitbay.net/API/Public";
    private String TICKER_ALL_URL = MAIN_URL_PRIVATE + "/rest/trading/ticker";
    private String TICKER_SINGLE_URL = "/ticker.json";
    private String HISTORY_URL = "https://api.bitbay.net/rest/trading/candle/history";

    public Map<String, Item> getTickerAll() throws IOException {

        UUID uuid = UUID.randomUUID();
        String hexUUID = APIHashGenerator.bytesToHex(uuid.toString().getBytes("UTF-8"));
        String timestamp = new Timestamp(System.currentTimeMillis()).getTime() + "";

        Map<String, String> headers = new HashMap<>();
        headers.put("API-Key", apiKey);
        headers.put("API-Hash", APIHashGenerator.gemerate(apiKey + timestamp, "193d9517-69ab-4686-9808-b04d06554f2e"));
        headers.put("operation-id", hexUUID);
        headers.put("Request-Timestamp", timestamp);
        headers.put("Content-Type", "application/json");

        String data = HttpReader.getUrlSource(TICKER_ALL_URL, headers);
//        logger.info(data);
        TickerAll ex = new Gson().fromJson(data, TickerAll.class);
//        logger.info(ex.toString());
        return ex.getItems().entrySet().stream().map(f -> {
            Item tick = f.getValue();
            tick.setSymbol(f.getKey());
            return tick;
        }).collect(Collectors.toMap(Item::getSymbol, Function.identity()));
    }

    public Ticker getTickerSingle() throws IOException {

        UUID uuid = UUID.randomUUID();
        String hexUUID = APIHashGenerator.bytesToHex(uuid.toString().getBytes("UTF-8"));
        String timestamp = new Timestamp(System.currentTimeMillis()).getTime() + "";

        Map<String, String> headers = new HashMap<>();

        headers.put("operation-id", hexUUID);
        headers.put("Request-Timestamp", timestamp);
        headers.put("Content-Type", "application/json");

        String data = HttpReader.getUrlSource(MAIN_URL_PUBLIC + "/BTCUSD" + TICKER_SINGLE_URL, headers);

        Ticker ticker = new Gson().fromJson(data, Ticker.class);
        ticker.setSymbol(CurrencySymbolUtil.divideCurrencySymbol("BTCUSD"));

        return ticker;
    }


    public History getOrderHistory( String currencyPair, String resolution, String fromMillis, String toMillis) throws IOException {

        UUID uuid = UUID.randomUUID();
        String hexUUID = APIHashGenerator.bytesToHex(uuid.toString().getBytes("UTF-8"));
        String timestamp = new Timestamp(System.currentTimeMillis()).getTime() + "";

        Map<String, String> headers = new HashMap<>();

        headers.put("operation-id", hexUUID);
        headers.put("Request-Timestamp", timestamp);
        headers.put("Content-Type", "application/json");

        String data = HttpReader.getUrlSource(HISTORY_URL + "/" + currencyPair + "/" + resolution + "?from=" + fromMillis + "&to=" + toMillis, headers);

        data=data.replace("[[","[[{\"time\":").replace("],[","}],[{\"time\":").replace("000\",{","000\",\"candleData\":{").replace("}]]","}}]]");
        logger.info(data);
        Gson gson =
                new GsonBuilder()
                        .registerTypeAdapter(TestAnnotationBean.class, new AnnotatedDeserializer<TestAnnotationBean>())
                        .create();
        History history = gson.fromJson(data, History.class);

        return history;
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    @interface JsonRequired
    {
    }

    class TestAnnotationBean
    {
        @JsonRequired public String foo;
        public String bar;
    }

    class AnnotatedDeserializer<T> implements JsonDeserializer<T>
    {

        public T deserialize(JsonElement je, Type type, JsonDeserializationContext jdc) throws JsonParseException
        {
            T pojo = new Gson().fromJson(je, type);

            Field[] fields = pojo.getClass().getDeclaredFields();
            for (Field f : fields)
            {
                if (f.getAnnotation(JsonRequired.class) != null)
                {
                    try
                    {
                        f.setAccessible(true);
                        if (f.get(pojo) == null)
                        {
                            throw new JsonParseException("Missing field in JSON: " + f.getName());
                        }
                    }
                    catch (IllegalArgumentException ex)
                    {
                        Logger.getLogger(AnnotatedDeserializer.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    catch (IllegalAccessException ex)
                    {
                        Logger.getLogger(AnnotatedDeserializer.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            return pojo;

        }
    }
}
