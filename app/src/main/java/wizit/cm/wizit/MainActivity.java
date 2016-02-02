package wizit.cm.wizit;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

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


public class MainActivity extends AppCompatActivity {

    ListView lv;
    CityAdapter mCityAdapter;
    private ProgressDialog mPd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        String finish = intent.getStringExtra("finish");
        if (finish != null) {
            finish();
            System.exit(0);
        }

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.mapUsers){
            if(ParseUser.getCurrentUser() == null){
                Intent authintent = new Intent(getApplicationContext(), Authorize.class);
                startActivity(authintent);}
            else{
            Intent mapintent = new Intent(getApplicationContext(), MapUsers.class);
            startActivity(mapintent);}
        }
        if (id == R.id.allConvers){
            if(ParseUser.getCurrentUser() == null){
                Intent authintent = new Intent(getApplicationContext(), Authorize.class);
                startActivity(authintent);}
            else{
            Intent convint = new Intent(getApplicationContext(), AllChats.class);
            startActivity(convint);}
        }
        if (id == R.id.allMytours){
            if(ParseUser.getCurrentUser() == null){
                Intent authintent = new Intent(getApplicationContext(), Authorize.class);
                startActivity(authintent);}
            else{
            Intent allIntent = new Intent(getApplicationContext(), AllMyTours.class).putExtra("role", "userName");
            startActivity(allIntent);}
        }

        if (id == R.id.user) {
            if(ParseUser.getCurrentUser() == null){
                Intent authintent = new Intent(getApplicationContext(), Authorize.class);
                startActivity(authintent);}
            else{
                Intent userintent = new Intent(getApplicationContext(), User.class);
                startActivity(userintent);}
        }

        if (id == R.id.beGuiMe){
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
        }
        return super.onOptionsItemSelected(item);
    }

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
}
