package com.with.tourbuild;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.withcommon.R;
import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class AllChats extends Activity {

    ArrayList<String> intersNames;
    ListView lv;
    IntersAdapter iAdapter;
    private ProgressDialog mPd;
    TextView noDial;
    int a = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_chats);
        noDial = (TextView)findViewById(R.id.noDialogs);
        intersNames = new ArrayList<String>();
        lv = (ListView)findViewById(R.id.interList);
        getInterNames();
        iAdapter = new IntersAdapter(getApplicationContext());
        lv.setAdapter(iAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), Chat.class);
                intent.putExtra("name", IntersSing.getInstance().getInterlocsArray().get(position).getUsername());
                startActivity(intent);
            }
        });
    }

    //getting all interlocutors
    public void getInterNames(){
        mPd= new ProgressDialog(this);
        mPd.setMessage("Downloading");
        mPd.show();
        ParseQuery<ParseObject> queryIn = ParseQuery.getQuery("Messages");
        queryIn.whereEqualTo("To", MySing.getInstance().getName());
        ParseQuery<ParseObject> queryOut = ParseQuery.getQuery("Messages");
        queryOut.whereEqualTo("From", MySing.getInstance().getName());

        List<ParseQuery<ParseObject>> queries = new ArrayList<ParseQuery<ParseObject>>();
        queries.add(queryIn);
        queries.add(queryOut);

        ParseQuery<ParseObject> mainQuery = ParseQuery.or(queries);
        mainQuery.addAscendingOrder("createdAt");
        mainQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                intersNames.clear();
                if (e == null) {
                    for (int i = 0; i < objects.size(); i++) {
                        if (objects.get(i).get("From").equals(MySing.getInstance().getName()) && !intersNames.contains(objects.get(i).get("To").toString())) {
                            intersNames.add(objects.get(i).get("To").toString());
                        }
                        if (objects.get(i).get("To").equals(MySing.getInstance().getName()) && !intersNames.contains(objects.get(i).get("From").toString())) {
                            intersNames.add(objects.get(i).get("From").toString());
                        }
                    }
                    getInters();
                } else {
                    Toast.makeText(getApplicationContext(), e.getMessage().toString(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void getInters(){
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                IntersSing.getInstance().getInterlocsArray().clear();
                for (int i = 0; i < objects.size(); i++) {
                    final  String name = objects.get(i).getUsername();
                    final String role = objects.get(i).getString("Role");


                    if (intersNames.contains(name)) {
                        ParseFile imageFile = (ParseFile) objects.get(i).get("image");
                        if (imageFile!=null) {
                            imageFile.getDataInBackground(new GetDataCallback() {
                                @Override
                                public void done(byte[] data, com.parse.ParseException e) {
                                    if (e == null) {
                                        Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
                                        IntersSing.getInstance().getInterlocsArray().add(new User(name, bmp, role));
                                        lv.invalidateViews();
                                        lv.invalidate();

                                    }
                                }
                            });
                        }else{
                        IntersSing.getInstance().getInterlocsArray().add(new User(name, role));
                            }
                        a++;
                    }

                }
                lv.invalidateViews();
                lv.invalidate();
                mPd.cancel();
                noDialogs(a);
            }
        });
    }



    public void noDialogs(int a) {
      if (a==0){
          noDial.setVisibility(View.VISIBLE);
      }
        else{
          noDial.setVisibility(View.GONE);
      }
    }

}
