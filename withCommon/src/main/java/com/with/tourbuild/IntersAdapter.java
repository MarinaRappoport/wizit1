package com.with.tourbuild;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.withcommon.R;

/**
 * Created by Petrucco on 20.09.2015.
 */
public class IntersAdapter extends BaseAdapter {

    private Context mContext;
    public IntersAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return IntersSing.getInstance().getInterlocsArray().size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RelativeLayout rl = null;
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        rl = (RelativeLayout) inflater.inflate(R.layout.inter_int_table, parent, false);
        TextView name = (TextView)rl.findViewById(R.id.interlNam);
        TextView role = (TextView)rl.findViewById(R.id.interlRole);
        ImageView iv = (ImageView)rl.findViewById(R.id.interlPhoto);
        name.setText(IntersSing.getInstance().getInterlocsArray().get(position).getUsername());
        role.setText(IntersSing.getInstance().getInterlocsArray().get(position).getRole());
        iv.setImageDrawable(IntersSing.getInstance().getInterlocsArray().get(position).getPhoto());
        return rl;
    }
}
