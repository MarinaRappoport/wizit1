package com.with.tourbuild;

public class Poi {
	private String mObjectId;
	private String mName;
	private String mDescription;
	private double mLat;
	private double mLong;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	//	NEW!!!
	private String type;
	private String address;
	private String workHours;
	private String web;

//	NEW!!!


	public String getmObjectId() {
		return mObjectId;
	}
	public void setmObjectId(String mObjectId) {
		this.mObjectId = mObjectId;
	}
	public String getmName() {
		return mName;
	}
	public void setmName(String mName) {
		this.mName = mName;
	}
	public String getmDescription() {
		return mDescription;
	}
	public void setmDescription(String mDescription) {
		this.mDescription = mDescription;
	}
	public double getmLat() {
		return mLat;
	}
	public void setmLat(double mLat) {
		this.mLat = mLat;
	}
	public double getmLong() {
		return mLong;
	}
	public void setmLong(double mLong) {
		this.mLong = mLong;
	}

}
