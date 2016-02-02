package com.with.tourbuild;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

import com.example.withcommon.R;

public class PoiDetailsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_poi_details);
		
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			String poiName  = bundle.getString("PoiName");
			String poiDesc  = bundle.getString("PoiDesc");

			if (poiName != null && poiDesc != null) {
				TextView poiNameTV   = (TextView)findViewById(R.id.poiNameTV);
				TextView poiDescrTV  = (TextView)findViewById(R.id.poiDescrTV);
			
				poiNameTV.setText(poiName);
				poiDescrTV.setText(poiDesc);
			}
		}

		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.poi_details, menu);
		return true;
	}

}
