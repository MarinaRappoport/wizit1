package com.with.tours;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.with.tourbuilder.R;
import com.with.tourbuilder.RegisteredUsers;
import com.with.tourbuilder.SharedObjects;

public class RegisteredUsersAdapter extends BaseAdapter {

	private Context        myContext;

	public RegisteredUsersAdapter(Context context) {
		this.myContext = context;
	}

	@Override
	public int getCount() {
		return SharedObjects.getInstance().getmRegisteredUsers().size();
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
		rl = (RelativeLayout) inflater.inflate(R.layout.rer_item, parent, false);
		TextView name = (TextView)rl.findViewById(R.id.registerNam);
		TextView date = (TextView)rl.findViewById(R.id.registerDat);
		TextView amount = (TextView)rl.findViewById(R.id.registerNum);
		TextView comment = (TextView)rl.findViewById(R.id.regComm);
		RegisteredUsers r = SharedObjects.getInstance().getmRegisteredUsers().get(position);
		name.setText(r.getmUserName());
		date.setText(r.getDate());
		amount.setText(String.valueOf(r.getAmount()));
		comment.setText(r.getmComment());
		return rl;
	}

}
