package com.with.tourbuild;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.withcommon.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

public class TourForTouristDetails extends Activity {

    TextView tourNameTV;
    TextView tourDescrTV;
    TextView tourPrice;
    RatingBar rb;
    int tourId;
    Tour tour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tor_for_tourist_details);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            tourId  = bundle.getInt("tourId", -1);

            if (tourId != -1) {
                tour = CommonShared.getInstance().getmTours().get(tourId);
                tourNameTV   = (TextView)findViewById(R.id.tournFu);
                tourDescrTV  = (TextView)findViewById(R.id.tourdFu);
                tourPrice  = (TextView)findViewById(R.id.tourpFu);
                tourNameTV.setText(tour.getmTourName());
                tourDescrTV.setText(tour.getmTourDescription());
                tourPrice.setText(String.valueOf(tour.getmPrice()));
                rb = (RatingBar)findViewById(R.id.tourRat);
                rb.setRating(tour.finRate());
            }
        }
    }

    public void voteT(View v){
        if (MySing.getInstance().getName()!=null) {
            if (MySing.getInstance().getName().equals(tour.getmGuideName())) {
                Toast.makeText(getApplicationContext(), "You can't vote for your tour", Toast.LENGTH_LONG).show();
            } else {
                if (tour.isVoted==false){
                    ParseQuery<ParseObject> pq = new ParseQuery<ParseObject>("TOUR");
                    pq.whereEqualTo("objectId", tour.getmTourId());
                    pq.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> objects, ParseException e) {
                            objects.get(0).increment("RatesNumber");
                            objects.get(0).increment("Rate", rb.getRating()*2);
                            objects.get(0).saveInBackground();
                            tour.setIsVoted(true);
                            Toast.makeText(getApplicationContext(), "You have voted!", Toast.LENGTH_LONG).show();
                        }
                    });}
                else{
                    Toast.makeText(getApplicationContext(), "You already voted for this tour!", Toast.LENGTH_LONG).show();
                }
            }
        }
        else{
            startActivity(new Intent(getApplicationContext(), LogOS.class));
        }
    }

}
