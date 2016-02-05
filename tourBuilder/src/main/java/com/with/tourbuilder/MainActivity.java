package com.with.tourbuilder;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.osmdroid.tileprovider.IRegisterReceiver;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.PathOverlay;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.masa.tlalim.offlinemap.HybridMap;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.with.tourbuild.CommonShared;
import com.with.tourbuild.MySing;
import com.with.tourbuild.Poi;
import com.with.tourbuild.Tour;
import com.with.tourbuilder.SharedObjects.OpearionalMode;

public class MainActivity extends ActionBarActivity implements IPostListener,IRegisterReceiver {
	
	MapController mMapController;
	HybridMap hybridMap;
	MapView       mMapView; 
	GeoPoint mMyLocation;

	PoisOverlay            mPointOverlay = null;
	ArrayList<OverlayItem> mOverlayItems;
	PathOverlay            mPathOverlay = null;
	Drawable newMarker;
	Drawable markerAttr;

	private void createPathOverlay() {
		mPathOverlay  = new PathOverlay(Color.BLUE, this);
		Paint pPaint = mPathOverlay.getPaint();
	    pPaint.setStrokeWidth(5);
	    mPathOverlay.setPaint(pPaint);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tourbuilder);

		Bitmap original = BitmapFactory.decodeResource(getResources(), R.drawable.hlogo);
		newMarker = new BitmapDrawable(getResources(), original);

		//for all public points
		Bitmap tourism = BitmapFactory.decodeResource(getResources(), R.drawable.marker_gr_tourism);
		markerAttr = new BitmapDrawable(getResources(), tourism);
		
		mOverlayItems = new ArrayList<OverlayItem>();

		SharedObjects.getInstance().setmMode(OpearionalMode.NONE);


//		mMapView = (MapView) findViewById(R.id.mapview);
		hybridMap = (HybridMap) findViewById(R.id.hybridTBMap);
		mMapView = hybridMap.mv;

		mMapView.setBuiltInZoomControls(true);
		mMapView.setMultiTouchControls(true);
		if(MySing.getInstance().getCity().equals("Tel-Aviv")){
			mMyLocation = new GeoPoint(32.069454, 34.765379);
		}
		else if(MySing.getInstance().getCity().equals("Jerusalem")){
			mMyLocation = new GeoPoint(31.772518, 35.211699);
		}
		else if(MySing.getInstance().getCity().equals("Haifa")){
			mMyLocation = new GeoPoint(32.808899, 34.983733);}
		else{
			mMyLocation = new GeoPoint(32.808899, 34.983733);
		}
		mMapView.getController().setCenter(mMyLocation);
		mMapView.getController().setZoom(14);

		mMapController = mMapView.getController();
		mMapController.setZoom(15);
		mMapController.animateTo(mMyLocation);
		createPathOverlay();
		mPointOverlay = new PoisOverlay(mMapView.getContext(), mOverlayItems, this);
		mMapView.getOverlays().add(mPointOverlay);
		addPois(mOverlayItems);
	}

