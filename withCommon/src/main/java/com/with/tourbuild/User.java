package com.with.tourbuild;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.parse.ParseGeoPoint;

import java.util.ArrayList;

/**
 * Created by Petrucco on 14.09.2015.
 */
public class User {

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public ParseGeoPoint getGeoPoint() {
        return geoPoint;
    }

    public void setGeoPoint(ParseGeoPoint geoPoint) {
        this.geoPoint = geoPoint;
    }

    public ArrayList<String> getLangs() {
        return langs;
    }

    public void setLangs(ArrayList<String> langs) {
        this.langs = langs;
    }

    public ArrayList<String> getInterests() {
        return interests;
    }

    public void setInterests(ArrayList<String> interests) {
        this.interests = interests;
    }

    public User(String username, ParseGeoPoint geoPoint, ArrayList<String> langs, ArrayList<String> interests) {
        this.username = username;
        this.geoPoint = geoPoint;
        this.langs = langs;
        this.interests = interests;
    }

    public User(String username, Bitmap bmp, String role) {
        this.username = username;
        this.bmp = bmp;
        this.role = role;
    }


    public User(String username, String role) {
        this.username = username;
        this.role = role;
    }

    private String username;
    private ParseGeoPoint geoPoint;
    private ArrayList<String>langs;
    private ArrayList<String>interests;
    private String commonInterest = "";
    private Bitmap bmp;
    private String role;

    public Bitmap getBmp() {
        return bmp;
    }

    public void setBmp(Bitmap bmp) {
        this.bmp = bmp;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }



    public String getCommonLang() {
        return commonLang;
    }

    public void setCommonLang(String commonLang) {
        this.commonLang = commonLang;
    }

    private String commonLang = "";

    public String getCommonInterest() {
        return commonInterest;
    }

    public void setCommonInterest(String commonInterest) {
        this.commonInterest = commonInterest;
    }


    public Drawable getPhoto(){
        BitmapDrawable ob = new BitmapDrawable(bmp);
        return ob;
    }
}
