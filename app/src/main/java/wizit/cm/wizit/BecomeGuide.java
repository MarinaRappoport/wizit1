package wizit.cm.wizit;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.with.tourbuild.CitySing;
import com.with.tourbuild.MyGallery;
import com.with.tourbuild.MySing;
import com.with.tourbuild.Sure;

import java.io.IOException;

public class BecomeGuide extends Activity {

    TextView guiName;
    ImageView guiPh;
    Spinner citySp;
    Spinner specSp;
    TextView specias;
    String s = "";
    private Bitmap mBitmap;
    private Bitmap mBitmapTemp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_become_guide);
        citySp = (Spinner)findViewById(R.id.citySpinner);
        specSp = (Spinner)findViewById(R.id.specSpinner);
        specias = (TextView)findViewById(R.id.specias);
        getSpecs();
        guiPh = (ImageView)findViewById(R.id.GuiPho);
        if(MySing.getInstance().getBmp() != null) guiPh.setImageBitmap(MySing.getInstance().getBmp());
        guiName = (TextView)findViewById(R.id.guiName);
        guiName.setText(MySing.getInstance().getName());
        registerForContextMenu(specias);
        registerForContextMenu(guiPh);
        ArrayAdapter<String> ciAdapter = new ArrayAdapter<String>(this, R.layout.spinner1, CitySing.getInstance().getCityNames());
        ciAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        citySp.setAdapter(ciAdapter);
    }

    public void addToSpecs(View v){
        if(MySing.getInstance().getSpecias().size()>1 && !specSp.getSelectedItem().toString().equals("Choose your speciality")) {
            MySing.getInstance().getMySpecs().add(specSp.getSelectedItem().toString());
            ArrayAdapter<String> spAdapter = new ArrayAdapter<String>(this, R.layout.spinner1, MySing.getInstance().getSpecias());
            spAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            specSp.setAdapter(spAdapter);
            specias.setText(MySing.getInstance().getSpecs());
        }
    }

    public void getSpecs(){
        ArrayAdapter<String> spAdapter = new ArrayAdapter<String>(this, R.layout.spinner1, MySing.getInstance().getSpecias());
        spAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        specSp.setAdapter(spAdapter);
        specSp.setSelection(0);
    }

    public void subGui(View v){
        if(MySing.getInstance().getMySpecs().isEmpty()){
            Toast.makeText(getApplicationContext(), "You must choose your specialities", Toast.LENGTH_LONG).show();}
        if(MySing.getInstance().getBmp()==null){
            Toast.makeText(getApplicationContext(), "You must choose the userpic", Toast.LENGTH_LONG).show();}
        if(!MySing.getInstance().getMySpecs().isEmpty() && MySing.getInstance().getBmp()!=null ){
            startActivityForResult(new Intent(getApplicationContext(), Sure.class), 2);
        }
    }

    public void submit(){
        ParseObject guide = new ParseObject("Guide");
        guide.put("Rate", 0);
        guide.put("RatesNumber", 0);
        guide.put("City", citySp.getSelectedItem().toString());
        guide.put("PointerToUser", ParseUser.getCurrentUser());
        guide.put("Specialities", MySing.getInstance().getMySpecs());
        guide.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    ParseUser user = ParseUser.getCurrentUser();
                    user.put("Role", "Guide");
                    user.saveInBackground();
                    // Save guide role in shared preferences
                    SharedPreferences sharedPreferences = getSharedPreferences("wizit", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("role", "Guide");
                    editor.commit();

                    Toast.makeText(getApplicationContext(), "Done", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), StartActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), e.getMessage().toString(), Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        switch (v.getId()) {
            case R.id.specias:
                menu.add("Clear all");
                break;
            case R.id.GuiPho:
                menu.add("Camera");
                menu.add("Gallery");
                break;
        }
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getTitle().equals("Clear all")) {
            startActivityForResult(new Intent(getApplicationContext(), Sure.class), 1);
        }
        if (item.getTitle().equals("Camera")){
            makePhoto();
        }
        if (item.getTitle().equals("Gallery")){
            pick();
        }
        return super.onContextItemSelected(item);
}

    public void contex(View v){
        this.openContextMenu(v);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode ==1 && resultCode == RESULT_OK){
            specias.setText("");
            MySing.getInstance().getMySpecs().clear();
            getSpecs();
        }
        if (requestCode ==2 && resultCode == RESULT_OK){
            submit();
            finish();
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

            try {
                mBitmapTemp = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                MyGallery gallery = new MyGallery();
                mBitmap = gallery.getRotatedImage(getApplicationContext(), mBitmapTemp, selectedImage);
            } catch (IOException e) {
                e.printStackTrace();
            }

            guiPh.setImageBitmap(mBitmap);

            MySing.getInstance().setBmp(mBitmap);
            MySing.getInstance().picToParse(mBitmap);
        }
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

}
