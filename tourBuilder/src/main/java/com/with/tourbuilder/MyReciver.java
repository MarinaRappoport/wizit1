package com.with.tourbuilder;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.with.tourbuild.AllMyTours;
import com.with.tourbuild.Chat;
import com.with.tourbuild.MyService;
import com.with.tours.RegistationRequestsActivity;

import org.json.JSONObject;

public class MyReciver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub

        try {
            String action = intent.getAction();
            String channel = intent.getExtras().getString("com.parse.Channel");
            JSONObject json = new JSONObject(intent.getExtras().getString(
                    "com.parse.Data"));


            if (json.getString("type").equals("message")){

            String name = json.getString("name");
            String mes = json.getString("mes");
//            String to = json.getString("to");

            Intent i2 = new Intent(context, MyService.class);
            context.startService(i2);

            Intent i = new Intent(context, Chat.class);
            i.putExtra("name", name);
            i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pIntent = PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);

            Notification n = new NotificationCompat.Builder(context)
                    .setContentTitle(name)
                    .setContentText(mes).
                    setContentIntent(pIntent).setSmallIcon(R.drawable.notif).build();
            n.flags |= Notification.FLAG_AUTO_CANCEL;

            NotificationManager notificationManager = (NotificationManager) context
                    .getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(0, n);}


            if (json.getString("type").equals("register")){

            String name = json.getString("name");
            String mes = json.getString("mes");


            Intent i = new Intent(context, AllMyTours.class);
            i.putExtra("role", "userName");
            i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pIntent = PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);

            Notification n = new NotificationCompat.Builder(context)
                    .setContentTitle(name)
                    .setContentText(mes).
                            setContentIntent(pIntent).setSmallIcon(R.drawable.notif).build();
            n.flags |= Notification.FLAG_AUTO_CANCEL;

            NotificationManager notificationManager = (NotificationManager) context
                    .getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(0, n);}

            if (json.getString("type").equals("request")){

                String name = json.getString("name");
                String mes = json.getString("mes");
                String id = json.getString("id");


                Intent i = new Intent(context, RegistationRequestsActivity.class).putExtra("TourId", id);
                i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                PendingIntent pIntent = PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);

                Notification n = new NotificationCompat.Builder(context)
                        .setContentTitle(name)
                        .setContentText(mes).
                                setContentIntent(pIntent).setSmallIcon(R.drawable.notif).build();
                n.flags |= Notification.FLAG_AUTO_CANCEL;

                NotificationManager notificationManager = (NotificationManager) context
                        .getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(0, n);}


    } catch (Exception e) {
    }



		
	}

}
