package com.with.tourbuild;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.withcommon.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class AllMyTours extends Activity implements
        UpdateGuiListener {

    private ListView mListView;
    private AllToursAdapter mAdapter;
    private ProgressDialog mDialog;
    private String roleName;
    private String tourWith;
    private TextView noTours;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_my_tours);
        Bundle extras = getIntent().getExtras();
        roleName = extras.getString("role");
        noTours = (TextView)findViewById(R.id.noTours);
        if(roleName.equals("userName")){
            tourWith = "guideName";
        }
        if(roleName.equals("guideName")){
            tourWith = "userName";
        }
        mListView = (ListView) findViewById(R.id.listAllTours);
        mAdapter = new AllToursAdapter(getApplicationContext());
        mListView.setAdapter(mAdapter);
        mDialog = new ProgressDialog(this);
        mDialog.setCancelable(true);
        mDialog.setMessage("Loading...");
        mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mDialog.show();
        getAllTours();
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), Chat.class);
                intent.putExtra("name", AllToursSing.getInstance().getTourFromAllArrayList().get(position).getGuiName());
                startActivity(intent);
            }
        });
    }



    public void getAllTours(){
        AllToursSing.getInstance().getTourFromAllArrayList().clear();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("REGISTRATION");
        query.whereEqualTo(roleName, MySing.getInstance().getName());
        query.whereEqualTo("registered", true);
        query.addDescendingOrder("Date");
        query.findInBackground(new FindCallback<ParseObject>() {

            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    for (int i = 0; i < objects.size(); i++) {
                        TourFromAll r = new TourFromAll();
                        Date date = (Date) objects.get(i).get("Date");
                        SimpleDateFormat fromUser = new SimpleDateFormat("yyyy/MM/dd");
                        String da = fromUser.format(date);
                        r.setDate(da);
                        r.setGuiName(objects.get(i).getString(tourWith));
                        r.setTourName(objects.get(i).getString("tourName"));
                        r.setAmount(objects.get(i).getInt("TouristsNum"));
                        r.setComment(objects.get(i).getString("Comment"));
                        AllToursSing.getInstance().getTourFromAllArrayList().add(r);
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
                if  (AllToursSing.getInstance().getTourFromAllArrayList().isEmpty()){
                  noTours.setVisibility(View.VISIBLE);
                }
                else{
                    noTours.setVisibility(View.GONE);
                }
                mDialog.dismiss();
                mListView.invalidateViews();
            }
        });
    }

}
