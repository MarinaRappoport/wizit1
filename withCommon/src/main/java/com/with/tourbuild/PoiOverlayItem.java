package com.with.tourbuild;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.OverlayItem;

public class PoiOverlayItem extends OverlayItem {

	private Poi mPoi;

	public PoiOverlayItem(String aTitle, String aDescription,
			GeoPoint aGeoPoint, Poi poi) {
		super(aTitle, aDescription, aGeoPoint);
		setmPoi(poi);
	}

	public Poi getmPoi() {
		return mPoi;
	}

	public void setmPoi(Poi mPoi) {
		this.mPoi = mPoi;
	}


}
