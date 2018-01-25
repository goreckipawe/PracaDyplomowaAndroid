package com.example.pawe.pracaidemo2;

/**
 * Created by Paweł on 2017-09-03.
 */

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class serwer_wiadomość_wysylanie extends StringRequest {
    private static final String ADRES_PHP = "http://pracai.hol.es/wiadomosc_wysylanie.php";
    private Map<String, String> dane;

    public serwer_wiadomość_wysylanie(String id_pracownika, String id_pracownikan,  String tytul,  String data_godzina,  String tresc,  String typ_przeczytana_nieprzeczytana,  String plik_tak_nie,  String tabela, Response.Listener<String> nasluciwacz) {
        super(Method.POST, ADRES_PHP, nasluciwacz, null);
        dane = new HashMap<>();
        dane.put("id_pracownika", id_pracownika);
        dane.put("id_pracownikan", id_pracownikan);
        dane.put("tytul", tytul);
        dane.put("data_godzina", data_godzina);
        dane.put("tresc", tresc);
        dane.put("typ_przeczytana_nieprzeczytana", typ_przeczytana_nieprzeczytana);
        dane.put("plik_tak_nie", plik_tak_nie);
        dane.put("tabela", tabela);
    }

    @Override
    public Map<String, String> getParams() {
        return dane;
    }
}
