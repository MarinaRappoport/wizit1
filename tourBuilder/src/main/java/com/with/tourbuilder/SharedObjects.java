package com.with.tourbuilder;

import java.util.Vector;



public class SharedObjects  {
	
	public enum OpearionalMode { NONE, NEW_POI, BUILD_TOUR};
	OpearionalMode mMode;
	
	public OpearionalMode getmMode() {
		return mMode;
	}



	public void setmMode(OpearionalMode mMode) {
		this.mMode = mMode;
	}

	private static SharedObjects instance = null;

	public static SharedObjects getInstance()
	{
		if (instance == null)
			instance = new SharedObjects();
		return instance;
	}

	
	private SharedObjects() {
		mMode = OpearionalMode.NEW_POI;
		
		mRegistrationRequests = new  Vector<RegistrationRequest>();
		mRegisteredUsers = new  Vector<RegisteredUsers>();
	}



	private Vector<RegistrationRequest> mRegistrationRequests;

	public Vector<RegisteredUsers> getmRegisteredUsers() {
		return mRegisteredUsers;
	}

	private Vector<RegisteredUsers> mRegisteredUsers;

	public Vector<RegistrationRequest> getmRegistrationRequests() {
		return mRegistrationRequests;
	}


	
}
