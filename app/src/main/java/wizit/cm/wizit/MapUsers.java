package wizit.cm.wizit;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.masa.tlalim.offlinemap.HybridMap;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.with.tourbuild.CommonShared;
import com.with.tourbuild.GPS_Tracker;
import com.with.tourbuild.MapBinder;
import com.with.tourbuild.MySing;
import com.with.tourbuild.Poi;
import com.with.tourbuild.UpdateIF;
import com.with.tourbuild.User;
import com.with.tourbuild.UsersSing;
import com.with.tourbuilder.IPostListener;
import com.with.tourbuilder.PoisOverlay;

import org.osmdroid.tileprovider.IRegisterReceiver;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.OverlayItem;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

public class MapUsers extends Activity implements ServiceConnection, UpdateIF, IRegisterReceiver {

    private MapView mMapView;
    private HybridMap hybridMap;
    private GeoPoint mGeoPoint;
    MyItemizedOverlay myItemizedOverlay;
    ArrayList<OverlayItem> overlayItemArray;
    GPS_Tracker gps;
    Intent sintent;
    Drawable newMarker;

    private Drawable markerAttr;
    private Drawable maekerHotel;
    private Drawable markerInfo;
//    private ArrayList<Poi> attractions;
//    private PoisOverlay mPointOverlay = null;

    @Override
    protected void onStart() {
        load();
        sintent = new Intent(getApplicationContext(), GPS_Tracker.class);
        startService(sintent);
        bindService(sintent, this, 0);
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_map_users);
        UsersSing.getInstance();
        load();


//        mMapView = (MapView) findViewById(R.id.mapview);
        hybridMap = (HybridMap) findViewById(R.id.hybridMap);
        mMapView = hybridMap.mv;

        mMapView.setBuiltInZoomControls(true);
        mMapView.getController().setZoom(14);

        overlayItemArray = new ArrayList<>();

        sintent = new Intent(getApplicationContext(), GPS_Tracker.class);
        startService(sintent);
        bindService(sintent, this, 0);

        Bitmap original = BitmapFactory.decodeResource(getResources(), R.drawable.mlogo);
        newMarker = new BitmapDrawable(getResources(), original);

        //for all public points
        Bitmap tourism = BitmapFactory.decodeResource(getResources(), com.with.tourbuilder.R.drawable.marker_gr_tourism);
        markerAttr = new BitmapDrawable(getResources(), tourism);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_map_users, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.chanRadius){
            Intent chantent = new Intent(getApplicationContext(), ChangeRadius.class);
            startActivity(chantent);
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        MapBinder binder = (MapBinder) service;
        gps = binder.getmService();
        gps.resetCounter();
        gps.setmActivity(this);
        Update();
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

    }


    @Override
    protected void onDestroy() {
        unbindService(this);
        super.onDestroy();
    }

    @Override
    public void Update() {
        overlayItemArray.clear();
        mGeoPoint = new GeoPoint(MySing.getInstance().getMyLocation().getLatitude(), MySing.getInstance().getMyLocation().getLongitude());
        mMapView.getController().setCenter(mGeoPoint);

        OverlayItem mItem = new OverlayItem(MySing.getInstance().getName(), MySing.getInstance().getRole(), mGeoPoint);
        mItem.setMarker(newMarker);
        overlayItemArray.add(mItem);

        UsersSing.getInstance().filterUsers();
        for (int i=0; i<UsersSing.getInstance().getFilteredUsers().size(); i++){
            User user = UsersSing.getInstance().getFilteredUsers().get(i);
            ParseGeoPoint parseGeoPoint = user.getGeoPoint();
            GeoPoint geop = new GeoPoint(parseGeoPoint.getLatitude(), parseGeoPoint.getLongitude());
            OverlayItem olItem = new OverlayItem(user.getUsername(), user.getCommonInterest() + " | " + user.getCommonLang() , geop);
            olItem.setMarker(newMarker);
            overlayItemArray.add(olItem);
        }

        addPoints();
    }

    private void load() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        int rad = sharedPreferences.getInt("radius", 10);
        int tim = sharedPreferences.getInt("time", 60);
        boolean vis = sharedPreferences.getBoolean("visible", true);
        boolean seeOth = sharedPreferences.getBoolean("seeOther", true);
        UsersSing.getInstance().setRadius(rad);
        UsersSing.getInstance().setTime(tim);
        UsersSing.getInstance().setVisible(vis);
        UsersSing.getInstance().setSeeOthers(seeOth);
        ParseUser user = ParseUser.getCurrentUser();
        user.put("Visible", vis);
        user.saveInBackground();
     }

    private void addPoints(){
        ParseQuery<ParseObject> queryAttr = ParseQuery.getQuery("Attractions");
        queryAttr.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    for (int i = 0; i < objects.size(); i++) {
                        Poi poi = new Poi();
                        poi.setmName(objects.get(i).getString("Name"));
                        poi.setmDescription(objects.get(i).getString("Description"));
                        ParseGeoPoint geoPoint = objects.get(i).getParseGeoPoint("GeoPoint");

                        poi.setmLat(geoPoint.getLatitude());
                        poi.setmLong(geoPoint.getLongitude());
                        poi.setmObjectId(objects.get(i).getObjectId());
//                        poi.setType("attraction");
//                        attractions.add(poi);

                        OverlayItem item = new OverlayItem(poi.getmName(),
                                poi.getmDescription(), new GeoPoint(poi.getmLat(), poi.getmLong()));
                        item.setMarker(markerAttr);
                        overlayItemArray.add(item);
                    }
                    updateMap();
                }
            }
        });
    }

    private void updateMap(){
        mMapView.getOverlays().remove(myItemizedOverlay);
        myItemizedOverlay = new MyItemizedOverlay(this, overlayItemArray);
        mMapView.getOverlays().add(myItemizedOverlay);
    }

}
