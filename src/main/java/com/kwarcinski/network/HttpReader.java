package com.kwarcinski.network;

import com.kwarcinski.exchanges.bitbay.util.APIHashGenerator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;


public class HttpReader {
    public static Logger logger = Logger.getGlobal();

    public static String getUrlSourceBB(String sURL) throws IOException {
        long timeStart = System.currentTimeMillis();
//		'API-Key': apiKey,
//				'API-Hash': getHash(apiKey, timestamp, apiSecret, body),
//				'operation-id': uuidv4(),
        UUID uuid = UUID.randomUUID();
        String hexUUID = APIHashGenerator.bytesToHex(uuid.toString().getBytes("UTF-8"));
        String timestamp = new Timestamp(System.currentTimeMillis()).getTime() + "";
        String apiKey = "5add8d71-ab73-4156-8d07-d4bc43447e74";
        URL url = new URL(sURL);
        HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();

        httpCon.addRequestProperty("API-Key", apiKey);
        httpCon.addRequestProperty("API-Hash", APIHashGenerator.gemerate(apiKey + timestamp, "193d9517-69ab-4686-9808-b04d06554f2e"));
        httpCon.addRequestProperty("operation-id", hexUUID);
        httpCon.addRequestProperty("Request-Timestamp", timestamp);
        httpCon.addRequestProperty("Content-Type", "application/json");

        httpCon.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(new InputStreamReader(httpCon.getInputStream(), "utf-8"));
        String inputLine;
        StringBuilder a = new StringBuilder();
        while ((inputLine = in.readLine()) != null)
            a.append(inputLine + " \n");
        in.close();

        httpCon.disconnect();
        return a.toString();
    }

    public static String getUrlSource(String sURL, Map<String, String> headers) throws IOException {

        logger.info(sURL);
        URL url = new URL(sURL);
        HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
        headers.entrySet().stream().forEach(f -> httpCon.addRequestProperty(f.getKey(), f.getValue()));
        httpCon.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(new InputStreamReader(httpCon.getInputStream(), "utf-8"));
        String inputLine;
        StringBuilder a = new StringBuilder();
        while ((inputLine = in.readLine()) != null)
            a.append(inputLine + " \n");
        in.close();

        httpCon.disconnect();

        return a.toString();
    }
}
