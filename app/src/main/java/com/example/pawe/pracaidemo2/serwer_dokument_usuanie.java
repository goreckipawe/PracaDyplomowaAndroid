package com.example.pawe.pracaidemo2;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Paweł on 2017-09-13.
 */

public class serwer_dokument_usuanie extends StringRequest {
    private static final String ADRES_PHP = "http://pracai.hol.es/dokument_usuwanie.php";
    private Map<String, String> dane;

    public serwer_dokument_usuanie(String id_wiadomosc, Response.Listener<String> nasluciwacz) {
        super(Method.POST, ADRES_PHP, nasluciwacz, null);
        dane = new HashMap<>();
        dane.put("id_wiadomosc", id_wiadomosc);
    }

    @Override
    public Map<String, String> getParams() {
        return dane;
    }
}
