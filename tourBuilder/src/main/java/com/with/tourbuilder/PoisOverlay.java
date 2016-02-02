package com.with.tourbuilder;

import java.util.List;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.OverlayItem;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.Toast;

import com.with.tourbuild.CommonShared;
import com.with.tourbuild.Poi;
import com.with.tourbuilder.SharedObjects.OpearionalMode;

public class PoisOverlay extends ItemizedIconOverlay<OverlayItem> implements
		LocationListener {

	Context        mContext;
	IPostListener  mListener;

	public PoisOverlay(Context context, List<OverlayItem> listOverlay, IPostListener listener) {
		super(context, listOverlay, new OnItemGestureListener<OverlayItem>() {
			@Override
			public boolean onItemLongPress(int arg0, OverlayItem arg1) {
				return false;

			}

			@Override
			public boolean onItemSingleTapUp(int arg0, OverlayItem arg1) {

				return false;
			}

		});
		
		mContext  = context;
		mListener = listener;
	}

	@Override
	protected boolean onSingleTapUpHelper(int index, OverlayItem item,
			MapView mapView) {
		if(SharedObjects.getInstance().getmMode() == OpearionalMode.BUILD_TOUR){
			Poi poi = CommonShared.getInstance().getmPois().get(index);
			if (poi == null) {
				Toast.makeText(mContext, "This POI was not updated yet", Toast.LENGTH_LONG).show();
			}
			else {
				// Save POI id in the TOUR vector
				CommonShared.getInstance().getmCurrentTour().getmPois().add(poi);
				Toast.makeText(mContext, poi.getmName() + " added to TOUR", 
						Toast.LENGTH_SHORT).show();
				if (mListener != null) {
					mListener.updateGui("Tour");
				}
				
			}

		}
		else if (SharedObjects.getInstance().getmMode() == OpearionalMode.NONE) {
//			Intent detailsIntent = new Intent(mContext, PoiDetailsActivity.class);
//			detailsIntent.putExtra("PoiIndex", index);
//			((Activity)mContext).startActivity(detailsIntent);
			AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
			dialog.setTitle(item.getTitle());
			dialog.setMessage(item.getSnippet());
			dialog.show();

		}

		return super.onSingleTapUpHelper(index, item, mapView);
	}

	public boolean onSingleTapUp(MotionEvent e, MapView mapView) {
		GeoPoint gp = (GeoPoint) mapView.getProjection().fromPixels(
				e.getRawX(), e.getRawY());
		if (SharedObjects.getInstance().getmMode() == OpearionalMode.NEW_POI) {
			SharedObjects.getInstance().setmMode(OpearionalMode.NONE);
			Intent intent = new Intent(mContext, NewPointActivity.class);

			double lat = gp.getLatitudeE6() / 1E6;
			double lng = gp.getLongitudeE6() / 1E6;

			String latlng = String.valueOf(lat).concat(",")
					.concat(String.valueOf(lng));
			intent.putExtra("gPoint", latlng);

			// Start add item activity, result comes to MainActivity
			((Activity)mContext).startActivityForResult(intent, 1);
		}

		return super.onSingleTapUp(e, mapView);
	}

	@Override
	public void addItem(int location, OverlayItem item) {
		// TODO Auto-generated method stub
		super.addItem(location, item);
	}

	@Override
	protected boolean onLongPressHelper(int index, OverlayItem item) {
		// TODO Auto-generated method stub
		return super.onLongPressHelper(index, item);
	}

	@Override
	public void onLocationChanged(Location location) {
		double lat = location.getLatitude();
		double lon = location.getLongitude();
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

}
