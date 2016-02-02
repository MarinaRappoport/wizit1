package com.with.tourbuild;

import java.util.ArrayList;

/**
 * Created by Petrucco on 18.09.2015.
 */
public class AllReqsSing {

    private static AllReqsSing instance = null;
    private ArrayList<ReqFromAll> reqFromAllArrayList;

    public static AllReqsSing getInstance() {
        if(instance == null){
            instance = new AllReqsSing();
        }
        return instance;
    }

    private AllReqsSing(){
        reqFromAllArrayList = new ArrayList<ReqFromAll>();
    }

    public ArrayList<ReqFromAll> getTourFromAllArrayList() {
        return reqFromAllArrayList;
    }

}
