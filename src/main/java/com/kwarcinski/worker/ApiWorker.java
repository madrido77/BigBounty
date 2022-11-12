package com.kwarcinski.worker;

import com.kwarcinski.worker.entity.GlobalTicker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ApiWorker {

    @Autowired
    private ExchangeManager exchangeManager;

    public Map<String, List<Map.Entry<String, GlobalTicker>>> calculateCurrenciesCount() {
        Map<String, List<Map.Entry<String, GlobalTicker>>> result;
        result = exchangeManager.getGlobalTickerAll()
                .stream()
                .flatMap(f -> f.entrySet().stream())
//                .peek(f-> System.out.println(f.getValue().getExchange() +" " +f.getKey()))
                .collect(Collectors.groupingBy(f -> f.getKey()));
//                .collect(Collectors.groupingBy(f -> ""+ CurrencySymbolUtil.calculateSumCurrencyKey(f.getKey())));

        result.entrySet()
                .stream()
                .filter(t -> t.getValue().size() > 1)

                .forEach((f) -> System.out.println(f.getKey().toString() + " " + f.getValue().size()));
        return result;
//        Map<String, List<Map.Entry<String, GlobalTicker>>> filteredResult = result.entrySet()
//                .stream()
////                .map(f-> (List<Map.Entry<String, GlobalTicker>>) f)
//                .filter(t -> t.getValue().size() > 1)
//                .filter(t -> {
//                    String key = t.getKey();
//                    if (key.contains("USD") || key.contains("EUR") || key.contains("GPB"))
//                        return true;
//                    else
//                        return false;
//                })
//                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
//return filteredResult;
//        return filteredResult.entrySet()
//                .stream()
//                .filter(t -> {
//                    String key = t.getKey();
//                    if (key.contains("USD") || key.contains("EUR") || key.contains("GPB"))
//                        return true;
//                    else
//                        return false;
//                })
//                .peek(f -> System.out.println(f.getValue().toString() + " " + f.getKey()))
//                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }


    public void calculateExchangeGain(Map<String, List<Map.Entry<String, GlobalTicker>>> list) {


        Double startSum = 0.021718;
        String startCurrency = "USD";
        Double operationSum = 0D;
        String operationCurrency = "USD";
        String operationExchangeBuy = "";
        String operationExchangeSell = "";
        Double maxAsk = Double.MAX_VALUE;
        Double minBid = 0D;

        Map<String, List<Map.Entry<String, GlobalTicker>>> filteredResult = list.entrySet()
                .stream()
//                .map(f-> (List<Map.Entry<String, GlobalTicker>>) f)
                .filter(t -> t.getValue().size() > 1)
                .filter(t -> {
                    String key = t.getKey();
                    if (key.contains(startCurrency))
                        return true;
                    else
                        return false;
                })
//                .peek(f-> System.out.println(f.getValue().getExchange() +" " +f.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        for (Map.Entry<String, List<Map.Entry<String, GlobalTicker>>> mapEntry : filteredResult.entrySet()) {

            for (Map.Entry<String, GlobalTicker> listItem : mapEntry.getValue()) {
              System.out.println("#" + listItem.getValue().getExchange() + " searching currency: " + operationCurrency + " Pair: " + listItem.getValue().getCurrencyPair() + " Bid " + listItem.getValue().getBestBid() + " Ask: " + listItem.getValue().getBestAsk());
             //   if (listItem.getKey().contains(startCurrency)&& listItem.getValue().getExchange().equals("BitBay")) {
                if (listItem.getKey().contains(startCurrency)) {
                    if (listItem.getValue().getAskDouble() < maxAsk) {
                        maxAsk = listItem.getValue().getAskDouble();
                        operationSum = startSum / maxAsk;
                        operationCurrency = listItem.getValue().getCurrencyPair().replace("-", "").replace(startCurrency, "");
                        operationExchangeBuy = listItem.getValue().getExchange();
                    }
                }

            }
            for (Map.Entry<String, GlobalTicker> listItem : mapEntry.getValue()) {
              //  if (listItem.getKey().contains(startCurrency) && listItem.getValue().getExchange().equals("BitBay")) {
                if (listItem.getKey().contains(startCurrency)) {
                    if (listItem.getValue().getBidDouble() > minBid) {
                        minBid = listItem.getValue().getBidDouble();
                        operationExchangeSell = listItem.getValue().getExchange();
                    }
                }
            }
            System.out.println(operationExchangeBuy + " \t" + operationCurrency + " \t" + operationSum + " \t" + maxAsk + "  \t" + minBid + " \t" +operationExchangeSell + " \t" + BigDecimal.valueOf(minBid - maxAsk).setScale(2, RoundingMode.HALF_UP) + " \t" + BigDecimal.valueOf((operationSum*minBid - startSum)).setScale(2, RoundingMode.HALF_UP));
            startSum = 13.179;

            minBid = 0D;
            operationSum = 0D;
            operationCurrency = "USD";
            operationExchangeBuy = "";
            operationExchangeSell = "";
            maxAsk = Double.MAX_VALUE;
        }


    }
}
