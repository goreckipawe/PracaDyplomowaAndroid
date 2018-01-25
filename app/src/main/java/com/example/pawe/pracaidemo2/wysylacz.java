package com.example.pawe.pracaidemo2;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;

import static android.R.attr.name;

/**
 * Created by Paweł on 2017-09-03.
 */

public class wysylacz {
    String dzial;

    public wysylacz(String dzial) {
        this.dzial = dzial;
    }

    public String packData()
    {
        JSONObject jo=new JSONObject();
        StringBuffer danbuf=new StringBuffer();
        try
        {
            jo.put("id_dzialu",dzial);
            Boolean czyprawda=true;
            Iterator i=jo.keys();
            do {
                String klucz=i.next().toString();
                String dane=jo.get(klucz).toString();
                if(czyprawda)
                {
                    czyprawda=false;
                }else
                {
                    danbuf.append("&");
                }
                danbuf.append(URLEncoder.encode(klucz,"UTF-8"));
                danbuf.append("=");
                danbuf.append(URLEncoder.encode(dane,"UTF-8"));
            }while (i.hasNext());
            return danbuf.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
}