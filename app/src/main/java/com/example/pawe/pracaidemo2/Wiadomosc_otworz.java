package com.example.pawe.pracaidemo2;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.SpannableString;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Wiadomosc_otworz extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static RequestQueue rq;
    public static TextView wo_autor;
    public static TextView wo_identyfikator;
    public static TextView wo_data_godzina;
    public static WebView wo_text;
    public static String wo_plik;
    public static String wo_id;
    public static String wo_pdf_link;
    public static Wiadomosci w = new Wiadomosci();
    public static Button usun_wiadomosc;
    public static Button pobierz_zalacznik;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wiadomosc_otworz);
        Toolbar pasek = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(pasek);
        getSupportActionBar().setTitle("Wiadomość");

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
        wo_autor = (TextView) findViewById(R.id.wo_autor);
        wo_identyfikator = (TextView) findViewById(R.id.wo_identyfikator);
        wo_data_godzina = (TextView) findViewById(R.id.wo_data_godzina);
        wo_text = (WebView) findViewById(R.id.wo_text);
        usun_wiadomosc = (Button) findViewById(R.id.usun_wiadomosc);
        pobierz_zalacznik = (Button) findViewById(R.id.pobierz_zalacznik);
        wo_autor.setText(w.autor());
        wo_identyfikator.setText(w.identyfikator());
        wo_data_godzina.setText(w.data());
        wo_text.loadData(w.text(), "text/html; charset=utf-8", "UTF-8");
        wo_plik = w.plik();
        wo_id = w.id();
        wo_pdf_link = w.link();

        wo_text.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        usun_wiadomosc.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
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
                serwer_wiadomość_usuwanie z = new serwer_wiadomość_usuwanie(wo_id, res);
                rq = Volley.newRequestQueue(Wiadomosc_otworz.this);
                rq.add(z);
            }
        });

        pobierz_zalacznik.setEnabled(false);
        if (wo_plik.equals("1")){
            pobierz_zalacznik.setEnabled(true);
        }

        pobierz_zalacznik.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                new wiadomosc_pdf().execute(wo_pdf_link);
                Log.d("TAG", "Wiadomosc_otworz: " + link());
                File plikw = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + wo_pdf_link);
                Intent cel = new Intent(Intent.ACTION_VIEW);
                cel.setDataAndType(Uri.fromFile(plikw),"application/pdf");
                cel.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

                Intent z = Intent.createChooser(cel, "Otwórz plik");
                try {
                    startActivity(z);
                } catch (ActivityNotFoundException e) {
                }
            }
        });
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
