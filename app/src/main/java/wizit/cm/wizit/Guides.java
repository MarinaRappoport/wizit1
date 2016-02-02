package wizit.cm.wizit;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.with.tourbuild.Guide;
import com.with.tourbuild.GuideSing;

import java.util.ArrayList;
import java.util.List;


public class Guides extends Activity {

    ListView lv;
    GuidesAdapter gAdapter;
    String city;
    private ProgressDialog mPd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guides);
        Bundle extras = getIntent().getExtras();
        city = extras.getString("city");
        getGuides();
        lv = (ListView)findViewById(R.id.guidesList);
        gAdapter = new GuidesAdapter(getApplicationContext());
        lv.setAdapter(gAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent gIntent = new Intent(getApplicationContext(), GuideActivity.class);
                gIntent.putExtra("number", position);
                startActivity(gIntent);
            }
        });
    }

    public void getGuides(){
        mPd= new ProgressDialog(this);
        mPd.setMessage("Downloading");
        mPd.show();
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Guide");
        query.whereEqualTo("City", city);
        query.orderByDescending("Rate");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    GuideSing.getInstance().getGuideArrayList().clear();
                    for (int i = 0; i < objects.size(); i++) {
                        ParseObject po = objects.get(i);
                        final ParseGeoPoint location = (ParseGeoPoint) po.get("Location");
                        final String city = po.get("City").toString();
                        final String id = po.getObjectId().toString();
                        final int rate = (int) po.get("Rate");
                        final int ratesNumber = (int) po.get("RatesNumber");
                        final ArrayList<String> specialities = (ArrayList<String>) po.get("Specialities");
                        ParseUser pu = po.getParseUser("PointerToUser");


                        try {
                            pu.fetchIfNeeded();
                        } catch (ParseException e1) {
                            e1.printStackTrace();
                        }
                        final String name = pu.getUsername().toString();
                        ParseFile imageFile = (ParseFile) pu.get("image");

                        if (imageFile != null) {
                            imageFile.getDataInBackground(new GetDataCallback() {
                                @Override
                                public void done(byte[] data, com.parse.ParseException e) {
                                    if (e == null) {
                                        Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
                                        GuideSing.getInstance().getGuideArrayList().add(new Guide(name, rate, ratesNumber, bmp, location, city, specialities, id));
                                        lv.invalidateViews();
                                        lv.invalidate();
                                        mPd.cancel();
                                    }

                                }
                            });
                        }
                    }
                }
            }


        });
    }

    @Override
    public void onBackPressed() {
        GuideSing.getInstance().getGuideArrayList().clear();
        super.onBackPressed();
    }
}
