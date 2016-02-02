package com.with.tourbuild;

import java.util.ArrayList;

/**
 * Created by Petrucco on 20.09.2015.
 */
public class IntersSing {

    private static IntersSing instance = null;
    public static IntersSing getInstance() {
        if(instance == null){
            instance = new IntersSing();
        }

        return instance;
    }

    private IntersSing(){
        interlocsArray = new ArrayList<User>();
    }


    public ArrayList<User> getInterlocsArray() {
        return interlocsArray;
    }

    public void setInterlocsArray(ArrayList<User> interlocsArray) {
        this.interlocsArray = interlocsArray;
    }

    private ArrayList<User> interlocsArray;

}
