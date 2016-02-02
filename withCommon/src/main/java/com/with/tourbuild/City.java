package com.with.tourbuild;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

/**
 * Created by Petrucco on 19.08.2015.
 */
public class City {

    public City(String name, int popularity, Bitmap bmp) {
        this.name = name;
        this.bmp = bmp;
        this.popularity = popularity;
    }

    public String name;
    public Bitmap bmp;
    public int popularity;



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Bitmap getBmp() {
        return bmp;
    }

    public Drawable getPhoto(){
        BitmapDrawable ob = new BitmapDrawable(bmp);
        return ob;
    }

    public void setBmp(Bitmap bmp) {
        this.bmp = bmp;
    }

    public int getPopularity() {
        return popularity;
    }

    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }
}
