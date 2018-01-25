package com.example.pawe.pracaidemo2;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Srodki extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static RequestQueue rq;
    private static String p_id_pojazdu = "";
    private static String p_marka = "";
    private static String p_model = "";
    private static String p_rok = "";
    private static String p_rok_zakupu = "";
    private static String p_pojemnosc = "";
    private static String p_przebieg = "";
    private static String p_nrrejestracyjny = "";
    private static String p_ubezpieczenie = "";
    private static String p_uszkodzony = "";

    private static ListView lista_zasoby;
    private static Button usun_zasoby;
    private static List<String> lista_id = new ArrayList<String>();
    private static List<String> lista_marka = new ArrayList<String>();
    private static List<String> lista_model = new ArrayList<String>();
    private static List<String> lista_rok = new ArrayList<String>();
    private static List<String> lista_rok_zakupu = new ArrayList<String>();
    private static List<String> lista_pojemnosc = new ArrayList<String>();
    private static List<String> lista_przebieg = new ArrayList<String>();
    private static List<String> lista_nrrejestracyjny = new ArrayList<String>();
    private static List<String> lista_ubezpieczenie = new ArrayList<String>();
    private static List<String> lista_uszkodzony = new ArrayList<String>();
    private static List<String> przedmiot_do_usuniecia = new ArrayList<String>();
    private static List<Integer> pozycja_lista = new ArrayList<Integer>();
    private static List<String> zasoby_do_usuniecia = new ArrayList<String>();
    private static List<String> spiner = new ArrayList<String>();
    private static ArrayAdapter adapter;
    private static ArrayAdapter<String> spiner_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_srodki);
        Toolbar pasek = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(pasek);
        getSupportActionBar().setTitle("Poszczególne zasoby");

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
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);

        lista_zasoby = (ListView) findViewById(R.id.lista_zasoby);
        usun_zasoby = (Button) findViewById(R.id.usun_zasoby);

        spiner.add("Sprawny");
        spiner.add("Niesprawny");
        spiner.add("W naprawie");

        spiner_adapter = new ArrayAdapter<String>(this, R.layout.spiner_wyglad, spiner);
        spiner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        final Response.Listener<String> r = new Response.Listener<String>(){
            @Override
            public void onResponse(String odpowiedz) {
                try {
                    JSONArray obiekt = new JSONArray(odpowiedz);
                    for (int i = 0; i < obiekt.length(); i++) {
                        JSONObject obiekt2 = obiekt.getJSONObject(i);
                        lista_id.add(obiekt2.getString("id_pojazdu"));
                        lista_marka.add(obiekt2.getString("marka"));
                        lista_model.add(obiekt2.getString("model"));
                        lista_rok.add(obiekt2.getString("rok"));
                        lista_rok_zakupu.add(obiekt2.getString("rok_zakupu"));
                        lista_pojemnosc.add(obiekt2.getString("pojemnosc"));
                        lista_przebieg.add(obiekt2.getString("przebieg"));
                        lista_nrrejestracyjny.add(obiekt2.getString("nrrejestracyjny"));
                        lista_ubezpieczenie.add(obiekt2.getString("ubezpieczenie"));
                        lista_uszkodzony.add(obiekt2.getString("uszkodzony"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                adapter = new adapter_lista_zasoby(Srodki.this,lista_id,lista_marka,lista_model,lista_rok,lista_rok_zakupu,lista_pojemnosc,lista_przebieg,lista_nrrejestracyjny,lista_ubezpieczenie,lista_uszkodzony);
                lista_zasoby.setAdapter(adapter);
            }
        };
        serwer_zasoby_lista z = new serwer_zasoby_lista(r);
        rq = Volley.newRequestQueue(Srodki.this);
        rq.add(z);

        adapter.notifyDataSetChanged();

        usun_zasoby.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                for (String id: zasoby_do_usuniecia) {
                    final Response.Listener<String> r = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String odpowiedz) {}
                    };
                    serwer_pracownicy_usun z = new serwer_pracownicy_usun(id, r);
                    rq = Volley.newRequestQueue(Srodki.this);
                    rq.add(z);
                }

                for (Integer i : pozycja_lista) {
                    lista_id.remove(i);
                    lista_marka.remove(i);
                    lista_model.remove(i);
                    lista_rok.remove(i);
                    lista_rok_zakupu.remove(i);
                    lista_pojemnosc.remove(i);
                    lista_przebieg.remove(i);
                    lista_nrrejestracyjny.remove(i);
                    lista_ubezpieczenie.remove(i);
                    lista_uszkodzony.remove(i);
                }
                Log.d("TAG", "pozycja_lista: " + pozycja_lista.toString());
                for (Integer p : pozycja_lista) {
                    Object pozycja = adapter.getItem(p);
                    adapter.remove(pozycja);
                }
                adapter.notifyDataSetChanged();
                pozycja_lista.clear();
            }
        });
    }

    private class wyglad {
        TextView zasoby_model;
        TextView zasoby_marka;
        Spinner zasoby_stan;
        Button usun_zasob;
        Button pokaz_zasob;
        CheckBox zasoby_box;
    }

    class adapter_lista_zasoby extends ArrayAdapter<String> {
        Context cg;
        List<String> id_pojazdu;
        List<String> marka;
        List<String> model;
        List<String> rok;
        List<String> rok_zakupu;
        List<String> pojemnosc;
        List<String> przebieg;
        List<String> nrrejestracyjny;
        List<String> ubezpieczenie;
        List<String> uszkodzony;

        adapter_lista_zasoby(Context c, List<String> id, List<String> m, List<String> mo, List<String> r, List<String> rz, List<String> p, List<String> prz, List<String> n, List<String> ub, List<String> us){
            super(c,R.layout.row_lista_zasoby,R.id.zasoby_model,mo);
            this.cg = c;
            this.id_pojazdu = id;
            this.marka = m;
            this.model = mo;
            this.rok = r;
            this.rok_zakupu = rz;
            this.pojemnosc = p;
            this.przebieg = prz;
            this.nrrejestracyjny = n;
            this.ubezpieczenie = ub;
            this.uszkodzony = us;
        }

        @Override
        public View getView(final int pozycja, View widok, ViewGroup ojciec){
            LayoutInflater li = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View w = li.inflate(R.layout.row_lista_zasoby,ojciec,false);
            final Srodki.wyglad wyglad = new Srodki.wyglad();
            final TextView idp = new TextView(cg);
            final TextView rokp = new TextView(cg);
            final TextView rok_zakupup = new TextView(cg);
            final TextView pojemnoscp = new TextView(cg);
            final TextView przebiegp = new TextView(cg);
            final TextView nrrejestracyjnyp = new TextView(cg);
            final TextView ubezpieczeniep = new TextView(cg);
            final TextView uszkodzonyp = new TextView(cg);
            wyglad.zasoby_model = (TextView) w.findViewById(R.id.zasoby_model);
            wyglad.zasoby_marka = (TextView) w.findViewById(R.id.zasoby_marka);
            wyglad.zasoby_stan = (Spinner) w.findViewById(R.id.zasoby_stan);
            wyglad.usun_zasob = (Button) w.findViewById(R.id.usun_zasob);
            wyglad.pokaz_zasob = (Button) w.findViewById(R.id.pokaz_zasob);
            wyglad.zasoby_box = (CheckBox) w.findViewById(R.id.zasoby_box);
            wyglad.zasoby_model.setText(lista_model.get(pozycja));
            wyglad.zasoby_marka.setText(lista_marka.get(pozycja));
            wyglad.zasoby_stan.setAdapter(spiner_adapter);
            idp.setText(lista_id.get(pozycja));
            rokp.setText(lista_rok.get(pozycja));
            rok_zakupup.setText(lista_rok_zakupu.get(pozycja));
            pojemnoscp.setText(lista_pojemnosc.get(pozycja));
            przebiegp.setText(lista_przebieg.get(pozycja));
            nrrejestracyjnyp.setText(lista_nrrejestracyjny.get(pozycja));
            ubezpieczeniep.setText(lista_ubezpieczenie.get(pozycja));
            uszkodzonyp.setText(lista_uszkodzony.get(pozycja));

            wyglad.zasoby_box.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(wyglad.zasoby_box.isChecked())
                    {
                        przedmiot_do_usuniecia.add(idp.getText().toString());
                        pozycja_lista.add(pozycja);
                        Log.d("TAG", "position: " + przedmiot_do_usuniecia.toString());
                    }else {
                        przedmiot_do_usuniecia.remove(idp.getText().toString());
                        pozycja_lista.remove(pozycja);
                        Log.d("TAG", "position: " + przedmiot_do_usuniecia.toString());
                    }
                }
            });

            wyglad.zasoby_stan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
            {
                public void onItemSelected(AdapterView<?> ojciec, View w, int pozycja, long id)
                {
                    final Response.Listener<String> res = new Response.Listener<String>(){
                        @Override
                        public void onResponse(String odpowiedz) {}
                    };
                    serwer_zasoby_stan_zmian z = new serwer_zasoby_stan_zmian(idp.getText().toString(),ojciec.getItemAtPosition(pozycja).toString(),res);
                    rq = Volley.newRequestQueue(Srodki.this);
                    rq.add(z);
                }
                public void onNothingSelected(AdapterView<?> ojciec)
                {

                }
            });

            wyglad.pokaz_zasob.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View w) {
                    p_id_pojazdu = idp.getText().toString();
                    p_marka = wyglad.zasoby_marka.getText().toString();
                    p_model = wyglad.zasoby_model.getText().toString();
                    p_rok = rokp.getText().toString();
                    p_rok_zakupu = rok_zakupup.getText().toString();
                    p_pojemnosc = pojemnoscp.getText().toString();
                    p_przebieg = przebiegp.getText().toString();
                    p_nrrejestracyjny = nrrejestracyjnyp.getText().toString();
                    p_ubezpieczenie = ubezpieczeniep.getText().toString();
                    p_uszkodzony = uszkodzonyp.getText().toString();
                    Intent i=new Intent(cg, Srodki_otworz.class);
                    cg.startActivity(i);
                }
            });

            wyglad.usun_zasob.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View w) {
                    final Response.Listener<String> r = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String odpowiedz) {
                            final Response.Listener<String> r = new Response.Listener<String>() {
                                @Override
                                public void onResponse(String odpowiedz) {
                                    try {
                                        JSONObject obiekt = new JSONObject(odpowiedz);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            };
                        }
                    };

                    serwer_zasob_usuwanie z = new serwer_zasob_usuwanie(idp.getText().toString(), r);
                    rq = Volley.newRequestQueue(Srodki.this);
                    rq.add(z);

                    Object p = adapter.getItem(pozycja);

                    adapter.remove(p);
                    lista_id.remove(pozycja);
                    adapter.notifyDataSetChanged();
                }
            });

            return w;
        }
    }

    public static String id(){
        return p_id_pojazdu;
    }
    public static String marka(){
        return p_marka;
    }
    public static String model(){
        return p_model;
    }
    public static String rok(){
        return p_rok;
    }
    public static String rok_zakupu(){
        return p_rok_zakupu;
    }
    public static String pojemnosc(){
        return p_pojemnosc;
    }
    public static String przebieg(){
        return p_przebieg;
    }
    public static String nrrejestracyjny(){
        return p_nrrejestracyjny;
    }
    public static String ubezpieczenie(){
        return p_ubezpieczenie;
    }
    public static String uszkodzony(){
        return p_uszkodzony;
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
