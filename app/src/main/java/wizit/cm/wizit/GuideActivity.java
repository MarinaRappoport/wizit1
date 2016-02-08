package wizit.cm.wizit;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.with.tourbuild.Chat;
import com.with.tourbuild.DateSing;
import com.with.tourbuild.GuideSing;
import com.with.tourbuild.MySing;
import com.with.tours.ToursList;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import sun.bob.mcalendarview.vo.DateData;


public class GuideActivity extends Activity {

    ImageView gPhoto;
    TextView gName;
    TextView gSpecs;
    int n;
    RatingBar rb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        rb = (RatingBar)findViewById(R.id.ratingBarG);
        Drawable stars = rb.getProgressDrawable();
        DrawableCompat.setTint(stars, Color.BLACK);
//        stars.setTint(Color.CYAN);

        gPhoto = (ImageView)findViewById(R.id.GPhoto);
        gName = (TextView)findViewById(R.id.GName);
        gSpecs = (TextView)findViewById(R.id.specsTV);
        Bundle extras = getIntent().getExtras();
        n = extras.getInt("number");
        DateSing.getInstance().dates(GuideSing.getInstance().getGuideArrayList().get(n).getmGuideName());
        gName.setText(GuideSing.getInstance().getGuideArrayList().get(n).getmGuideName());
        gPhoto.setImageDrawable(GuideSing.getInstance().getGuideArrayList().get(n).getGuidePhoto());
        gSpecs.setText(GuideSing.getInstance().getGuideArrayList().get(n).getSpecs());
        rb.setRating(GuideSing.getInstance().getGuideArrayList().get(n).finRate());

    }

    public void messToGid(View v){
        if (ParseUser.getCurrentUser()!= null){
        Intent intent = new Intent(getApplicationContext(), Chat.class);
        intent.putExtra("name", GuideSing.getInstance().getGuideArrayList().get(n).getmGuideName());
        startActivity(intent);}
        else{
            startActivity(new Intent(getApplicationContext(), Authorize.class));
        }
    }

    public void openGuiTours(View v){
        startActivity(new Intent(getApplicationContext(), ToursList.class).putExtra("number", n).putExtra("role", "tourist"));
    }

    public void vote(View v){
        if (MySing.getInstance().getName()!=null) {
            if (MySing.getInstance().getName().equals(GuideSing.getInstance().getGuideArrayList().get(n).getmGuideName())) {
                Toast.makeText(getApplicationContext(), "You can't vote for yourself", Toast.LENGTH_LONG).show();
            } else {
                if (GuideSing.getInstance().getGuideArrayList().get(n).isVoted()==false){
            ParseQuery<ParseObject>pq = new ParseQuery<ParseObject>("Guide");
                pq.whereEqualTo("objectId", GuideSing.getInstance().getGuideArrayList().get(n).getmGuideId());
                pq.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {
                        objects.get(0).increment("RatesNumber");
                        objects.get(0).increment("Rate", rb.getRating()*2);
                        objects.get(0).saveInBackground();
                        GuideSing.getInstance().getGuideArrayList().get(n).setVoted(true);
                        Toast.makeText(getApplicationContext(), "You have voted!", Toast.LENGTH_LONG).show();
                    }
                });}
                else{
                    Toast.makeText(getApplicationContext(), "You already voted for this guide!", Toast.LENGTH_LONG).show();
                }
            }
        }
        else{
            startActivity(new Intent(getApplicationContext(), Authorize.class));
        }
    }



}
