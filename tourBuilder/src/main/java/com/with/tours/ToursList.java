package com.with.tours;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.with.tourbuild.Calendar;
import com.with.tourbuild.CommonShared;
import com.with.tourbuild.GuideSing;
import com.with.tourbuild.LogOS;
import com.with.tourbuild.MySing;
import com.with.tourbuild.Tour;
import com.with.tourbuild.TourDetailsActivity;
import com.with.tourbuild.TourForTouristDetails;
import com.with.tourbuild.UpdateGuiListener;
import com.with.tourbuilder.R;

import org.json.JSONObject;

import java.util.Date;
import java.util.List;

public class ToursList extends Activity implements UpdateGuiListener {

    private TourListAdapter mTourListAdapter;
    private ProgressDialog mDialog;
    private int n;
    Date date;
    String comment = "";
    String role;
    int amount;
    ListView lv;
    TextView notTv;

    private int position;
    private static final int MENU_REG = 1001;
    private static final int MENU_REG_REQ = 1003;
    private static final int MENU_REG_USERS = 1004;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_list);

        mTourListAdapter = new TourListAdapter(getApplicationContext());
        lv = (ListView)findViewById(R.id.tourListV);
        notTv = (TextView)findViewById(R.id.notTv);
        lv.setAdapter(mTourListAdapter);
        registerForContextMenu(lv);
        mDialog = new ProgressDialog(this);
        mDialog.setCancelable(true);
        mDialog.setMessage("Loading...");
        mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mDialog.show();
        Bundle extras = getIntent().getExtras();
        role = extras.getString("role");
        if (extras != null){if (!extras.isEmpty()){n = extras.getInt("number");}}

        loadTours();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3) {
                setPosition(position);
                openContextMenu(arg1);
            }
        });
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.tours_list, menu);
        if (role.equals("tourist")){
            menu.add(0, MENU_REG, 0, "REGISTER FOR THE TOUR");}
        if (role.equals("guide")){
            menu.add(0, MENU_REG_REQ, 0, "REGISTRTRATION REQUESTS");
            menu.add(0, MENU_REG_USERS, 0, "REGISTERED USERS");
        }
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();

        if (item.getItemId() == R.id.seeOnMap) {
            Intent intent1 = new Intent(getApplicationContext(), ToursActivity.class);
            intent1.putExtra("tourId", info.position);
            startActivity(intent1);
        }

        if (item.getItemId() == MENU_REG) {
            if (ParseUser.getCurrentUser() == null){
                startActivity(new Intent(getApplicationContext(), LogOS.class));
            }
            else
                startActivityForResult(new Intent(getApplicationContext(), Calendar.class).putExtra("Name", CommonShared.getInstance().getmTours().get(position).getmGuideName()), 7);
        }

        if (item.getItemId() == MENU_REG_REQ) {
            Tour tour = CommonShared.getInstance().getmTours().get(position);

            startActivity(new Intent(getApplicationContext(), RegistationRequestsActivity.class).putExtra("TourId", tour.getmTourId()));
        }

        if (item.getItemId() ==  R.id.tourDetails) {
            Intent detailsIntent;
            if(MySing.getInstance().isImGuide()==true){
             detailsIntent = new Intent(getApplicationContext(),
                    TourDetailsActivity.class);}
            else{
                 detailsIntent = new Intent(getApplicationContext(),
                        TourForTouristDetails.class);
            }
            detailsIntent.putExtra("tourId", info.position);
            startActivity(detailsIntent);
        }

        if (item.getItemId() ==  MENU_REG_USERS) {
            Intent intent2 = new Intent(getApplicationContext(), RegisteredUsers.class);
            intent2.putExtra("tourId", CommonShared.getInstance().getmTours().get(info.position).getmTourId());
            startActivity(intent2);
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void UpdateTours() {
        runOnUiThread(new Runnable() {
            public void run() {
                if (CommonShared.getInstance().getmTours().isEmpty()) {
                    notTv.setVisibility(View.VISIBLE);
                } else {
                    notTv.setVisibility(View.GONE);
                }
                mDialog.dismiss();
                lv.invalidateViews();
            }
        });
    }


    @Override
    protected void onPause() {
        CommonShared.getInstance().setmUpdateGuiListener(null);
        super.onPause();
    }

    @Override
    protected void onResume() {
        CommonShared.getInstance().setmUpdateGuiListener(this);
        //UpdateTours();
        super.onResume();
    }

    private void loadTours() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("POI");
        query.findInBackground(new FindCallback<ParseObject>() {


            @Override
            public void done(List<ParseObject> objects, ParseException e) {

                if (e == null) {
                    CommonShared.getInstance().ReadPois(objects, "private");

                    ParseQuery<ParseObject> query = ParseQuery.getQuery("TOUR");


                    if (role.equals("guide")) {
                        query.whereEqualTo("GuideName", MySing.getInstance().getName());
                    }
                    if (role.equals("tourist")) {
                        query.whereEqualTo("GuideName", GuideSing.getInstance().getGuideArrayList().get(n).getmGuideName());
                    }

                    query.findInBackground(new FindCallback<ParseObject>() {

                        @Override
                        public void done(List<ParseObject> objects,
                                         ParseException e) {
                            if (e == null) {
                                CommonShared.getInstance().ReadTours(objects);
                            }
                        }

                    });
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 7 && resultCode == RESULT_OK){
            this.date = (Date)data.getSerializableExtra("date");
            amount = data.getIntExtra("amount", 1);
            if (data.getStringExtra("comment")!=null){
                comment = data.getStringExtra("comment");
            }
            registerForTour();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void registerForTour(){
        if (ParseUser.getCurrentUser()!=null) {
            final ParseObject object = new ParseObject("REGISTRATION");
            object.put("Comment", comment);
            object.put("TouristsNum", amount);
            object.put("registered", false);
            object.put("Date", date);
            Tour tour = CommonShared.getInstance().getmTours().get(position);
            object.put("tourId", tour.getmTourId());
            object.put("tourName", tour.getmTourName());
            object.put("guideName", tour.getmGuideName());
            object.put("userName", ParseUser.getCurrentUser().getUsername());
            final String guname = tour.getmGuideName();
            final String tuname = tour.getmTourName();
            final String tid = tour.getmTourId();
            object.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        JSONObject data;
                        try {
                            data = new JSONObject();
                            data.put("action", "Wizit");
                            data.put("name", MySing.getInstance().getName());
                            data.put("to", guname);
                            data.put("type", "request");
                            data.put("id", tid);
                            data.put("mes", "User " + MySing.getInstance().getName() + " have sent request for tour " + tuname);
                            ParsePush push = new ParsePush();
                            push.setChannel(guname);
                            push.setData(data);
                            push.sendInBackground();
                        } catch (Exception ex) {
                            // TODO Auto-generated catch block
                            ex.printStackTrace();
                        }
                        Toast.makeText(getApplicationContext(), "You have sent request", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), e.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        }

    }

}
