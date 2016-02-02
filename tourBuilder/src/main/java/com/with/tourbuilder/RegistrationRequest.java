package com.with.tourbuilder;


public class RegistrationRequest {

	private String  mTourId;
	private String  mUserName;
	private String  mTourName;
	private String  mObjectId;
	private String  mGuideName;
	private String  mComment;
	private boolean mRegistered;
	private int amount;
	private String date;
	
	
	public boolean ismRegistered() {
		return mRegistered;
	}

	public void setmRegistered(boolean mRegistered) {
		this.mRegistered = mRegistered;
	}

	public String getmGuideName() {
		return mGuideName;
	}

	public void setmGuideName(String mGuideName) {
		this.mGuideName = mGuideName;
	}

	public RegistrationRequest() {
		
	}
	
	public String getmObjectId() {
		return mObjectId;
	}

	public void setmObjectId(String mObjectId) {
		this.mObjectId = mObjectId;
	}

	public String getmTourId() {
		return mTourId;
	}
	public void setmTourId(String mTourId) {
		this.mTourId = mTourId;
	}
	public String getmUserName() {
		return mUserName;
	}
	public void setmUserName(String mUserName) {
		this.mUserName = mUserName;
	}
	public String getmTourName() {
		return mTourName;
	}
	public void setmTourName(String mTourName) {
		this.mTourName = mTourName;
	}
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getmComment() {
		return mComment;
	}

	public void setmComment(String mComment) {
		this.mComment = mComment;
	}
	

}
