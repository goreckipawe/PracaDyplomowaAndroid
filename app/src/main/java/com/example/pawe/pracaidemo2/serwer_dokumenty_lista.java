package com.example.pawe.pracaidemo2;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Paweł on 2017-09-13.
 */

public class serwer_dokumenty_lista extends StringRequest {
    private static final String ADRES_PHP = "http://pracai.hol.es/dokumenty_lista.php";
    private Map<String, String> dane;

    public serwer_dokumenty_lista(Response.Listener<String> nasluciwacz) {
        super(Method.GET, ADRES_PHP, nasluciwacz, null);
        dane = new HashMap<>();
    }

    @Override
    public Map<String, String> getParams() {
        return dane;
    }
}