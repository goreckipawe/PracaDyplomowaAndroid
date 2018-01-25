package com.example.pawe.pracaidemo2;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Paweł on 2017-09-14.
 */

public class serwer_logowanie extends StringRequest {
    private static final String ADRES_PHP = "http://pracai.hol.es/logowanie.php";
    private Map<String, String> dane;

    public serwer_logowanie(String login, String haslo, String dzial, String stanowisko, Response.Listener<String> nasluciwacz) {
        super(Method.POST, ADRES_PHP, nasluciwacz, null);
        dane = new HashMap<>();
        dane.put("login", login);
        dane.put("haslo", haslo);
        dane.put("dzial", dzial);
        dane.put("stanowisko", stanowisko);
    }

    @Override
    public Map<String, String> getParams() {
        return dane;
    }
}