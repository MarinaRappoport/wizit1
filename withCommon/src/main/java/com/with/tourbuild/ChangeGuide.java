package com.with.tourbuild;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.withcommon.R;
import com.facebook.login.LoginManager;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.IOException;
import java.util.List;

public class ChangeGuide extends Activity {

    TextView guiName;
    TextView rate;
    TextView ranum;
    TextView langs;
    TextView specias;
    ImageView guiPh;
    Spinner citySp;
    Spinner specSp;
    Spinner langSp;

    private Bitmap mBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_guide);
        citySp = (Spinner) findViewById(R.id.citySpinner);
        specSp = (Spinner) findViewById(R.id.specSpinner);
        langSp = (Spinner) findViewById(R.id.lanSpinner);
        specias = (TextView) findViewById(R.id.specias);
        langs = (TextView) findViewById(R.id.langTv);
        rate = (TextView) findViewById(R.id.rate);
        ranum = (TextView) findViewById(R.id.ratNum);
        SharedPreferences sp = getSharedPreferences("wizit", MODE_PRIVATE);
        MySing.getInstance().getAllMyData(sp.getString("role", "Tourist"));
        guiPh = (ImageView) findViewById(R.id.GuiPho);
        if (MySing.getInstance().getBmp() != null)
        guiPh.setImageBitmap(MySing.getInstance().getBmp());
        guiName = (TextView) findViewById(R.id.guiName);
        guiName.setText(MySing.getInstance().getName());
        guiPh = (ImageView) findViewById(R.id.GuiPho);
        if (MySing.getInstance().getBmp() != null)
        guiPh.setImageBitmap(MySing.getInstance().getBmp());
        ArrayAdapter<String> ciAdapter = new ArrayAdapter<String>(this, R.layout.spinner, CitySing.getInstance().getCityNames());
        ciAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        citySp.setAdapter(ciAdapter);
        int cp = ciAdapter.getPosition(MySing.getInstance().getCity());
        citySp.setSelection(cp);
        registerForContextMenu(specias);
        registerForContextMenu(guiPh);
        registerForContextMenu(langs);
        registerForContextMenu(guiPh);

        specias.setText(MySing.getInstance().getSpecs());
        langs.setText(MySing.getInstance().getLangsString());
        rate.setText(String.valueOf(MySing.getInstance().getRate()));
        ranum.setText(String.valueOf(MySing.getInstance().getRatesNum()));

        ArrayAdapter<String> spAdapter = new ArrayAdapter<String>(this, R.layout.spinner, MySing.getInstance().getSpecias());
        spAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        specSp.setAdapter(spAdapter);

        ArrayAdapter<String> laAdapter = new ArrayAdapter<String>(this, R.layout.spinner, MySing.getInstance().getLangs());
        laAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        langSp.setAdapter(laAdapter);

    }


    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {

        if (v.getId() == R.id.langTv){
            menu.add("Clear languages");}
        if (v.getId() == R.id.specias){
            menu.add("Clear specialities");}
        if (v.getId() == R.id.GuiPho) {
            menu.add("Camera");
            menu.add("Gallery");
        }
    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getTitle().equals("Clear specialities")) {
            specias.setText("");
            MySing.getInstance().getMySpecs().clear();
        }
        if (item.getTitle().equals("Clear languages")) {
            langs.setText("");
            MySing.getInstance().getMyLangs().clear();
        }
        if (item.getTitle().equals("Camera")){
            makePhoto();
        }
        if (item.getTitle().equals("Gallery")){
            pick();
        }
        return super.onContextItemSelected(item);
    }

    public void makePhoto(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, 3);
        }
    }

    public void pick (){
        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto, 4);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode ==1 && resultCode == RESULT_OK){
            specias.setText("");
            MySing.getInstance().getMySpecs().clear();
        }


        if (requestCode == 3 && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            mBitmap = (Bitmap) extras.get("data");
            MySing.getInstance().setBmp(mBitmap);
            guiPh.setImageBitmap(mBitmap);
            MySing.getInstance().picToParse(mBitmap);
        }

        if(requestCode == 4 && resultCode == RESULT_OK){
            Uri selectedImage = data.getData();
            guiPh.setImageURI(selectedImage);
            try {
                mBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
            MySing.getInstance().setBmp(mBitmap);
            MySing.getInstance().picToParse(mBitmap);
        }

        if(requestCode == 5 && resultCode == RESULT_OK){
            guiName.setText(MySing.getInstance().getName());
        }
    }

    public void contex(View v){
        this.openContextMenu(v);
    }

    public void chaName(View v){
        startActivityForResult(new Intent(getApplicationContext(), ChangeName.class), 5);
    }

    public void submit (View v){
        if (MySing.getInstance().getMyLangs().isEmpty()){
            Toast.makeText(getApplicationContext(), "You must choose at least one language", Toast.LENGTH_LONG).show();
        }
        else{
        ParseUser user =  ParseUser.getCurrentUser();
        user.put("Langs", MySing.getInstance().getMyLangs());
        ParseUser.getCurrentUser().saveInBackground();

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Guide");
            query.whereEqualTo("PointerToUser", user);
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {
                    ParseObject guide = objects.get(0);
                    guide.put("City", citySp.getSelectedItem().toString());
                    guide.put("Specialities", MySing.getInstance().getMySpecs());
                    MySing.getInstance().setCity(citySp.getSelectedItem().toString());
                    guide.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            Toast.makeText(getApplicationContext(), "Done", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });}
    }

    public void logout(View v){
        MySing.getInstance().logout();
        Toast.makeText(getApplicationContext(), "Logged out", Toast.LENGTH_LONG).show();
        setResult(RESULT_OK);
        LoginManager.getInstance().logOut();
        finish();
    }

    public void addToSpecs(View v){
        if(MySing.getInstance().getSpecias().size()>1 && !specSp.getSelectedItem().toString().equals("Choose your speciality")) {
            MySing.getInstance().getMySpecs().add(specSp.getSelectedItem().toString());
            ArrayAdapter<String> spAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, MySing.getInstance().getSpecias());
            spAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            specSp.setAdapter(spAdapter);
            specias.setText(MySing.getInstance().getSpecs());
        }
    }

    public void addToLangs(View v){
        if(MySing.getInstance().getLangs().size()>1 && !langSp.getSelectedItem().toString().equals("Choose language")) {
            MySing.getInstance().getMyLangs().add(langSp.getSelectedItem().toString());
            ArrayAdapter<String> laAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, MySing.getInstance().getLangs());
            laAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            langSp.setAdapter(laAdapter);
            langs.setText(MySing.getInstance().getLangsString());
        }
    }


}

