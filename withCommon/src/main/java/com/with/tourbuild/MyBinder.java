package com.with.tourbuild;



import android.os.Binder;

public class MyBinder extends Binder {
	
	MyService mService;

	public MyService getmService() {
		return mService;
	}

	public void setmService(MyService mService) {
		this.mService = mService;
	}

	public MyBinder(MyService mService) {
		super();
		this.mService = mService;
	}
	

}
