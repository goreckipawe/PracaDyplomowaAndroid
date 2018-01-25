package com.example.pawe.pracaidemo2;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

/**
 * Created by Paweł on 2017-09-08.
 */

public class wiadomosc_pdf extends AsyncTask<String, Void, Wiadomosc_otworz> {

    private Exception exception;
    Wiadomosc_otworz wo = new Wiadomosc_otworz();

    protected Wiadomosc_otworz doInBackground(String... urls) {
        try {
            File plikpobierany = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + wo.link());
            downloadAndSaveFile("ftp.pracai.hol.es","u224299645","kajak66",wo.link(),plikpobierany);
        } catch (Exception e) {
            this.exception = e;
        }
        return null;
    }

    protected void onPostExecute(Wiadomosc_otworz f) {
    }

    private Boolean downloadAndSaveFile(String server, String login, String haslo, String plik, File plikt)
            throws IOException {
        FTPClient ftpc = null;

        try {
            ftpc = new FTPClient();
            ftpc.connect(server);
            Log.d("TAG", "Połączony: " + ftpc.getReplyString());

            ftpc.login(login, haslo);
            Log.d("TAG", "Zalogowany");
            ftpc.setFileType(FTP.BINARY_FILE_TYPE);
            Log.d("TAG", "Pobieranie");
            ftpc.enterLocalPassiveMode();

            OutputStream os = null;
            boolean udalo_sie = false;
            try {
                os = new BufferedOutputStream(new FileOutputStream(plikt));
                udalo_sie = ftpc.retrieveFile(plik, os);
            } finally {
                if (os != null) {
                    Log.d("TAG", os.toString());
                    os.close();
                }
            }
            return udalo_sie;
        } finally {
            if (ftpc != null) {
                ftpc.logout();
                ftpc.disconnect();
            }
        }
    }
}
