package com.with.tourbuild;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import sun.bob.mcalendarview.vo.DateData;

/**
 * Created by Petrucco on 09.09.2015.
 */
public class DateSing {
    private static DateSing instance = null;
    private ArrayList<DateData> dateList;
    private ArrayList<RegistrationTour> tourArrayList;

    public ArrayList<RegistrationTour> getTourArrayList() {
        return tourArrayList;
    }

    public void setTourArrayList(ArrayList<RegistrationTour> tourArrayList) {
        this.tourArrayList = tourArrayList;
    }



    public static DateSing getInstance() {
        if(instance == null){
            instance = new DateSing();
        }
        return instance;
    }

    private DateSing(){
        dateList = new ArrayList<DateData>();
    }

    public ArrayList<DateData> getDateList() {
        return dateList;
    }


    public void dates(String name){
        DateSing.getInstance().getDateList().clear();
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("REGISTRATION");
        query.whereEqualTo("guideName", name);
        query.findInBackground(new FindCallback<ParseObject>() {

                                   @Override
                                   public void done(List<ParseObject> objects, ParseException e) {
                                       for (int i = 0; i < objects.size(); i++) {
                                           ParseObject po = objects.get(i);
                                           Date date = (Date) po.get("Date");
                                           SimpleDateFormat fromUser = new SimpleDateFormat("yyyy/MM/dd");
                                           String da = fromUser.format(date);
                                           String[] nums = da.split("/");
                                           int y = Integer.parseInt(nums[0]);
                                           int m = Integer.parseInt(nums[1]);
                                           int d = Integer.parseInt(nums[2])-1;
                                           DateSing.getInstance().getDateList().add(new DateData(y, m, d));
                                       }
                                   }
                               }
        );
    }
}
