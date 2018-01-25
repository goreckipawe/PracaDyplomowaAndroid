package com.example.pawe.pracaidemo2;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Paweł on 2017-09-02.
 */

public class Lacznik {
    public static HttpURLConnection connect(String adres)
    {
        try {
            URL url=new URL(adres);
            HttpURLConnection httpurlp= (HttpURLConnection) url.openConnection();

            httpurlp.setRequestMethod("GET");
            httpurlp.setRequestMethod("POST");
            httpurlp.setConnectTimeout(20000);
            httpurlp.setReadTimeout(20000);
            httpurlp.setDoInput(true);
            httpurlp.setDoOutput(true);

            return httpurlp;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  null;
    }
}
