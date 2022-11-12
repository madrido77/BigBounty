package com.kwarcinski.worker;

import com.kwarcinski.worker.coverter.*;
import com.kwarcinski.worker.entity.GlobalTicker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ExchangeManager {

    @Autowired
    private List<AbstractTickerConverter> exchanges;

    public void getGlobalTicker(){
        exchanges.parallelStream().map(f-> {
            try {
                return f.getGlobalTicker().toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }).filter(Objects::nonNull).collect(Collectors.toList()).forEach(System.out::println);
    }


    public List<String> getGlobalTickerAllString(){
        List<String> results = exchanges.parallelStream()
                // .filter(f->f instanceof TickerOkCoinConverter)
                .map(f -> {
                    try {
                        return f.getGlobalTickerAll().toString();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return null;
                })
                .filter(Objects::nonNull).collect(Collectors.toList());
        results.forEach(System.out::println);
        return results;
    }

    public List<Map<String,GlobalTicker>> getGlobalTickerAll(){
        List<Map<String,GlobalTicker>> results = exchanges.parallelStream()
                .map(f -> {
                    try {
                        return (Map<String,GlobalTicker>) f.getGlobalTickerAll();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return null;
                })
                .filter(Objects::nonNull).collect(Collectors.toList());

        return results;
    }
}
