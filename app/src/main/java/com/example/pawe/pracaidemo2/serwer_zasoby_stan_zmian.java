package com.example.pawe.pracaidemo2;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Paweł on 2017-09-11.
 */

public class serwer_zasoby_stan_zmian extends StringRequest {
    private static final String ADRES_PHP = "http://pracai.hol.es/zasoby_stan_zmiana.php";
    private Map<String, String> dane;

    public serwer_zasoby_stan_zmian(String id_pojazdu, String uszkodzony, Response.Listener<String> nasluciwacz) {
        super(Method.POST, ADRES_PHP, nasluciwacz, null);
        dane = new HashMap<>();
        dane.put("id_pojazdu", id_pojazdu);
        dane.put("uszkodzony", uszkodzony);
    }

    @Override
    public Map<String, String> getParams() {
        return dane;
    }
}