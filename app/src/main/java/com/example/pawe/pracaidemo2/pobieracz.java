package com.example.pawe.pracaidemo2;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

/**
 * Created by Paweł on 2017-09-02.
 */

public class pobieracz extends AsyncTask<Void,Void,String> {

    Context c;
    String adresg;
    Spinner sp;

    ProgressDialog pd;

    public pobieracz(Context c, String adres, Spinner sp) {
        this.c = c;
        this.adresg = adres;
        this.sp = sp;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        pd=new ProgressDialog(c);
        pd.setTitle("Sprawdzanie");
        pd.setMessage("Sprawdzanie...Prosze czekać");
        pd.show();
    }

    @Override
    protected String doInBackground(Void... params) {
        return this.downloadData();
    }


    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        pd.dismiss();
        if(s==null)
        {
            Toast.makeText(c,"Nie można pobrać, zwraca null",Toast.LENGTH_SHORT).show();
        }else
        {
            Toast.makeText(c,"Udało się",Toast.LENGTH_SHORT).show();
            parser parser=new parser(c,sp,s);
            parser.execute();
        }
    }

    private String downloadData()
    {
        HttpURLConnection httpadresp=Lacznik.connect(adresg);
        if(httpadresp==null)
        {
            return null;
        }
        InputStream is=null;
        try {
            is=new BufferedInputStream(httpadresp.getInputStream());
            BufferedReader br=new BufferedReader(new InputStreamReader(is));
            String l=null;
            StringBuffer odpowiedzi=new StringBuffer();
            if(br != null)
            {
                while ((l=br.readLine()) != null)
                {
                    odpowiedzi.append(l+"\n");
                }

                br.close();

            }else {
                return null;
            }
            return odpowiedzi.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(is != null)
            {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}













