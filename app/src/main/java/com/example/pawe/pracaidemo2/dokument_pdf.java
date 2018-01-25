package com.example.pawe.pracaidemo2;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by Paweł on 2017-09-13.
 */

public class dokument_pdf extends AsyncTask<String, Void, Dokumenty> {

    private Exception exception;
    Dokumenty dok = new Dokumenty();

    protected Dokumenty doInBackground(String... urls) {
        try {
            Log.d("TAG", "Wiadomosc_otworz: " + dok.link_plik_nazwa);
            File plik_pobierany = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + dok.link_plik_nazwa);
            pobranie_pdf("ftp.pracai.hol.es","u224299645","kajak66",dok.link_plik_nazwa,plik_pobierany);
        } catch (Exception e) {
            this.exception = e;
        }
        return null;
    }

    protected void onPostExecute(Dokumenty feed) {}

    private Boolean pobranie_pdf(String server, String login, String haslo, String plik_nazwa, File plik_nazwa_telefon) throws IOException {
        FTPClient ftpc = null;

        try {
            ftpc = new FTPClient();
            ftpc.connect(server);

            ftpc.login(login, haslo);
            ftpc.setFileType(FTP.BINARY_FILE_TYPE);
            ftpc.enterLocalPassiveMode();

            OutputStream os = null;
            boolean wynik = false;
            try {
                os = new BufferedOutputStream(new FileOutputStream(plik_nazwa_telefon));
                wynik = ftpc.retrieveFile(plik_nazwa, os);
            } finally {
                if (os != null) {
                    os.close();
                }
            }
            return wynik;
        } finally {
            if (ftpc != null) {
                ftpc.logout();
                ftpc.disconnect();
            }
        }
    }
}
