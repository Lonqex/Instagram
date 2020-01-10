package com.f22labs.instalikefragmenttransaction.Adapter;

public class Item_Chat {
    private String mesaj,uid;
    private long time;

    public Item_Chat()
    {

    }

    public Item_Chat(String mesaj, String uid, long time) {
        this.mesaj = mesaj;
        this.uid = uid;
        this.time = time;
    }

    public String getMesaj() {
        return mesaj;
    }

    public void setMesaj(String mesaj) {
        this.mesaj = mesaj;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
