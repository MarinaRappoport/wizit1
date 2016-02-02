package com.with.tourbuild;

import java.util.ArrayList;

/**
 * Created by Petrucco on 14.09.2015.
 */
public class UsersSing {

    private int radius;

    private int time;

    private boolean visible;

    public boolean isSeeOthers() {
        return seeOthers;
    }

    public void setSeeOthers(boolean seeOthers) {
        this.seeOthers = seeOthers;
    }

    private boolean seeOthers;

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public ArrayList<User> getUserArrayList() {
        return userArrayList;
    }

    public void setUserArrayList(ArrayList<User> userArrayList) {
        this.userArrayList = userArrayList;
    }

    private ArrayList<User> userArrayList = new ArrayList<User>();

    public ArrayList<User> getInterList() {
        return interList;
    }

    public void setInterList(ArrayList<User> interList) {
        this.interList = interList;
    }

    private ArrayList<User> interList = new ArrayList<User>();

    public ArrayList<User> getFilteredUsers() {
        return filteredUsers;
    }

    public void setFilteredUsers(ArrayList<User> filteredUsers) {
        this.filteredUsers = filteredUsers;
    }

    private ArrayList<User> filteredUsers = new ArrayList<User>();
    private ArrayList<User> filteredByIntUsers = new ArrayList<User>();

    private static UsersSing instance = null;

    public static UsersSing getInstance() {
        if (instance == null) {
            instance = new UsersSing();
        }

        return instance;
    }

    public void filterUsers() {
        filteredByIntUsers.clear();
        filteredUsers.clear();
        for (int i = 0; i < userArrayList.size(); i++) { //идем по юзерам
            userArrayList.get(i).setCommonInterest("");
            for (int j = 0; j < userArrayList.get(i).getInterests().size(); j++) { //идем по интересам
                String interest = userArrayList.get(i).getInterests().get(j);
                if (MySing.getInstance().getMyInterests().contains(interest)) {
                    userArrayList.get(i).setCommonInterest(userArrayList.get(i).getCommonInterest() + " " + interest);
                    filteredByIntUsers.add(userArrayList.get(i));
                }
            }
        }
        for (int i = 0; i < filteredByIntUsers.size(); i++) {
            filteredByIntUsers.get(i).setCommonLang("");
            for (int j = 0; j < filteredByIntUsers.get(i).getLangs().size(); j++) {
                String lang = filteredByIntUsers.get(i).getLangs().get(j);
                if (MySing.getInstance().getMyLangs().contains(lang)) {
                    filteredByIntUsers.get(i).setCommonLang(filteredByIntUsers.get(i).getCommonLang() + " " + lang);
                    filteredUsers.add(filteredByIntUsers.get(i));
                }
            }
        }
    }
}
