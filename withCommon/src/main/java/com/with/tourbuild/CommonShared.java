package com.with.tourbuild;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Vector;

import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseUser;


public class CommonShared {
	private static CommonShared instance = null;

	public static CommonShared getInstance()
	{
		if (instance == null)
			instance = new CommonShared();
		return instance;
	}
	

	public CommonShared() {
		mPois   = new Vector<Poi>();
		mTours  = new Vector<Tour>();
		mGuides = new Vector<Guide>();
	}


	public void ReadPois(List<ParseObject> objects, String type) {
//		mPois.clear();
		for(int i = 0; i<objects.size();i++) {
			Poi poi = new Poi();
			poi.setmName(objects.get(i).getString("Name"));
			poi.setmDescription(objects.get(i).getString("Description"));
			ParseGeoPoint geoPoint = objects.get(i).getParseGeoPoint("GeoPoint");
			
			poi.setmLat(geoPoint.getLatitude());
			poi.setmLong(geoPoint.getLongitude());
			poi.setmObjectId(objects.get(i).getObjectId());
			poi.setType(type);
			mPois.add(poi);
		}
		
	}
	
	public void ReadTours(List<ParseObject> objects) {
		mTours.clear();
		for(int i = 0; i<objects.size();i++) {
			final Tour tour = new Tour();
			tour.setmTourName(objects.get(i).getString("Name"));
			tour.setmTourDescription(objects.get(i).getString("Description"));
			tour.setmGuideName(objects.get(i).getString("GuideName"));
			tour.setmTourId(objects.get(i).getObjectId());
			tour.setmTourRate(objects.get(i).getInt("Rate"));
			tour.setmPrice(objects.get(i).getInt("Price"));
			tour.setmRatesNumber(objects.get(i).getInt("RatesNumber"));
			tour.setmParseObject(objects.get(i));

			ParseFile parseFile = objects.get(i).getParseFile("image");
			if (parseFile != null) {
				parseFile.getDataInBackground(new GetDataCallback() {
					@Override
					public void done(byte[] bytes, ParseException e) {
						tour.setmTourImage(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
					}
				});
			}

			List<ParseObject> pois = objects.get(i).getList("pois");
			for (ParseObject parseObject : pois) {
				Poi poi = findPoiById(parseObject.getObjectId());
				tour.getmPois().add(poi);
			}
			mTours.add(tour);
		}
		
		if (mUpdateGuiListener != null) {
			mUpdateGuiListener.UpdateTours();
		}
	}

	public ParseFile saveTourImToParse(Bitmap mBitmap, String tourName){
		double w = mBitmap.getWidth();
		double h = mBitmap.getHeight();
		double cf = 180 / w;
		double he = h * cf;

		Bitmap resized = Bitmap.createScaledBitmap(mBitmap, 180, (int) he, true);

		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		resized.compress(Bitmap.CompressFormat.JPEG, 100, stream);
		byte[] byteArray = stream.toByteArray();
		final ParseFile parseFile = new ParseFile(tourName.replaceAll("\\W", "")+ ".jpg", byteArray);
		return parseFile;
	}

	public void ReadGuides(List<ParseObject> objects) {
		mGuides.clear();
		for(int i = 0; i<objects.size();i++) {
			Guide guide = new Guide();
			guide.setmGuideName(objects.get(i).getString("Name"));
			guide.setmGuideRate(objects.get(i).getInt("Rate"));
			guide.setmRatesNumber(objects.get(i).getInt("RatesNumber"));
			guide.setmParseObject(objects.get(i));
			
			mGuides.add(guide);
		}
	}
	
	public Vector<Poi> getmPois() {
		return mPois;
	}

	// Pois
	private Vector<Poi> mPois;
	
	public Poi findPoiById(String id) {
		for (Poi poi : mPois) {
			if (poi.getmObjectId().equals(id)) {
				return poi;
			}
		}
		return null;
	}


	// Tours
	// Current tour - built by TOUR BUILDER
	private Tour mCurrentTour;

	public Tour getmCurrentTour() {
		return mCurrentTour;
	}

	public void setmCurrentTour(Tour mCurrentTour) {
		this.mCurrentTour = mCurrentTour;
	}

	// Guides
	private Vector<Guide> mGuides;

	public Vector<Guide> getmGuides() {
		return mGuides;
	}
	

	
	// List of all tours
	private Vector<Tour> mTours;

	public Vector<Tour> getmTours() {
		return mTours;
	}
	
	UpdateGuiListener mUpdateGuiListener = null;

	public void setmUpdateGuiListener(UpdateGuiListener mUpdateGuiListener) {
		this.mUpdateGuiListener = mUpdateGuiListener;
	}

	private ParseUser mParseUser;

	public ParseUser getmParseUser() {
		return mParseUser;
	}


	public void setmParseUser(ParseUser mParseUser) {
		this.mParseUser = mParseUser;
	}

}
