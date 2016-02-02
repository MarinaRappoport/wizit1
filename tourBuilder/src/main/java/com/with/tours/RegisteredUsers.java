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

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.with.tourbuild.Chat;
import com.with.tourbuild.CommonShared;
import com.with.tourbuilder.R;
import com.with.tourbuilder.SharedObjects;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class RegisteredUsers extends Activity {

    private String         mTourId = null;
    private ProgressDialog mDialog;
    private ListView lv;
    private TextView noReg;

    RegisteredUsersAdapter mRegisteredUsersAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registered_user);
        lv = (ListView)findViewById(R.id.ReisteredListv);
        noReg = (TextView)findViewById(R.id.noReg);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mTourId = bundle.getString("tourId");
        }

        mRegisteredUsersAdapter = new RegisteredUsersAdapter(getApplicationContext());
        lv.setAdapter(mRegisteredUsersAdapter);
        registerForContextMenu(lv);
        mDialog = new ProgressDialog(this);
        mDialog.setCancelable(true);
        mDialog.setMessage("Loading...");
        mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mDialog.show();

        loadmRegisteredUsers();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                openContextMenu(arg1);
            }
        });
    }


    private void loadmRegisteredUsers() {
        SharedObjects.getInstance().getmRegisteredUsers().clear();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("REGISTRATION");
        if (mTourId == null) {
            return;
        }
        query.whereEqualTo("tourId", mTourId);
        query.whereEqualTo("registered", true);
        query.findInBackground(new FindCallback<ParseObject>() {

            @Override
            public void done(List<ParseObject> objects, ParseException e) {

                if (e == null) {
                    for (int i = 0; i < objects.size(); i++) {

                        com.with.tourbuilder.RegisteredUsers r = new com.with.tourbuilder.RegisteredUsers();
                        Date date = (Date) objects.get(i).get("Date");
                        SimpleDateFormat fromUser = new SimpleDateFormat("yyyy/MM/dd");
                        String da = fromUser.format(date);
                        r.setDate(da);
                        r.setmUserName(objects.get(i).getString("userName"));
                        r.setmComment(objects.get(i).getString("Comment"));
                        r.setmTourId(objects.get(i).getString("tourId"));
                        r.setmTourName(objects.get(i).getString("tourName"));
                        r.setAmount(objects.get(i).getInt("TouristsNum"));
                        r.setmObjectId(objects.get(i).getObjectId());
                        r.setmGuideName(CommonShared.getInstance().getmParseUser().getUsername());
                        SharedObjects.getInstance().getmRegisteredUsers().add(r);
                    }
                    UpdateUsers();
                }

            }

        });
    }

    public void UpdateUsers() {
        runOnUiThread(new Runnable() {
            public void run() {
                if(SharedObjects.getInstance().getmRegisteredUsers().isEmpty()){
                    noReg.setVisibility(View.VISIBLE);
                }
                else{
                    noReg.setVisibility(View.GONE);
                }
                mDialog.dismiss();
                lv.invalidateViews();
            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.registarion_requests, menu);
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();

        if (item.getItemId() == R.id.messToUs) {
            startActivity(new Intent(getApplicationContext(), Chat.class).putExtra("name", SharedObjects.getInstance().getmRegisteredUsers().get(info.position).getmUserName()));
        }
        return super.onContextItemSelected(item);
    }

}
