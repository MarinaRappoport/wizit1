package com.with.tours;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.with.tourbuild.CommonShared;
import com.with.tourbuilder.R;

public class TourListAdapter extends BaseAdapter {

	private Context myContext;
	
	public TourListAdapter(Context myContext) {
		// TODO Auto-generated constructor stub
				
		this.myContext = myContext;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return CommonShared.getInstance().getmTours().size();
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
		rl = (RelativeLayout) inflater.inflate(R.layout.tour_item, parent, false);

		RatingBar rb = (RatingBar)rl.findViewById(R.id.TourRaTab);
		TextView tourName = (TextView) rl.findViewById(R.id.listTourName);
		TextView tourDesc = (TextView) rl.findViewById(R.id.descInList);

		ImageView imageView = (ImageView) rl.findViewById(R.id.imvTourSmall);

		rb.setRating(CommonShared.getInstance().getmTours().get(position).finRate());
		tourName.setText(CommonShared.getInstance().getmTours().get(position).getmTourName());
		tourDesc.setText(CommonShared.getInstance().getmTours().get(position).getmTourDescription());

		if(CommonShared.getInstance().getmTours().get(position).getmTourImage()!=null) {
			imageView.setImageBitmap(CommonShared.getInstance().getmTours().get(position).getmTourImage());
		} else {
			imageView.setImageResource(R.drawable.no_image_tour1);
		}
		return rl;
	}

}
