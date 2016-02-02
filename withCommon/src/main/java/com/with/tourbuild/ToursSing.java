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
public class ToursSing {

    private static ToursSing instance = null;
    private ArrayList<Tour> toursArray;


    public static ToursSing getInstance() {
        if(instance == null){
            instance = new ToursSing();
        }

        return instance;
    }

    private ToursSing(){
        toursArray = new ArrayList<Tour>();
        getCities();
    }

    public ArrayList<Tour> getToursArray() {
        return toursArray;
    }

public void getCities() {
    ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Tour");
    query.findInBackground(new FindCallback<ParseObject>() {

        @Override
        public void done(List<ParseObject> objects, com.parse.ParseException e) {
            if (e == null) {
                for (int i = 0; i < objects.size(); i++) {
                    ParseObject po = objects.get(i);
                    final String name = po.get("Name").toString();
                    final String price = po.get("Price").toString();
                    final String desc = po.get("Description").toString();
                    final String guiName = po.get("GuideName").toString();
                    final String type = String.valueOf(po.get("Type"));
                    final int popularity = (int) po.get("popularity");
                    ToursSing.getInstance().getToursArray().add(new Tour());

                }
            }
        }


    });
}
}
