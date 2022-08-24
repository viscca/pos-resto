package com.pos.restokasir.tools;

import android.graphics.drawable.Drawable;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class NavigationItem {
    private Drawable mDrawable;
    public JSONObject Data;

    public NavigationItem() {
    }

    public static NavigationItem BuatItem(JSONObject dt, Drawable gmbr) {
        NavigationItem x=new NavigationItem();
        try {
            x.Data = new JSONObject(dt.toString());
        } catch (JSONException e) {
            x.Data = new JSONObject();
        }
        x.mDrawable = gmbr;
        return x;
    }

    public static NavigationItem cusitem(String nama, Drawable gmbr, String HP, String email){
        NavigationItem x=new NavigationItem();
        x.Data = new JSONObject();
        try {
            x.Data.put("nama",nama);
            x.Data.put("hp",HP);
            x.Data.put("Email",email);
        } catch (JSONException e) {}
        x.mDrawable = gmbr;
        return x;
    }

    public static NavigationItem discitem(String nama, String Jml, Drawable gmbr){
        NavigationItem x=new NavigationItem();
        x.Data = new JSONObject();
        try {
            x.Data.put("nama",nama);
            x.Data.put("Jumlah",Jml);
        } catch (JSONException e) {}
        x.mDrawable = gmbr;
        return x;
    }

    public static NavigationItem catitem(String nama, String ket){
        NavigationItem x=new NavigationItem();
        x.Data = new JSONObject();
        try {
            x.Data.put("nama",nama);
            x.Data.put("ket",ket);
        } catch (JSONException e) {}
        return x;
    }

    public String getKey(String key){
        try {
            return Data.getString(key);
        } catch (JSONException e) {
            return "";
        }
    }

    public Drawable getDrawable(){ return mDrawable;}
}
