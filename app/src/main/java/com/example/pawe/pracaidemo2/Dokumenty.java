package com.example.pawe.pracaidemo2;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.app.AppCompatActivity;
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
import android.widget.Button;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.apache.commons.net.ftp.FTPClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.R.attr.host;
import static android.R.attr.port;

public class Dokumenty extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static RecyclerView karta_dokument;
    public static dokumenty_adapter adapter;
    public static List<dokument_get_set> lista_dokument;
    public static Button usun_dokumenty;
    public static RequestQueue requestQueue;
    public static String link_plik_nazwa = "";

    public static List<String> dokumenty_do_usuniecia = new ArrayList<String>();
    public static List<String> dokumenty_do_usuniecia2 = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dokumenty);
        Toolbar pasek = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(pasek);
        getSupportActionBar().setTitle("Dokumenty");

        DrawerLayout dl = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle abdtprzelacznik = new ActionBarDrawerToggle(
                this, dl, pasek, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        dl.setDrawerListener(abdtprzelacznik);
        abdtprzelacznik.syncState();

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

        karta_dokument = (RecyclerView) findViewById(R.id.karta_dokument);
        usun_dokumenty = (Button) findViewById(R.id.usun_dokumenty);
        karta_dokument.setHasFixedSize(true);
        karta_dokument.setLayoutManager(new LinearLayoutManager(this));

        lista_dokument = new ArrayList<>();

        final Response.Listener<String> res = new Response.Listener<String>(){
            @Override
            public void onResponse(String odpowiedz) {
                try {
                    JSONArray obiekt = new JSONArray(odpowiedz);
                    for (int i = 0; i < obiekt.length(); i++) {
                        JSONObject obiekt2 = obiekt.getJSONObject(i);
                        lista_dokument.add(new dokument_get_set(obiekt2.getString("id_dokumentu"), obiekt2.getString("tytul"), obiekt2.getString("data_godzina"), obiekt2.getString("rodzaj"), obiekt2.getString("plik")));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                adapter = new dokumenty_adapter(Dokumenty.this,lista_dokument);
                karta_dokument.setAdapter(adapter);
            }
        };
        serwer_dokumenty_lista z = new serwer_dokumenty_lista(res);
        requestQueue = Volley.newRequestQueue(Dokumenty.this);
        requestQueue.add(z);

        usun_dokumenty.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                dokumenty_do_usuniecia = adapter.do_usuniecia();
                for (String id: dokumenty_do_usuniecia) {
                    final Response.Listener<String> res = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String odpowiedz) {}
                    };
                    serwer_pracownicy_usun z = new serwer_pracownicy_usun(id, res);
                    requestQueue = Volley.newRequestQueue(Dokumenty.this);
                    requestQueue.add(z);
                }
                dokumenty_do_usuniecia2 = adapter.dokumenty_do_usuniecia2;
                for (String nazwa: dokumenty_do_usuniecia2) {
                   link_usun(nazwa);
                }
            }
        });
    }

    public void link(String nazwa){
        link_plik_nazwa = nazwa;
        new dokument_pdf().execute(nazwa);
        File plik = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + nazwa);
        Intent cel = new Intent(Intent.ACTION_VIEW);
        cel.setDataAndType(Uri.fromFile(plik),"application/pdf");
        cel.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        Intent zamiar = Intent.createChooser(cel, "Otwórz plik");
        try {
            startActivity(zamiar);
        } catch (ActivityNotFoundException e) {
            // komunukat żeby zainstalował coś do pdf
        }
    }

    public void link_usun(String nazwa){
        try {
            FTPClient ftpc = new FTPClient();
            ftpc.connect("ftp.pracai.hol.es");
            ftpc.login("u224299645", "kajak66");
            ftpc.deleteFile(nazwa);
            ftpc.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
