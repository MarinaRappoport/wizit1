package com.with.tourbuild;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.parse.ParseGeoPoint;
import com.parse.ParseObject;

import java.util.ArrayList;

public class Guide {


	private boolean voted = false;
	private String         mGuideName;
	private String         mGuideId;
	private int            mGuideRate;
	private int            mRatesNumber;
	private ParseObject    mParseObject; //Tour object using for rating the guide

	private Bitmap bmp;
	private String city;
	private ParseGeoPoint location;

	public ArrayList<String> specialities;

	public Guide(String name, int rate, int ratesNumber, Bitmap bmp, ParseGeoPoint location, String city, ArrayList<String>specialities, String id) {
		this.mGuideName = name;
		this.mGuideRate = rate;
		this.mRatesNumber = ratesNumber;
		this.bmp = bmp;
		this.location = location;
		this.city = city;
		this.specialities = specialities;
		this.mGuideId = id;
	}

	public Guide(){};






	public Bitmap getBmp() {
		return bmp;
	}

	public void setBmp(Bitmap bmp) {
		this.bmp = bmp;
	}

	public ParseGeoPoint getLocation() {
		return location;
	}

	public void setLocation(ParseGeoPoint location) {
		this.location = location;
	}


	public Drawable getGuidePhoto(){
		BitmapDrawable ob = new BitmapDrawable(bmp);
		return ob;
	}


	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public ArrayList<String> getSpecialities() {
		return specialities;
	}

	public void setSpecialities(ArrayList<String> specialities) {
		this.specialities = specialities;
	}

	public String getSpecs(){
		String sp = "";
		for (int i = 0; i<specialities.size(); i++){
			sp = sp + specialities.get(i);
			if(i<specialities.size()-1){
				sp = sp + ", ";
			}
		}
		return sp;
	}



	public String getmGuideName() {
		return mGuideName;
	}
	public void setmGuideName(String mGuideName) {
		this.mGuideName = mGuideName;
	}
	public int getmGuideRate() {
		return mGuideRate;
	}
	public void setmGuideRate(int mGuideRate) {
		this.mGuideRate = mGuideRate;
	}
	public int getmRatesNumber() {
		return mRatesNumber;
	}
	public void setmRatesNumber(int mRatesNumber) {
		this.mRatesNumber = mRatesNumber;
	}
	public ParseObject getmParseObject() {
		return mParseObject;
	}
	public void setmParseObject(ParseObject mParseObject) {
		this.mParseObject = mParseObject;
	}
	public String getmGuideId() {
		return mGuideId;
	}
	public void setmGuideId(String mGuideId) {
		this.mGuideId = mGuideId;
	}

	public float finRate(){
		float fr;
		if (getmRatesNumber()>0){
		fr = (float)(getmGuideRate()/getmRatesNumber())/2;}
		else{
			fr = 0;
		}
		return fr;
	}

	public boolean isVoted() {
		return voted;
	}

	public void setVoted(boolean voted) {
		this.voted = voted;
	}

}
