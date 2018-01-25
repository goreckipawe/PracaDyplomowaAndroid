package com.example.pawe.pracaidemo2;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.TextAppearanceSpan;
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
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class Pracownik_otworz extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static RequestQueue rq;
    private static TextView p_imie_nazwisko;
    private static TextView p_dzial;
    private static TextView p_stanowisko;
    private static TextView p_id;
    private static TextView p_pesel;
    private static TextView p_telefon;
    private static TextView p_adres;
    private static TextView p_email;
    private static TextView p_identyfikator;
    private static EditText p_haslo;
    private static Button zmien_haslo;
    private static Pracownicy p = new Pracownicy();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pracownik_otworz);
        Toolbar pasek = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(pasek);
        getSupportActionBar().setTitle("Pracownik");

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

        p_imie_nazwisko = (TextView) findViewById(R.id.p_imie_nazwisko);
        p_dzial = (TextView) findViewById(R.id.p_dzial);
        p_stanowisko = (TextView) findViewById(R.id.p_stanowisko);
        p_id = (TextView) findViewById(R.id.p_id);
        p_pesel = (TextView) findViewById(R.id.p_pesel);
        p_telefon = (TextView) findViewById(R.id.p_telefon);
        p_adres = (TextView) findViewById(R.id.p_adres);
        p_email = (TextView) findViewById(R.id.p_email);
        p_identyfikator = (TextView) findViewById(R.id.p_identyfikator);
        p_haslo = (EditText) findViewById(R.id.p_haslo);
        zmien_haslo = (Button) findViewById(R.id.zmien_haslo);
        p_imie_nazwisko.setText(p.imie_nazwisko());
        p_dzial.setText(p.dzial());
        p_stanowisko.setText(p.stanowisko());
        p_id.setText(p.id());
        p_pesel.setText(p.pesel());
        p_telefon.setText(p.telefon());
        p_adres.setText(p.adres());
        p_email.setText(p.email());
        p_identyfikator.setText(p.identyfikator());
        p_haslo.setText(p.haslo());

        zmien_haslo.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
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
                serwer_pracownicy_haslo z = new serwer_pracownicy_haslo(p_id.getText().toString(),p_haslo.getText().toString(), r);
                rq = Volley.newRequestQueue(Pracownik_otworz.this);
                rq.add(z);
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
