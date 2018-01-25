package com.example.pawe.pracaidemo2;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Paweł on 2017-09-11.
 */

public class serwer_zasoby_lista extends StringRequest {
    private static final String ADRES_PHP = "http://pracai.hol.es/zasoby_lista.php";
    private Map<String, String> dane;

    public serwer_zasoby_lista(Response.Listener<String> nasluciwacz) {
        super(Method.GET, ADRES_PHP, nasluciwacz, null);
        dane = new HashMap<>();
    }

    @Override
    public Map<String, String> getParams() {
        return dane;
    }
}