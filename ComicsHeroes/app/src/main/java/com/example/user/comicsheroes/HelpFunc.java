package com.example.user.comicsheroes;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by User on 25.08.2015.
 */
public class HelpFunc {
    public static String md5(final String s) {
        final String MD5 = "MD5";
        try {

            MessageDigest digest = MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();


            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
    public static String marvelUrl(final String BASE_URL,int offset) {
        String privateKey = "130bde7243a388c13aef3a0d2f39d54e72d10f40";
        String apiKey = "415a228d6131327beb660255e8bd4ef3";
        String limit = "100";
        String offsetString = Integer.toString(offset);
        final String LIMIT_PARAM = "limit";
        final String OFFSET_PARAM = "offset";
        final String TS_PARAM = "ts";
        final String API_KEY_PARAM = "apikey";
        final String HASH_PARAM = "hash";
        String ts = "timestamp";
        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendQueryParameter(OFFSET_PARAM,offsetString)
                .appendQueryParameter(LIMIT_PARAM, limit)
                .appendQueryParameter(TS_PARAM, ts)
                .appendQueryParameter(API_KEY_PARAM, apiKey)
                .appendQueryParameter(HASH_PARAM, md5(ts + privateKey + apiKey)).build();
        return builtUri.toString();
    }

}
