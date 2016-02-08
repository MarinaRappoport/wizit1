package com.with.tourbuild;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.withcommon.R;
import com.parse.ParseException;
import com.parse.ParsePush;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignUp extends Activity {

    private ProgressDialog mPd;
    boolean gui;
    Spinner langSp;
    TextView langsTv;
    private RelativeLayout rl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        rl = (RelativeLayout) findViewById(R.id.signupLayout);
        langsTv = (TextView) rl.findViewById(R.id.myLangsTv);
        registerForContextMenu(langsTv);
        langSp = (Spinner) rl.findViewById(R.id.langSp);
        Bundle extras = getIntent().getExtras();
        if (extras != null){
            if (!extras.isEmpty()){
                gui = extras.getBoolean("guide");}}

        if(MySing.getInstance().getLangs().size()==0){
            MySing.getInstance().getAllLangs();
        }

        ArrayAdapter<String> laAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, MySing.getInstance().getLangs());
        laAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        langSp.setAdapter(laAdapter);
    }

    public void signUp(View v){
        EditText name  = (EditText) rl.findViewById(R.id.signName);
        EditText email = (EditText) rl.findViewById(R.id.signMail);
        EditText p1 = (EditText) rl.findViewById(R.id.signPass);
        EditText p2 = (EditText) rl.findViewById(R.id.signConPass);

        if (MySing.getInstance().getMyLangs().isEmpty()){
            Toast.makeText(getApplicationContext(), "You must choose at least one language", Toast.LENGTH_LONG).show();
        }

        if(!MySing.getInstance().getMyLangs().isEmpty() && p1.getText().toString().equals(p2.getText().toString()) && name.getText().toString() != null && email.getText().toString() != null){

            final ParseUser user = new ParseUser();
            user.setUsername(name.getText().toString());
            user.setPassword(p1.getText().toString());
            user.put("Role", "Tourist");
            user.put("Name", name.getText().toString());
            user.put("Langs", MySing.getInstance().getMyLangs());
            user.setEmail(email.getText().toString());
            mPd= new ProgressDialog(this);
            mPd.setMessage("Signing Up");
            mPd.show();
            user.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {
                    // TODO Auto-generated method stub
                    if (e == null) {
                        user.signUpInBackground(new SignUpCallback() {
                            @Override
                            public void done(ParseException e) {
                                SharedPreferences sp = getSharedPreferences("wizit", MODE_PRIVATE);
                                MySing.getInstance().getAllMyData(sp.getString("role", "Tourist"));
                                // TODO Auto-generated method stub
                                mPd.cancel();
                                if (e == null) {

                                    Toast.makeText(getApplicationContext(), "Done", Toast.LENGTH_LONG).show();
                                } else {
//                                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                                if (gui == true) {
                                    setResult(RESULT_OK);
                                }
                                ParsePush.subscribeInBackground(MySing.getInstance().getName());
                                finish();
                            }
                        });
                    }
                    if (e != null) {
                        Toast.makeText(getBaseContext(), e.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
        else
            Toast.makeText(getApplicationContext(), "Wrong password", Toast.LENGTH_LONG).show();
    }

    public void addLang(View v){
        if(MySing.getInstance().getLangs().size()>1 && !langSp.getSelectedItem().toString().equals("Choose language")) {
            MySing.getInstance().getMyLangs().add(langSp.getSelectedItem().toString());
            ArrayAdapter<String> laAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, MySing.getInstance().getLangs());
            laAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            langSp.setAdapter(laAdapter);
            langsTv.setText(MySing.getInstance().getLangsString());
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId() == R.id.myLangsTv) menu.add("Clear");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        if (item.getTitle().equals("Clear")){
            MySing.getInstance().getMyLangs().clear();
            MySing.getInstance().getAllLangs();
            langsTv.setText("");
        }

        return super.onContextItemSelected(item);
    }

    public void contex1(View v){
        this.openContextMenu(v);
    }

}
