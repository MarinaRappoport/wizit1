package com.with.tourbuild;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.withcommon.R;

public class LogOS extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_os);
    }

    public void chLogin(View v){
        startActivity(new Intent(getApplicationContext(), Login.class));
        finish();

    }

    public void chSign(View v){
        startActivity(new Intent(getApplicationContext(), SignUp.class));
        finish();
    }
}
