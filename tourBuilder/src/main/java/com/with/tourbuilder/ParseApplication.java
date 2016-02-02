package com.with.tourbuilder;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseInstallation;
import com.parse.ParseUser;

import android.app.Application;

public class ParseApplication extends Application {
	
	@Override
	public void onCreate() {
		
		super.onCreate();
		
//		Parse.initialize(this, "OzYWKFRMuZiSM42TCU9jvYhl5wlQohkkvQhJptfY",
//				"O3gSqnIuGqPDSqCPgwvNp85bjaBNOIKB8B4kD3i9");
//		ParseInstallation.getCurrentInstallation().saveInBackground();

		//ACL MAKES BUGS!!
//		ParseACL defaultACL = new ParseACL();
//		defaultACL.setPublicReadAccess(true);
//		defaultACL.setPublicWriteAccess(true);
//		ParseACL.setDefaultACL(defaultACL, true);
	}

}
