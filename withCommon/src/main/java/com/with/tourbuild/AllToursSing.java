package com.with.tourbuild;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Petrucco on 18.09.2015.
 */
public class AllToursSing {

    private static AllToursSing instance = null;
    private ArrayList<TourFromAll> tourFromAllArrayList;

    public static AllToursSing getInstance() {
        if(instance == null){
            instance = new AllToursSing();
        }
        return instance;
    }

    private AllToursSing(){
        tourFromAllArrayList = new ArrayList<TourFromAll>();
    }

    public ArrayList<TourFromAll> getTourFromAllArrayList() {
        return tourFromAllArrayList;
    }

}
