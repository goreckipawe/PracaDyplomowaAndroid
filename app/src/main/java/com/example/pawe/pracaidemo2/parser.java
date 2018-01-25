package com.example.pawe.pracaidemo2;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Paweł on 2017-09-02.
 */

public class parser extends AsyncTask<Void,Void,Integer> {

    private static Context c;
    private static Spinner sp;
    private static String jdg;

    private static ProgressDialog pd;
    private static ArrayList<String> spacecrafts=new ArrayList<>();

    public parser(Context c, Spinner sp, String jd) {
        this.c = c;
        this.sp = sp;
        this.jdg = jd;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        pd=new ProgressDialog(c);
        pd.setTitle("Parse");
        pd.setMessage("Parsing...Please wait");
        pd.show();
    }

    @Override
    protected Integer doInBackground(Void... params) {
        return this.parseData();
    }

    @Override
    protected void onPostExecute(Integer wynik) {
        super.onPostExecute(wynik);

        pd.dismiss();

        if(wynik==0)
        {
            Toast.makeText(c,"Nie udało się sparsować",Toast.LENGTH_SHORT).show();
        }else
        {
            Toast.makeText(c,"Udało się sparsować",Toast.LENGTH_SHORT).show();

            ArrayAdapter adapter=new ArrayAdapter(c,android.R.layout.simple_list_item_1,spacecrafts);
            sp.setAdapter(adapter);

            sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> ojciec, View w, int pozycja, long id) {
                    Toast.makeText(c,spacecrafts.get(pozycja),Toast.LENGTH_SHORT).show();
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {}
            });

        }
    }

    private int parseData()
    {
        try {
            JSONArray ja=new JSONArray(jdg);
            JSONObject jo=null;

            spacecrafts.clear();
            pracownicy_spiner_dane s=null;

            for(int i=0;i<ja.length();i++)
            {
                jo=ja.getJSONObject(i);

                int id=jo.getInt("id_pracownika");
                String identyfikator=jo.getString("identyfikator");

                s=new pracownicy_spiner_dane();
                s.setId_pracownika(id);
                s.setIdentyfikator(identyfikator);
                spacecrafts.add(identyfikator);
            }
            return 1;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
