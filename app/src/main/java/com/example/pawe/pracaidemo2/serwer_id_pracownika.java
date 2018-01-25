package com.example.pawe.pracaidemo2;

/**
 * Created by Paweł on 2017-09-03.
 */

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class serwer_id_pracownika extends StringRequest {
    private static final String ADRES_PHP = "http://pracai.hol.es/id_pracownika.php";
    private Map<String, String> dane;

    public serwer_id_pracownika(String identyfikator, Response.Listener<String> nasluciwacz) {
        super(Method.POST, ADRES_PHP, nasluciwacz, null);
        dane = new HashMap<>();
        dane.put("identyfikator", identyfikator);
    }

    @Override
    public Map<String, String> getParams() {
        return dane;
    }
}
