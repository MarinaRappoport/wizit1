package com.with.tourbuilder;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.with.tourbuild.CommonShared;
import com.with.tourbuild.MyGallery;
import com.with.tourbuild.MySing;
import com.with.tourbuild.Tour;
import com.with.tours.RegistationRequestsActivity;

import java.io.IOException;

public class TourDetails extends Activity {

    private EditText mName;
	private EditText mDescr;
	private Spinner  mType;
	private Tour     mTour;
	private Button   mSaveButton;
	private EditText price;

	private Bitmap mBitmap;
	private Bitmap mBitmapTemp;
	private ImageView mImageView;
	private final int REQUEST_GALLERY = 1;
	private boolean isImageChoosed;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_data);
        
    	mName  = (EditText)findViewById(R.id.tourName);     
    	mDescr = (EditText)findViewById(R.id.tourDescription);     
    	mType  = (Spinner) findViewById(R.id.tourType);
		price  = (EditText) findViewById(R.id.price);
		mSaveButton = (Button)findViewById(R.id.saveButton);

		mImageView = (ImageView) findViewById(R.id.imvTour);
    	
    	mTour = CommonShared.getInstance().getmCurrentTour();
        if (mTour.getmTourName() != null) {
        	mName.setText(mTour.getmTourName());
        }
        if (mTour.getmTourDescription() != null) {
        	mDescr.setText(mTour.getmTourDescription());
        }
		if (mTour.getmTourImage()!=null){
			mImageView.setImageBitmap(mTour.getmTourImage());
		}else{
			mImageView.setImageResource(R.drawable.camera);
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
		if(isImageChoosed) {
			mTour.setmTourImage(mBitmap);
		}
    	if (!tourName.equals("") && !tourDesc.equals("")) {
    		mSaveButton.setEnabled(true);
    	}
    }
    
    public void save(View v) {
    	MainActivity.tourToParse(this);
    	finish();
    }

	public void addTourImage(View view) {
		Intent pickPhoto = new Intent(Intent.ACTION_PICK,
				android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		startActivityForResult(pickPhoto , REQUEST_GALLERY);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode==REQUEST_GALLERY && resultCode==RESULT_OK){
			Uri selectedImage = data.getData();
			try {
				mBitmapTemp = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
				MyGallery gallery = new MyGallery();
				mBitmap = gallery.getRotatedImage(getApplicationContext(), mBitmapTemp, selectedImage);
				mImageView.setImageBitmap(mBitmap);
				isImageChoosed = true;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
