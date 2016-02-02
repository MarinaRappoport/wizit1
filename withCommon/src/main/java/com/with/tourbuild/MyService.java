package com.with.tourbuild;


import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MyService extends Service implements Runnable{
	ExecutorService executor;
	private UpdateIF mActivity;
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return new MyBinder(this);
	}
	
	public UpdateIF getmActivity() {
		return mActivity;
	}


	public void setmActivity(UpdateIF mActivity) {
		this.mActivity = mActivity;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		executor = Executors.newFixedThreadPool(1);
	}
	
	

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		executor.execute(this);
		return super.onStartCommand(intent, flags, startId);
	}
	
@Override
public void run() {
	// TODO Auto-generated method stub
		try {
			ParseQuery<ParseObject> query = ParseQuery.getQuery("Messages");
			query.whereEqualTo("To", MySing.getInstance().getName());
			query.whereEqualTo("ReadState", false);
			query.findInBackground(new FindCallback<ParseObject>() {
			    public void done(List<ParseObject> scoreList, ParseException e) {
			        if (e == null) {
	                 for (final ParseObject parseObject : scoreList) {
	                	 Message m = new Message(parseObject.getString("From"), parseObject.getString("To"), parseObject.getString("Message"), parseObject.getBoolean("ReadState"),parseObject.getObjectId());
	                	 if (mActivity != null) {
							 MessageSing.getInstance().getVictor().add(m);
		 					mActivity.Update();
	 						ParseObject po =ParseObject.createWithoutData("Messages", m.getmObjectId());
		            		po.put("ReadState", true);
		            		m.setmRead(true);
		            		po.saveInBackground();
	 					}
	                 }
			        } else {
			        	
			        }
					if (mActivity != null) {
						mActivity.Update();
					}
			    }
			});
			if (mActivity != null) {
				mActivity.Update();
			}

} catch (Exception e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}
}


@Override
public void onDestroy() {
	// TODO Auto-generated method stub
	executor.shutdown();
	try {
		executor.awaitTermination(1, TimeUnit.SECONDS);
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	super.onDestroy();
}

}
