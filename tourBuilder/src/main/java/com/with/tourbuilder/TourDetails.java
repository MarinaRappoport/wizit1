package com.with.tourbuilder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.with.tourbuild.CommonShared;
import com.with.tourbuild.Tour;
import com.with.tours.RegistationRequestsActivity;

public class TourDetails extends Activity {

    private EditText mName;
	private EditText mDescr;
	private Spinner  mType;
	private Tour     mTour;
	private Button   mSaveButton;
	private EditText price;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_data);
        
    	mName  = (EditText)findViewById(R.id.tourName);     
    	mDescr = (EditText)findViewById(R.id.tourDescription);     
    	mType  = (Spinner) findViewById(R.id.tourType);
		price  = (EditText) findViewById(R.id.price);

    	mSaveButton = (Button)findViewById(R.id.saveButton);
    	
    	mTour = CommonShared.getInstance().getmCurrentTour();
        if (mTour.getmTourName() != null) {
        	mName.setText(mTour.getmTourName());
        }
        if (mTour.getmTourDescription() != null) {
        	mDescr.setText(mTour.getmTourDescription());
        }
        mType.setId(mTour.getmTourType() - 1);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.tour_details, menu);
        return true;
    }
    
    public void cancel(View v) {
    	finish();
    }
    
    public void ok(View v) {
    	String tourName = mName.getText().toString();
    	String tourDesc = mDescr.getText().toString();
		int pric = Integer.parseInt(price.getText().toString());
		mTour.setmPrice(pric);
    	mTour.setmTourName(tourName);
    	mTour.setmTourDescription(tourDesc);
    	mTour.setmTourType((int) mType.getSelectedItemId() + 1);
    	if (!tourName.equals("") && !tourDesc.equals("")) {
    		mSaveButton.setEnabled(true);
    	}
    }
    
    public void save(View v) {
    	MainActivity.tourToParse(this);
    	finish();
    }
    
}
