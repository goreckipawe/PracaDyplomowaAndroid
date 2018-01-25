package com.example.pawe.pracaidemo2;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Paweł on 2017-09-03.
 */

public class serwer_wiadomość_wysylanie_wszyscy extends StringRequest {
    private static final String ADRES_PHP = "http://pracai.hol.es/wiadomosc_wysylanie_wszyscy.php";
    private Map<String, String> dane;

    public serwer_wiadomość_wysylanie_wszyscy(String id_dzialu,  String tytul,  String data_godzina,  String tresc,  String typ_przeczytana_nieprzeczytana, Response.Listener<String> nasluciwacz) {
        super(Method.POST, ADRES_PHP, nasluciwacz, null);
        dane = new HashMap<>();
        dane.put("id_dzialu", id_dzialu);
        dane.put("tytul", tytul);
        dane.put("data_godzina", data_godzina);
        dane.put("tresc", tresc);
        dane.put("typ_przeczytana_nieprzeczytana", typ_przeczytana_nieprzeczytana);
    }

    @Override
    public Map<String, String> getParams() {
        return dane;
    }
}
