package com.with.tourbuild;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class GPS_Tracker extends Service implements LocationListener, Runnable{

	private final Context context;
    private UpdateIF mActivity;
	ParseGeoPoint pgp;
	
	boolean isGPSEnabled = false;
	boolean isNetworkEnabled = false;
	boolean canGetLocation = false;
	
	Location location;
	private Thread T;
	
	double latitude;
	double longitude;
	int counter = 0;
	
	private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 30;
	private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;
	
	protected LocationManager locationManager;

    public UpdateIF getmActivity() {
        return mActivity;
    }


    public void setmActivity(UpdateIF mActivity) {
        this.mActivity = mActivity;
    }


    public GPS_Tracker(Context context) {
		this.context = context;
		getLocation();
	}

    public GPS_Tracker() {
        context = this;
        getLocation();
    }
	
	public Location getLocation() {
		try {
			locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
			
			isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
			
			isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
			
			if(!isGPSEnabled && !isNetworkEnabled) {
				
			} else {
				this.canGetLocation = true;
				
				if (isNetworkEnabled) {
					
					locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

					if (locationManager != null) {
						location = locationManager
								.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

						if (location != null) {
							
							latitude = location.getLatitude();
							longitude = location.getLongitude();
						}
					}

				}
				
				if(isGPSEnabled) {
					if(location == null) {
						locationManager.requestLocationUpdates(
								LocationManager.GPS_PROVIDER,
								MIN_TIME_BW_UPDATES,
								MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
						
						if(locationManager != null) {
							location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
							
							if(location != null) {
								latitude = location.getLatitude();
								longitude = location.getLongitude();
							}
						}
					}
				}
 			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		MySing.getInstance().setMyLocation(new ParseGeoPoint(getLatitude(), getLongitude()));
		pgp = new ParseGeoPoint(getLatitude(), getLongitude());
		return location;
	}
	
	
	public void stopUsingGPS() {
		if(locationManager != null) {
			locationManager.removeUpdates(GPS_Tracker.this);
		}
	}
	
	public double getLatitude() {
		if(location != null) {
			latitude = location.getLatitude();
		}
		return latitude;
	}
	
	public double getLongitude() {
		if(location != null) {
			longitude = location.getLongitude();
		}
		
		return longitude;
	}
	
	public boolean canGetLocation() {
		return this.canGetLocation;
	}
	
	public void showSettingsAlert() {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
		
		alertDialog.setTitle("GPS is settings");
		
		alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");
		
		alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
				context.startActivity(intent);
			}
		});
		
		alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		
		alertDialog.show();
	}
	
	@Override
	public void onLocationChanged(Location arg0) {

	}

	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return new MapBinder(this);
	}

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        getAllLocs();
        return super.onStartCommand(intent, flags, startId);
    }

    private void getAllLocs(){
        getLocation();
        UsersSing.getInstance().getUserArrayList().clear();
        ParseQuery<ParseUser> pq = ParseUser.getQuery();
		pgp = new ParseGeoPoint(getLatitude(), getLongitude());
        pq.whereWithinKilometers("location", pgp, UsersSing.getInstance().getRadius());
		pq.whereEqualTo("Visible", true);
        pq.setLimit(50);
        pq.whereNotEqualTo("username", MySing.getInstance().getName());
        pq.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                for (int i = 0; i < objects.size(); i++) {
                    if (objects.get(i).get("location") != null) {
                        ParseUser pu = objects.get(i);
                        ParseGeoPoint pg = (ParseGeoPoint) pu.get("location");
                        UsersSing.getInstance().getUserArrayList().add(new User(pu.getUsername(), pg, (ArrayList) pu.get("Langs"), (ArrayList) pu.get("Interests")));
                        if (mActivity != null) {
                            mActivity.Update();
                        }
                    }
                }
            }
        });

    }

	private void saveMyLoc(){
		getLocation();
		if (UsersSing.getInstance().isVisible()==true){
			ParseUser.getCurrentUser().put("location", pgp);
		}
	}

	@Override
	public void run() {
		getLocation();
		if(UsersSing.getInstance().getTime()>0){
		counter = (counter + 1)%UsersSing.getInstance().getTime();}
		else{counter = (counter + 1)%60;}
		if (counter==1){
			if (UsersSing.getInstance().isVisible()==true) {saveMyLoc();}
			if (UsersSing.getInstance().isSeeOthers()==true){getAllLocs();}}
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onCreate() {
		T = new Thread(this);
		T.start();
		super.onCreate();
	}

	public void resetCounter(){
		counter=0;
	}


}
