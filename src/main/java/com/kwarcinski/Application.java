package com.kwarcinski;

import com.google.gson.Gson;
import com.kwarcinski.exchanges.bitbay.BitBayApi;
import com.kwarcinski.exchanges.bitbay.entity.DataTime;
import com.kwarcinski.exchanges.bitbay.entity.History;
import com.kwarcinski.exchanges.bitbay.entity.Item;
import com.kwarcinski.worker.ApiWorker;
import com.kwarcinski.worker.Socket;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootApplication
@EnableWebSocket
public class Application {

    public static void main(String[] args) throws IOException {

        //  String data =  HttpReader.getUrlSource("https://api.bitbay.net/rest/trading/orderbook/BTC-PLN");

//         ApplicationContext app = SpringApplication.run(Application.class, args);//init the context
//        CoinBaseApi exchangeManager = app.getBean(CoinBaseApi.class);//get the bean by type
//     System.out.println(exchangeManager.getTickerAll());
//      System.out.println(new BitStampApi().getCurrenciesExcangeCourses());
//      System.out.println(new CoinBaseApi().getCurrenciesBuyCourses());
//        System.out.println(new OkCoinApi().getCurrenciesExcangeCourses());
        //       System.out.println(new BitfinexApi().getCurrenciesExcangeCourses());

//        ApplicationContext app = SpringApplication.run(Application.class, args);//init the context
//        ExchangeManager exchangeManager = app.getBean(ExchangeManager.class);//get the bean by type
//        exchangeManager.getGlobalTickerAll();

        ApplicationContext app = SpringApplication.run(Application.class, args);//init the context
        ApiWorker exchangeManager = app.getBean(ApiWorker.class);//get the bean by type
        exchangeManager.calculateExchangeGain(exchangeManager.calculateCurrenciesCount());
//        ApplicationContext app = SpringApplication.run(Application.class, args);//init the context
//        Socket socket = app.getBean(Socket.class);//get the bean by type
//        socket.sock();

//        ApplicationContext app = SpringApplication.run(Application.class, args);//init the context
//        BitBayApi exchangeManager = app.getBean(BitBayApi.class);//get the bean by type
//
//        History history = exchangeManager.getOrderHistory("BTC-PLN", "60", (System.currentTimeMillis() - 60 * 1000 * 60 * 24 * 7) + "", System.currentTimeMillis() + "");
//        System.out.println(history.getItems().size());
//       // history.getItems().stream().forEach(f-> System.out.println("time="+f.get(0).getTime()+" size="+f.size()+" candle=" + f.get(0).getCandleData().toString()+""));
////        printData(history);
//        sellBuy(history);
//        SpringApplication.exit(app);

    }

    private static void printData(History history) {
        for (int i=11 ; i<history.getItems().size();i++){
            DataTime it = history.getItems().get(i).get(0);
            System.out.print(getD(history, i-10, it) + ",");
            System.out.print(getD(history, i-9, it) + ",");
            System.out.print(getD(history, i-8, it) + ",");
            System.out.print(getD(history, i-7, it) + ",");
            System.out.print(getD(history, i-6, it) + ",");
            System.out.print(getD(history, i-5, it) + ",");
            System.out.print(getD(history, i-4, it) + ",");
            System.out.print(getD(history, i-3, it) + ",");
            System.out.print(getD(history, i-2, it) + ",");
            System.out.print(getD(history, i-1, it) + ",");




            System.out.println("time="+it.getTime()+" candle=" + it.getCandleData().toString()+"");
        }
    }

    // dodatniejesli poprzedni kurs był wysoki i spadł
    private static double getD(History history, int i, DataTime it) {
        return Double.valueOf(history.getItems().get(i).get(0).getCandleData().getL()) - Double.valueOf(it.getCandleData().getL());
    }

    public static void sellBuy(History history){
        int depth =11;
        int countOfexchanges =0;
        int counter =0;
        boolean isBought = false;
        double accountSumUSD = 1000;
        double lastAccountSum = 1000;
        double accountSumBTC = 0;
        for (int i=depth ; i<history.getItems().size();i++){
            DataTime actualCourse = history.getItems().get(i).get(0);
            List<Double> backHistory = new ArrayList<>();
            for (int j=0 ; j<depth;j++){
                backHistory.add(getD(history, i-j, actualCourse));
            }
         //   System.out.print(" Bought BTC: "+isBought+ " "+actualCourse.getCandleData().getC()+ " ");
            if(!isBought)
            for (int k=0 ; k<backHistory.size();k++){
           //     System.out.print(backHistory.get(k)+ " ");
                if(backHistory.get(k) > 80){
                    isBought = true;
                    lastAccountSum =accountSumUSD;
                    accountSumBTC = accountSumUSD/Double.valueOf(actualCourse.getCandleData().getL());
                    accountSumUSD =0;
                    break;
                }
            }
            counter++;
            if(isBought && counter >1)
                for (int k=0 ; k<backHistory.size();k++){
                    if(lastAccountSum< accountSumBTC*Double.valueOf(actualCourse.getCandleData().getL())-0.004*(accountSumBTC*Double.valueOf(actualCourse.getCandleData().getL()))){
                        //   if(backHistory.get(k) < 60){
                        isBought = false;
                        accountSumUSD = accountSumBTC*Double.valueOf(actualCourse.getCandleData().getL())-0.004*(accountSumBTC*Double.valueOf(actualCourse.getCandleData().getL()));
                        accountSumBTC =0;
                        counter=0;
                        countOfexchanges++;
                        break;
                    }
                }
      //      System.out.println();
            System.out.println("Saldo USD: "+(accountSumUSD)+" Saldo BTC: " +accountSumBTC+ " countOfexchanges: " +countOfexchanges);
//            System.out.println("time="+actualCourse.getTime()+" candle=" + actualCourse.getCandleData().toString()+"");
        }

    }
}

