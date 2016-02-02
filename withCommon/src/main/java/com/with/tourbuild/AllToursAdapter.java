package com.with.tourbuild;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.withcommon.R;

/**
 * Created by Petrucco on 18.09.2015.
 */
public class AllToursAdapter  extends BaseAdapter {

    private Context myContext;

    public AllToursAdapter(Context context) {
        this.myContext = context;
    }

    @Override
    public int getCount() {
        return AllToursSing.getInstance().getTourFromAllArrayList().size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        RelativeLayout rl = null;
        LayoutInflater inflater = (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        rl = (RelativeLayout) inflater.inflate(R.layout.tour_from_all, parent, false);
        TextView tourName = (TextView)rl.findViewById(R.id.allRTourName);
        TextView date = (TextView)rl.findViewById(R.id.allRDate);
        TextView amount = (TextView)rl.findViewById(R.id.allRAmount);
        TextView comment = (TextView)rl.findViewById(R.id.allRComment);
        TextView guiName = (TextView)rl.findViewById(R.id.allRGuideName);
        TourFromAll r = AllToursSing.getInstance().getTourFromAllArrayList().get(position);
        tourName.setText(r.getTourName());
        date.setText(r.getDate());
        amount.setText(String.valueOf(r.getAmount()));
        comment.setText(r.getComment());
        guiName.setText(r.getGuiName());
        return rl;
    }

}