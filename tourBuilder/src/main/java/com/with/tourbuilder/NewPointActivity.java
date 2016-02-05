package com.with.tourbuilder;

import java.util.StringTokenizer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.with.tourbuild.CommonShared;
import com.with.tourbuild.Poi;

public class NewPointActivity extends Activity {

	EditText NAMEPOI;
	EditText DESCPOI;
	TextView cor;
	String point;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_point);

		cor = (TextView) findViewById(R.id.getCordination);
		NAMEPOI = (EditText) findViewById(R.id.namePoi);
		DESCPOI = (EditText) findViewById(R.id.descPoi);

		Intent intent = getIntent();
		point = intent.getStringExtra("gPoint");
		cor.setText(point);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.new_point, menu);
		return true;
	}

	// Button Cancel
	public void cancel(View v) {
		setResult(RESULT_CANCELED);
		finish();
	}

	public void send(View v) {

		String name = NAMEPOI.getText().toString();
		String description = DESCPOI.getText().toString();

		StringTokenizer strToken = new StringTokenizer(point, ",");

		String[] separated = new String[2];
		separated[0] = strToken.nextToken();
		separated[1] = strToken.nextToken();

		double lat = Double.parseDouble(separated[0]);
		double lon = Double.parseDouble(separated[1]);

		ParseGeoPoint geoPoint = new ParseGeoPoint(lat, lon);

		poiToParse(name, description, geoPoint);

		Intent intent = new Intent();
		intent.putExtra("name", name);
		intent.putExtra("description", description);
		intent.putExtra("lat", lat);
		intent.putExtra("lon", lon);
		setResult(RESULT_OK, intent);
		finish();

	}

	private void poiToParse(String name, String descriptionPoi,
			ParseGeoPoint geoPoint) {

		final ParseObject object = new ParseObject("POI");
		final Poi         poi    = new Poi();
		object.put("Name", name);
		poi.setmName(name);
		object.put("Description", descriptionPoi);
		poi.setmDescription(descriptionPoi);
		object.put("GeoPoint", geoPoint);
		object.put("Creator", ParseUser.getCurrentUser());
		poi.setmLat(geoPoint.getLatitude());
		poi.setmLong(geoPoint.getLongitude());
		object.saveInBackground((new SaveCallback() {
			public void done(ParseException e) {
				poi.setmObjectId(object.getObjectId());
				CommonShared.getInstance().getmPois().add(poi);
			}
		}));
	}

}
