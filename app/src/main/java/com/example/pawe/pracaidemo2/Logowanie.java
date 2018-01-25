package com.example.pawe.pracaidemo2;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Logowanie extends AppCompatActivity {

    public static TextView logowanie_problem;
    public static EditText logowanie_login;
    public static EditText logowanie_haslo;
    public static Button logowanie_zaloguj;
    public static String p_id = "";
    public static String p_dzial = "";
    public static String p_stanowisko = "";
    public static String p_imie_nazwisko = "";
    public static String p_pesel = "";
    public static String p_telefon = "";
    public static String p_adres = "";
    public static String p_email = "";
    public static String pracownik = "";
    public static RequestQueue rq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logowanie);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
        logowanie_problem = (TextView) findViewById(R.id.logowanie_problem);
        logowanie_login = (EditText) findViewById(R.id.logowanie_login);
        logowanie_haslo = (EditText) findViewById(R.id.logowanie_haslo);
        logowanie_zaloguj = (Button) findViewById(R.id.logowanie_zaloguj);

        logowanie_zaloguj.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                final Response.Listener<String> res = new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obiekt = new JSONObject(response);
                            boolean takczynie = obiekt.getBoolean("success");
                            if (takczynie){
                            p_id = obiekt.getString("id_pracownika");
                            p_dzial = obiekt.getString("id_dzialu");
                            p_stanowisko = obiekt.getString("id_stanowiska");
                            p_imie_nazwisko = obiekt.getString("imie") + " " + obiekt.getString("nazwisko");
                            p_pesel = obiekt.getString("telefon");
                            p_telefon = obiekt.getString("telefon");
                            p_adres = obiekt.getString("adres");
                            p_email = obiekt.getString("email");
                                pracownik = p_id;
                                Intent i1 = new Intent(Logowanie.this, MainActivity.class);
                                startActivity(i1);
                            }else {
                                AlertDialog.Builder adb = new AlertDialog.Builder(Logowanie.this);
                                adb.setMessage("Nie udało się zalogować!")
                                        .setNegativeButton("OK", null)
                                        .create()
                                        .show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                serwer_logowanie z = new serwer_logowanie(logowanie_login.getText().toString(),logowanie_haslo.getText().toString(),"1","1",res);
                rq = Volley.newRequestQueue(Logowanie.this);
                rq.add(z);
            }
        });

        logowanie_problem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder adb = new AlertDialog.Builder(Logowanie.this);
                adb.setMessage("Skontaktuj się z administratorem lub kierownictwem. Możesz też zmienić login i hasło w aplikacji desktopowej.")
                        .setNegativeButton("OK", null)
                        .create()
                        .show();
            }
        });
    }

    public static String id(){
        return pracownik;
    }

    public static final String kodowanie(final String haslo) {
        final String kodowanie = "MD5";
        try {
            MessageDigest md = java.security.MessageDigest.getInstance(kodowanie);
            md.update(haslo.getBytes());
            byte wiadomosch[] = md.digest();

            StringBuilder sb = new StringBuilder();
            for (byte danew : wiadomosch) {
                String znakih = Integer.toHexString(0xFF & danew);
                while (znakih.length() < 2)
                    znakih = "0" + znakih;
                sb.append(znakih);
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}
