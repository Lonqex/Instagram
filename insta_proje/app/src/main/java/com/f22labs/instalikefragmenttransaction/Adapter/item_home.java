package com.f22labs.instalikefragmenttransaction.Adapter;

/**
 * Created by sierra on 26.10.2017.
 */

public class item_home {

    private String tip;
    private String url;
    private String uid;
    private long time;


    public item_home(){

    }

    public item_home(String tip,String url, String uid, long time) {

        this.tip=tip;
        this.url = url;
        this.uid = uid;
        this.time = time;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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
