package com.with.tourbuild;



import android.os.Binder;

public class MapBinder extends Binder {

	GPS_Tracker mService;

	public GPS_Tracker getmService() {
		return mService;
	}

	public void setmService(GPS_Tracker mService) {
		this.mService = mService;
	}

	public MapBinder(GPS_Tracker mService) {
		super();
		this.mService = mService;
	}
}
