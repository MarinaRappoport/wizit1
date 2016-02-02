package wizit.cm.wizit;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.with.tourbuild.Login;
import com.with.tourbuild.MySing;
import com.with.tourbuild.SignUp;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Authorize extends Activity {

    boolean gui = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorize);
        Bundle extras = getIntent().getExtras();
        if (extras != null){
            if (!extras.isEmpty()){gui = extras.getBoolean("guide");}
        }
        try {
            PackageInfo info =     getPackageManager().getPackageInfo("com.parse.integratingfacebooktutorial",     PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String sign= Base64.encodeToString(md.digest(), Base64.DEFAULT);
                Log.e("MY KEY HASH:", sign);
                Toast.makeText(getApplicationContext(),sign, Toast.LENGTH_LONG).show();
            }
        } catch (Exception e){}
        ParseFacebookUtils.initialize(this);


    }

    public void logn(View v){
        Intent logintent = new Intent(getApplicationContext(), Login.class);
        if (gui == true){
                logintent.putExtra("guide", true);
        }
        startActivityForResult(logintent, 13);
    }

    public void signp(View v){
        Intent signintent = new Intent(getApplicationContext(), SignUp.class);
        if (gui == true){
            signintent.putExtra("guide", true);
        }
        startActivityForResult(signintent, 14);
    }

    public void onLoginClick(View v) {

        List<String> permissions = Arrays.asList("public_profile", "email");
        ParseFacebookUtils.logInWithReadPermissionsInBackground(this, permissions, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException err) {
                if (user == null) {
                } else if (user.isNew()) {
                    makeMeRequest();
                } else {
                    showUserDetailsActivity();
                }
            }
        });
    }
    private void makeMeRequest() {
        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject jsonObject, GraphResponse graphResponse) {
                        if (jsonObject != null) {
                            JSONObject userProfile = new JSONObject();

                            try {
                                String id = jsonObject.optString("id");
                                userProfile.put("facebookId", jsonObject.getLong("id"));
                                userProfile.put("name", jsonObject.getString("name"));

                                if (jsonObject.getString("gender") != null)
                                    userProfile.put("gender", jsonObject.getString("gender"));

                                if (jsonObject.getString("email") != null)
                                    userProfile.put("email", jsonObject.getString("email"));

                                // Save the user profile info in a user property
                                ParseUser currentUser = ParseUser.getCurrentUser();
                                if(currentUser.get("Role") == null){
                                currentUser.put("Role", "Tourist");
                                    ArrayList<String> lang = new ArrayList<String>();
                                    lang.add("English");
                                    currentUser.put("Name", jsonObject.getString("name"));
                                    currentUser.setEmail(jsonObject.getString("email"));
                                    MySing.getInstance().setRole("Tourist");
                                    MySing.getInstance().setName(jsonObject.getString("name"));
                                    MySing.getInstance().setMyLangs(lang);
                                    currentUser.put("Langs", lang);
                                    currentUser.setUsername(jsonObject.getString("name"));
                                    currentUser.saveInBackground(new SaveCallback() {
                                        @Override
                                        public void done(ParseException e) {
                                            if (gui == true){
                                                Intent guinetnt = new Intent(getApplicationContext(), BecomeGuide.class);
                                                startActivity(guinetnt);
                                            }
                                        }
                                    });}
                        Toast.makeText(getApplicationContext(),  jsonObject.getString("name"), Toast.LENGTH_LONG ).show();
                            } catch (JSONException e) {
                            }
                        } else if (graphResponse.getError() != null) {
                            switch (graphResponse.getError().getCategory()) {
                                case LOGIN_RECOVERABLE:
                                    break;

                                case TRANSIENT:
                                    break;

                                case OTHER:
                                    break;
                            }
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,email,gender,name");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private void showUserDetailsActivity() {
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 13 && resultCode ==RESULT_OK && MySing.getInstance().getRole().equals("Tourist")){
            Intent guinetnt = new Intent(getApplicationContext(), BecomeGuide.class);
            startActivity(guinetnt);
        }
        if (requestCode == 13 && resultCode ==RESULT_OK && MySing.getInstance().getRole().equals("Guide")){
            Intent intent = new Intent(getApplicationContext(), StartActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

        if (requestCode == 14 && resultCode ==RESULT_OK && MySing.getInstance().getRole().equals("Tourist")){
        Intent guinetnt = new Intent(getApplicationContext(), BecomeGuide.class);
        startActivity(guinetnt);
        }
        if (requestCode == 64206) {
            ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);
        }
        finish();
    }
}
