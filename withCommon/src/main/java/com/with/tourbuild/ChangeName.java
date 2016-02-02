package com.with.tourbuild;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.withcommon.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class ChangeName extends Activity {

    EditText editName;
    String oldName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        oldName = MySing.getInstance().getName();
        setContentView(R.layout.activity_change_name);
        editName = (EditText)findViewById(R.id.editName);
        editName.setText(MySing.getInstance().getName());
    }

    public void yes(View v){
        if (!editName.getText().equals("")){
            MySing.getInstance().setName(editName.getText().toString());
            changeNames();
            setResult(RESULT_OK);
        finish();}
        else {
            Toast.makeText(getApplicationContext(), "Name can't be empty", Toast.LENGTH_LONG).show();
        }
    }
    public void no (View v){
        finish();
    }

    private void changeNames(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Messages");
        query.whereEqualTo("From", oldName);

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                for (int i=0; i<objects.size(); i++){
                    objects.get(i).put("From", MySing.getInstance().getName());
                    objects.get(i).saveInBackground();
                }
            }
        });


        ParseQuery<ParseObject> query2 = ParseQuery.getQuery("Messages");
        query2.whereEqualTo("To", oldName);

        query2.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                for (int i=0; i<objects.size(); i++){
                    objects.get(i).put("To", MySing.getInstance().getName());
                    objects.get(i).saveInBackground();
                }
            }
        });


        ParseQuery<ParseObject> query3 = ParseQuery.getQuery("REGISTRATION");
        query3.whereEqualTo("guideName", oldName);

        query3.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                for (int i=0; i<objects.size(); i++){
                    objects.get(i).put("guideName", MySing.getInstance().getName());
                    objects.get(i).saveInBackground();
                }
            }
        });

        ParseQuery<ParseObject> query4 = ParseQuery.getQuery("REGISTRATION");
        query4.whereEqualTo("userName", oldName);

        query4.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                for (int i=0; i<objects.size(); i++){
                    objects.get(i).put("userName", MySing.getInstance().getName());
                    objects.get(i).saveInBackground();
                }
            }
        });

        ParseQuery<ParseObject> query5 = ParseQuery.getQuery("TOUR");
        query5.whereEqualTo("GuideName", oldName);

        query5.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                for (int i = 0; i < objects.size(); i++) {
                    objects.get(i).put("GuideName", MySing.getInstance().getName());
                    objects.get(i).saveInBackground();
                }
            }
        });

        ParseUser user =  ParseUser.getCurrentUser();
        user.setUsername(MySing.getInstance().getName());
        ParseUser.getCurrentUser().saveInBackground();

    }


}
