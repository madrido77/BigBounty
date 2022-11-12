package com.kwarcinski.util;

public class CurrencySymbolUtil {


    public static String divideCurrencySymbol(String currencySymbol) {
        currencySymbol = currencySymbol.replace("\"", "");
        if (currencySymbol.length() == 6) {
            return currencySymbol.substring(0, 3).toUpperCase() + "-" + currencySymbol.substring(3, currencySymbol.length()).toUpperCase();
        } else
            System.err.println("Conversion type I error: " + currencySymbol);
        return currencySymbol;
    }

    public static String divideCurrencySymbolAndRemove(String currencySymbol) {
        currencySymbol = currencySymbol.replace("\"", "");

        if (currencySymbol.charAt(0) == 't') {
            currencySymbol = currencySymbol.substring(1, currencySymbol.length());
        }

        if (currencySymbol.length() == 6) {
            return currencySymbol.substring(0, 3).toUpperCase() + "-" + currencySymbol.substring(3, currencySymbol.length()).toUpperCase();
        } else
            System.err.println("Conversion type II error: " + currencySymbol);
        return currencySymbol;
    }

    public static int calculateSumCurrencyKey(String currencySymbol) {
        String[] splited = currencySymbol.split("-");
        if (splited.length == 2)
            return splited[0].hashCode() + splited[1].hashCode();
        else
            return splited[0].hashCode();
    }
}
