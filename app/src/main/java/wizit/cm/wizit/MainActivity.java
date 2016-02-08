package wizit.cm.wizit;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.with.tourbuild.AllChats;
import com.with.tourbuild.AllMyTours;
import com.with.tourbuild.City;
import com.with.tourbuild.CitySing;
import com.with.tourbuild.CommonShared;
import com.with.tourbuild.MySing;

import java.util.List;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

//    add drawer

    ListView lv;
    CityAdapter mCityAdapter;
    private ProgressDialog mPd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Intent intent = getIntent();
        String finish = intent.getStringExtra("finish");
        if (finish != null) {
            finish();
            System.exit(0);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        MySing.getInstance().getAllSpecs();
        MySing.getInstance().getAllInterests();
        MySing.getInstance().getAllLangs();
        MySing.getInstance().setImGuide(false);

        FacebookSdk.sdkInitialize(getApplicationContext());

        if(ParseUser.getCurrentUser()!=null){
            ParseUser user  = ParseUser.getCurrentUser();
            SharedPreferences sp = getSharedPreferences("wizit", MODE_PRIVATE);
            String role = sp.getString("role", "Tourist");
            CommonShared.getInstance().setmParseUser(user);
            MySing.getInstance().getAllMyData(role);
            if (role.equals("Guide")){
                Intent tourin = new Intent(getApplicationContext(), StartActivity.class);
                startActivity(tourin);
            }
        }

            lv = (ListView) findViewById(R.id.citiesList);
            mCityAdapter = new CityAdapter(getApplicationContext());
            lv.setAdapter(mCityAdapter);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(getApplicationContext(), Guides.class);
                    intent.putExtra("city", CitySing.getInstance().getCityArray().get(position).getName());
                    startActivity(intent);
                }
            });
        getCity();
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//
//        if (id == R.id.mapUsers){
//            if(ParseUser.getCurrentUser() == null){
//                Intent authintent = new Intent(getApplicationContext(), Authorize.class);
//                startActivity(authintent);}
//            else{
//            Intent mapintent = new Intent(getApplicationContext(), MapUsers.class);
//            startActivity(mapintent);}
//        }
//        if (id == R.id.allConvers){
//            if(ParseUser.getCurrentUser() == null){
//                Intent authintent = new Intent(getApplicationContext(), Authorize.class);
//                startActivity(authintent);}
//            else{
//            Intent convint = new Intent(getApplicationContext(), AllChats.class);
//            startActivity(convint);}
//        }
//        if (id == R.id.allMytours){
//            if(ParseUser.getCurrentUser() == null){
//                Intent authintent = new Intent(getApplicationContext(), Authorize.class);
//                startActivity(authintent);}
//            else{
//            Intent allIntent = new Intent(getApplicationContext(), AllMyTours.class).putExtra("role", "userName");
//            startActivity(allIntent);}
//        }
//
//        if (id == R.id.user) {
//            if(ParseUser.getCurrentUser() == null){
//                Intent authintent = new Intent(getApplicationContext(), Authorize.class);
//                startActivity(authintent);}
//            else{
//                Intent userintent = new Intent(getApplicationContext(), User.class);
//                startActivity(userintent);}
//        }
//
//        if (id == R.id.beGuiMe){
//            if(ParseUser.getCurrentUser() == null){
//
//                Intent authintent = new Intent(getApplicationContext(), Authorize.class);
//                authintent.putExtra("guide", true);
//                startActivity(authintent);
//            }
//            else{
//                SharedPreferences sharedPreferences = getSharedPreferences("wizit", MODE_PRIVATE);
//                SharedPreferences.Editor editor = sharedPreferences.edit();
//                editor.putString("role", "Guide");
//                editor.commit();
//                if(ParseUser.getCurrentUser().get("Role").toString().equals("Tourist")){
//                    Intent intent = new Intent(getApplicationContext(), BecomeGuide.class);
//                    startActivity(intent);}
//                if(ParseUser.getCurrentUser().get("Role").toString().equals("Guide")){
//                    Intent intent = new Intent(getApplicationContext(), StartActivity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                    startActivity(intent);
//                }
//            }
//        }
//        return super.onOptionsItemSelected(item);
//    }

    public void getCity(){
        mPd= new ProgressDialog(this);
        mPd.setMessage("Downloading");
        mPd.show();
        ParseQuery<ParseObject>  query = new ParseQuery<ParseObject>("City");
        query.addDescendingOrder("popularity");
        query.findInBackground(new FindCallback<ParseObject>() {

            @Override
            public void done(List<ParseObject> objects, com.parse.ParseException e) {

                if (e == null) {
                    CitySing.getInstance().getCityArray().clear();
                    for (int i = 0; i < objects.size(); i++) {
                        ParseObject po = objects.get(i);
                        final String name = po.get("Name").toString();
                        final int popularity = (int) po.get("popularity");
                        ParseFile imageFile = (ParseFile) po.get("Photo");
                        imageFile.getDataInBackground(new GetDataCallback() {
                            @Override
                            public void done(byte[] data, com.parse.ParseException e) {
                                // TODO Auto-generated method stub
                                if (e == null) {
                                    Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
                                    CitySing.getInstance().getCityArray().add(new City(name, popularity, bmp));
                                    lv.invalidateViews();
                                    lv.invalidate();
                                }

                            }
                        });
                    }
                }
                lv.invalidateViews();
                lv.invalidate();
                mPd.cancel();
            }

        });
    }

    @Override
    protected void onDestroy() {
        if (ParseUser.getCurrentUser()!=null){
        ParseUser user = ParseUser.getCurrentUser();
        user.put("Visible", false);
        user.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
            }
        });}
        super.onDestroy();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id){
            case R.id.acc_settings:
                if(ParseUser.getCurrentUser() == null){
                    Intent authintent = new Intent(getApplicationContext(), Authorize.class);
                    startActivity(authintent);}
                else{
                    Intent userintent = new Intent(getApplicationContext(), User.class);
                    startActivity(userintent);}
                break;
            case R.id.become_guide:
                if(ParseUser.getCurrentUser() == null){

                    Intent authintent = new Intent(getApplicationContext(), Authorize.class);
                    authintent.putExtra("guide", true);
                    startActivity(authintent);
                }
                else{
                    SharedPreferences sharedPreferences = getSharedPreferences("wizit", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("role", "Guide");
                    editor.commit();
                    if(ParseUser.getCurrentUser().get("Role").toString().equals("Tourist")){
                        Intent intent = new Intent(getApplicationContext(), BecomeGuide.class);
                        startActivity(intent);}
                    if(ParseUser.getCurrentUser().get("Role").toString().equals("Guide")){
                        Intent intent = new Intent(getApplicationContext(), StartActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                }
                break;
            case R.id.conversations:
                if(ParseUser.getCurrentUser() == null){
                    Intent authintent = new Intent(getApplicationContext(), Authorize.class);
                    startActivity(authintent);}
                else{
                    Intent convint = new Intent(getApplicationContext(), AllChats.class);
                    startActivity(convint);}
                break;
            case R.id.future_tours:
                if(ParseUser.getCurrentUser() == null){
                    Intent authintent = new Intent(getApplicationContext(), Authorize.class);
                    startActivity(authintent);}
                else{
                    Intent allIntent = new Intent(getApplicationContext(), AllMyTours.class).putExtra("role", "userName");
                    startActivity(allIntent);}
                break;
            case R.id.map:
                if(ParseUser.getCurrentUser() == null){
                    Intent authintent = new Intent(getApplicationContext(), Authorize.class);
                    startActivity(authintent);}
                else{
                    Intent mapintent = new Intent(getApplicationContext(), MapUsers.class);
                    startActivity(mapintent);}
                break;
            case R.id.myguides:
                break;
            case R.id.mytours:
                break;
            case R.id.off_guides:
                break;
            case R.id.off_tours:
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
