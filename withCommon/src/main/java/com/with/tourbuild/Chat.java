package com.with.tourbuild;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.withcommon.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class Chat extends Activity implements UpdateIF, ServiceConnection {

    TextView nameInterloc;
    TextView myName;
    public String name = ParseUser.getCurrentUser().getUsername();
    TextView allChat;
    public String to = "";
    EditText Mess;
    Intent sintent;
    private MyService mService;
    private ProgressDialog mPd;

    @Override
    protected void onStart() {
        startSet();
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
    }

    @Override
    public synchronized void Update() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String str = "";
                for (int i = 0; i < MessageSing.getInstance().getVictor().size(); i++) {
                    if (MessageSing.getInstance().getVictor().get(i).getmFrom().equals(name) || MessageSing.getInstance().getVictor().get(i).getmTo().equals(name)) {
                        str = str + MessageSing.getInstance().getVictor().get(i).getmFrom() + " :  " + MessageSing.getInstance().getVictor().get(i).getmMessage() + "\n";
                        allChat = (TextView) findViewById(R.id.allMsgs);
                        allChat.setText(str);
                        allChat.invalidate();
                        allChat.setMovementMethod(new ScrollingMovementMethod());
                    }
                }

            }
        });
    }

    public void getAllMessages() {
        mPd= new ProgressDialog(this);
        mPd.setMessage("Downloading");
        mPd.show();
        try {
            MessageSing.getInstance().getVictor().clear();
            ParseQuery<ParseObject> queryIn = ParseQuery.getQuery("Messages");
            queryIn.whereEqualTo("To", MySing.getInstance().getName());
            queryIn.whereEqualTo("From", to);
            queryIn.whereEqualTo("ReadState", true);

            ParseQuery<ParseObject> queryOut = ParseQuery.getQuery("Messages");
            queryOut.whereEqualTo("To", to);
            queryOut.whereEqualTo("From", MySing.getInstance().getName());
            queryIn.whereEqualTo("ReadState", true);


            List<ParseQuery<ParseObject>> queries = new ArrayList<ParseQuery<ParseObject>>();
            queries.add(queryIn);
            queries.add(queryOut);

            ParseQuery<ParseObject> mainQuery = ParseQuery.or(queries);
            mainQuery.addAscendingOrder("createdAt");


            mainQuery.findInBackground(new FindCallback<ParseObject>() {
                public void done(List<ParseObject> scoreList, ParseException e) {
                    ser();
                    if (e == null) {
                        for (final ParseObject parseObject : scoreList) {
                            Message m = new Message(parseObject.getString("From"), parseObject.getString("To"), parseObject.getString("Message"), parseObject.getBoolean("ReadState"), parseObject.getObjectId());
                            MessageSing.getInstance().getVictor().add(m);
                            Update();

                        }
                    } else {

                    }
                    mPd.cancel();
                }
            });


        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void send(View v) {
        ParseObject messages = new ParseObject("Messages");
        EditText Mess = (EditText) findViewById(R.id.newMsg);
        if (to.equals("") || Mess.getText().length() == 0) {
            Toast.makeText(this, "ENTER TEXT", Toast.LENGTH_SHORT).show();
            return;
        } else {
            messages.put("From", name);
            messages.put("To", to);
            messages.put("Message", Mess.getText().toString());
            messages.put("ReadState", false);
            MessageSing.getInstance().getVictor().add(new Message(name, to, Mess.getText().toString(), false, ParseUser.getCurrentUser().getObjectId().toString()));
            Update();


            JSONObject data;
            try {
                data = new JSONObject();
                data.put("action", "Wizit");
                data.put("name", name);
                data.put("to", to);
                data.put("type", "message");
                data.put("mes", Mess.getText().toString());
                ParsePush push = new ParsePush();
                push.setChannel(to);
                push.setData(data);
                push.sendInBackground();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            Toast.makeText(getApplicationContext(), "Sent", Toast.LENGTH_SHORT).show();


            Mess.setText("");
            messages.saveInBackground(new SaveCallback() {

                @Override
                public void done(ParseException arg0) {
                    // TODO Auto-generated method stub
                    if (arg0 == null) {
                        System.out.println("OK!");
                    } else {
                        System.out.println(arg0);
                    }
                }
            });
        }
    }

    @Override
    public void onServiceConnected(ComponentName arg0, IBinder arg1) {
        // TODO Auto-generated method stub
        MyBinder binder = (MyBinder) arg1;
        mService = binder.getmService();
        mService.setmActivity(this);
    }


    @Override
    public void onServiceDisconnected(ComponentName arg0) {

    }

    @Override
    protected void onDestroy() {
        unbindService(this);
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_chat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.refresh) {
            getApplicationContext().startService(new Intent(getApplicationContext(), MyService.class));
        }

        return super.onOptionsItemSelected(item);
    }

    public void startSet(){
        MySing.getInstance().setName(ParseUser.getCurrentUser().getUsername());
        Bundle extras = getIntent().getExtras();
        to = extras.getString("name");
        nameInterloc = (TextView) findViewById(R.id.nameDialCh);
        myName = (TextView) findViewById(R.id.nameInCh);
        nameInterloc.setText(to);
        myName.setText(name);
        Mess = (EditText) findViewById(R.id.newMsg);
        getAllMessages();
    }

    public void ser(){
        sintent = new Intent(getApplicationContext(), MyService.class);
        startService(sintent);
        bindService(sintent, this, 0);
    }

}
