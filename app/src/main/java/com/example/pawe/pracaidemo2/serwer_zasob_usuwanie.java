package com.example.pawe.pracaidemo2;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Paweł on 2017-09-11.
 */

public class serwer_zasob_usuwanie extends StringRequest {
    private static final String ADRES_PHP = "http://pracai.hol.es/zasoby_usuwanie.php";
    private Map<String, String> dane;

    public serwer_zasob_usuwanie(String id_pojazdu, Response.Listener<String> nasluciwacz) {
        super(Method.POST, ADRES_PHP, nasluciwacz, null);
        dane = new HashMap<>();
        dane.put("id_pojazdu", id_pojazdu);
    }

    @Override
    public Map<String, String> getParams() {
        return dane;
    }
}