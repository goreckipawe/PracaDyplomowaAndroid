package com.example.pawe.pracaidemo2;

/**
 * Created by Paweł on 2017-09-13.
 */

public class dokument_get_set {

    private String id;
    private String tytul;
    private String data;
    private String typ;
    private String plik;

    public dokument_get_set(String idd, String tytull, String dataa, String typp, String plikk) {
        this.tytul = tytull;
        this.data = dataa;
        this.typ = typp;
        this.id = idd;
        this.plik = plikk;
    }

    public String getTytul() {
        return tytul;
    }

    public void setTytul(String tytul) {
        this.tytul = tytul;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getTyp() {
        return typ;
    }

    public void setTyp(String typ) {
        this.typ = typ;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPlik() {
        return plik;
    }

    public void setPlik(String plik) {
        this.plik = plik;
    }
}
