package com.kwarcinski.exchanges.bitbay.util;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public abstract class APIHashGenerator {

    public static String gemerate(String data, String key) {

        String result = "";

        try{
            byte [] byteKey = key.getBytes("UTF-8");
            final String HMAC_SHA512 = "HmacSHA512";
            Mac sha512_HMAC = null;
            sha512_HMAC = Mac.getInstance(HMAC_SHA512);
            SecretKeySpec keySpec = new SecretKeySpec(byteKey, HMAC_SHA512);
            sha512_HMAC.init(keySpec);
            byte [] mac_data = sha512_HMAC.
                    doFinal(data.getBytes("UTF-8"));
            result = bytesToHex(mac_data);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public static String bytesToHex(byte[] hashInBytes) {

        StringBuilder sb = new StringBuilder();

        for (byte b : hashInBytes) {
            sb.append(String.format("%02x", b));
        }

        return sb.toString();

    }
}