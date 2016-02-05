package com.with.tourbuild;

import com.example.withcommon.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class TourDetailsActivity extends Activity {
	String oldTourName;
	EditText tourNameTV;
	EditText tourDescrTV;
	EditText tourPrice;
	int tourId;
	private Bitmap mBitmap;
	private Bitmap mBitmapTemp;
	private ImageView mImageView;
	private boolean isImageChoosed;
	private Tour tour;

	private final int REQUEST_GALLERY = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tour_details);
		
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			 tourId  = bundle.getInt("tourId", -1);

			if (tourId != -1) {
				tour = CommonShared.getInstance().getmTours().get(tourId);
				oldTourName = tour.getmTourName();
				tourNameTV   = (EditText)findViewById(R.id.tourNameTV);
				tourDescrTV  = (EditText)findViewById(R.id.tourDescrTV);
				tourPrice  = (EditText)findViewById(R.id.pric);
				mImageView = (ImageView) findViewById(R.id.imvTourImage);

				tourNameTV.setText(tour.getmTourName());
				tourDescrTV.setText(tour.getmTourDescription());
				tourPrice.setText(String.valueOf(tour.getmPrice()));

				if(tour.getmTourImage()!=null){
					mImageView.setImageBitmap(tour.getmTourImage());
				}else {
					mImageView.setImageResource(R.drawable.camera);
				}
			}
		}
	}

	public void saveDet(View v){

		ParseQuery<ParseObject> query = ParseQuery.getQuery("TOUR");
		query.whereEqualTo("Name", oldTourName);

		query.findInBackground(new FindCallback<ParseObject>() {
			@Override
			public void done(List<ParseObject> objects, ParseException e) {
				for (int i = 0; i < objects.size(); i++) {
					objects.get(i).put("Name", tourNameTV.getText().toString());
					objects.get(i).put("Description", tourDescrTV.getText().toString());
					objects.get(i).put("Price", Integer.parseInt(tourPrice.getText().toString()));

					//		save the tourpic to parse
					if (mBitmap != null) {
						final ParseFile parseFile =
								CommonShared.getInstance().saveTourImToParse(mBitmap,tourNameTV.getText().toString());
						objects.get(i).put("image",parseFile);
					}

					objects.get(i).saveInBackground(new SaveCallback() {
						@Override
						public void done(ParseException e) {
							Tour tour = CommonShared.getInstance().getmTours().get(tourId);
							tour.setmTourName(tourNameTV.getText().toString());
							tour.setmTourDescription(tourDescrTV.getText().toString());
							tour.setmPrice(Integer.parseInt(tourPrice.getText().toString()));
							tour.setmTourImage(mBitmap);
						}
					});
				}
			}
		});


		ParseQuery<ParseObject> query2 = ParseQuery.getQuery("REGISTRATION");
		query2.whereEqualTo("tourName", oldTourName);

		query2.findInBackground(new FindCallback<ParseObject>() {
			@Override
			public void done(List<ParseObject> objects, ParseException e) {
				for (int i = 0; i < objects.size(); i++) {
					objects.get(i).put("tourName", tourNameTV.getText().toString());
					objects.get(i).saveInBackground();
				}
			}
		});
		Toast.makeText(getApplicationContext(), "Tour " + tourNameTV.getText().toString() + " saved!", Toast.LENGTH_LONG).show();
finish();
	}

	public void cancel(View v){
		finish();
	}

	public void changeTourImage(View view) {
		Intent pickPhoto = new Intent(Intent.ACTION_PICK,
				android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		startActivityForResult(pickPhoto, REQUEST_GALLERY);
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
