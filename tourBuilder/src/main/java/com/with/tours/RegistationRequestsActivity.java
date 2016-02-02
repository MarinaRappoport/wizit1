package com.with.tours;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.with.tourbuild.Chat;
import com.with.tourbuild.CommonShared;
import com.with.tourbuild.MySing;
import com.with.tourbuild.UpdateGuiListener;
import com.with.tourbuilder.R;
import com.with.tourbuilder.RegistrationRequest;
import com.with.tourbuilder.SharedObjects;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class RegistationRequestsActivity extends Activity implements
		UpdateGuiListener {

	private ProgressDialog mDialog;
	private ListView mListView;
	private RegistertrationRequestsAdapter mAdapter;
	private String tourId;
	private TextView noRegReq;

	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registarion_requests);
		noRegReq = (TextView)findViewById(R.id.noRegReq);
		Bundle extras = getIntent().getExtras();
		tourId = extras.getString("TourId");

		// This activity can be the first application activity so the user can be null
		if (CommonShared.getInstance().getmParseUser() == null) {
		ParseUser currentUser = ParseUser.getCurrentUser();
			if (currentUser != null) {
				// If so - go to start activity
				CommonShared.getInstance().setmParseUser(currentUser);
			}
			else {
				Toast.makeText(getApplicationContext(), "PLEASE LOGIN TO APPLICATION", Toast.LENGTH_LONG).show();
				finish();
			}
		}
		
		mListView = (ListView) findViewById(R.id.registrationList);
		mAdapter = new RegistertrationRequestsAdapter(getApplicationContext());
		mListView.setAdapter(mAdapter);
		mDialog = new ProgressDialog(this);
		mDialog.setCancelable(true);
		mDialog.setMessage("Loading...");
		mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mDialog.show();
		loadTours();
		registerForContextMenu(mListView);

		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				openContextMenu(arg1);			
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.registarion_requests, menu);
		return true;
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		getMenuInflater().inflate(R.menu.reg_requests_list, menu);
		super.onCreateContextMenu(menu, v, menuInfo);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo)item.getMenuInfo();
		
		if (item.getItemId() == R.id.register) {
			final RegistrationRequest r = SharedObjects.getInstance().getmRegistrationRequests().get(info.position);
			final ParseObject object = ParseObject.createWithoutData("REGISTRATION", r.getmObjectId());
			object.put("registered", true);
			object.saveInBackground(new SaveCallback() {

				@Override
				public void done(ParseException arg0) {
					JSONObject data;
					try {
						data = new JSONObject();
						data.put("action", "Wizit");
						data.put("name", MySing.getInstance().getName());
						data.put("to", r.getmUserName());
						data.put("type", "register");
						data.put("mes", "Your request for tour " + r.getmTourName() + " was accepted!");
						ParsePush push = new ParsePush();
						push.setChannel(r.getmUserName());
						push.setData(data);
						push.sendInBackground();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
			r.setmRegistered(true);
			mListView.invalidateViews();
		}
		if (item.getItemId() == R.id.messToUs) {
			startActivity(new Intent(getApplicationContext(), Chat.class).putExtra("name", SharedObjects.getInstance().getmRegistrationRequests().get(info.position).getmUserName()));
		}

		if (item.getItemId() == R.id.decline) {
			final RegistrationRequest r = SharedObjects.getInstance().getmRegistrationRequests().get(info.position);
			final ParseObject object = ParseObject.createWithoutData("REGISTRATION", r.getmObjectId());
			object.deleteInBackground(new DeleteCallback() {
				@Override
				public void done(ParseException e) {
					if (e==null){
						JSONObject data;
						try {
							data = new JSONObject();
							data.put("action", "Wizit");
							data.put("name", MySing.getInstance().getName());
							data.put("to", r.getmUserName());
							data.put("type", "register");
							data.put("mes", "Your request for tour " + r.getmTourName() + " was declined");
							ParsePush push = new ParsePush();
							push.setChannel(r.getmUserName());
							push.setData(data);
							push.sendInBackground();
						} catch (Exception ex) {
							// TODO Auto-generated catch block
							ex.printStackTrace();
						}
						Toast.makeText(getApplicationContext(), "Declined", Toast.LENGTH_LONG).show();
					}
					else{
						Toast.makeText(getApplicationContext(), e.getMessage().toString(), Toast.LENGTH_LONG).show();
					}
				}
			});
			SharedObjects.getInstance().getmRegistrationRequests().remove(info.position);
			mListView.invalidateViews();
		}
		return super.onContextItemSelected(item);

	}


	private void loadTours() {
		ParseQuery<ParseObject> query = ParseQuery.getQuery("REGISTRATION");
		query.whereEqualTo("guideName", CommonShared.getInstance()
				.getmParseUser().getUsername());
		query.whereEqualTo("tourId", tourId);
		query.whereEqualTo("registered", false);
		query.findInBackground(new FindCallback<ParseObject>() {

			@Override
			public void done(List<ParseObject> objects, ParseException e) {

				SharedObjects.getInstance().getmRegistrationRequests().clear();
				if (e == null) {
					for(int i = 0; i<objects.size();i++) {
						RegistrationRequest r = new RegistrationRequest();
						Date date = (Date) objects.get(i).get("Date");
						SimpleDateFormat fromUser = new SimpleDateFormat("yyyy/MM/dd");
						String da = fromUser.format(date);
						r.setDate(da);
						r.setmUserName(objects.get(i).getString("userName"));
						r.setmTourId(objects.get(i).getString("tourId"));
						r.setmTourName(objects.get(i).getString("tourName"));
						r.setAmount(objects.get(i).getInt("TouristsNum"));
						r.setmComment(objects.get(i).getString("Comment"));
						r.setmObjectId(objects.get(i).getObjectId());
						r.setmGuideName(CommonShared.getInstance().getmParseUser().getUsername());
						r.setmRegistered(false);
						SharedObjects.getInstance().getmRegistrationRequests().add(r);
					}
				}
				UpdateTours();
			}

		});
	}

	@Override
	public void UpdateTours() {
		runOnUiThread(new Runnable() {
			public void run() {
				if(SharedObjects.getInstance().getmRegistrationRequests().size()==0){
					noRegReq.setVisibility(View.VISIBLE);
				}
				else{
					noRegReq.setVisibility(View.GONE);
				}
				mDialog.dismiss();
				mListView.invalidateViews();
			}
		});
	}

}
