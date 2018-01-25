package com.example.pawe.pracaidemo2;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Paweł on 2017-09-10.
 */

public class serwer_pracownicy_haslo extends StringRequest {
    private static final String ADRES_PHP = "http://pracai.hol.es/pracownik_haslo_zmiana.php";
    private Map<String, String> dane;

    public serwer_pracownicy_haslo(String id_pracownika, String haslo, Response.Listener<String> nasluciwacz) {
        super(Method.POST, ADRES_PHP, nasluciwacz, null);
        dane = new HashMap<>();
        dane.put("id_pracownika", id_pracownika);
        dane.put("haslo", haslo);
    }

    @Override
    public Map<String, String> getParams() {
        return dane;
    }
}