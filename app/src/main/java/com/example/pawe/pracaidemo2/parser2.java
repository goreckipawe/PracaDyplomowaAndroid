package com.example.pawe.pracaidemo2;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;

/**
 * Created by Paweł on 2017-09-03.
 */

public class parser2 extends AsyncTask<Void,Void,String> {
    Context c;
    String adresg;
    EditText id_dzialuTxt;
    String id_dzialu;
    ProgressDialog pd;

    public parser2(Context c, String adres,EditText...editTexts) {
        this.c = c;
        this.adresg = adres;
        this.id_dzialuTxt=editTexts[0];
        id_dzialu=id_dzialuTxt.getText().toString();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pd=new ProgressDialog(c);
        pd.setTitle("Wysyłanie");
        pd.setMessage("Wysyłanie..Prosze czekać");
        pd.show();
    }

    @Override
    protected String doInBackground(Void... params) {
        return this.wysylanie();
    }

    @Override
    protected void onPostExecute(String wynik) {
        super.onPostExecute(wynik);
        pd.dismiss();
        if(wynik != null)
        {
            Toast.makeText(c,wynik,Toast.LENGTH_LONG).show();
            id_dzialuTxt.setText("");
        }else
        {
            Toast.makeText(c,"Nie udało się "+wynik,Toast.LENGTH_LONG).show();
        }
    }

    private String wysylanie()
    {
        HttpURLConnection httpadresp=Lacznik.connect(adresg);
        if(httpadresp==null)
        {
            return null;
        }
        try
        {
            OutputStream os=httpadresp.getOutputStream();
            BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(os,"UTF-8"));
            bw.write(new wysylacz(id_dzialu).packData());
            bw.flush();
            bw.close();
            os.close();
            int rc=httpadresp.getResponseCode();
            if(rc==httpadresp.HTTP_OK)
            {
                BufferedReader br=new BufferedReader(new InputStreamReader(httpadresp.getInputStream()));
                StringBuffer odpowiedzi=new StringBuffer();
                String l;
                while ((l=br.readLine()) != null)
                {
                    odpowiedzi.append(l);
                }
                br.close();
                return odpowiedzi.toString();
            }else
            {
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}