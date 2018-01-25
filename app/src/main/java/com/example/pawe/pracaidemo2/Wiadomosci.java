package com.example.pawe.pracaidemo2;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.TextAppearanceSpan;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Wiadomosci extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static RequestQueue rq;
    private static String wo_autor = "";
    private static String wo_identyfikator = "";
    private static String wo_data_godzina = "";
    private static String wo_text = "";
    private static String wo_plik = "";
    private static String wo_id = "";
    private static String wo_pdf_link = "";
    private static Logowanie log = new Logowanie();
    private static String id = log.id();
    private static String pp;

    private static ListView lista_wiadomosc;
    private static List<String> lista_tytul = new ArrayList<String>();
    private static List<String> lista_imie_nazwisko = new ArrayList<String>();
    private static List<String> lista_identyfikator = new ArrayList<String>();
    private static List<String> lista_id_wiadomosc = new ArrayList<String>();
    private static List<String> lista_data_wiadomosc = new ArrayList<String>();
    private static List<String> lista_text_wiadomosc = new ArrayList<String>();
    private static List<String> lista_plik_wiadomosc = new ArrayList<String>();
    private static List<String> lista_plik_url = new ArrayList<String>();
    private static List<String> wiadomosc_do_usuniecia = new ArrayList<String>();
    private static Button usun_zaznaczone_wiadomosc;
    private static ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wiadomosci);
        Toolbar pasek = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(pasek);
        getSupportActionBar().setTitle("Wiadomości");

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
        usun_zaznaczone_wiadomosc = (Button) findViewById(R.id.usun_zaznaczone_wiadomosc);
        lista_wiadomosc = (ListView) findViewById(R.id.lista_wiadomosc);

        final Response.Listener<String> res = new Response.Listener<String>(){
            @Override
            public void onResponse(String odpowiedz) {
                try {
                    JSONArray obiekt = new JSONArray(odpowiedz);
                    for (int i = 0; i < obiekt.length(); i++) {
                        JSONObject obiekt2 = obiekt.getJSONObject(i);
                        lista_id_wiadomosc.add(obiekt2.getString("id_wiadomosc"));
                        lista_tytul.add(obiekt2.getString("tytul"));
                        lista_text_wiadomosc.add(obiekt2.getString("tresc"));
                        lista_data_wiadomosc.add(obiekt2.getString("data_godzina"));
                        lista_imie_nazwisko.add(obiekt2.getString("imie") + " " + obiekt2.getString("nazwisko"));
                        lista_identyfikator.add(obiekt2.getString("identyfikator"));
                        lista_plik_wiadomosc.add(obiekt2.getString("plik_tak_nie"));
                        lista_plik_url.add(obiekt2.getString("pdf_adres_ftp"));
                        pp = pp +" "+ obiekt2.getString("id_wiadomosc");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                adapter = new adapter_lista_wiadomosc(Wiadomosci.this,lista_tytul,lista_imie_nazwisko,lista_identyfikator,lista_id_wiadomosc,lista_data_wiadomosc,lista_text_wiadomosc,lista_plik_wiadomosc,lista_plik_url);
                lista_wiadomosc.setAdapter(adapter);
            }
        };
        serwer_wiadomość z = new serwer_wiadomość(id,res);
        rq = Volley.newRequestQueue(Wiadomosci.this);
        rq.add(z);

        usun_zaznaczone_wiadomosc.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                for (String id: wiadomosc_do_usuniecia) {
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
        serwer_wiadomość_usuwanie z = new serwer_wiadomość_usuwanie(id, res);
        rq = Volley.newRequestQueue(Wiadomosci.this);
        rq.add(z);
                }
                adapter.notifyDataSetChanged();
            }
        });

    }

    private class wyglad {
        TextView lista_tytul;
        TextView lista_imie_nazwisko;
        TextView lista_identyfikator;
        CheckBox lista_box;
        Button lista_usun;
        Button lista_pokaz;
    }

    class adapter_lista_wiadomosc extends ArrayAdapter<String> {
        Context cg;
        List<String> tytul;
        List<String> imie_nazwisko;
        List<String> identyfikator;
        List<String> id_wiadomosc;
        List<String> data;
        List<String> text;
        List<String> plik;
        List<String> link;

        adapter_lista_wiadomosc(Context c, List<String> t, List<String> in, List<String> i, List<String> id, List<String> d, List<String> te, List<String> p, List<String> l){
            super(c,R.layout.row_lista_wiadomosc,R.id.lista_tytul,t);
            this.cg = c;
            this.tytul = t;
            this.imie_nazwisko = in;
            this.identyfikator = i;
            this.id_wiadomosc= id;
            this.data= d;
            this.text= te;
            this.plik= p;
            this.link= l;
        }

        @Override
        public View getView(final int pozycja, View widok, ViewGroup ojciec){
            LayoutInflater li = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View w = li.inflate(R.layout.row_lista_wiadomosc,ojciec,false);
            final wyglad wyglad = new wyglad();
            final TextView idw = new TextView(cg);
            final TextView dataw = new TextView(cg);
            final TextView textw = new TextView(cg);
            final TextView plikw = new TextView(cg);
            final TextView linkw = new TextView(cg);
            wyglad.lista_tytul = (TextView) w.findViewById(R.id.lista_tytul);
            wyglad.lista_imie_nazwisko = (TextView) w.findViewById(R.id.lista_imie_nazwisko);
            wyglad.lista_identyfikator = (TextView) w.findViewById(R.id.lista_identyfikator);
            wyglad.lista_box = (CheckBox) w.findViewById(R.id.lista_box);
            wyglad.lista_usun = (Button) w.findViewById(R.id.lista_usun);
            wyglad.lista_pokaz = (Button) w.findViewById(R.id.lista_pokaz);
            wyglad.lista_tytul.setText(lista_tytul.get(pozycja));
            wyglad.lista_imie_nazwisko.setText(lista_imie_nazwisko.get(pozycja));
            wyglad.lista_identyfikator.setText(lista_identyfikator.get(pozycja));
            idw.setText(lista_id_wiadomosc.get(pozycja));
            dataw.setText(lista_data_wiadomosc.get(pozycja));
            textw.setText(lista_text_wiadomosc.get(pozycja));
            plikw.setText(lista_plik_wiadomosc.get(pozycja));
            linkw.setText(lista_plik_url.get(pozycja));
            wyglad.lista_box.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(wyglad.lista_box.isChecked())
                    {
                        wiadomosc_do_usuniecia.add(idw.getText().toString());
                    }else {
                        wiadomosc_do_usuniecia.remove(idw.getText().toString());
                    }
                }
            });
            wyglad.lista_usun.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View w) {
                    final Response.Listener<String> res = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String odpowiedz) {
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
                        }
                    };
                    serwer_wiadomość_usuwanie z = new serwer_wiadomość_usuwanie(idw.getText().toString(), res);
                    rq = Volley.newRequestQueue(Wiadomosci.this);
                    rq.add(z);

                    Object p = adapter.getItem(pozycja);

                    adapter.remove(p);
                    lista_id_wiadomosc.remove(pozycja);
                    adapter.notifyDataSetChanged();
                }
            });
            wyglad.lista_pokaz.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    wo_autor = wyglad.lista_imie_nazwisko.getText().toString();
                    wo_identyfikator = wyglad.lista_identyfikator.getText().toString();
                    wo_data_godzina = dataw.getText().toString();
                    wo_text = textw.getText().toString();
                    wo_plik = plikw.getText().toString();
                    wo_id = idw.getText().toString();
                    wo_pdf_link = linkw.getText().toString();
                    Intent i=new Intent(cg, Wiadomosc_otworz.class);
                    cg.startActivity(i);
                }
            });
            return w;
        }
    }

    public static String autor(){
        return wo_autor;
    }
    public static String identyfikator(){
        return wo_identyfikator;
    }
    public static String data(){
        return wo_data_godzina;
    }
    public static String text(){
        return wo_text;
    }
    public static String plik(){
        return wo_plik;
    }
    public static String id(){
        return wo_id;
    }
    public static String link(){
        return wo_pdf_link;
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

