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

public class Pracownicy extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static RequestQueue rq;
    private static String p_id = "";
    private static String p_dzial = "";
    private static String p_stanowisko = "";
    private static String p_imie_nazwisko = "";
    private static String p_pesel = "";
    private static String p_telefon = "";
    private static String p_adres = "";
    private static String p_email = "";
    private static String p_identyfikator = "";
    private static String p_haslo = "";
    private static int p[];

    private static ListView lista_pracownicy;
    private static Button usun_pracownika;
    private static List<String> lista_id = new ArrayList<String>();
    private static List<String> lista_dzial = new ArrayList<String>();
    private static List<String> lista_stanowisko = new ArrayList<String>();
    private static List<String> lista_imie_nazwisko = new ArrayList<String>();
    private static List<String> lista_pesel = new ArrayList<String>();
    private static List<String> lista_telefon = new ArrayList<String>();
    private static List<String> lista_adres = new ArrayList<String>();
    private static List<String> lista_email = new ArrayList<String>();
    private static List<String> lista_identyfikator = new ArrayList<String>();
    private static List<String> lista_haslo = new ArrayList<String>();
    private static List<String> pracownicy_do_usuniecia = new ArrayList<String>();
    private static List<Integer> pozycja_lista = new ArrayList<Integer>();
    private static ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pracownicy);
        Toolbar pasek = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(pasek);
        getSupportActionBar().setTitle("Pracownicy");

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

        lista_pracownicy = (ListView) findViewById(R.id.lista_pracownicy);
        usun_pracownika = (Button) findViewById(R.id.usun_pracownika);

        final Response.Listener<String> res = new Response.Listener<String>(){
            @Override
            public void onResponse(String odpowiedz) {
                try {
                    JSONArray obiekt = new JSONArray(odpowiedz);
                    for (int i = 0; i < obiekt.length(); i++) {
                        JSONObject obiekt2 = obiekt.getJSONObject(i);
                        lista_id.add(obiekt2.getString("id_pracownika"));
                        lista_dzial.add(obiekt2.getString("nazwa_dzialu"));
                        lista_stanowisko.add(obiekt2.getString("nazwa_stanowiska"));
                        lista_imie_nazwisko.add(obiekt2.getString("imie") + " " + obiekt2.getString("nazwisko"));
                        lista_pesel.add(obiekt2.getString("pesel"));
                        lista_telefon.add(obiekt2.getString("telefon"));
                        lista_adres.add(obiekt2.getString("adres"));
                        lista_email.add(obiekt2.getString("email"));
                        lista_identyfikator.add(obiekt2.getString("identyfikator"));
                        lista_haslo.add(obiekt2.getString("haslo"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                adapter = new Pracownicy.adapter_lista_pracownicy(Pracownicy.this,lista_id,lista_dzial,lista_stanowisko,lista_imie_nazwisko,lista_pesel,lista_telefon,lista_adres,lista_email,lista_identyfikator,lista_haslo);
                lista_pracownicy.setAdapter(adapter);
            }
        };

        serwer_pracownicy_lista z = new serwer_pracownicy_lista(res);
        rq = Volley.newRequestQueue(Pracownicy.this);
        rq.add(z);

        usun_pracownika.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                for (String id: pracownicy_do_usuniecia) {
                    final Response.Listener<String> res = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String odpowiedz) {}
                    };
                    serwer_pracownicy_usun z = new serwer_pracownicy_usun(id, res);
                    rq = Volley.newRequestQueue(Pracownicy.this);
                    rq.add(z);
                }

                for (Integer i : pozycja_lista) {
                    lista_id.remove(i);
                    lista_dzial.remove(i);
                    lista_stanowisko.remove(i);
                    lista_imie_nazwisko.remove(i);
                    lista_pesel.remove(i);
                    lista_telefon.remove(i);
                    lista_adres.remove(i);
                    lista_email.remove(i);
                    lista_identyfikator.remove(i);
                    lista_haslo.remove(i);
                }
                Log.d("TAG", "pozycja_lista: " + pozycja_lista.toString());
                for (Integer p : pozycja_lista) {
                    Object item = adapter.getItem(p);
                    adapter.remove(item);
                }
                adapter.notifyDataSetChanged();
                pozycja_lista.clear();
            }
        });
    }

    private class wyglad {
        TextView pracownik_imie_nazwisko;
        TextView pracownik_stanowisko;
        TextView pracownik_identyfikator;
        CheckBox pracownik_box;
        Button dane_pracownika;
    }

    class adapter_lista_pracownicy extends ArrayAdapter<String> {
        Context cg;
        List<String> id_pracownika;
        List<String> dzial;
        List<String> stanowisko;
        List<String> imie_nazwisko;
        List<String> pesel;
        List<String> telefon;
        List<String> adres;
        List<String> email;
        List<String> identyfikator;
        List<String> haslo;

        adapter_lista_pracownicy(Context c, List<String> id, List<String> d, List<String> s, List<String> in, List<String> p, List<String> t, List<String> a, List<String> e, List<String> i, List<String> h){
            super(c,R.layout.row_lista_pracownicy,R.id.lista_imie_nazwisko,in);
            this.cg = c;
            this.id_pracownika = id;
            this.dzial = d;
            this.stanowisko = s;
            this.imie_nazwisko = in;
            this.pesel = p;
            this.telefon = t;
            this.adres = a;
            this.email = e;
            this.identyfikator = i;
            this.haslo = h;
        }

        @Override
        public View getView(final int pozycja, View widok, ViewGroup ojciec){
            LayoutInflater li = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View w = li.inflate(R.layout.row_lista_pracownicy,ojciec,false);
            final Pracownicy.wyglad wyglad = new Pracownicy.wyglad();
            final TextView idp = new TextView(cg);
            final TextView dzialp = new TextView(cg);
            final TextView peselp = new TextView(cg);
            final TextView telefonp = new TextView(cg);
            final TextView adresp = new TextView(cg);
            final TextView emailp = new TextView(cg);
            final TextView haslop = new TextView(cg);
            wyglad.pracownik_imie_nazwisko = (TextView) w.findViewById(R.id.pracownik_imie_nazwisko);
            wyglad.pracownik_stanowisko = (TextView) w.findViewById(R.id.pracownik_stanowisko);
            wyglad.pracownik_identyfikator = (TextView) w.findViewById(R.id.pracownik_identyfikator);
            wyglad.pracownik_box = (CheckBox) w.findViewById(R.id.pracownik_box);
            wyglad.dane_pracownika = (Button) w.findViewById(R.id.dane_pracownika);
            wyglad.pracownik_imie_nazwisko.setText(lista_imie_nazwisko.get(pozycja));
            wyglad.pracownik_stanowisko.setText(lista_stanowisko.get(pozycja));
            wyglad.pracownik_identyfikator.setText(lista_identyfikator.get(pozycja));
            idp.setText(lista_id.get(pozycja));
            dzialp.setText(lista_dzial.get(pozycja));
            peselp.setText(lista_pesel.get(pozycja));
            telefonp.setText(lista_telefon.get(pozycja));
            adresp.setText(lista_adres.get(pozycja));
            emailp.setText(lista_email.get(pozycja));
            haslop.setText(lista_haslo.get(pozycja));
            wyglad.pracownik_box.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(wyglad.pracownik_box.isChecked())
                    {
                        pracownicy_do_usuniecia.add(idp.getText().toString());
                        pozycja_lista.add(pozycja);
                        Log.d("TAG", "position: " + pracownicy_do_usuniecia.toString());
                    }else {
                        pracownicy_do_usuniecia.remove(idp.getText().toString());
                        pozycja_lista.remove(pozycja);
                        Log.d("TAG", "position: " + pracownicy_do_usuniecia.toString());
                    }
                }
            });

            wyglad.dane_pracownika.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    p_id = idp.getText().toString();
                    p_dzial = dzialp.getText().toString();
                    p_stanowisko = wyglad.pracownik_stanowisko.getText().toString();
                    p_imie_nazwisko = wyglad.pracownik_imie_nazwisko.getText().toString();
                    p_pesel = peselp.getText().toString();
                    p_telefon = telefonp.getText().toString();
                    p_adres = adresp.getText().toString();
                    p_email = emailp.getText().toString();
                    p_identyfikator = wyglad.pracownik_identyfikator.getText().toString();
                    p_haslo = haslop.getText().toString();
                    Intent i=new Intent(cg, Pracownik_otworz.class);
                    cg.startActivity(i);
                }
            });
            return w;
        }
    }

    public static String id(){
        return p_id;
    }
    public static String dzial(){
        return p_dzial;
    }
    public static String stanowisko(){
        return p_stanowisko;
    }
    public static String imie_nazwisko(){
        return p_imie_nazwisko;
    }
    public static String pesel(){
        return p_pesel;
    }
    public static String telefon(){
        return p_telefon;
    }
    public static String adres(){
        return p_adres;
    }
    public static String email(){
        return p_email;
    }
    public static String identyfikator(){
        return p_identyfikator;
    }
    public static String haslo(){
        return p_haslo;
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
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

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
