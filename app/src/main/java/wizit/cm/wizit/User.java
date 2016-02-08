package wizit.cm.wizit;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.with.tourbuild.ChangeName;
import com.with.tourbuild.MyGallery;
import com.with.tourbuild.MySing;

import java.io.IOException;


public class User extends Activity {

    TextView usernam;
    TextView userType;
    TextView langs;
    TextView interests;
    private ImageView userpic;
    private Bitmap mBitmap;
    private Bitmap mBitmapTemp;

    private Button becomeguide;
    String role;
    Spinner langSp;
    Spinner interSp;

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sp = getSharedPreferences("wizit", MODE_PRIVATE);
        MySing.getInstance().getAllMyData(sp.getString("role", "Tourist"));
        setContentView(R.layout.activity_user);
        langSp = (Spinner)findViewById(R.id.lanSpin);
        interSp = (Spinner)findViewById(R.id.interSpU);
        usernam = (TextView)findViewById(R.id.usernam);
        interests = (TextView)findViewById(R.id.interU);
        userType = (TextView)findViewById(R.id.userType);
        usernam.setText(MySing.getInstance().getName());
        userpic = (ImageView)findViewById(R.id.userpic);
        langs = (TextView)findViewById(R.id.myLangsU);
        becomeguide = (Button)findViewById(R.id.becGuiButt);

        registerForContextMenu(userpic);
        registerForContextMenu(usernam);
        registerForContextMenu(langs);
        registerForContextMenu(interests);


        loadImage();
        role = MySing.getInstance().getRole();
        if (role.equals("Guide")){
        becomeguide.setVisibility(View.GONE);
            becomeguide.setClickable(false);
        }
        userType.setText(role);
        langs.setText(MySing.getInstance().getLangsString());
        interests.setText(MySing.getInstance().getInterString());
        ArrayAdapter<String> laAdapter = new ArrayAdapter<String>(this, R.layout.spinner1, MySing.getInstance().getLangs());
        laAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        langSp.setAdapter(laAdapter);

        ArrayAdapter<String> inAdapter = new ArrayAdapter<String>(this, R.layout.spinner1, MySing.getInstance().getInterests());
        inAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        interSp.setAdapter(inAdapter);

    }

    public void logout(View v){
        MySing.getInstance().logout();
        Toast.makeText(getApplicationContext(), "Logged out", Toast.LENGTH_LONG).show();
        LoginManager.getInstance().logOut();
        startActivity(new Intent(getApplicationContext(),WelcomeActivity.class));
        finish();

    }

    public void addLangU(View v){
        if(MySing.getInstance().getLangs().size()>1 && !langSp.getSelectedItem().toString().equals("Choose language")) {
          MySing.getInstance().getMyLangs().add(langSp.getSelectedItem().toString());
            ArrayAdapter<String> laAdapter = new ArrayAdapter<String>(this, R.layout.spinner1, MySing.getInstance().getLangs());
            laAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            langSp.setAdapter(laAdapter);
            langs.setText(MySing.getInstance().getLangsString());
        }
    }

    public void addInterU(View v){
        if(MySing.getInstance().getInterests().size()>1 && !interSp.getSelectedItem().toString().equals("Choose your interests")) {
            MySing.getInstance().getMyInterests().add(interSp.getSelectedItem().toString());
            ArrayAdapter<String> laAdapter = new ArrayAdapter<String>(this, R.layout.spinner1, MySing.getInstance().getInterests());
            laAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            interSp.setAdapter(laAdapter);
            interests.setText(MySing.getInstance().getInterString());
        }
    }

    public void makePhoto(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, 1);
        }
    }

    public void pick (){
        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto , 2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            mBitmap = (Bitmap) extras.get("data");
            MySing.getInstance().setBmp(mBitmap);
            userpic.setImageBitmap(mBitmap);
            MySing.getInstance().picToParse(mBitmap);
        }

        if(requestCode == 2 && resultCode == RESULT_OK){
            Uri selectedImage = data.getData();
            try {
                mBitmapTemp = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                MyGallery gallery = new MyGallery();
                mBitmap = gallery.getRotatedImage(getApplicationContext(), mBitmapTemp, selectedImage);
            } catch (IOException e) {
                e.printStackTrace();
            }

            userpic.setImageBitmap(mBitmap);
            MySing.getInstance().setBmp(mBitmap);
            MySing.getInstance().picToParse(mBitmap);
        }

        if (requestCode == 12 && resultCode == RESULT_OK){
            usernam.setText(MySing.getInstance().getName());
        }
    }


    public void loadImage(){
        if (MySing.getInstance().getBmp() != null){
        Drawable dr = new BitmapDrawable(getResources(), MySing.getInstance().getBmp());
            userpic.setImageDrawable(dr);}
    }


    public void BecomeGuide(View v){
        SharedPreferences sharedPreferences = getSharedPreferences("wizit", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("role", "Guide");
        editor.commit();
        if(ParseUser.getCurrentUser().getString("Role").equals("Tourist")){
            intent = new Intent(getApplicationContext(), BecomeGuide.class);
        }else{
            intent = new Intent(getApplicationContext(), StartActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        }
        startActivity(intent);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        switch (v.getId()) {
            case R.id.userpic:
                menu.add("Camera");
                menu.add("Gallery");
                break;
            case R.id.usernam:
                menu.add("Change name");
                break;
            case R.id.myLangsU:
                menu.add("Clear languages");
                break;
            case R.id.interU:
                menu.add("Clear interests");
                break;
        }
    }

    public void contexCam(View v){
        this.openContextMenu(v);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getTitle().equals("Camera")){
            makePhoto();
        }
        if (item.getTitle().equals("Gallery")){
            pick();
        }
        if (item.getTitle().equals("Clear languages")){
            MySing.getInstance().getMyLangs().clear();
            MySing.getInstance().getAllLangs();
            langs.setText("");
        }
        if (item.getTitle().equals("Clear interests")){
            MySing.getInstance().getMyInterests().clear();
            MySing.getInstance().getAllInterests();
            interests.setText("");
        }
        if (item.getTitle().equals("Change name")){
            startActivityForResult(new Intent(getApplicationContext(), ChangeName.class), 12);
        }
        return super.onContextItemSelected(item);
    }

    public void saveU(View v) {
        if (MySing.getInstance().getMyLangs().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Choose at least one language ", Toast.LENGTH_LONG).show();
        }
else{
        ParseUser user = ParseUser.getCurrentUser();
        user.setUsername(MySing.getInstance().getName());
        user.put("Langs", MySing.getInstance().getMyLangs());
        user.put("Interests", MySing.getInstance().getMyInterests());
        user.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                Toast.makeText(getApplicationContext(), "Done", Toast.LENGTH_LONG).show();
            }
        });
    }
    }

}
