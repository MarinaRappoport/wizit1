package com.with.tourbuild;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.withcommon.R;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParsePush;
import com.parse.ParseUser;

public class Login extends Activity {

    EditText name;
    EditText password;
    boolean gui;
    String role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Bundle extras = getIntent().getExtras();
        if (extras != null){
            if (!extras.isEmpty()){
                gui = extras.getBoolean("guide");}}
        name = (EditText)findViewById(R.id.logName);
        password = (EditText)findViewById(R.id.logPass);
    }

    public void login(View v){
        if (name.getText() != null && password.getText() != null) {
            ParseUser.logInInBackground(name.getText().toString(), password.getText().toString(), new LogInCallback() {
                @Override
                public void done(ParseUser user, ParseException e) {
                    if (e == null) {
                        role = ParseUser.getCurrentUser().get("Role").toString();
                        MySing.getInstance().getAllMyData(role);
                        ParsePush.subscribeInBackground(MySing.getInstance().getName());
                        Toast.makeText(getApplicationContext(), "Logged as " + ParseUser.getCurrentUser().getUsername().toString(), Toast.LENGTH_LONG).show();
                        if (gui == true && role.equals("Tourist")){
                           setResult(RESULT_OK);
                        }
                        if (role.equals("Guide")){
                            setResult(RESULT_OK);
                        }

                        finish();
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Wrong name or password", Toast.LENGTH_LONG).show();
                    }

                }
            });
        }
    }
}
