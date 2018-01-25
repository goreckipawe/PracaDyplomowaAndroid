package com.example.pawe.pracaidemo2;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Paweł on 2017-09-05.
 */

public class serwer_wiadomość extends StringRequest {
    private static final String ADRES_PHP = "http://pracai.hol.es/wiadomosc.php";
    private Map<String, String> dane;

    public serwer_wiadomość(String id_pracownika, Response.Listener<String> nasluciwacz) {
        super(Method.POST, ADRES_PHP, nasluciwacz, null);
        dane = new HashMap<>();
        dane.put("id_pracownika", id_pracownika);
    }

    @Override
    public Map<String, String> getParams() {
        return dane;
    }
}