//	for what we need the newoverlay ???

	public void addPois(final ArrayList<OverlayItem> newoverlay) {
		ParseQuery<ParseObject> query = ParseQuery.getQuery("POI");
		//filter by guide
		query.whereEqualTo("Creator", ParseUser.getCurrentUser());
		query.findInBackground(new FindCallback<ParseObject>() {
			@Override
			public void done(List<ParseObject> objects, ParseException e) {

				if (e == null) {
					CommonShared.getInstance().getmPois().clear();
					CommonShared.getInstance().ReadPois(objects, "private");
//					updateGui("Pois");
				}
			}
		});

		ParseQuery<ParseObject> queryAttr = ParseQuery.getQuery("Attractions");
		queryAttr.findInBackground(new FindCallback<ParseObject>() {
			@Override
			public void done(List<ParseObject> objects, ParseException e) {
				if(e == null) {
//					CommonShared.getInstance().getmPois().clear();
					CommonShared.getInstance().ReadPois(objects, "attraction");
					updateGui("Pois");
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_normal_mode, menu);
		
		return true;
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		if(menu != null) {
			menu.clear();
		}
		if(SharedObjects.getInstance().getmMode() == OpearionalMode.NONE){
			menu.setGroupVisible(R.menu.menu_normal_mode, false);
			getMenuInflater().inflate(R.menu.menu_normal_mode, menu);
			
		}
		else if(SharedObjects.getInstance().getmMode() == OpearionalMode.NEW_POI){
			menu.setGroupVisible(R.menu.menu_normal_mode, false);
			getMenuInflater().inflate(R.menu.menu_normal_mode, menu);
			
		}
		else if(SharedObjects.getInstance().getmMode() == OpearionalMode.BUILD_TOUR) {
			menu.setGroupVisible(R.menu.menu_tour, false);
			getMenuInflater().inflate(R.menu.menu_tour, menu);
			
		}
		return super.onPrepareOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent;
		if (item.getItemId() == R.id.starttour) {
			CommonShared.getInstance().setmCurrentTour(new Tour());
			SharedObjects.getInstance().setmMode(OpearionalMode.BUILD_TOUR);
		}
		if (item.getItemId() == R.id.addpoint) {
			SharedObjects.getInstance().setmMode(OpearionalMode.NEW_POI);
		}
		if (item.getItemId() == R.id.tourDetails) {
			intent = new Intent(getApplicationContext(), TourDetails.class);
			startActivity(intent);
		}

		if (item.getItemId() == R.id.save){
			Tour tour = CommonShared.getInstance().getmCurrentTour();
			if (tour.getmTourName() == null || tour.getmTourDescription() == null) {
				Toast.makeText(getApplicationContext(), "Please fill tour details", Toast.LENGTH_LONG).show();
				intent = new Intent(getApplicationContext(), TourDetails.class);
				startActivity(intent);
			}
			else {
				SharedObjects.getInstance().setmMode(OpearionalMode.NONE);
				tourToParse(this);
			}}
		if (item.getItemId() ==  R.id.cancel) {
			SharedObjects.getInstance().setmMode(OpearionalMode.NONE);
		}
		return super.onOptionsItemSelected(item);
	}

	
	@Override
	public void updateGui(final String dataType) {
		runOnUiThread(new Runnable() {
			public void run() {
				if (dataType.equals("Pois")) {
					Vector<Poi> pois = CommonShared.getInstance().getmPois();
					
					// Clear old overlay
					mMapView.getOverlays().remove(mPointOverlay);
					mOverlayItems.clear();
					
					// Build new overlay
					for (Iterator iterator = pois.iterator(); iterator.hasNext();) {
						Poi poi = (Poi) iterator.next();
						OverlayItem item = new OverlayItem(poi.getmName(),
								poi.getmDescription(), new GeoPoint(poi.getmLat(), poi.getmLong()));
						if(poi.getType().equals("attraction")){
							item.setMarker(markerAttr);
						}else item.setMarker(newMarker);
						mOverlayItems.add(item);
					}
	
					mPointOverlay = new PoisOverlay(mMapView.getContext(), mOverlayItems, MainActivity.this);
					mMapView.getOverlays().add(mPointOverlay);
				}
				else if (dataType.equals("Tour")) {
					mMapView.getOverlays().remove(mPathOverlay);
					Vector<Poi> tourPois = CommonShared.getInstance().getmCurrentTour().getmPois();
					createPathOverlay();
					for (Iterator iterator = tourPois.iterator(); iterator.hasNext();) {
						Poi poi = (Poi) iterator.next();
						GeoPoint geoPoint = new GeoPoint(poi.getmLat(), poi.getmLong());
						mPathOverlay.addPoint(geoPoint);
						mMapView.getOverlays().add(mPathOverlay);
					}

				}
				mMapView.postInvalidate();
				
			}
		});
	}

	
	public static void tourToParse(final Activity a) {

		final ParseObject object = new ParseObject("TOUR");
		Tour tour = CommonShared.getInstance().getmCurrentTour();

		object.put("Name", tour.getmTourName());
		object.put("Price", tour.getmPrice());
		object.put("Description", tour.getmTourDescription());
		object.put("Type", tour.getmTourType());
		object.put("GuideName", MySing.getInstance().getName());

		for (Iterator iterator = tour.getmPois().iterator(); iterator.hasNext(); ) {
			Poi poi = (Poi) iterator.next();
			object.add("pois", ParseObject.createWithoutData("Poi", poi.getmObjectId()));

		}

//		save the tourpic to parse
		if (tour.getmTourImage() != null) {
			final ParseFile parseFile =
					CommonShared.getInstance().saveTourImToParse(tour.getmTourImage(),tour.getmTourName());
			parseFile.saveInBackground(new SaveCallback() {
				@Override
				public void done(ParseException e) {
					if (e == null) {
						object.put("image", parseFile);
						object.saveInBackground((new SaveCallback() {
							public void done(ParseException e) {
								if (e == null) {
									Toast.makeText(a.getApplicationContext(), "Tour saved", Toast.LENGTH_LONG).show();
								} else {
									Toast.makeText(a.getApplicationContext(), "Problem with network!", Toast.LENGTH_LONG).show();
								}
							}
						}));
					}
				}
			});
		} else {
			object.saveInBackground((new SaveCallback() {
				public void done(ParseException e) {
					if (e == null) {
						Toast.makeText(a.getApplicationContext(), "Tour saved", Toast.LENGTH_LONG).show();
					} else {
						Toast.makeText(a.getApplicationContext(), "Problem with network!", Toast.LENGTH_LONG).show();
					}
				}
			}));
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			// Add new item to overlay
			OverlayItem item = new OverlayItem(data.getStringExtra("name"), data.getStringExtra("description"), 
					new GeoPoint(data.getDoubleExtra("lat", 0), data.getDoubleExtra("lon", 0)));
			item.setMarker(newMarker);
			mPointOverlay.addItem(item);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}	
