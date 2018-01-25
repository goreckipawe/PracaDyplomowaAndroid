package com.example.pawe.pracaidemo2;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class ludzie_lista extends StringRequest {
    private static final String ADRES_PHP = "http://pracai.hol.es/pracownicy.php";
    private Map<String, String> dane;

    public ludzie_lista(String id_dzialu, Response.Listener<String> nasluchiwacz) {
        super(Method.POST, ADRES_PHP, nasluchiwacz, null);
        dane = new HashMap<>();
        dane.put("id_dzialu", id_dzialu);
    }

    @Override
    public Map<String, String> getParams() {
        return dane;
    }
}
