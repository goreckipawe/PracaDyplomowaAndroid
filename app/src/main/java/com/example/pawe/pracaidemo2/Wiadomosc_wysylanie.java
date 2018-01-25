package com.example.pawe.pracaidemo2;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Wiadomosc_wysylanie extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static Spinner dzial, pracownik;
    private static TextView tytul_wiadomosc_wysylanie;
    private static TextView tresc_wiadomosc_wysylanej;
    private static Button wyslanie_wiadomosc, wyslanie_wiadomosc_wszyscy, wyslanie_wiadomosc_dzial;

    private static RequestQueue rq;

    private static String identyfikator_pobrany = "";
    private static String id_pracownika = "";

    private static ArrayList<String> listItems=new ArrayList<>();
    private static ArrayList<String> listItems2=new ArrayList<>();
    private static ArrayAdapter<String> adapter;
    private static ArrayAdapter<String> adapter2;
    private static List<String> spiner_dzial = new ArrayList<String>();

    private static DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    private static Logowanie log =new Logowanie();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wiadomosc_wysylanie);
        Toolbar pasek = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(pasek);
        getSupportActionBar().setTitle("Wyślij wiadomość");

        DrawerLayout dl = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle abdtp = new ActionBarDrawerToggle(
                this, dl, pasek, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        dl.setDrawerListener(abdtp);
        abdtp.syncState();

        NavigationView nv = (NavigationView) findViewById(R.id.nav_view);
        Menu menu = nv.getMenu();
        MenuItem informacja = menu.findItem(R.id.informacja);
        MenuItem wyjdz = menu.findItem(R.id.wyjdz);
        SpannableString s = new SpannableString(informacja.getTitle());
        SpannableString s2 = new SpannableString(wyjdz.getTitle());
        s.setSpan(new TextAppearanceSpan(this, R.style.menu_tytul), 0, s.length(), 0);
        s2.setSpan(new TextAppearanceSpan(this, R.style.menu_tytul), 0, s2.length(), 0);
        informacja.setTitle(s);
        wyjdz.setTitle(s2);
        nv.setNavigationItemSelectedListener(this);
        nv.setItemIconTintList(null);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);

        dzial = (Spinner)findViewById(R.id.dzial);
        pracownik = (Spinner)findViewById(R.id.pracownik);
        tytul_wiadomosc_wysylanie = (TextView) findViewById(R.id.tytul_wiadomosc_wysylanie);
        tresc_wiadomosc_wysylanej = (TextView) findViewById(R.id.tresc_wiadomosc_wysylanej);
        wyslanie_wiadomosc = (Button) findViewById(R.id.wyslanie_wiadomosc);
        wyslanie_wiadomosc_wszyscy = (Button) findViewById(R.id.wyslanie_wiadomosc_wszyscy);
        wyslanie_wiadomosc_dzial = (Button) findViewById(R.id.wyslanie_wiadomosc_dzial);

        spiner_dzial.add("Kierownictwo");
        spiner_dzial.add("Księgowość");
        spiner_dzial.add("Magazyn");
        spiner_dzial.add("Brygadzista");
        spiner_dzial.add("Transport/Pracownicy");
        adapter2 = new ArrayAdapter<String>(Wiadomosc_wysylanie.this, R.layout.spiner_wyglad, spiner_dzial);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dzial.setAdapter(adapter2);
        dzial.setPrompt("Działy");
        pracownik.setPrompt("Wybierz pracownika");
        dzial.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> ojciec, View w, int pozycja, long id)
            {
                final Response.Listener<String> res = new Response.Listener<String>(){
                    @Override
                    public void onResponse(String odpowiedz) {
                        try {
                            listItems.clear();
                            JSONArray obiekt = new JSONArray(odpowiedz);
                            for (int i = 0; i < obiekt.length(); i++) {
                                JSONObject obiekt2 = obiekt.getJSONObject(i);
                                String imie = obiekt2.getString("imie");
                                String nazwisko = obiekt2.getString("nazwisko");
                                String identyfikator = obiekt2.getString("identyfikator");
                                listItems.add(imie + " " + nazwisko + ": " + identyfikator);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                ludzie_lista z = new ludzie_lista(ojciec.getItemAtPosition(pozycja).toString(),res);
                rq = Volley.newRequestQueue(Wiadomosc_wysylanie.this);
                rq.add(z);
                listItems2 = listItems;
                adapter = new ArrayAdapter<String>(Wiadomosc_wysylanie.this, R.layout.spiner_wyglad, listItems2);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                pracownik.setAdapter(adapter);
            }
            public void onNothingSelected(AdapterView<?> ojciec) {}
        });

        pracownik.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> ojciec, View w, int pozycja, long id)
            {
                Log.d("TAG", "Element: " + ojciec.getItemAtPosition(pozycja).toString().substring(ojciec.getItemAtPosition(pozycja).toString().indexOf(':') + 2));
                identyfikator_pobrany = ojciec.getItemAtPosition(pozycja).toString().substring(ojciec.getItemAtPosition(pozycja).toString().indexOf(':') + 2);
            }
            public void onNothingSelected(AdapterView<?> ojciec) {}
        });

        wyslanie_wiadomosc.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                final Response.Listener<String> res = new Response.Listener<String>(){
                    @Override
                    public void onResponse(String odpowiedz) {
                        if (!(identyfikator_pobrany.equals(""))) {
                            if(!(tytul_wiadomosc_wysylanie.getText().toString().equals("") && tresc_wiadomosc_wysylanej.getText().toString().equals(""))) {
                        String tabela = "";
                        String tresc;
                        if (dzial.getSelectedItem().toString().equals("Kierownictwo")){
                            tabela = "wiadomosc_pracownicy";
                        }else if(dzial.getSelectedItem().toString().equals("Księgowość")){
                            tabela = "wiadomosc_ksiegowosic";
                        }else if(dzial.getSelectedItem().toString().equals("Magazyn")){
                            tabela = "wiadomosc_magazyniera";
                        }else if(dzial.getSelectedItem().toString().equals("Brygadzista")){
                            tabela = "wiadomosc_brygadzista";
                        }else if(dzial.getSelectedItem().toString().equals("Transport/Pracownicy")){
                            tabela = "wiadomosc_kierowcyPracownicy";
                        }
                        try {
                            JSONObject obiekt = new JSONObject(odpowiedz);
                            boolean takczynie = obiekt.getBoolean("success");
                            if (takczynie) {
                                String id = obiekt.getString("id_pracownika");
                                id_pracownika = id;
                                        tresc = "<!DOCTYPE html><html><body><p>" + tresc_wiadomosc_wysylanej.getText().toString() + "</p></body></html>";
                                        final Response.Listener<String> res = new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String odpowiedz) {
                                                try {
                                                    JSONObject obiekt = new JSONObject(odpowiedz);
                                                    boolean takczynie = obiekt.getBoolean("success");
                                                    if (takczynie) {
                                                        Toast.makeText(getApplicationContext(), "Wysłano wiadomość", Toast.LENGTH_LONG).show();
                                                    }
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        };
                                        serwer_wiadomość_wysylanie z = new serwer_wiadomość_wysylanie(id_pracownika, log.p_id, tytul_wiadomosc_wysylanie.getText().toString(), df.format(Calendar.getInstance().getTime()), tresc, "1", "0", tabela, res);
                                        rq = Volley.newRequestQueue(Wiadomosc_wysylanie.this);
                                        rq.add(z);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                            }else {
                                Toast.makeText(getApplicationContext(), "Nie podano tytułu lub treść", Toast.LENGTH_LONG).show();
                            }
                        }else {
                            Toast.makeText(getApplicationContext(), "Nie wybrano pracownika", Toast.LENGTH_LONG).show();
                        }
                    }
                };
                serwer_id_pracownika z = new serwer_id_pracownika(identyfikator_pobrany,res);
                rq = Volley.newRequestQueue(Wiadomosc_wysylanie.this);
                rq.add(z);
            }
        });

        wyslanie_wiadomosc_wszyscy.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                        if(!(tytul_wiadomosc_wysylanie.getText().toString().equals("") && tresc_wiadomosc_wysylanej.getText().toString().equals(""))) {
                        String tresc = "<!DOCTYPE html><html><body><p>" + tresc_wiadomosc_wysylanej.getText().toString() + "</p></body></html>";
                        for(int i = 1; i < 6; i++) {
                            final Response.Listener<String> res = new Response.Listener<String>() {
                                @Override
                                public void onResponse(String odpowiedz) {
                                    try {
                                        JSONObject obiekt = new JSONObject(odpowiedz);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            };
                            serwer_wiadomość_wysylanie_wszyscy z = new serwer_wiadomość_wysylanie_wszyscy(String.valueOf(i), tytul_wiadomosc_wysylanie.getText().toString(), df.format(Calendar.getInstance().getTime()), tresc, "0", res);
                            rq = Volley.newRequestQueue(Wiadomosc_wysylanie.this);
                            rq.add(z);
                            Toast.makeText(getApplicationContext(), "Wysłano wiadomość", Toast.LENGTH_LONG).show();
                        }
                        }else {
                            Toast.makeText(getApplicationContext(), "Nie podano tytułu lub treść", Toast.LENGTH_LONG).show();
                        }
            }
        });

        wyslanie_wiadomosc_dzial.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                        String dzial_firmy = "";
                        if (dzial.getSelectedItem().toString().equals("Kierownictwo")){
                            dzial_firmy = "1";
                        }else if(dzial.getSelectedItem().toString().equals("Księgowość")){
                            dzial_firmy = "2";
                        }else if(dzial.getSelectedItem().toString().equals("Magazyn")){
                            dzial_firmy = "3";
                        }else if(dzial.getSelectedItem().toString().equals("Brygadzista")){
                            dzial_firmy = "4";
                        }else if(dzial.getSelectedItem().toString().equals("Transport/Pracownicy")){
                            dzial_firmy = "5";
                        }
                        if(!(tytul_wiadomosc_wysylanie.getText().toString().equals("") && tresc_wiadomosc_wysylanej.getText().toString().equals(""))) {
                            String tresc = "<!DOCTYPE html><html><body><p>" + tresc_wiadomosc_wysylanej.getText().toString() + "</p></body></html>";
                                final Response.Listener<String> res = new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String odpowiedz) {
                                        try {
                                            JSONObject obiekt = new JSONObject(odpowiedz);
                                            boolean takczynie = obiekt.getBoolean("success");
                                            if (takczynie) {
                                                Toast.makeText(getApplicationContext(), "Wysłano wiadomość", Toast.LENGTH_LONG).show();
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                };
                                serwer_wiadomość_wysylanie_wszyscy z = new serwer_wiadomość_wysylanie_wszyscy(dzial_firmy, tytul_wiadomosc_wysylanie.getText().toString(), df.format(Calendar.getInstance().getTime()), tresc, "0", res);
                                rq = Volley.newRequestQueue(Wiadomosc_wysylanie.this);
                                rq.add(z);
                        }else {
                            Toast.makeText(getApplicationContext(), "Nie podano tytułu lub treść", Toast.LENGTH_LONG).show();
                        }
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout dl = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (dl.isDrawerOpen(GravityCompat.START)) {
            dl.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem pozycja) {
        int id = pozycja.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(pozycja);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem pozycja) {
        int id = pozycja.getItemId();

        if (id == R.id.nav_ekran_glowny) {
            Intent i=new Intent(this, MainActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_wiadomosci) {
            Intent i1 = new Intent(this, Wiadomosci.class);
            startActivity(i1);
        } else if (id == R.id.nav_wiadomosc_wysylanie) {
            Intent i2 = new Intent(this, Wiadomosc_wysylanie.class);
            startActivity(i2);
        } else if (id == R.id.nav_dokumenty) {
            Intent i3 = new Intent(this, Dokumenty.class);
            startActivity(i3);
        } else if (id == R.id.nav_zasoby) {
            Intent i4 = new Intent(this, Zasoby.class);
            startActivity(i4);
        } else if (id == R.id.nav_informacje) {
            Intent i5 = new Intent(this, Informacje.class);
            startActivity(i5);
        } else if (id == R.id.nav_zamknij) {
            finishAffinity();
        }
        DrawerLayout dl = (DrawerLayout) findViewById(R.id.drawer_layout);
        dl.closeDrawer(GravityCompat.START);
        return true;
    }
}
