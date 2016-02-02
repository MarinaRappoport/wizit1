package com.with.tourbuild;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Petrucco on 20.08.2015.
 */
public class CitySing {

    private static CitySing instance = null;
    private ArrayList<City> cityArray;


    public static CitySing getInstance() {
        if(instance == null){
            instance = new CitySing();
        }

        return instance;
    }

    private CitySing(){
        cityArray = new ArrayList<City>();
    }

    public ArrayList<City> getCityArray() {
        return cityArray;
    }
    public ArrayList<String> getCityNames() {
        ArrayList<String> cityNames = new ArrayList<String>();
        for (int i = 0; i<cityArray.size(); i++){
            cityNames.add(cityArray.get(i).getName());
        }
        return cityNames;
    }
}
