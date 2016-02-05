package com.with.tourbuild;

import android.graphics.Bitmap;

import java.util.Vector;

import com.parse.ParseObject;

public class Tour {


	public boolean isVoted = false;
	private Vector<Poi>    mPois;
	private int            mTourType;
	private String         mTourDescription;
	private String         mTourName;
	private String         mGuideName;
	private String         mTourId;
	private int            mTourRate;
	private int            mRatesNumber;
	private int            mPrice;
	private ParseObject    mParseObject; //Tour object using for rating the tour

	private Bitmap mTourImage;

	public Bitmap getmTourImage() {
		return mTourImage;
	}

	public void setmTourImage(Bitmap mTourImage) {
		this.mTourImage = mTourImage;
	}

	public boolean isVoted() {
		return isVoted;
	}

	public void setIsVoted(boolean isVoted) {
		this.isVoted = isVoted;
	}

	public int getmPrice() {
		return mPrice;
	}

	public void setmPrice(int mPrice) {
		this.mPrice = mPrice;
	}

	public ParseObject getmParseObject() {
		return mParseObject;
	}

	public void setmParseObject(ParseObject mParseObject) {
		this.mParseObject = mParseObject;
	}

	public int getmTourRate() {
		return mTourRate;
	}

	public float finRate(){
		float fr;

		if (getmRatesNumber()>0) {
			fr = (float)(getmTourRate()/getmRatesNumber())/2;}
		else fr = 0;
		return fr;
	}

	public void setmTourRate(int mTourRate) {
		this.mTourRate = mTourRate;
	}

	public int getmRatesNumber() {
		return mRatesNumber;
	}

	public void setmRatesNumber(int mRatesNumber) {
		this.mRatesNumber = mRatesNumber;
	}

	public String getmTourId() {
		return mTourId;
	}

	public void setmTourId(String mTourId) {
		this.mTourId = mTourId;
	}

	public String getmGuideName() {
		return mGuideName;
	}

	public void setmGuideName(String mGuideName) {
		this.mGuideName = mGuideName;
	}

	public Tour() {
		mPois = new Vector<Poi>();
		mTourType        = 1;
		mTourDescription = null;
		mTourName        = null;
		mGuideName       = null;
		mPrice       	 = 0;
	}
	
	public int getmTourType() {
		return mTourType;
	}
	public void setmTourType(int mTourType) {
		this.mTourType = mTourType;
	}
	public String getmTourDescription() {
		return mTourDescription;
	}
	public void setmTourDescription(String mTourDescription) {
		this.mTourDescription = mTourDescription;
	}
	public String getmTourName() {
		return mTourName;
	}
	public void setmTourName(String mTourName) {
		this.mTourName = mTourName;
	}
	public Vector<Poi> getmPois() {
		return mPois;
	}

}
