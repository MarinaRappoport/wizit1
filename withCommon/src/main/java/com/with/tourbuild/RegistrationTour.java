package com.with.tourbuild;

import java.util.Date;

/**
 * Created by Petrucco on 17.09.2015.
 */
public class RegistrationTour {

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getTouristsNum() {
        return touristsNum;
    }

    public void setTouristsNum(int touristsNum) {
        this.touristsNum = touristsNum;
    }

    public String getTourName() {
        return tourName;
    }

    public void setTourName(String tourName) {
        this.tourName = tourName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public RegistrationTour(int touristsNum, String tourName, String userName, String comment, Date date) {
        this.touristsNum = touristsNum;
        this.tourName = tourName;
        this.userName = userName;
        this.comment = comment;
        this.date = date;
    }

    private int touristsNum;
    private String tourName;
    private String userName;
    private String comment;
    private Date date;




}
