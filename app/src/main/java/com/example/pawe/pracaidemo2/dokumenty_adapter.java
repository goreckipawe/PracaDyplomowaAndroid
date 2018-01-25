package com.example.pawe.pracaidemo2;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Paweł on 2017-09-12.
 */

public class dokumenty_adapter extends RecyclerView.Adapter<dokumenty_adapter.ViewHolder> {

    public static List<dokument_get_set> dgs;
    public static Context cg;
    public static List<String> dokumenty_do_usuniecia = new ArrayList<String>();
    public static List<String> dokumenty_do_usuniecia2 = new ArrayList<String>();
    public static RequestQueue rq;
    public static List<String> pozycja_lista = new ArrayList<String>();
    public static Dokumenty dok = new Dokumenty();

    public dokumenty_adapter(Context c,List<dokument_get_set> tdt) {
        this.cg = c;
        this.dgs = tdt;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View widok = LayoutInflater.from(parent.getContext()).inflate(R.layout.dokumenty_karty, parent, false);
        return new ViewHolder(widok);
    }

    @Override
    public void onBindViewHolder(final ViewHolder vh, final int pozycja) {

        final dokument_get_set lista = dgs.get(pozycja);
        vh.dokument_tytul.setText(lista.getTytul());
        vh.dokument_data.setText(lista.getData());
        vh.dokument_typ.setText(lista.getTyp());
        final TextView idd = new TextView(cg);
        final TextView plik = new TextView(cg);
        idd.setText(lista.getId());
        plik.setText(lista.getPlik());

        vh.dokument_box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(vh.dokument_box.isChecked())
                {
                    dokumenty_do_usuniecia.add(idd.getText().toString());
                    dokumenty_do_usuniecia2.add(plik.getText().toString());
                    pozycja_lista.add(idd.getText().toString());
                }else {
                    dokumenty_do_usuniecia.remove(idd.getText().toString());
                    dokumenty_do_usuniecia2.remove(plik.getText().toString());
                    pozycja_lista.remove(idd.getText().toString());
                }
            }
        });
        vh.dokument_usun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dgs.remove(pozycja);
                notifyItemRemoved(pozycja);
                notifyItemRangeChanged(pozycja,dgs.size());

                final Response.Listener<String> res = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String odpowiedz) {
                    }
                };

                serwer_pracownicy_usun z = new serwer_pracownicy_usun(idd.getText().toString(), res);
                rq = Volley.newRequestQueue(cg.getApplicationContext());
                rq.add(z);
                dok.link_usun(plik.getText().toString());
            }
        });

        vh.dokument_pobierz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               dok.link(plik.getText().toString());
            }
        });
    }

    @Override
    public int getItemCount() {
        return dgs.size();
    }

    public List<String> do_usuniecia() {
        return dokumenty_do_usuniecia;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView dokument_tytul;
        public TextView dokument_data;
        public TextView dokument_typ;
        public CheckBox dokument_box;
        public Button dokument_usun;
        public Button dokument_pobierz;
        public ViewHolder(View itemView) {
            super(itemView);
            dokument_tytul = (TextView) itemView.findViewById(R.id.dokument_tytul);
            dokument_data = (TextView) itemView.findViewById(R.id.dokument_data);
            dokument_typ = (TextView) itemView.findViewById(R.id.dokument_typ);
            dokument_box = (CheckBox) itemView.findViewById(R.id.dokument_box);
            dokument_usun = (Button) itemView.findViewById(R.id.dokument_usun);
            dokument_pobierz = (Button) itemView.findViewById(R.id.dokument_pobierz);
        }
    }}