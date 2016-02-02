package com.with.tourbuild;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Petrucco on 20.08.2015.
 */
public class MySing {


    private boolean imGuide;
    private Bitmap bmp;
    private String name;
    private String role;
    private String city;
    private int rate;
    private int ratesNum;
    private String id;
    private ParseGeoPoint myLocation;

    private ArrayList<String> specias = new ArrayList<String>();
    private ArrayList<String> interests = new ArrayList<String>();
    private ArrayList<String> langs = new ArrayList<String>();

    private ArrayList<String> mySpecs = new ArrayList<String>();
    private ArrayList<String> myLangs = new ArrayList<String>();
    private ArrayList<String> myInterests = new ArrayList<String>();


    public ArrayList<String> getLangs() {
        if (!myLangs.isEmpty()) {
            for (int i = 0; i < myLangs.size(); i++) {
                langs.remove(myLangs.get(i));
            }
        }
        return langs;
    }


    public ArrayList<String> getInterests() {
        for (int i = 0; i < myInterests.size(); i++) {
            interests.remove(myInterests.get(i));
        }
        return interests;
    }

    public void setInterests(ArrayList<String> interests) {
        this.interests = interests;
    }


    public Bitmap getBmp() {
        return bmp;
    }

    public void setBmp(Bitmap bmp) {
        this.bmp = bmp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLangs(ArrayList<String> langs) {
        this.langs = langs;
    }


    public String getRole() {
        return role;
    }

    public ArrayList<String> getSpecias() {
        for (int i = 0; i < mySpecs.size(); i++) {
            specias.remove(mySpecs.get(i));
        }
        return specias;
    }


    public ParseGeoPoint getMyLocation() {
        return myLocation;
    }

    public void setMyLocation(ParseGeoPoint myLocation) {
        this.myLocation = myLocation;
    }


    public void setRole(String role) {
        this.role = role;
    }

    public void setSpecias(ArrayList<String> specias) {
        this.specias = specias;
    }

    public ArrayList<String> getMySpecs() {
        return mySpecs;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setMySpecs(ArrayList<String> mySpecs) {
        this.mySpecs = mySpecs;
    }

    public ArrayList<String> getMyLangs() {
        return myLangs;
    }

    public void setMyLangs(ArrayList<String> myLangs) {
        this.myLangs = myLangs;
    }

    public boolean isImGuide() {
        return imGuide;
    }

    public void setImGuide(boolean imGuide) {
        this.imGuide = imGuide;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public int getRatesNum() {
        return ratesNum;
    }

    public void setRatesNum(int ratesNum) {
        this.ratesNum = ratesNum;
    }


    public ArrayList<String> getMyInterests() {
        return myInterests;
    }

    public void setMyInterests(ArrayList<String> myInterests) {
        this.myInterests = myInterests;
    }


    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    private static MySing instance = null;


    public static MySing getInstance() {
        if (instance == null) {
            instance = new MySing();
        }

        return instance;
    }

    public String getSpecs() {
        String sp = "";
        for (int a = 0; a < mySpecs.size(); a++) {
            sp = sp + mySpecs.get(a);
            if (a < mySpecs.size() - 1) {
                sp = sp + ", ";
            }
        }
        return sp;
    }

    public void loadImage() {
        if (ParseUser.getCurrentUser() != null) {
            ParseFile imageFile = (ParseFile) ParseUser.getCurrentUser().get("image");
            if (imageFile != null) {
                imageFile.getDataInBackground(new GetDataCallback() {
                    public void done(byte[] data, ParseException e) {
                        if (e == null) {
                            Bitmap bmp = BitmapFactory
                                    .decodeByteArray(
                                            data, 0,
                                            data.length);
                            setBmp(bmp);
                        } else {
                        }
                    }
                });
            }
        }
    }

    public void picToParse(Bitmap mBitmap) {
        double w = mBitmap.getWidth();
        double h = mBitmap.getHeight();
        double cf = 180 / w;
        double he = h * cf;

        Bitmap resized = Bitmap.createScaledBitmap(mBitmap, 180, (int) he, true);
        final ParseUser user = ParseUser.getCurrentUser();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        resized.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] ph = stream.toByteArray();
        final ParseFile imgFile = new ParseFile(ParseUser.getCurrentUser().getUsername() + ".jpg", ph);
        imgFile.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                } else {
                    user.put("image", imgFile);
                    user.saveInBackground();
                }
            }
        });
    }

    public void getAllSpecs() {
        specias.clear();
        specias.add("Choose your speciality");
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Specialities");
        query.findInBackground(new FindCallback<ParseObject>() {

            @Override
            public void done(List<ParseObject> objects, com.parse.ParseException e) {
                if (e == null) {
                    for (int i = 0; i < objects.size(); i++) {
                        ParseObject po = objects.get(i);
                        final String sp = po.get("Speciality").toString();
                        specias.add(sp);
                    }
                }
            }
        });
    }

    public void getAllInterests() {
        interests.clear();
        interests.add("Choose your interests");
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Specialities");
        query.findInBackground(new FindCallback<ParseObject>() {

            @Override
            public void done(List<ParseObject> objects, com.parse.ParseException e) {
                if (e == null) {
                    for (int i = 0; i < objects.size(); i++) {
                        ParseObject po = objects.get(i);
                        final String sp = po.get("Speciality").toString();
                        interests.add(sp);
                    }
                }
            }
        });
    }


    public void getAllLangs() {
        langs.clear();
        langs.add("Choose language");
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Languages");
        query.findInBackground(new FindCallback<ParseObject>() {

            @Override
            public void done(List<ParseObject> objects, com.parse.ParseException e) {
                if (e == null) {
                    for (int i = 0; i < objects.size(); i++) {
                        ParseObject po = objects.get(i);
                        final String lan = po.get("Lang").toString();
                        langs.add(lan);
                    }
                }
            }
        });
    }


    public void getAllMyData(String theRole) {
        ParseUser user = ParseUser.getCurrentUser();
        name = user.getUsername();
        role = theRole;
        myLangs = (ArrayList<String>) user.get("Langs");
        if (user.get("Interests") != null) {
            myInterests = (ArrayList<String>) user.get("Interests");
        }
        loadImage();
        if (role.equals("Guide")) getGuideData();
    }

    public String getLangsString() {
        String la = "";
        for (int a = 0; a < myLangs.size(); a++) {
            la = la + myLangs.get(a);
            if (a < myLangs.size() - 1) {
                la = la + ", ";
            }
        }
        return la;
    }

    public String getInterString() {
        String la = "";
        for (int a = 0; a < myInterests.size(); a++) {
            la = la + myInterests.get(a);
            if (a < myInterests.size() - 1) {
                la = la + ", ";
            }
        }
        return la;
    }

    public void getGuideData() {
        ParseUser user = ParseUser.getCurrentUser();
        try {
            user.fetch();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Guide");

        query.whereEqualTo("PointerToUser", user);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    try {
                        objects.get(0).fetch();
                    } catch (ParseException e1) {
                        e1.printStackTrace();
                    }
                    int rate = objects.get(0).getInt("Rate");
                    int ratesn = objects.get(0).getInt("RatesNumber");
                    String city = objects.get(0).getString("City");
                    ArrayList<String> spe = (ArrayList<String>) objects.get(0).get("Specialities");
                    MySing.getInstance().setRate(rate);
                    MySing.getInstance().setCity(city);
                    MySing.getInstance().setRatesNum(ratesn);
                    MySing.getInstance().setMySpecs(spe);
                }
            }
        });
    }

    public void logout() {
        ParsePush.unsubscribeInBackground(MySing.getInstance().getName());
        ParseUser.logOut();
        MySing.getInstance().setName(null);
        MySing.getInstance().setCity(null);
        MySing.getInstance().setBmp(null);
        MySing.getInstance().mySpecs.clear();
        MySing.getInstance().setRate(0);
        MySing.getInstance().setRatesNum(0);
        MySing.getInstance().myLangs.clear();
        MySing.getInstance().setRole(null);
        MySing.getInstance().setId(null);
        MySing.getInstance().myInterests.clear();
        getAllLangs();
        getAllSpecs();
        getAllInterests();
        CommonShared.getInstance().setmParseUser(null);
    }


}
