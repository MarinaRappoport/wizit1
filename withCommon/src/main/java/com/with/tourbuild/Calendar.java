package com.with.tourbuild;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.withcommon.R;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;

import java.util.Date;

import sun.bob.mcalendarview.MarkStyle;
import sun.bob.mcalendarview.listeners.OnDateClickListener;
import sun.bob.mcalendarview.listeners.OnMonthChangeListener;
import sun.bob.mcalendarview.mCalendarView;
import sun.bob.mcalendarview.vo.DateData;

public class Calendar extends AppCompatActivity {

    mCalendarView calendarView;
    EditText amount;
    EditText comment;
    TextView picked;
    DateData dat;
    Date pickedData;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        Intent intent = getIntent();
        name = intent.getStringExtra("Name");
        amount = (EditText)findViewById(R.id.amoTour);
        comment = (EditText)findViewById(R.id.comm);
        picked = (TextView)findViewById(R.id.pickedDate);
        calendarView = ((mCalendarView) findViewById(R.id.calendar));
        for (int i=0; i<DateSing.getInstance().getDateList().size(); i++){
            calendarView.markDate(DateSing.getInstance().getDateList().get(i));
        }

        calendarView.setOnDateClickListener(new OnDateClickListener() {
            @Override
            public void onDateClick(View view, DateData date) {
                if (DateSing.getInstance().getDateList().contains(date)) {
                    Toast.makeText(getApplicationContext(), "This date is unavailable", Toast.LENGTH_SHORT).show();
                } else {
                    picked.setText(date.getDay() + "." + date.getMonth()+"."+date.getYear());
                    dat = date;

                }

            }
        }).setOnMonthChangeListener(new OnMonthChangeListener() {
            @Override
            public void onMonthChange(int year, int month) {
            }
        }).setMarkedStyle(MarkStyle.BACKGROUND, MarkStyle.color);
    }

    public void setOk(View v){
        if (amount.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(), "Enter the number of tourists", Toast.LENGTH_LONG).show();
        }
        if (dat==null){
            Toast.makeText(getApplicationContext(), "Choose the date", Toast.LENGTH_LONG).show();
        }
        if (!amount.getText().toString().equals("") && dat!=null){
            Intent surein = new Intent(getApplicationContext(), Sure.class);
            startActivityForResult(surein, 10);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode ==10 && resultCode == RESULT_OK){
            Intent calRes = new Intent();
            calRes.putExtra("amount", Integer.parseInt(amount.getText().toString()));
            if (comment.getText() != null){
                calRes.putExtra("comment", comment.getText().toString());
            }
            saveDate();
            calRes.putExtra("date", pickedData);
            setResult(RESULT_OK, calRes);
            finish();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void saveDate(){
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.clear();
        calendar.set(java.util.Calendar.MONTH, dat.getMonth() - 1);
        calendar.set(java.util.Calendar.YEAR, dat.getYear());
        calendar.set(java.util.Calendar.DAY_OF_MONTH, dat.getDay()+1);
        pickedData = calendar.getTime();
    }
}
