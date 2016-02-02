package wizit.cm.wizit;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseGeoPoint;
import com.parse.ParseUser;
import com.with.tourbuild.MySing;
import com.with.tourbuild.UsersSing;

public class ChangeRadius extends Activity {

    EditText radET;
    EditText timeET;
    CheckBox visCb;
    CheckBox seeOtCb;
    int radius;
    int time;
    boolean vis;
    boolean seeOt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_radius);
        radET = (EditText) findViewById(R.id.RadiusEt);
        timeET = (EditText) findViewById(R.id.timeEt);
        visCb = (CheckBox) findViewById(R.id.visiblecb);
        seeOtCb = (CheckBox) findViewById(R.id.seeOthCb);

        radius = UsersSing.getInstance().getRadius();
        time = UsersSing.getInstance().getTime();
        vis = UsersSing.getInstance().isVisible();
        seeOt = UsersSing.getInstance().isSeeOthers();

        radET.setText(String.valueOf(radius));
        timeET.setText(String.valueOf(time));
        visCb.setChecked(vis);
        seeOtCb.setChecked(seeOt);
    }

    public void radOk(View v) {
        if (radET.getText().toString() != null && timeET.getText().toString() != null) {
            radius = Integer.parseInt(radET.getText().toString());
            time = Integer.parseInt(timeET.getText().toString());
            vis = visCb.isChecked();
            seeOt = seeOtCb.isChecked();
            UsersSing.getInstance().setRadius(radius);
            UsersSing.getInstance().setTime(time);
            UsersSing.getInstance().setVisible(vis);
            UsersSing.getInstance().setSeeOthers(seeOt);
            ParseUser user = ParseUser.getCurrentUser();
            user.put("Visible", vis);
            user.saveInBackground();
            save();
            finish();
        } else {
            Toast.makeText(getApplicationContext(), "You must fill all fields", Toast.LENGTH_LONG).show();
        }
    }

    private void save() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("radius", radius);
        editor.putInt("time", time);
        editor.putBoolean("visible", vis);
        editor.putBoolean("seeOther", seeOt);
        editor.commit();
    }
}
