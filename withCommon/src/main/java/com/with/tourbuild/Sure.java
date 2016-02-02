package com.with.tourbuild;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.withcommon.R;

public class Sure extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sure);
    }

    public void yes(View v){
        setResult(RESULT_OK);
        finish();
    }
    public void no (View v){
        setResult(RESULT_CANCELED);
        finish();
    }
}
