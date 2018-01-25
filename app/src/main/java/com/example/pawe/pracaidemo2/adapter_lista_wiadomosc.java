package com.example.pawe.pracaidemo2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

/**
 * Created by Paweł on 2017-09-05.
 */

class adapter_lista_wiadomosc extends ArrayAdapter<String>{
    Context context;
    String[] tytul;
    String[] imie_nazwisko;
    String[] identyfikator;

    adapter_lista_wiadomosc(Context c, String [] t, String []in, String []i){
        super(c,R.layout.row_lista_wiadomosc,R.id.lista_tytul,t);
        this.context = c;
        this.tytul = t;
        this.imie_nazwisko = in;
        this.identyfikator = i;
    }
}

