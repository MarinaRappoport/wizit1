package com.with.tourbuild;

import java.util.ArrayList;

/**
 * Created by Petrucco on 20.08.2015.
 */
public class GuideSing {

    private static GuideSing instance = null;
    private ArrayList<Guide> guideArrayList;

    public static GuideSing getInstance() {
        if(instance == null){
            instance = new GuideSing();
        }
        return instance;
    }

    private GuideSing(){
        guideArrayList = new ArrayList<Guide>();
    }

    public ArrayList<Guide> getGuideArrayList() {
        return guideArrayList;
    }
}
