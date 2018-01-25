package com.example.pawe.pracaidemo2;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import android.widget.TextView;

public class Srodki_otworz extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static TextView z_marka;
    private static TextView z_model;
    private static TextView z_rok;
    private static TextView z_rok_zakupu;
    private static TextView z_pojemnosc;
    private static TextView z_przebieg;
    private static TextView z_numer_rejestracyjny;
    private static TextView z_ubezpieczenie;
    private static TextView z_uszkodzony;
    private static TextView z_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_srodki_otworz);
        Toolbar pasek = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(pasek);
        getSupportActionBar().setTitle("Zasób");

        DrawerLayout dl = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle abdtp = new ActionBarDrawerToggle(
                this, dl, pasek, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        dl.setDrawerListener(abdtp);
        abdtp.syncState();

        NavigationView nv = (NavigationView) findViewById(R.id.nav_view);
        Menu menu = nv.getMenu();
        MenuItem informacja = menu.findItem(R.id.informacja);
        MenuItem wyjdz = menu.findItem(R.id.wyjdz);
        SpannableString s3 = new SpannableString(informacja.getTitle());
        SpannableString s2 = new SpannableString(wyjdz.getTitle());
        s3.setSpan(new TextAppearanceSpan(this, R.style.menu_tytul), 0, s3.length(), 0);
        s2.setSpan(new TextAppearanceSpan(this, R.style.menu_tytul), 0, s2.length(), 0);
        informacja.setTitle(s3);
        wyjdz.setTitle(s2);
        nv.setNavigationItemSelectedListener(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);

        Srodki s = new Srodki();
        z_marka = (TextView) findViewById(R.id.z_marka);
        z_model = (TextView) findViewById(R.id.z_model);
        z_rok = (TextView) findViewById(R.id.z_rok);
        z_rok_zakupu = (TextView) findViewById(R.id.z_rok_zakupu);
        z_pojemnosc = (TextView) findViewById(R.id.z_pojemnosc);
        z_przebieg = (TextView) findViewById(R.id.z_przebieg);
        z_numer_rejestracyjny = (TextView) findViewById(R.id.z_numer_rejestracyjny);
        z_ubezpieczenie = (TextView) findViewById(R.id.z_ubezpieczenie);
        z_uszkodzony = (TextView) findViewById(R.id.z_uszkodzony);
        z_id = (TextView) findViewById(R.id.z_id);
        z_marka.setText(s.marka());
        z_model.setText(s.model());
        z_rok.setText(s.rok());
        z_rok_zakupu.setText(s.rok_zakupu());
        z_pojemnosc.setText(s.pojemnosc());
        z_przebieg.setText(s.przebieg());
        z_numer_rejestracyjny.setText(s.nrrejestracyjny());
        z_ubezpieczenie.setText(s.ubezpieczenie());
        z_uszkodzony.setText(s.uszkodzony());
        z_id.setText(s.id());
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
