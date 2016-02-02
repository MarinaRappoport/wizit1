package com.with.tourbuild;

import java.util.List;

import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.OverlayItem;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

public class PoisViewOverlay extends ItemizedIconOverlay<OverlayItem> implements
		LocationListener {

	Context        mContext;

	public PoisViewOverlay(Context context, List<OverlayItem> listOverlay) {
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
		
		mContext       = context;
	}

	@Override
	protected boolean onSingleTapUpHelper(int index, OverlayItem item,
			MapView mapView) {
		Intent detailsIntent = new Intent(mContext, PoiDetailsActivity.class);
		detailsIntent.putExtra("PoiName", ((PoiOverlayItem)item).getmPoi().getmName());
		detailsIntent.putExtra("PoiDesc", ((PoiOverlayItem)item).getmPoi().getmDescription());
		((Activity)mContext).startActivity(detailsIntent);
		return super.onSingleTapUpHelper(index, item, mapView);
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
