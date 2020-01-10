package com.f22labs.instalikefragmenttransaction.Adapter;

public class item_comment {

    private String id;
    private String Aciklama;
    private long time;

    public item_comment(){

    }

    public item_comment(String id, String Aciklama, long time) {
        this.id = id;
        this.Aciklama = Aciklama;
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAciklama() {
        return Aciklama;
    }

    public void setAciklama(String aciklama) {
        this.Aciklama = aciklama;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
