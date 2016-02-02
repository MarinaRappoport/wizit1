package wizit.cm.wizit;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.with.tourbuild.AllChats;
import com.with.tourbuild.AllMyTours;
import com.with.tourbuild.AllRequests;
import com.with.tourbuild.ChangeGuide;
import com.with.tourbuild.CitySing;
import com.with.tourbuild.DateSing;
import com.with.tourbuild.MySing;
import com.with.tourbuilder.*;
import com.with.tours.ToursList;

public class StartActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(com.with.tourbuilder.R.layout.activity_start);

		MySing.getInstance();
		MySing.getInstance().setImGuide(true);
		CitySing.getInstance();
		DateSing.getInstance().dates(MySing.getInstance().getName());
	}

	public void gotoBuild(View v) {
		Intent intent = new Intent(getApplicationContext(), com.with.tourbuilder.MainActivity.class);
		startActivity(intent);

	}

	public void gotoRegisteredUsers(View v) {
		Intent intent = new Intent(getApplicationContext(), RegisteredUsers.class);
		startActivity(intent);
		
	}

	public void gotoTours(View v) {
		Intent intent = new Intent(getApplicationContext(), ToursList.class).putExtra("role", "guide");
		startActivity(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(com.with.tourbuilder.R.menu.start_menu, menu);
		return true;
	}

	@Override
	public void onBackPressed() {
		Intent intent = new Intent(this,MainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
		intent.putExtra("finish", "finish");
		startActivity(intent);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == com.with.tourbuilder.R.id.user){
			startActivityForResult(new Intent(getApplicationContext(), ChangeGuide.class), 17);
		}
		if (id == com.with.tourbuilder.R.id.calenGui){
			startActivity(new Intent(getApplicationContext(), AllMyTours.class).putExtra("role", "guideName"));
		}
		if (id == com.with.tourbuilder.R.id.becomeTourist){
			MySing.getInstance().setImGuide(false);
			Intent intent = new Intent(this,MainActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
			SharedPreferences sharedPreferences = getSharedPreferences("wizit", MODE_PRIVATE);
			SharedPreferences.Editor editor = sharedPreferences.edit();
			editor.putString("role", "Tourist");
			editor.commit();
			startActivity(intent);
		}

		if (id == com.with.tourbuilder.R.id.reqs){
			startActivity(new Intent(getApplicationContext(), AllRequests.class));
		}

		if (id == com.with.tourbuilder.R.id.allConvers){
			Intent convint = new Intent(getApplicationContext(), AllChats.class);
			startActivity(convint);
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode==17&&resultCode==RESULT_OK)finish();
		super.onActivityResult(requestCode, resultCode, data);
	}
}
