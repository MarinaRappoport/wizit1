package wizit.cm.wizit;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.with.tourbuild.CitySing;

import java.util.ArrayList;
import java.util.Vector;

/**
 * Created by Petrucco on 19.08.2015.
 */
public class CityAdapter extends BaseAdapter {



    public CityAdapter(Context mContext) {
        this.mContext = mContext;
    }

    private Context mContext;

    @Override
    public int getCount() {
       return CitySing.getInstance().getCityArray().size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RelativeLayout rl = null;
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        rl = (RelativeLayout) inflater.inflate(R.layout.city, parent, false);
        TextView cityname = (TextView)rl.findViewById(R.id.cityName);
        cityname.setText(CitySing.getInstance().getCityArray().get(position).getName());
        rl.setBackgroundDrawable(CitySing.getInstance().getCityArray().get(position).getPhoto());
        return rl;
    }
}
