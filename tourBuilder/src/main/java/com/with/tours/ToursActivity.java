package com.with.tours;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

import org.osmdroid.tileprovider.IRegisterReceiver;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.PathOverlay;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;

import com.masa.tlalim.offlinemap.HybridMap;
import com.with.tourbuild.CommonShared;
import com.with.tourbuild.Poi;
import com.with.tourbuild.PoiOverlayItem;
import com.with.tourbuild.PoisViewOverlay;
import com.with.tourbuild.Tour;
import com.with.tourbuild.UpdateGuiListener;
import com.with.tourbuilder.R;
import com.with.tourbuilder.SharedObjects;

public class ToursActivity extends Activity implements UpdateGuiListener, IRegisterReceiver {

	private GeoPoint mMyLocation = null;
	private HybridMap hybridMap;
	private MapView mMapView;
	PathOverlay     mPathOverlay = null;
	PoisViewOverlay mPoisViewOverlay = null;
	private int mTourId;
	Drawable newMarker;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tours);
		Bitmap original = BitmapFactory.decodeResource(getResources(), R.drawable.hlogo);
		Drawable d = new BitmapDrawable(getResources(), original);
		newMarker = d;

		//if (myLocation == null) {
//			mMyLocation = new GeoPoint(32.332225, 34.86312);
		//} else {
//			mMyLocation = new GeoPoint(myLocation.getLatitude(),
//					myLocation.getLongitude());
//		}

		hybridMap = (HybridMap) findViewById(R.id.hybridToursMap);
		mMapView = hybridMap.mv;

//		mMapView = (MapView) findViewById(R.id.toursmap);
		mMapView.setBuiltInZoomControls(true);
		mMapView.getController().setZoom(14);
//		mMapView.getController().setCenter(mMyLocation);

		Intent intent = getIntent();
		mTourId = intent.getIntExtra("tourId", -1);
		if (mTourId != -1) {
			mMyLocation = new GeoPoint(
					CommonShared.getInstance().getmTours().get(mTourId).getmPois().get(0).getmLat(),
					CommonShared.getInstance().getmTours().get(mTourId).getmPois().get(0).getmLong());
//			System.out.println("-------------------");
			mMapView.getController().setCenter(mMyLocation);
			ShowTour(mTourId);
		}

	}

	@Override
	protected void onPause() {
		CommonShared.getInstance().setmUpdateGuiListener(null);
		super.onPause();
	}

	@Override
	protected void onResume() {
		CommonShared.getInstance().setmUpdateGuiListener(this);
		//UpdateTours();
		super.onResume();
	}

	@Override
	public void UpdateTours() {
		runOnUiThread(new Runnable() {
			public void run() {

				// mMapView.getOverlays().remove(mPathOverlay);
//				mPathOverlay = new PathOverlay(Color.RED,
//						getApplicationContext());
				Vector<Tour> tours = CommonShared.getInstance().getmTours();
				for (Tour tour : tours) {
					Vector<Poi> tourPois = tour.getmPois();
					for (Iterator iterator = tourPois.iterator(); iterator
							.hasNext();) {
						Poi poi = (Poi) iterator.next();
						GeoPoint geoPoint = new GeoPoint(poi.getmLat(),
								poi.getmLong());
						mPathOverlay.addPoint(geoPoint);
					}
				}
				mMapView.getOverlays().add(mPathOverlay);
				mMapView.postInvalidate();
			}
		});
	}
	
	private void ShowTour(int tourId) {
		Tour tour = CommonShared.getInstance().getmTours().get(tourId);
		mPathOverlay     = new PathOverlay(Color.RED, this);
		ArrayList<OverlayItem> overlayItemArrayList = new ArrayList<OverlayItem>();
		mPoisViewOverlay = new PoisViewOverlay(this, overlayItemArrayList);
		
		Vector<Poi> tourPois = tour.getmPois();
		for (Iterator iterator = tourPois.iterator(); iterator
				.hasNext();) {
			Poi poi = (Poi) iterator.next();
			GeoPoint geoPoint = new GeoPoint(poi.getmLat(),
					poi.getmLong());
			mPathOverlay.addPoint(geoPoint);

			PoiOverlayItem item =
					new PoiOverlayItem(poi.getmName(), poi.getmDescription(), geoPoint, poi);
			item.setMarker(newMarker);
			mPoisViewOverlay.addItem(item);
		}
		mMapView.getOverlays().add(mPathOverlay);
		mMapView.getOverlays().add(mPoisViewOverlay);

	}
}
