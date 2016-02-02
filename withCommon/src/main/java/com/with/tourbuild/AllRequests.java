package com.with.tourbuild;

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

import com.example.withcommon.R;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class AllRequests extends Activity implements
        UpdateGuiListener {


    private ListView mListView;
    private AllReqsAdapter mAdapter;
    private ProgressDialog mDialog;
    private String tourWith;
    private TextView noTours;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_requests);
        noTours = (TextView)findViewById(R.id.noReq);
        tourWith = "userName";
        mListView = (ListView) findViewById(R.id.listAllReqs);
        mAdapter = new AllReqsAdapter(getApplicationContext());
        mListView.setAdapter(mAdapter);
        mDialog = new ProgressDialog(this);
        mDialog.setCancelable(true);
        mDialog.setMessage("Loading...");
        mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mDialog.show();
        getAllTours();
        registerForContextMenu(mListView);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                openContextMenu(arg1);
            }
        });
    }

    public void getAllTours(){
        AllReqsSing.getInstance().getTourFromAllArrayList().clear();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("REGISTRATION");
        query.whereEqualTo("guideName", MySing.getInstance().getName());
        query.whereEqualTo("registered", false);
        query.addDescendingOrder("Date");
        query.findInBackground(new FindCallback<ParseObject>() {

            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    for (int i = 0; i < objects.size(); i++) {
                        ReqFromAll r = new ReqFromAll();
                        Date date = (Date) objects.get(i).get("Date");
                        SimpleDateFormat fromUser = new SimpleDateFormat("yyyy/MM/dd");
                        String da = fromUser.format(date);
                        r.setDate(da);
                        r.setGuiName(objects.get(i).getString(tourWith));
                        r.setId(objects.get(i).getObjectId().toString());
                        r.setTourName(objects.get(i).getString("tourName"));
                        r.setAmount(objects.get(i).getInt("TouristsNum"));
                        r.setComment(objects.get(i).getString("Comment"));
                        AllReqsSing.getInstance().getTourFromAllArrayList().add(r);
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
                if (AllReqsSing.getInstance().getTourFromAllArrayList().isEmpty()) {
                    noTours.setVisibility(View.VISIBLE);
                } else {
                    noTours.setVisibility(View.GONE);
                }
                mDialog.dismiss();
                mListView.invalidateViews();
            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.reg_requests_m, menu);
        super.onCreateContextMenu(menu, v, menuInfo);
    }



    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();

        if (item.getItemId() == R.id.register) {
            final ReqFromAll r = AllReqsSing.getInstance().getTourFromAllArrayList().get(info.position);
            final ParseObject object = ParseObject.createWithoutData("REGISTRATION", r.getId());
            object.put("registered", true);
            object.saveInBackground(new SaveCallback() {

                @Override
                public void done(ParseException arg0) {
                    JSONObject data;
                    try {
                        data = new JSONObject();
                        data.put("action", "Wizit");
                        data.put("name", MySing.getInstance().getName());
                        data.put("to", r.getGuiName());
                        data.put("type", "register");
                        data.put("mes", "Your request for tour " + r.getTourName() + " was accepted!");
                        ParsePush push = new ParsePush();
                        push.setChannel(r.getGuiName());
                        push.setData(data);
                        push.sendInBackground();
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            });
            AllReqsSing.getInstance().getTourFromAllArrayList().remove(info.position);
            mListView.invalidateViews();
        }
        if (item.getItemId() == R.id.messToUs) {
            startActivity(new Intent(getApplicationContext(), Chat.class).putExtra("name", AllReqsSing.getInstance().getTourFromAllArrayList().get(info.position).getGuiName()));
        }

        if (item.getItemId() == R.id.decline) {
            final ReqFromAll r =  AllReqsSing.getInstance().getTourFromAllArrayList().get(info.position);
            final ParseObject object = ParseObject.createWithoutData("REGISTRATION", r.getId());
            object.deleteInBackground(new DeleteCallback() {
                @Override
                public void done(ParseException e) {
                    if (e==null){
                        JSONObject data;
                        try {
                            data = new JSONObject();
                            data.put("action", "Wizit");
                            data.put("name", MySing.getInstance().getName());
                            data.put("to", r.getGuiName());
                            data.put("type", "register");
                            data.put("mes", "Your request for tour " + r.getTourName() + " was declined");
                            ParsePush push = new ParsePush();
                            push.setChannel(r.getGuiName());
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
            AllReqsSing.getInstance().getTourFromAllArrayList().remove(info.position);
            mListView.invalidateViews();
        }
        if (AllReqsSing.getInstance().getTourFromAllArrayList().isEmpty()){
            noTours.setVisibility(View.VISIBLE);
        }
        return super.onContextItemSelected(item);
    }


}
